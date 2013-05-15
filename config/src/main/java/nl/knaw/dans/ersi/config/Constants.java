package nl.knaw.dans.ersi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Constants {

	public static final String ERSY_HOME = readErsyHomeFromSystemProperties();
	private static Logger LOG = LoggerFactory
			.getLogger(Constants.class);
	
	public Constants() {
		
	}
	private static String readErsyHomeFromSystemProperties() {
		String ersyHome = System.getenv("ERSY_HOME");
		LOG.debug("=======ERSY_HOME=======: " + ersyHome);
		if (ersyHome != null)
			return ersyHome;
		return "/home/ekoi/ersy_home";
	}

}
