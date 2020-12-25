package ala.vcode.num_letter;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ala.vcode.AVCode;
import ala.vcode.App;

/**
 *	图片数字验证码	
 *
 *	@Auther luoyi
 *	@Date	2020年12月8日
 */
public class VCodeNumLetter extends AVCode {
	
	
	static Logger logger = LoggerFactory.getLogger("num_letter");

	
	// 使用到Algerian字体，系统里没有的话需要安装字体，字体只显示大写，去掉了2,1,0,i,o几个容易混淆的字符
	public static final String VERIFY_CODES = "3456789ABCDEFGHJKLMNPQRSTUVWXYZ";

	
	@Override
	protected CreateImageResult createImage(String title) {
		//	高度都是180，每个字符给120的宽度
		int h = 180;
		int w = 120 * title.length();
		int w_code = 100;			//	实际每个字只有100的宽度
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
        //	设置背景色
		g2.setColor(randomColor(200, 250));
		g2.fillRect(0, 0, w, h);
		
		//	绘制干扰线
		drawInterferenceLine(App.CONF.getNum_letter().getNoise_line_num(), w, h, g2);
		
		//	追加标注信息json格式
		//	标注格式：{title:'${title}', annos:[{key:'值', x:x, y:y, w:w, h:h}, {key:'值', x:x, y:y, w:w, h:h}...]}
		AnnoInfo info = new AnnoInfo();
		info.setTitle(title);
		info.setAnnos(new ArrayList<Anno>());
		
		//	写验证码
		int x = 20, y = h / 2;
		Font font = new Font("Algerian", Font.ITALIC, w_code);
		g2.setFont(font);
		for (int i = 0 ; i < title.length() ; i++) {
			String code = title.charAt(i) + "";
			g2.setColor(randomColor());
			int cx = x, cy = y + random.nextInt(h / 4);
			//	设置旋转（暂时先不旋转，标注框会对不准。。。）
//			g2.setTransform(transform(Math.PI / 6f, cx + w_code/2, cy - w_code/2));
			g2.drawString(code, cx, cy);

			//	特殊判断下，貌似标注框对不准
			adjustAnno(code, cx, cy - w_code, w_code, w_code, info);
			
			//	更新下个字的x坐标
			x = x + w_code + 20;
		}
        
		g2.dispose();
		return new CreateImageResult(image, info);
	}
	
	
	/**
	 * 微调标注框
	 */
	protected void adjustAnno(String code, int anno_x, int anno_y, int anno_w, int anno_h, AnnoInfo info) {
		if (code.equals("J")) {
			anno_x = anno_x - 20;
			anno_y = anno_y + 20;
			anno_w = (int)(anno_w * 0.75f);
		}else if (code.equals("Q")) {
			anno_y = anno_y + 20;
		}else if (code.equals("W")) {
			anno_y = anno_y + 20;
			anno_h = (int)(anno_h * 0.8);
		}
		else if (code.equals("1") || 
					code.equals("2") || 
					code.equals("3") || 
					code.equals("4") || 
					code.equals("5") || 
					code.equals("6") ||
					code.equals("7") ||
					code.equals("8") ||
					code.equals("9") ||
					code.equals("0")) {
			anno_w = (int)(anno_w * 0.8);
			anno_y = anno_y + 20;
			anno_h = (int)(anno_h * 0.8);
		}
		else {
			anno_w = (int)(anno_w * 0.8);
			anno_y = anno_y + 20;
			anno_h = (int)(anno_h * 0.8);
		}
		info.getAnnos().add(new Anno(code, anno_x, anno_y, anno_w, anno_h));
	}
	
	
	@Override
	protected String createTitle() {
		//	取验证码长度
		int code_num = App.CONF.getNum_letter().getCode_num();
		if (code_num < 4 || code_num > 6) {
			code_num = 4 + random.nextInt(3);
		}
		
		//	根据长度从VERIFY_CODES中取
		StringBuffer sbuf = new StringBuffer();
		for (int i = 0 ; i < code_num ; i++) {
			int idx = random.nextInt(VERIFY_CODES.length());
			
			sbuf.append(VERIFY_CODES.charAt(idx));
		}
		return sbuf.toString();
	}

	@Override
	protected String dir() {
		return App.CONF.getNum_letter().getOut();
	}

	@Override
	protected int count() {
		return App.CONF.getNum_letter().getCount();
	}

	@Override
	protected String annotation() {
		return App.CONF.getNum_letter().getAnnotation();
	}
	
}
