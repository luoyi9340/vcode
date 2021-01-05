package ala.vcode.idiom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ala.vcode.AVCode;
import ala.vcode.App;
import ala.vcode.conf.AConfItem;

/**
 *	成语验证码	
 *
 *	@Auther luoyi
 *	@Date	2020年12月9日
 */
public class VCodeIdiom extends AVCode {
	
	
	static Logger logger = LoggerFactory.getLogger("idiom");
	
	
	//	成语词典
	public static final String PATH_DICT = "/dictionary/simple_lexicon.txt";
	//	背景
	public static final String DIR_BACKGROUND = "/background";
	
	
	//	成语词典
	protected String[] _idioms;
	/**
	 * 加载成语词典
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public void loadDict() throws IOException, URISyntaxException {
		URL url = this.getClass().getResource(PATH_DICT);
		BufferedReader br = new BufferedReader(new FileReader(new File(url.toURI())));
		
		List<String> list = new LinkedList<String>();
		String line = null;
		while ((line = br.readLine()) != null) {
			list.add(line);
		}
		
		_idioms = list.toArray(new String[0]);
		br.close();
	}
	

	@Override
	protected CreateImageResult createImage(String title) {
		//	成语固定大小200 * 200，每个字50，在每个100*100的区域内
		int w = 200;
		int h = 200;
		int w_code = 50;
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D  g2 = image.createGraphics();
		
		//	随机取一个背景
		try {
			BufferedImage bg = getBackground(random.nextInt(4));
			g2.drawImage(bg, 0, 0, w, h, null);
		} catch (IOException e) {
			logger.warn("load background error.", e);
			g2.setColor(randomColor(200, 250));
			g2.fillRect(0, 0, w, h);
		} catch (URISyntaxException e) {
			logger.warn("load background error.", e);
			g2.setColor(randomColor(200, 250));
			g2.fillRect(0, 0, w, h);
		}
		
		//	标注信息
		AnnoInfo annoInfo = new AnnoInfo();
		annoInfo.setTitle(title);
		annoInfo.setAnnos(new LinkedList<Anno>());
		
		Font font = new Font("Algerian", Font.ITALIC, w_code);
		g2.setFont(font);
		g2.setColor(Color.BLACK);
		//	乱序写入成语
		String[] disorderIdiom = disorderIdiom(title);
		int idx = 0, i = 0, j = 0;
		int x = 0, y = 0;
		for (String code : disorderIdiom) {
			i = idx / 2;
			j = idx % 2;
			
			x = 25 + i * 100;
			y = 25 + j * 100;
			g2.drawString(code, x, y + w_code);
			
			//	追加标注信息
			annoInfo.getAnnos().add(new Anno(code, x + 5, y + 5, w_code, w_code));
			idx++;
		}
		
		return new CreateImageResult(image, annoInfo);
	}
	/**
	 * 乱序成语
	 */
	protected String[] disorderIdiom(String idiom) {
		List<String> list = new ArrayList<String>(idiom.length());
		for (char c : idiom.toCharArray()) {
			list.add(c + "");
		}
		Collections.shuffle(list);
		
		return list.toArray(new String[0]);
	}
	
	
	protected BufferedImage[] bgCache;
	/**
	 * 取背景图片
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	protected BufferedImage getBackground(int idx) throws IOException, URISyntaxException {
		if (idx < 0 || idx > 3) {idx = 0;}
		
		if (bgCache == null) {bgCache = new BufferedImage[4];}
		if (bgCache[idx] == null) {
			URL url = this.getClass().getResource(DIR_BACKGROUND + "/" + idx + ".png");
			BufferedImage image = ImageIO.read(new File(url.toURI()));
			bgCache[idx] = image;
		}
		
		return bgCache[idx];
	}
	

	@Override
	protected String createTitle() {
		//	从词典中随机取一个
		return _idioms[random.nextInt(_idioms.length)];
	}

	@Override
	protected AConfItem conf() {
		return App.CONF.getIdiom();
	}


	public String[] get_idioms() {
		return _idioms;
	}

	public void set_idioms(String[] _idioms) {
		this._idioms = _idioms;
	}

}
