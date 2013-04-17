package nl.knaw.dans.ersy.webui;

import nl.knaw.dans.ersy.webui.pages.ApiPage;
import nl.knaw.dans.ersy.webui.pages.ContactPage;
import nl.knaw.dans.ersy.webui.pages.HomePage;
import nl.knaw.dans.ersy.webui.pages.HowItWorkPage;
import nl.knaw.dans.ersy.webui.pages.publications.PublicationPage;
import nl.knaw.dans.ersy.webui.secure.view.AdminPage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Navigation panel for the ersy project.
 * 
 * @author Eko Indarto
 */
public final class ErsyPageHeader extends Panel {
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
	public ErsyPageHeader(String id, WebPage page) {
		super(id);
		add(new BookmarkablePageLink<HomePage>("home", HomePage.class).add(new ErsyNavigationLabel("homeLabel", "Home")));
		add(new BookmarkablePageLink<AdminPage>("admin", AdminPage.class).add(new ErsyNavigationLabel("adminLabel", "Admin")));
		add(new BookmarkablePageLink<HowItWorkPage>("how", HowItWorkPage.class).add(new ErsyNavigationLabel("howLabel", "How it works")));
		add(new BookmarkablePageLink<ApiPage>("api", ApiPage.class).add(new ErsyNavigationLabel("apiLabel", "API")));
		add(new BookmarkablePageLink<PublicationPage>("pub",PublicationPage.class).add(new ErsyNavigationLabel("pubLabel", "Publications")));
		add(new BookmarkablePageLink<ContactPage>("contact", ContactPage.class).add(new ErsyNavigationLabel("contactLabel", "Contact")));
	}
}
