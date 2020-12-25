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
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


/**
 * 验证码公共父类
 *
 * @Auther luoyi
 * @Date 2020年12月7日
 */

public abstract class AVCode {
	
	
	protected Logger logger = LoggerFactory.getLogger(AVCode.class);
	
	
	protected static Random random = new Random();
	
	
	/**
	 * 生成验证码
	 * @throws Exception 
	 * @throws IOException 
	 * @throws VCodeException 
	 */
	public void createVCodeImage() throws Exception {
		before();
		
		//	取要生成的验证码图片数量
		int count = count();
		
		//	每1%条打印一次log
		int log_unit = count / 100;
		
		for (int i = 0 ; i < count ; i++) {
			String title = createTitle();
			CreateImageResult res = createImage(title);
			
			try {
				if (res.getImage() != null) {
					createVCodeImage(res.getImage(), title);
					
					if (res.getAnno() != null) {
						annotationWriter.write(JSONObject.toJSONString(res.getAnno()) + "\r\n");
					}
				}
				
				logger.debug("create vcode image succ. file:" + dir() + "/" + title);
				if (i % log_unit == 0) {
					logger.info("create vcode image:(" + i + "/" + count + ")");
				}
			} catch (VCodeException e) {
				logger.warn("create vcode image faild. file:" + dir() + "/" + title, e);
			} catch (IOException e) {
				logger.warn("create vcode image faild. file:" + dir() + "/" + title, e);
			}
		}
		
		after();
	}
	
	
	//	标注文件输出
	protected BufferedWriter annotationWriter;
	protected void before() throws IOException {
		String annotation = annotation();
		if (annotation == null) {return;}
		
		File annotationFile = new File(annotation);
		File dir = annotationFile.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		annotationWriter = new BufferedWriter(new FileWriter(annotationFile));
	}

	protected void after() throws IOException {
		if (annotationWriter != null) {
			annotationWriter.close();
		}
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
	 * 取文件目录
	 */
	protected abstract String dir();
	/**
	 * 要生成的验证码数
	 */
	protected abstract int count();
	/**
	 * 取标注文件path
	 */
	protected abstract String annotation();

	
	/**
	 * 生成验证码图片
	 * 
	 * @param image 	验证码图像
	 * @param title		文件标题
	 * @throws VCodeException 
	 * @throws IOException 
	 */
	protected void createVCodeImage(BufferedImage image, String title) throws VCodeException, IOException {
		if (App.CONF.getNum_letter() == null) {
			throw new VCodeException("config item: 'num_letter' is null.");
		}
		
		//	输出文件目录
		String outDir = dir();
		if (outDir == null) {
			throw new VCodeException("config item: 'num_letter.out' is null.");
		}
		File dir = new File(outDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//	输出文件完整路径
		String filePath = outDir + "/" + title + ".png";
		
		
		OutputStream out = new FileOutputStream(filePath);
		ImageIO.write(image, "png", out);
		out.close();
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
