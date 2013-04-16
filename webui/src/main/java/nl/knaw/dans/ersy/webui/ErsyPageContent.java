package nl.knaw.dans.ersy.webui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Navigation panel for the ersy project.
 * 
 * @author Eko Indarto
 */

public final class ErsyPageContent extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 970185289293886277L;

	/**
	 * Construct.
	 * 
	 * @param id
	 *            id of the component
	 * @param exampleTitle
	 *            title of the example
	 * @param page
	 *            The example page
	 */
	public ErsyPageContent(String id, String exampleTitle, WebPage page)
	{
		super(id);

		add(new Label("exampleTitle", exampleTitle));


	}
}
