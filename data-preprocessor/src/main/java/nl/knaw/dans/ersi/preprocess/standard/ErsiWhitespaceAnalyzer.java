/*
 * Source code for Listing 9.5
 * 
 */

package nl.knaw.dans.ersi.preprocess.standard;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ErsiWhitespaceAnalyzer extends Analyzer {
	public static int minTernLength = 1;
	private static Logger LOG = LoggerFactory.getLogger(ErsiWhitespaceAnalyzer.class);
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
				// int startOffset = offsetAttribute.startOffset();
				// int endOffset = offsetAttribute.endOffset();
				String term = charTermAttribute.toString().toLowerCase();
				
				
				//Matcher m = alphabets.matcher(term);
				//if (m.matches()) {
					buf.append(term).append(" ");
					//LOG.debug(buf.toString());
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