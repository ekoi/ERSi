package nl.knaw.dans.ersi.config;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class SimpleDimensionReductionConfig {
	@Element (name="min-word-length")
	int minWordLength;
	
	@ElementList(name="skip-word")
	private List<String> skipWord;

	public int getMinWordLength() {
		return minWordLength;
	}

	public void setMinWordLength(int minWordLength) {
		this.minWordLength = minWordLength;
	}

	public List<String> getSkipWord() {
		return skipWord;
	}

	public void setSkipWord(List<String> skipWord) {
		this.skipWord = skipWord;
	}
}
