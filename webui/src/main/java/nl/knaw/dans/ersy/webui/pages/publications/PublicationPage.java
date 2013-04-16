package nl.knaw.dans.ersy.webui.pages.publications;

import nl.knaw.dans.ersy.webui.ErsyBasePage;

import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author Eko Indarto
 */
public class PublicationPage extends ErsyBasePage
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
	public PublicationPage(final PageParameters pageParameters)
	{
		super(pageParameters);
		add(new PublicationPanel("publicationPanel"));
		
	}

	
}
