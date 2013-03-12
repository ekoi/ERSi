package nl.knaw.dans.ersi.dataselector.util;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ListRecords")
public class ListRecords {

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	@ElementList(entry="Record", inline=true) 
	private List<Record> records;
	
	public ListRecords() {
	      super();
	   }

	public List<Record> getRecords() {
		return records;
	}  
	
	

}