package nl.knaw.dans.ersi.dataselector;

import java.util.ArrayList;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;

public class LanguageRecognition {
	public static String NL = "nl";
	public static String EN = "en";
	public static String FR = "fr";
	public static String DE = "de";
	public static String ID = "id";
	public static String UN_RECOGNIZED = "[UN-RECOGNIZED]";
	
	
	public LanguageRecognition() throws LangDetectException{
		init(new String[]{EN, NL, FR, DE, ID});
	}
	private void init(String[] profileDirectory) throws LangDetectException {
        DetectorFactory.loadProfiles(profileDirectory);
    }
    public String detect(String text) {
        Detector detector;
		try {
			detector = DetectorFactory.create();
			detector.append(text.toLowerCase());
			return detector.detect();
		} catch (LangDetectException e) {
			e.printStackTrace();
		}
        return UN_RECOGNIZED + text;
        
    }
    public ArrayList<Language> detectLangs(String text) throws LangDetectException {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.getProbabilities();
    }
  
}
