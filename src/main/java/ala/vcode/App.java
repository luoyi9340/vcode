package ala.vcode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ala.vcode.conf.Conf;
import ala.vcode.conf.ConfLoader;
import ala.vcode.idiom.VCodeIdiom;
import ala.vcode.letter.VCodeLetter;
import ala.vcode.num_letter.VCodeNumLetter;

/**
 * 验证码生成器
 *
 */
public class App {
	
	static Logger logger = LoggerFactory.getLogger(App.class);
	
	
	public static Conf CONF;
	
	public static void main(String[] args) {
		try {
			CONF = ConfLoader.load();
		} catch (FileNotFoundException e) {
			logger.warn("load config error.", e);
			System.exit(1);
		}
		
		//	如果存在绘制数字字母验证码需求
		if (CONF.getNum_letter() != null && CONF.getNum_letter().isEnable()) {drawNumLetter();}
		//	如果存在绘制成语验证码需求
		if (CONF.getIdiom() != null && CONF.getIdiom().isEnable()) {drawIdiom();}
		//	如果存在绘制字母识别需求
		if (CONF.getLetter() != null && CONF.getLetter().isEnable()) {drawLetter();}
	}
	
	
	/**
	 * 根据配置绘制数字字母验证码
	 */
	protected static void drawNumLetter() {
		AVCode avCode = new VCodeNumLetter();
		try {
			avCode.createVCodeImage();
		} catch (Exception e) {
			logger.warn("create num_letter image error.", e);
		}
	}
	/**
	 * 根据配置绘制成语验证码
	 */
	protected static void drawIdiom() {
		VCodeIdiom avCode = new VCodeIdiom();
		try {
			avCode.loadDict();
			avCode.createVCodeImage();
		} catch (IOException e) {
			logger.warn("create idiom image error.", e);
		} catch (URISyntaxException e) {
			logger.warn("create idiom image error.", e);
		} catch (Exception e) {
			logger.warn("create idiom image error.", e);
		}
	}
	/**
	 * 根据配置绘制字母识别
	 * 	给CNNs练手用
	 */
	protected static void drawLetter() {
		AVCode avCode = new VCodeLetter();
		try {
			avCode.createVCodeImage();
		} catch (Exception e) {
			logger.warn("create letter image error.", e);
		}
	}
}
