/*
 * Source code for Listing 9.5
 * 
 */

package nl.knaw.dans.ersi.preprocess.standard;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class ErsiDutchAnalyzer extends Analyzer {
	private static int i=0;

	private final Pattern alphabets = Pattern.compile("[a-z-]+|^[^\\-]+\\-[^\\-]+$");

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		DutchAnalyzer da = new DutchAnalyzer(Version.LUCENE_36);
		
		TokenStream result = null;
		try {
			result = da.reusableTokenStream(fieldName, reader);
			// OffsetAttribute offsetAttribute =
			// result.addAttribute(OffsetAttribute.class);
			CharTermAttribute charTermAttribute = result
					.addAttribute(CharTermAttribute.class);

			StringBuilder buf = new StringBuilder();
			while (result.incrementToken()) {
				// int startOffset = offsetAttribute.startOffset();
				// int endOffset = offsetAttribute.endOffset();
				String term = charTermAttribute.toString().toLowerCase();
				if (term.length() < 3)
					continue;
				if (term.contains("onderzoeksrapport")) {
					i++;
					System.out.println(i);
					continue;
				}
				Matcher m = alphabets.matcher(term);
				
				if (m.matches()) {
					buf.append(term).append(" ");
					System.out.println(term);
				}
				if (term.indexOf("brabant") > -1)
				System.out.println("====================================================");
			}
			return new WhitespaceTokenizer(Version.LUCENE_36, new StringReader(
					buf.toString()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		da.close();
		return result;
	}
}