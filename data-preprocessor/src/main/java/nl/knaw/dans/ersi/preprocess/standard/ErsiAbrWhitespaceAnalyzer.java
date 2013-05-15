/*
 * Source code for Listing 9.5
 * 
 */

package nl.knaw.dans.ersi.preprocess.standard;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ErsiAbrWhitespaceAnalyzer extends Analyzer {
	private static Logger LOG = LoggerFactory.getLogger(ErsiAbrWhitespaceAnalyzer.class);	
	private static int numberOfTokens;
	private final Pattern alphabets = Pattern.compile("[a-z-]+|^[^\\-]+\\-[^\\-]+$");

	public static int minTernLength = 1;
	
	public static List<String> ignoreWords =  new ArrayList<String>();
	
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		WhitespaceAnalyzer da = new WhitespaceAnalyzer(Version.LUCENE_36);
		
		TokenStream result = null;
		try {
			result = da.tokenStream(fieldName, reader);

			// OffsetAttribute offsetAttribute =
			// result.addAttribute(OffsetAttribute.class);
			CharTermAttribute charTermAttribute = result
					.addAttribute(CharTermAttribute.class);

			StringBuilder buf = new StringBuilder();
			while (result.incrementToken()) {
				numberOfTokens++;
				// int startOffset = offsetAttribute.startOffset();
				// int endOffset = offsetAttribute.endOffset();
				String term = charTermAttribute.toString().toLowerCase();
				if (term.length() <= minTernLength)
					continue;
				if (ignoreWords.contains(term)) {
					continue;
				}
				
				//Matcher m = alphabets.matcher(term);
				//if (m.matches()) {
					buf.append(term).append(" ");
					LOG.debug(buf.toString());
				//}
			}
			//LOG.debug("Number of tokens: " + numberOfTokens);
			return new WhitespaceTokenizer(Version.LUCENE_36, new StringReader(
					buf.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		da.close();
		
		return result;
	}
}