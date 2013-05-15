package nl.knaw.dans.ersi.dataselector;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;

public class LanguageRecognition {
	
	private static Logger LOG = LoggerFactory.getLogger(LanguageRecognition.class);
	public static String NL = "nl";
	public static String EN = "en";
	public static String FR = "fr";
	public static String DE = "de";
	public static String UN_RECOGNIZED = "[UN-RECOGNIZED]";
	
	
	public LanguageRecognition() throws LangDetectException{
		init(new String[]{EN, NL, FR, DE});
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
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
        return UN_RECOGNIZED + text;
        
    }
    public ArrayList<Language> detectLangs(String text) throws LangDetectException {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.getProbabilities();
    }
    
    public static void main(String args[]) {
    	try {
			LanguageRecognition dr = new LanguageRecognition();
			String result = dr.detect("Emmer-Compascuum, Herstructurering Runde (Dr.) onderzoeksrapport");
			LOG.debug(result);
    	} catch (LangDetectException e) {
			LOG.error(e.getMessage());
		}
    }

}
