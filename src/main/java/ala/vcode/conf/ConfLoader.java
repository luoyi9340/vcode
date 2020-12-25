package ala.vcode.conf;

import java.io.FileNotFoundException;

import org.yaml.snakeyaml.Yaml;

/**
 *	配置文件读取
 *
 *	@Auther luoyi
 *	@Date	2020年12月7日
 */
public class ConfLoader {

	
	//	默认配置文件名
	public static final String CONF_NAME = "conf.yml";

	
	/**
	 * 读取配置文件
	 * @throws FileNotFoundException 
	 */
	public static Conf load() throws FileNotFoundException {
		Yaml yaml = new Yaml();
		Conf conf = yaml.loadAs(ConfLoader.class.getResourceAsStream("/conf.yml"), Conf.class);
		return conf;
	}
	
}
