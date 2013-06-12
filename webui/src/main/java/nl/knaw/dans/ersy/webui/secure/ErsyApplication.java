package nl.knaw.dans.ersy.webui.secure;

import nl.knaw.dans.ersy.webui.ExceptionPage;
import nl.knaw.dans.ersy.webui.pages.ApiPage;
import nl.knaw.dans.ersy.webui.pages.ContactPage;
import nl.knaw.dans.ersy.webui.pages.HomePage;
import nl.knaw.dans.ersy.webui.pages.HowItWorkPage;
import nl.knaw.dans.ersy.webui.pages.publications.PublicationPage;
import nl.knaw.dans.ersy.webui.secure.view.AdminPage;
import nl.knaw.dans.ersy.webui.service.CookieService;
import nl.knaw.dans.ersy.webui.service.SessionProvider;
import nl.knaw.dans.ersy.webui.service.UserService;

import org.apache.wicket.Session;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

public class ErsyApplication extends WebApplication {

    private UserService userService = new UserService();
    private CookieService cookieService = new CookieService();
    private SessionProvider sessionProvider = new SessionProvider(userService, cookieService);

    public ErsyApplication() {
    	 // In case of unhandled exception redirect it to a custom page
		  this.getRequestCycleListeners().add(new AbstractRequestCycleListener() {
			  @Override
		      public IRequestHandler onException(RequestCycle cycle, Exception e) {
				  return new RenderPageRequestHandler(new PageProvider(new ExceptionPage(e)));
			  }
		  });
    }
    
    @Override
    public Class<HomePage> getHomePage() {
        return HomePage.class;
    }

    @Override
    public void init() {
        super.init();

        mountPage("/admin", AdminPage.class);
        mountPage("/how", HowItWorkPage.class);
        mountPage("/api", ApiPage.class);
        mountPage("/pub", PublicationPage.class);
        mountPage("/contact", ContactPage.class);
		getDebugSettings().setDevelopmentUtilitiesEnabled(false);
		getDebugSettings().setAjaxDebugModeEnabled(false);

    }

    public static ErsyApplication get() {
        return (ErsyApplication) WebApplication.get();
    }

    @Override
    public Session newSession(Request request, Response response) {
        return sessionProvider.createNewSession(request);
    }



    public UserService getUserService() {
        return userService;
    }

    public CookieService getCookieService() {
        return cookieService;
    }

    public SessionProvider getSessionProvider() {
        return sessionProvider;
    }


}
