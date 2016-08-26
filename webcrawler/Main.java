package webcrawler;

import static webcrawler.Config.*;

import java.util.Queue;

public class Main {

	public static void main(String[] args) {
		
		System.setProperty("http.proxyHost", "111.13.136.46");
		System.setProperty("http.proxyPort", "80");
		
		String startUrl = "http://appstore.huawei.com/more/all/40";
		String outputFileName = "C:\\Users\\bbccyy\\Desktop\\output.txt";
		
		Controler ctrler = new Controler(outputFileName, startUrl);
		
		ctrler.start();  //do it!
		
	}

}
