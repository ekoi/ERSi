package nl.knaw.dans.ersy.webui.pages;

import nl.knaw.dans.ersy.webui.ErsyBasePage;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author Eko Indarto
 */
public class HomePage extends ErsyBasePage
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -243672095955455501L;
	/**
	 * Constructor
	 * 
	 * @param pageParameters
	 */
	public HomePage(final PageParameters pageParameters)
	{
		super(pageParameters);

		add(new RecommendationPage("content", "", this));
	}
	public HomePage() {
		super(new PageParameters());

		add(new RecommendationPage("content", "", this));
	}

	
}
