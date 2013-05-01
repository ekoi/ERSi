package nl.knaw.dans.ersi.datapreprocessor;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.PorterStemmer;

public class App3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> abrlist = new ArrayList<String>();
		abrlist.add("eten");
		abrlist.add("drinken");
				
		PorterStemmer stem = new PorterStemmer();
		for (String word : abrlist) {
		 stem.setCurrent(word);
		 stem.stem();
		 String result = stem.getCurrent();
		 System.out.println(result);
		}
		
		DutchAnalyzer en_an = new DutchAnalyzer(Version.LUCENE_36);
		QueryParser parser = new QueryParser(Version.LUCENE_36, "your_field", en_an);
		String str = "eten drinken slapen";
		try {
			System.out.println("result: " + parser.parse(str));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
