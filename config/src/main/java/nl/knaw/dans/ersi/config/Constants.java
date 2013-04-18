package nl.knaw.dans.ersi.config;

import java.util.Properties;

public final class Constants {

	public static final String ERSY_HOME = readErsyHomeFromSystemProperties();;
	
	public Constants() {
		
	}
	private static String readErsyHomeFromSystemProperties() {
		String ersyHome = System.getenv("ERSY_HOME");
		System.out.println("ersyHome: " + ersyHome);
		if (ersyHome != null)
			return ersyHome;
		return "/tmp/ersy";
	}

}
