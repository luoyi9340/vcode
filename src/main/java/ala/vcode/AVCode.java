package ala.vcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import ala.vcode.conf.AConfItem;


/**
 * 验证码公共父类
 *
 * @Auther luoyi
 * @Date 2020年12月7日
 */

public abstract class AVCode {
	
	
	protected Logger logger = LoggerFactory.getLogger(AVCode.class);
	
	
	protected static Random random = new Random();
	
	
	//	训练集线程池
	protected ThreadPoolExecutor trainTP;
	//	验证集线程池
	protected ThreadPoolExecutor valTP;
	//	测试集线程池
	protected ThreadPoolExecutor testTP;
	/**
	 * 	初始化创建任务线程池
	 */
	protected void initThreadPool() {
		trainTP = new ThreadPoolExecutor(App.CONF.getThread_pool().getTrain_min(), 
											App.CONF.getThread_pool().getTrain_max(), 
											6000, TimeUnit.MILLISECONDS, 
											new LinkedBlockingDeque<Runnable>());
		valTP = new ThreadPoolExecutor(App.CONF.getThread_pool().getVal_min(), 
											App.CONF.getThread_pool().getVal_max(), 
											6000, TimeUnit.MILLISECONDS, 
											new LinkedBlockingDeque<Runnable>());
		testTP = new ThreadPoolExecutor(App.CONF.getThread_pool().getTest_min(), 
											App.CONF.getThread_pool().getTest_max(), 
											6000, TimeUnit.MILLISECONDS, 
											new LinkedBlockingDeque<Runnable>());
	}
	
	
	/**
	 * 生成验证码
	 * @throws Exception 
	 * @throws IOException 
	 * @throws VCodeException 
	 */
	public void createVCodeImage() throws Exception {
		//	取配置信息
		AConfItem conf = conf();
		
		//	如果非多线程模式直接跑
		if (!conf.isThreadpool()) {
			logger.info("run as once thread.");
			
			createVCodeImageSingleThread(conf);
		}
		//	否则多线程伺候
		else {
			logger.info("run as mutiple thread.");
			
			createVCodeImageMultipleThread(conf);
			
			//	如果是多线程模式，让主线程等在这里
			//	程序的结束只能人工看靠日志了。。。
			synchronized (this) {
				int trainTaskNum = trainTP.getQueue().size();
				int valTaskNum = valTP.getQueue().size();
				int testTaskNum = testTP.getQueue().size();
				do {
					//	每60s查看一次当前剩余任务数
					this.wait(App.CONF.getThread_pool().getShow_leftover());
					
					trainTaskNum = trainTP.getQueue().size();
					valTaskNum = valTP.getQueue().size();
					testTaskNum = testTP.getQueue().size();
					logger.info("trainTaskNum:" + trainTaskNum + " valTaskNum:" + valTaskNum + " testTaskNum:" + testTaskNum);
					
				} while(trainTaskNum > 0 || valTaskNum > 0 || testTaskNum > 0);
				
				//	当所有任务都执行完成后执行回调
				afterAllThreadPoolTask();
			}
		}
	}
	protected List<IMutipleThreadTaskCallback> callbacks = new LinkedList<IMutipleThreadTaskCallback>();
	/**
	 * 所有任务都执行完成后的回调
	 */
	protected void afterAllThreadPoolTask() {
		for (IMutipleThreadTaskCallback callback : callbacks) {
			callback.afterAllTask();
		}
	}
	/**
	 * 注册回调任务
	 */
	protected void registoryCallback(IMutipleThreadTaskCallback callback) {
		callbacks.add(callback);
	}
	
	
	/**
	 * 生成图片，单线程模式
	 * @throws Exception 
	 */
	protected void createVCodeImageSingleThread(AConfItem conf) throws Exception {
		//	生成训练数据集
		createVCodeImage(conf.getCount_train(), conf.getOut_train(), conf.getLabel_train(), conf.getLabel_train_everyfile());
		//	生成验证集数据
		createVCodeImage(conf.getCount_val(), conf.getOut_val(), conf.getLabel_val(), conf.getLabel_val_everyfile());
		//	生成测试集数据
		createVCodeImage(conf.getCount_test(), conf.getOut_test(), conf.getLabel_test(), conf.getLabel_test_everyfile());
	}
	/**
	 * 生成图片，多线程模式
	 * @throws Exception 
	 */
	protected void createVCodeImageMultipleThread(AConfItem conf) throws Exception {
		initThreadPool();
		
		//	生成训练数据集
		createVCodeImageMutipleThread(conf.getCount_train(), conf.getOut_train(), conf.getLabel_train(), conf.getLabel_train_everyfile(), trainTP);
		//	生成验证集数据
		createVCodeImageMutipleThread(conf.getCount_val(), conf.getOut_val(), conf.getLabel_val(), conf.getLabel_val_everyfile(), valTP);
		//	生成测试集数据
		createVCodeImageMutipleThread(conf.getCount_test(), conf.getOut_test(), conf.getLabel_test(), conf.getLabel_test_everyfile(), testTP);
	}
	
	
	/**
	 * 生成图片
	 * @param	count			要生成的文件数量
	 * @param	dir				要生成的文件目录
	 * @param	labelFilePath	要生成的标签文件完路径
	 * @param	label_everyfile	每个label文件多少条记录（<0则不予区分）
	 */
	protected void createVCodeImage(int count, String dir, String labelFilePath, int label_everyfile) throws Exception  {
		//	每1%条打印一次log
		int log_unit = count / 100;
		//	标签文件
		AtomicInteger labelFileIdx = new AtomicInteger(label_everyfile > 0 ? 0 : -1);
		BufferedWriter labelWriter = createLabelFile(labelFilePath, labelFileIdx.get());
		AtomicInteger crtLabelCount = new AtomicInteger(0);
		boolean isMutipleLabelFile = label_everyfile > 0;
		
		for (int i = 0 ; i < count ; i++) {
			String title = createTitle();
			CreateImageResult res = createImage(title);
			
			try {
				if (res.getImage() != null) {
					createVCodeImage(res.getImage(), title, dir);
					
					if (res.getAnno() != null) {
						labelWriter.write(JSONObject.toJSONString(res.getAnno()) + "\r\n");
					}
					
					//	如果标签需要切文件
					if (isMutipleLabelFile) {
						//	如果标签文件记录数超过了上限，则写入新文件
						if (crtLabelCount.incrementAndGet() > label_everyfile) {
							BufferedWriter crtLabellWriter = createLabelFile(labelFilePath, labelFileIdx.incrementAndGet());
							
							//	没加写锁，可能会存在多一个少一个的情况，无所谓了。。。
							BufferedWriter oldLabelWriter = labelWriter;
							crtLabelCount.set(0);
							labelWriter = crtLabellWriter;
							oldLabelWriter.close();
							logger.info("labelfile change. current:" + (labelFilePath + labelFileIdx.get()));
						}
					}
				}
				
//				logger.debug("create vcode image succ. file:" + dir + "/" + title);
				if (log_unit > 0 && i % log_unit == 0) {
					logger.info("create vcode image:(" + i + "/" + count + ")");
				}
			} catch (VCodeException e) {
				logger.warn("create vcode image faild. file:" + dir + "/" + title, e);
			} catch (IOException e) {
				logger.warn("create vcode image faild. file:" + dir + "/" + title, e);
			}
		}
		
		labelWriter.close();
	}
	/**
	 * 生成图片多线程模式
	 * @param	count			要生成的文件数量
	 * @param	dir				要生成的文件目录
	 * @param	labelFilePath	要生成的标签文件完路径
	 * @param	label_everyfile	每个标签文件多少条记录(<0表示不切分)
	 * @param	threadPool		线程池
	 */
	protected void createVCodeImageMutipleThread(final int count, final String dir, final String labelFilePath, final int label_everyfile, ThreadPoolExecutor threadPool) throws Exception  {
		//	每1%条打印一次log
		final int log_unit = count / 100;
		
		//	标签文件
		final AtomicInteger labelFileIdx = new AtomicInteger(label_everyfile > 0 ? 0 : -1);
		//	final 类型不能直接给值，外面拿个数组包一下
		final BufferedWriter[] labelWriter = new BufferedWriter[]{createLabelFile(labelFilePath, labelFileIdx.get())};
		final AtomicInteger crtLabelCount = new AtomicInteger(0);
		//	是否需要分文件
		final boolean isMutipleLabelFile = label_everyfile > 0;
		//	是否正在切分
		final AtomicBoolean isChanging = new AtomicBoolean(false);
		
		for (int i = 0 ; i < count ; i++) {
			final int fi = i;
			threadPool.submit(new Runnable() {
				public void run() {
					final String title = createTitle();
					final CreateImageResult res = createImage(title);
					
					try {
						if (res.getImage() != null) {
							try {
								createVCodeImage(res.getImage(), title, dir);
								
								if (res.getAnno() != null) {
									while (isChanging.get()) {
										synchronized (this) {
											this.wait(100);
										}
									}
									labelWriter[0].write(JSONObject.toJSONString(res.getAnno()) + "\r\n");
								}
								
								//	如果标签需要切文件
								if (isMutipleLabelFile) {
									//	如果标签文件记录数超过了上限，则写入新文件
									if (crtLabelCount.incrementAndGet() > label_everyfile) {
										isChanging.set(true);
										
										BufferedWriter crtLabellWriter = createLabelFile(labelFilePath, labelFileIdx.incrementAndGet());
										
										//	没加写锁，可能会存在多一个少一个的情况，无所谓了。。。
										BufferedWriter oldLabelWriter = labelWriter[0];
										labelWriter[0] = crtLabellWriter;
										crtLabelCount.set(0);
										
										//	等一等其他正在写的人
										synchronized (this) {
											this.wait(100);
										}
										isChanging.set(false);
										oldLabelWriter.close();
										
										logger.info("labelfile change. current:" + (labelFilePath + labelFileIdx.get()));
									}
								}
								
//								logger.debug("create vcode image succ. file:" + dir + "/" + title);
								if (log_unit > 0 && fi % log_unit == 0) {
									logger.info("create vcode image:(" + fi + "/" + count + ")");
								}
							} catch (VCodeException e) {
								logger.warn("create vcode image faild. file:" + dir + "/" + title, e);
							} catch (IOException e) {
								logger.warn("create vcode image faild. file:" + dir + "/" + title, e);
							}
						}
					} catch (Exception e) {
						logger.warn("create vcode image faild.", e);
					}
				}
			});
		}
		
		//	注册回调
		registoryCallback(new IMutipleThreadTaskCallback() {
			public void afterAllTask() {
				try {
					labelWriter[0].close();
				} catch (IOException e) {
					logger.warn("close labelWriter error.", e);
				}
			}
		});
	}
	
	
	//	标注文件输出
	protected BufferedWriter createLabelFile(String filePath, int index) throws IOException {
		if (index >= 0) {filePath = filePath + index;}
		File annotationFile = new File(filePath);
		File dir = annotationFile.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		BufferedWriter annotationWriter = new BufferedWriter(new FileWriter(annotationFile));
		return annotationWriter;
	}

	
	/**
	 * 生成图像对象
	 */
	protected abstract CreateImageResult createImage(String title);
	/**
	 * 生成文件名
	 */
	protected abstract String createTitle();
	/**
	 * 取配置项
	 */
	protected abstract AConfItem conf();

	
	/**
	 * 生成验证码图片
	 * 
	 * @param image 	验证码图像
	 * @param title		文件标题
	 * @param dir		文件目录	
	 * @throws VCodeException 
	 * @throws IOException 
	 */
	protected void createVCodeImage(BufferedImage image, String title, String dir) throws VCodeException, IOException {
		if (App.CONF.getNum_letter() == null) {
			throw new VCodeException("config item: 'num_letter' is null.");
		}
		
		//	输出文件目录
		String outDir = dir;
		if (outDir == null) {
			throw new VCodeException("config item: 'num_letter.out' is null.");
		}
		File dirFile = new File(outDir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		//	输出文件完整路径
		String filePath = outDir + "/" + title + ".png";
		
		
		OutputStream out = new FileOutputStream(filePath);
		ImageIO.write(image, "png", out);
		out.close();
		image.getGraphics().dispose();
	}
	
	
	/**
	 * 随机取颜色
	 */
	protected Color randomColor() {
		return randomColor(0, 255);
	}
	/**
	 * 随机颜色
	 * @param fc	随机rgb值下限（0 ~ 255）
	 * @param bc	随机rgb值上限（0 ~ 255，且大于fc）
	 * @return
	 */
	protected Color randomColor(int fc, int bc) {
		if (fc < 0) {fc = 0;} if (fc > 255) {fc = 255;}
		if (bc < 0) {bc = 0;} if (bc > 255) {bc = 255;}
		if (fc > bc) {bc = fc;}
		
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	
	/**
	 * 绘制干扰线
	 * @param	n		干扰线条数（0 ~ 100）
	 * @param	w		区域宽度
	 * @param	h		区域长度
	 * @param	g2d		绘制区域对象
	 */
	protected void drawInterferenceLine(int n, int w, int h, Graphics2D g2d) {
		Color color = randomColor();
		drawInterferenceLine(n, w, h, g2d, color);
	}
	/**
	 * 随机生成n条干扰线
	 * @param	n		干扰线条数（0 ~ 100）
	 * @param	w		区域宽度
	 * @param	h		区域长度
	 * @param	g2d		绘制区域对象
	 * @param	color	干扰线颜色
	 */
	protected void drawInterferenceLine(int n, int w, int h, Graphics2D g2d, Color color) {
		if (n < 0 || n > 100) {n = 100;}
		
		//	设置线条颜色
		g2d.setColor(color);
		for (int i = 0 ; i < n ; i++) {
			//	干扰线起始点坐标
			int x_s = random.nextInt(w - 1),
				y_s = random.nextInt(h - 1);
			//	干扰线终点坐标
			int x_e = x_s + random.nextInt(h / 2) * (random.nextFloat() > 0.5f ? 1 : -1),
				y_e = y_s + random.nextInt(h / 2) * (random.nextFloat() > 0.5f ? 1 : -1);
			
			g2d.drawLine(x_s, y_s, x_e, y_e);
		}
	}
	
	
	/**
	 * 绘制噪声点
	 * 	慎用，效率太低
	 * 
	 * @param	noiseRate	噪声像素比例（0 ~ 1之间，默认0）
	 * @param	w			区域宽度
	 * @param	h			区域高度
	 * @param	g2d			绘制区域
	 */
	@Deprecated
	protected void drawNoise(float noiseRate, int w, int h, Graphics2D g2d) {
		if (noiseRate < 0 || noiseRate > 1) {noiseRate = 0;}
		int count = (int)(noiseRate * w * h);
		
		g2d.setColor(randomColor());
		for (int i = 0 ; i < count ; i++) {
			g2d.translate(random.nextInt(w), random.nextInt(h));
		}
	}
	
	
	/**
	 * 设置旋转
	 * @return
	 */
	protected AffineTransform transform(double theta, int anchorx, int anchory) {
		AffineTransform affine = new AffineTransform();
		affine.setToRotation(theta * random.nextDouble() * (random.nextBoolean() ? 1 : -1), 
				anchorx, 
				anchory);
		return affine;
	}
	
	
	/**
	 * 创建图片返回信息
	 */
	public class CreateImageResult {
		//	图片信息
		protected BufferedImage image;
		//	标注信息
		protected IAnno anno;
		
		public CreateImageResult(BufferedImage image, IAnno anno) {
			super();
			this.image = image;
			this.anno = anno;
		}
		public BufferedImage getImage() {
			return image;
		}
		public void setImage(BufferedImage image) {
			this.image = image;
		}
		public IAnno getAnno() {
			return anno;
		}
		public void setAnno(IAnno anno) {
			this.anno = anno;
		}
	}
}
