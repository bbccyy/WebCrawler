package webcrawler;

import java.util.List;

public class Item {
	public String title;
	public String url;
	public String appid;
	public String intro;
	public List<String> recommended;
	public String thumbnailurl;
	
	public Item(){}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(appid).append('\t').append(title).append('\t').append(url).append('\t');
		if(recommended != null){
			for(String aid : recommended){
				sb.append(aid).append(',');
			}
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	
}