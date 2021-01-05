package ala.vcode.letter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.UUID;

import ala.vcode.AVCode;
import ala.vcode.App;
import ala.vcode.conf.AConfItem;

/**
 *	单一字母
 *		手写字母识别
 *	
 *	@Auther luoyi
 *	@Date	2020年12月15日
 */
public class VCodeLetter extends AVCode {
	
	
	//	字母表
	static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	

	@Override
	protected CreateImageResult createImage(String title) {
		//	高度都是100，每个字符给80的宽度
		int h = 100;
		int w = 100;
		int w_code = 80;			//	实际每个字只有100的宽度
//		int h = 224, w = 224, w_code = 240;
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
//		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
        //	绘制背景
        g2.setColor(Color.BLACK);
//        g2.setColor(randomColor(200, 250));
        g2.drawRect(0, 0, w, h);
        
        //	写入标注信息
        AnnoInfo info = new AnnoInfo();
        info.setFilename(title);
        
        //	随机取字母
        String code = LETTERS.charAt(random.nextInt(LETTERS.length())) + "";
        info.setLetter(code);
        
        //	绘制字母
        Font font = new Font("Algerian", Font.ITALIC, w_code);
        g2.setColor(Color.white);
//        g2.setColor(randomColor());
		g2.setFont(font);
        //	长宽100，每个字80，那么在10，90的地方写字（drawString是在坐标的位置往上写的）
        //	在50，50的地方旋转π/6
        g2.setTransform(transform(Math.PI / 6f, w/2, h/2));
        g2.drawString(code, 20, h - 20);
        
		return new CreateImageResult(image, info);
	}

	@Override
	protected String createTitle() {
		return UUID.randomUUID().toString();
	}

	@Override
	protected AConfItem conf() {
		return App.CONF.getLetter();
	}


}
