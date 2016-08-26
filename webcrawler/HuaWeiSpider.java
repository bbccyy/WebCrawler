package webcrawler;

import static webcrawler.Config.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HuaWeiSpider implements Spider {
	
	private Document htmlDocument = null;
	private Item item = null;
	private List<String> urls = null;
	
	@Override
	public boolean crawl(String url) {
		item = null;
		urls = null;
		if(url==null || url.equals("")){
			return false;
		}
		System.out.println(Thread.currentThread().getName() + " going to crawl url: " + url);
		try{
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			htmlDocument = connection.get();
			if(connection.response().statusCode() == 200){
				System.out.println("Received web page at " + url);
			}
			if(!connection.response().contentType().contains("text/html")){
				System.out.println("**Failure** Retrieved something other than HTML");
				return false;
			}
		}catch(IOException e){
			System.out.println("Connection failure!");
			return false;
		}
		
		String[] url_parts = url.split("/");
		String layer = url_parts[url_parts.length-2];
		System.out.println("layer: "+layer);
		if(layer.equals("app")){
			item = new Item();
			parseItem(htmlDocument);
		}
		else{
			urls = new LinkedList<>();
			parse(htmlDocument);
		}
		
		return true;
	}
	
	public void parse(Document htmlDocument){
		Elements titles = htmlDocument.select("h4.title > a");
		if(!titles.isEmpty()){
			urls.add(nextUrl(htmlDocument.baseUri()));
			for(Element title : titles){
				urls.add(title.attr("href"));
			}	
		}
	}
	
	public void parseItem(Document htmlDocument){
		Element titleEntry = htmlDocument.select("div.app-info.flt").first();
		String url = htmlDocument.baseUri();
		String title = titleEntry.select("span.title").text();
		String thumbnailurl = titleEntry.select("img.app-ico").attr("src");
		String appid = getAppId(url);
		String intro = htmlDocument.getElementById("app_strdesc").text();
		List<String> recommandedList = new ArrayList<>();
		Element recommandedEntry = htmlDocument.select("div.unit-main.nofloat").first();
		Elements recommandedApps = recommandedEntry.select("p.name > a");
		for(Element App : recommandedApps){
			recommandedList.add(getAppId(App.attr("href")));
		}
		item.appid = appid;
		item.url = url;
		item.title = title;
		item.intro = intro;
		item.recommended = recommandedList;
		item.thumbnailurl = thumbnailurl;
	}
	
	String getAppId(String url){
		return url.substring(url.lastIndexOf('/')+1);
	}
	
	public List<String> getUrls(){
		return this.urls;
	}
	
	public Item getItems(){
		return this.item;
	}
	
	private String nextUrl(String url){
		int idx = url.lastIndexOf('/');
		int val = Integer.valueOf(url.substring(idx+1, url.length())) + 1;
		return url.substring(0,idx+1) + val;
	}

}
