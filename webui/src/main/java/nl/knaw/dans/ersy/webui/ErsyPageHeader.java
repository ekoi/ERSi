package nl.knaw.dans.ersy.webui;

import javax.servlet.ServletContext;

import nl.knaw.dans.ersy.webui.pages.ApiPage;
import nl.knaw.dans.ersy.webui.pages.ContactPage;
import nl.knaw.dans.ersy.webui.pages.HomePage;
import nl.knaw.dans.ersy.webui.pages.HowItWorkPage;
import nl.knaw.dans.ersy.webui.pages.publications.PublicationPage;
import nl.knaw.dans.ersy.webui.secure.view.AdminPage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * Navigation panel for the ersy project.
 * 
 * @author Eko Indarto
 */
public final class ErsyPageHeader extends Panel
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
	public ErsyPageHeader(String id, WebPage page)
	{
		super(id);
		add(new BookmarkablePageLink<HomePage>("home", HomePage.class));
		add(new BookmarkablePageLink<AdminPage>("admin", AdminPage.class));
		add(new BookmarkablePageLink<HowItWorkPage>("how", HowItWorkPage.class));
		add(new BookmarkablePageLink<ApiPage>("api", ApiPage.class));
		add(new BookmarkablePageLink<PublicationPage>("pub", PublicationPage.class));
		add(new BookmarkablePageLink<ContactPage>("contact", ContactPage.class));
	}
	
	public static String getRootContext(){
		 
		String rootContext = "";
 
		WebApplication webApplication = WebApplication.get();
		if(webApplication!=null){
			ServletContext servletContext = webApplication.getServletContext();
			if(servletContext!=null){
				rootContext = servletContext.getServletContextName();
			}else{
				//do nothing
			}
		}else{
			//do nothing
		}
 
		return rootContext;
 
}
}
