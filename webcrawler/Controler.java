package webcrawler;

import static webcrawler.Config.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Queue;

public class Controler {
	
	private static Queue<String> pool = InstanceHolder.getPool();
	private String fileName = null;
	
	public Controler(){}
	
	public Controler(String fileName, String startUrl){
		pool.add(startUrl);
		this.fileName = fileName;
	}
	
	public Controler(String fileName, List<String> startUrls){
		pool.addAll(startUrls);
		this.fileName = fileName;
	}
	
	public void start(){
		
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(this.fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		for(int idx=0; idx<MAX_OPERATORS; idx++){
			Operator op = new Operator(new HuaWeiSpider(), pool, pw);
			Thread th = new Thread(op);
			th.start();
		}
		
	}

}
