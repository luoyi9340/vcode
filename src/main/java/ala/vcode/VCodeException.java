package ala.vcode;


/**
 *	验证码过程中出现的一切异常
 *
 *	@Auther luoyi
 *	@Date	2020年12月7日
 */
public class VCodeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public VCodeException() {}
	public VCodeException(String msg) {super(msg);}
	public VCodeException(Exception e) {super(e);}
	public VCodeException(String msg, Exception e) {super(msg, e);}
}
