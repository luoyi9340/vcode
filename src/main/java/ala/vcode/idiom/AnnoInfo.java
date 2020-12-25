package ala.vcode.idiom;

import java.util.List;

import ala.vcode.IAnno;

/**
 *	标注信息	
 *		标注格式(Json)：{title:'${title}', annos:[{key:'值', x:x, y:y, w:w, h:h}, {key:'值', x:x, y:y, w:w, h:h}...]}
 *
 *	@Auther luoyi
 *	@Date	2020年12月9日
 */
public class AnnoInfo implements IAnno {

	
	//	成语原文
	protected String title;
	//	标注框
	protected List<Anno> annos;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Anno> getAnnos() {
		return annos;
	}
	public void setAnnos(List<Anno> annos) {
		this.annos = annos;
	}


}
