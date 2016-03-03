package wfsnrnet.data;

import java.util.ArrayList;
import java.util.List;

public class TopicList {
	
	List<TopicData> list;
	
	public TopicList(){
		list = new ArrayList<TopicData>();
	}
	
	public void add(TopicData e){
		list.add(e);
	}
	
	public int size(){
		return list.size();
	}
	
	public TopicData get(int index){
		return list.get(index);
	}
	
	public String findName(String alias){
		for (TopicData e : list)
			if (e.alias().equals(alias))
				return e.name();
		return "";
	}
	
	public String toString(){
		String ret = " List with "+size()+" element(s) \n";
		for (TopicData e : list)
			ret += e.toString();
		return ret;
	}
}
