package ala.vcode;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import ala.vcode.idiom.VCodeIdiom;

/**
 *	
 *	@Auther luoyi
 *	@Date	2020年12月9日
 */
public class IdiomTest {

	
	@Test
	public void testLoad() {
		VCodeIdiom idiom = new VCodeIdiom();
		try {
			idiom.loadDict();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		System.out.println(idiom.get_idioms().length);
		System.out.println(idiom.get_idioms()[0]);
	}
	
	
}
