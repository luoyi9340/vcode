package ala.vcode.conf;


/**
 *	
 *	@Auther luoyi
 *	@Date	2020年12月15日
 */
public class Letter {

	protected boolean enable;
	
	//	生成验证码数量
	protected int count;
	//	图片输出目录
	protected String out;
	//	标记输出目录
	protected String annotation;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getOut() {
		return out;
	}
	public void setOut(String out) {
		this.out = out;
	}
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
