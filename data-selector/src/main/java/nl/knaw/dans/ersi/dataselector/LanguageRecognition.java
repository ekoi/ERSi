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
			e.printStackTrace();
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
			String eko = "Emmer-Compascuum, Herstructurering Runde (Dr.) onderzoeksrapport";
			String eko1 = "As you have already known, I am using the most newest technology";
			String eko2= "Berhenti tak ada tempat di jalan ini. Sikap lambat berarti mati. Mereka yang bergerak, merekalah yang maju ke muka. Mereka yang menunggu sejenak sekalipun, pasti tergilas";
			String result = dr.detect(eko2);
			System.out.println(result);
    	} catch (LangDetectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
