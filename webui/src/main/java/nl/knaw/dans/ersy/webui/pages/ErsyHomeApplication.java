package nl.knaw.dans.ersy.webui.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see nl.knaw.dans.ersy.webui.Start#main(String[])
 */
public class ErsyHomeApplication extends WebApplication
{    	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		
		mountPage("/how", HowItWorkPage.class);
		mountPage("/api", ApiPage.class);
		mountPage("/pub", PublicationPage.class);
		mountPage("/contact", ContactPage.class);
		getDebugSettings().setDevelopmentUtilitiesEnabled(true);
		getDebugSettings().setAjaxDebugModeEnabled(true);
	}
}
