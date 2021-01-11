package ala.vcode.conf;


/**
 *	配置项公共属性	
 *
 *	@Auther luoyi
 *	@Date	2020年12月28日
 */
public abstract class AConfItem {
	//	是否启用
	protected boolean enable;
	
	//	是否多线程模式
	protected boolean threadpool;
	
	//	训练集相关配置
	protected String out_train;
	protected int count_train;
	protected String label_train;
	protected int label_train_everyfile = -1;
	//	验证集相关配置
	protected String out_val;
	protected int count_val;
	protected String label_val;
	protected int label_val_everyfile = -1;
	//	测试集相关配置
	protected String out_test;
	protected int count_test;
	protected String label_test;
	protected int label_test_everyfile = -1;
	
	public String getOut_train() {
		return out_train;
	}
	public void setOut_train(String out_train) {
		this.out_train = out_train;
	}
	public int getCount_train() {
		return count_train;
	}
	public void setCount_train(int count_train) {
		this.count_train = count_train;
	}
	public String getLabel_train() {
		return label_train;
	}
	public void setLabel_train(String label_train) {
		this.label_train = label_train;
	}
	public String getOut_val() {
		return out_val;
	}
	public void setOut_val(String out_val) {
		this.out_val = out_val;
	}
	public int getCount_val() {
		return count_val;
	}
	public void setCount_val(int count_val) {
		this.count_val = count_val;
	}
	public String getLabel_val() {
		return label_val;
	}
	public void setLabel_val(String label_val) {
		this.label_val = label_val;
	}
	public String getOut_test() {
		return out_test;
	}
	public void setOut_test(String out_test) {
		this.out_test = out_test;
	}
	public int getCount_test() {
		return count_test;
	}
	public void setCount_test(int count_test) {
		this.count_test = count_test;
	}
	public String getLabel_test() {
		return label_test;
	}
	public void setLabel_test(String label_test) {
		this.label_test = label_test;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public boolean isThreadpool() {
		return threadpool;
	}
	public void setThreadpool(boolean threadpool) {
		this.threadpool = threadpool;
	}
	public int getLabel_train_everyfile() {
		return label_train_everyfile;
	}
	public void setLabel_train_everyfile(int label_train_everyfile) {
		this.label_train_everyfile = label_train_everyfile;
	}
	public int getLabel_val_everyfile() {
		return label_val_everyfile;
	}
	public void setLabel_val_everyfile(int label_val_everyfile) {
		this.label_val_everyfile = label_val_everyfile;
	}
	public int getLabel_test_everyfile() {
		return label_test_everyfile;
	}
	public void setLabel_test_everyfile(int label_test_everyfile) {
		this.label_test_everyfile = label_test_everyfile;
	}
}
