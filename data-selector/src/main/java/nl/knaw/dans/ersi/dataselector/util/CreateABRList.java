package nl.knaw.dans.ersi.dataselector.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateABRList {

	private static Logger LOG = LoggerFactory.getLogger(CreateABRList.class);
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		FileReader a = new FileReader(System.getenv("ERSY_HOME") + "/abrlist.txt");
        BufferedReader br = new BufferedReader(a);
        String line;
        List<String> list = new ArrayList<String>();
        int i = 0;
        while((line = br.readLine()) != null) {
            // do something with line.
        	int index = line.indexOf("...");
        	//LOG.debug(line);
        	if (index > 0) {
        		String s = line.substring(0, index);
        		s = s.trim().toLowerCase();
        		if (s.indexOf(":") > 0 ) {
        			extract(list, s, ":");
        		} else if (s.indexOf(",")>0) {
        			extract(list, s, ",");
        		} else if (s.indexOf("/") > 0){
        			extract(list, s, "/");
        		} else {
        			list.add(s);
        		}
        	} else {
        		LOG.debug("+++++++++++++++" + line);
        	}
        	
       }
        
	}

	/**
	 * @param list
	 * @param s
	 */
	private static void extract(List<String> list, String s, String delimiter) {
		String ss[] = s.split(delimiter);
		for (String s1 : ss) {
			list.add(s1);
		}
	}

	public static List<String> getABRList() throws IOException {
		FileReader a = new FileReader(System.getenv("ERSY_HOME") + "/ABR/" + "abrlist.txt");
        BufferedReader br = new BufferedReader(a);
        String line;
        List<String> list = new ArrayList<String>();
        int i = 0;
        while((line = br.readLine()) != null) {
            // do something with line.
        	int index = line.indexOf("...");
        	//LOG.debug(line);
        	if (index > 0) {
        		String s = line.substring(0, index);
        		s = s.trim().toLowerCase();
        		if (s.indexOf(":") > 0 ) {
        			extract(list, s, ":");
        		} else if (s.indexOf(",")>0) {
        			extract(list, s, ",");
        		} else if (s.indexOf("/") > 0){
        			extract(list, s, "/");
        		} else {
        			list.add(s);
        		}
        	} else {
        		LOG.debug("+++++++++++++++" + line);
        	}
       }
        LOG.debug("size: " + list.size());
        return list;
	}
}
