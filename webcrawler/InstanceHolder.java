package webcrawler;

import java.util.LinkedList;
import java.util.Queue;

public class InstanceHolder{
	private static final Queue<String> urlPool = new LinkedList<>();
	
	public static Queue<String> getPool(){
		return urlPool;
	}
}
