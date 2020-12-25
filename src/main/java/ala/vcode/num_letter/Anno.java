package ala.vcode.num_letter;

/**
 * 标注信息框
 * 
 * @Auther luoyi
 * @Date 2020年12月9日
 */
public class Anno {
//	标注识别结果
	protected String key;
	// 标注框中心x坐标
	protected int x;
	// 标注框中心y坐标
	protected int y;
	// 标注框宽度
	protected int w;
	// 标注框高度
	protected int h;

	public Anno() {
		super();
	}

	public Anno(String key, int x, int y, int w, int h) {
		super();
		this.key = key;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}
}
