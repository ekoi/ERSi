package nl.knaw.dans.ersi.datapreprocessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stemmer{
	private static Logger LOG = LoggerFactory.getLogger(Stemmer.class);
    public static String Stem(String text){
        StringBuffer result = new StringBuffer();
        if (text!=null && text.trim().length()>0){
            StringReader tReader = new StringReader(text);
            Analyzer analyzer = new DutchAnalyzer(Version.LUCENE_36);
            TokenStream tStream = analyzer.tokenStream("contents", tReader);
            CharTermAttribute charTermAttribute = tStream
					.addAttribute(CharTermAttribute.class);

            try {
                while (tStream.incrementToken()){
                    result.append(charTermAttribute.toString().toLowerCase());
                    result.append(" ");
                }
            } catch (IOException ioe){
                LOG.error("Error: "+ioe.getMessage());
            }
        }

        // If, for some reason, the stemming did not happen, return the original text
        if (result.length()==0)
            result.append(text);
        return result.toString().trim();
    }

    public static void main (String[] args) throws IOException{
    	StringBuffer sb = new StringBuffer();
    	List<String> l = getABRList();
    	for (String s : l) {
    		LOG.debug(s + "\t" + Stemmer.Stem(s.trim()));
    	}
    	for (String s : l) {
    		 String s2= Stemmer.Stem(s);
    		 String[] s3 = s2.split(" ");
    		 for (String s4 : s3)
    			 if (s4.length()>1)
    			 LOG.debug(s4);
//    		sb.append(s);
//    		sb.append(" ");
    	}
//       String s2= Stemmer.Stem(sb.toString());
//       LOG.debug(s2);
    	LOG.debug("===================");
    	List<String> ls = getStemmedABr();
    	for(String s: ls) {
    		LOG.debug(s);
    	}
    	//LOG.debug(getStemmedABr().size());
    }
    /**
	 * @param list
	 * @param s
	 */
	private static void extract(List<String> list, String s, String delimiter) {
		String ss[] = s.split(delimiter);
		for (String s1 : ss) {
			list.add(s1.trim());
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
        	line = line.toLowerCase().trim();
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
        return list;
	}
    
	public static List<String> getStemmedABr() {
		List<String> stemmedAbr = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		List<String> l;
		try {
			l = getABRList();

			for (String s : l) {
				String s2 = Stemmer.Stem(s).trim();
				LOG.debug(s2);
				String[] s3 = s2.split(" ");
				for (String s4 : s3) {
					s4 = s4.trim();
					if (s4.length() > 1 && !stemmedAbr.contains(s4))
						stemmedAbr.add(s4);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stemmedAbr;
	}
}