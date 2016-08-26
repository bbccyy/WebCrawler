package webcrawler;

import java.io.PrintWriter;
import java.util.List;
import java.util.Queue;

public class Operator implements Runnable {
	
	private Queue<String> pool = null;
	private Spider spider = null;
	private PrintWriter pw = null;
	
	private static volatile int ActiveOperator = 0;
	
	public Operator(Spider spider, Queue<String> pool){
		this.pool = pool;
		this.spider = spider;
		ActiveOperator++;
	}
	
	public Operator(Spider spider, Queue<String> pool, PrintWriter pw) {
		this.pool = pool;
		this.spider = spider;
		this.pw = pw;
		ActiveOperator++;
	}

	@Override
	public void run(){
		System.out.println(Thread.currentThread().getName() + " run!" + "    Act=" + ActiveOperator);
		while(!Thread.currentThread().isInterrupted()){
			String nextUrl = fetchUrl();
			System.out.println(Thread.currentThread().getName() + " fetch nextUrl: " + nextUrl);
			
			if(nextUrl==null) continue;
			
			if(!spider.crawl(nextUrl)){
				feedUrl(nextUrl);
				continue;
			}
			Item item = spider.getItems();
			List<String> urls = spider.getUrls();
			
			if(item!=null){
				System.out.println(Thread.currentThread().getName() +" get an item!");
				writeToFile(item);
			}
			
			if(urls!=null && !urls.isEmpty()){
				System.out.println(Thread.currentThread().getName() +" get a bunch of urls!");
				feedUrl(urls);
			}
		}
		
	}
	
	private void writeToFile(Item item){
		synchronized(pw){
			pw.println(item.toString());
		}
	}
	
	
	private void feedUrl(String url){
		synchronized(pool){			
			pool.add(url);			
			pool.notifyAll();
		}
	}
	
	
	private void feedUrl(List<String> urls){
		synchronized(pool){
			for(String url : urls){
				pool.add(url);
			}
			pool.notifyAll();
		}
	}
	
	private String fetchUrl(){
		String url = null;
		System.out.println(Thread.currentThread().getName() + " inside fetchUrl, pool.size="+pool.size());
		synchronized(pool){
			System.out.println(Thread.currentThread().getName() + " enter pool block!");
			while(url==null){
				if(pool.isEmpty()){
					System.out.println("but pool is empty...");
					ActiveOperator--;
					if(ActiveOperator == 0){
						Thread.currentThread().interrupt();
						pw.close();
						pool.notifyAll();
						return null;
					}
					try {
						pool.wait();
					} catch (InterruptedException e) {
						return null;
					}
					if(ActiveOperator == 0){
						Thread.currentThread().interrupt();
						return null;
					}
					ActiveOperator++;
				}
				url = pool.poll();
				System.out.println("Get an url: " + url);
			}
		}		
		return url;
	}
	
}
