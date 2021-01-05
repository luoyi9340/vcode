package ala.vcode.num_letter;

import java.util.List;

import ala.vcode.IAnno;

/**
 *	标注信息	
 *		标注格式(Json)：{fileName:'${fileName}', vcode='{vcode}', annos:[{key:'值', x:x, y:y, w:w, h:h}, {key:'值', x:x, y:y, w:w, h:h}...]}
 *
 *	@Auther luoyi
 *	@Date	2020年12月9日
 */
public class AnnoInfo implements IAnno {

	
	//	文件名名
	protected String fileName;
	
	//	验证码值
	protected String vcode;
	//	标注框
	protected List<Anno> annos;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getVcode() {
		return vcode;
	}
	public void setVcode(String vcode) {
		this.vcode = vcode;
	}
	public List<Anno> getAnnos() {
		return annos;
	}
	public void setAnnos(List<Anno> annos) {
		this.annos = annos;
	}


}
