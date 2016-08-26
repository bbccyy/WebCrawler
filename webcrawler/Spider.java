package webcrawler;

import java.util.List;

public interface Spider {
	public boolean crawl (String url);
	public List<String> getUrls();
	public Item getItems();

}
