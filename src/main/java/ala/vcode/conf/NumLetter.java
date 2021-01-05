package ala.vcode.conf;


/**
 *	数字字母验证码配置项	
 *
 *	@Auther luoyi
 *	@Date	2020年12月8日
 */
public class NumLetter extends AConfItem {

	
	//	干扰线个数
	protected int noise_line_num = 0;
	//	噪点比例
	protected float noise_point_rate = 0f;
	//	验证码长度
	protected int code_num = 0;
	
	public int getNoise_line_num() {
		return noise_line_num;
	}
	public void setNoise_line_num(int noise_line_num) {
		this.noise_line_num = noise_line_num;
	}
	public float getNoise_point_rate() {
		return noise_point_rate;
	}
	public void setNoise_point_rate(float noise_point_rate) {
		this.noise_point_rate = noise_point_rate;
	}
	public int getCode_num() {
		return code_num;
	}
	public void setCode_num(int code_num) {
		this.code_num = code_num;
	}
	
}
