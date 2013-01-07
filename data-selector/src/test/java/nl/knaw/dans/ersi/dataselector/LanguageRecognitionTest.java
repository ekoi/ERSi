/**
 * 
 */
package nl.knaw.dans.ersi.dataselector;

import junit.framework.TestCase;

/**
 * @author akmi
 *
 */
public class LanguageRecognitionTest extends TestCase {
	
	private static LanguageRecognition languageRecognition;
	private static String text_nl;
	private static String text_en;
	private static String text_id;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		text_nl = "Emmer-Compascuum, Herstructurering Runde (Dr.) onderzoeksrapport";
		text_en = "Be not entangled in this world of days and nights; Thou hast another time and space as well.";
		text_id = "Berhenti tak ada tempat di jalan ini. Sikap lambat berarti mati. Mereka yang bergerak, merekalah yang maju ke muka. Mereka yang menunggu sejenak sekalipun, pasti tergilas";
		languageRecognition = new LanguageRecognition();
	}

	public void testDetect() {
		String lang_nl = languageRecognition.detect(text_nl);	
		assertTrue (lang_nl.equals(LanguageRecognition.NL));
		assertFalse(lang_nl.equals(LanguageRecognition.UN_RECOGNIZED));
		
		String lang_en = languageRecognition.detect(text_en);	
		assertTrue (lang_en.equals(LanguageRecognition.EN));
		assertFalse(lang_en.equals(LanguageRecognition.UN_RECOGNIZED));
		
		String lang_id = languageRecognition.detect(text_id);	
		assertTrue (lang_id.equals(LanguageRecognition.ID));
		assertFalse(lang_id.equals(LanguageRecognition.UN_RECOGNIZED));
		
		
	}
}
