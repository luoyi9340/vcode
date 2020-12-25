package ala.vcode;

import java.io.FileNotFoundException;

import org.junit.Test;

import ala.vcode.conf.Conf;
import ala.vcode.conf.ConfLoader;

/**
 * 
 * @Auther luoyi
 * @Date 2020年12月8日
 */
public class ConfigurationTest {


	@Test
	public void testYml() {
		try {
			Conf CONF = ConfLoader.load();
			
			System.out.print(CONF.getNum_letter().getOut());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
