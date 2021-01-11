package ala.vcode.conf;


/**
 *	配置项
 *	
 *	@Auther luoyi
 *	@Date	2020年12月8日
 */
public class Conf {
	
	
	//	线程配置
	protected ThreadPoolConf thread_pool;

	//	数字字母配置项
	protected NumLetter num_letter;
	//	成语配置
	protected Idiom idiom;
	//	字母配置（基础CNNs练手用）
	protected Letter letter;
	
	public NumLetter getNum_letter() {
		return num_letter;
	}

	public void setNum_letter(NumLetter num_letter) {
		this.num_letter = num_letter;
	}

	public Idiom getIdiom() {
		return idiom;
	}

	public void setIdiom(Idiom idiom) {
		this.idiom = idiom;
	}

	public Letter getLetter() {
		return letter;
	}

	public void setLetter(Letter letter) {
		this.letter = letter;
	}

	public ThreadPoolConf getThread_pool() {
		return thread_pool;
	}

	public void setThread_pool(ThreadPoolConf thread_pool) {
		this.thread_pool = thread_pool;
	}
	
}
