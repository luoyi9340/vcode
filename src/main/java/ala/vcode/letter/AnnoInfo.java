package ala.vcode.letter;

import ala.vcode.IAnno;

/**
 *	生成标记	
 *
 *
 *	@Auther luoyi
 *	@Date	2020年12月15日
 */
public class AnnoInfo implements IAnno {

	
	//	文件名
	protected String filename;
	//	字母
	protected String letter;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	
}
