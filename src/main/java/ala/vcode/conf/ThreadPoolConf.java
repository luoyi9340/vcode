package ala.vcode.conf;


/**
 *	线程池相关配置	
 *
 *	@Auther luoyi
 *	@Date	2021年1月11日
 */
public class ThreadPoolConf {
	
	//	训练集线程配置
	protected int train_min = 10;
	protected int train_max = 20;
	
	//	验证集线程配置
	protected int val_min = 5;
	protected int val_max = 10;
	
	//	测试集线程配置
	protected int test_min = 5;
	protected int test_max = 10;
	
	//	每隔多少ms查看一次剩余任务数
	protected int show_leftover = 60000;
	
	public int getTrain_min() {
		return train_min;
	}
	public void setTrain_min(int train_min) {
		this.train_min = train_min;
	}
	public int getTrain_max() {
		return train_max;
	}
	public void setTrain_max(int train_max) {
		this.train_max = train_max;
	}
	public int getVal_min() {
		return val_min;
	}
	public void setVal_min(int val_min) {
		this.val_min = val_min;
	}
	public int getVal_max() {
		return val_max;
	}
	public void setVal_max(int val_max) {
		this.val_max = val_max;
	}
	public int getTest_min() {
		return test_min;
	}
	public void setTest_min(int test_min) {
		this.test_min = test_min;
	}
	public int getTest_max() {
		return test_max;
	}
	public void setTest_max(int test_max) {
		this.test_max = test_max;
	}
	public int getShow_leftover() {
		return show_leftover;
	}
	public void setShow_leftover(int show_leftover) {
		this.show_leftover = show_leftover;
	}
}
