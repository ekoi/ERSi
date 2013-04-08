/**
 * 
 */
package nl.knaw.dans.ersy.webui.secure.view;

import nl.knaw.dans.ersy.webui.secure.ErsyApplication;
import nl.knaw.dans.ersy.webui.secure.UserSession;
import nl.knaw.dans.ersy.webui.service.CookieService;
import nl.knaw.dans.ersy.webui.service.SessionProvider;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author akmi
 * 
 */
public class AdminPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1973574682018245001L;

	public AdminPanel(String id) {
		super(id);
        String userName = UserSession.get().userLoggedIn() ? UserSession.get().getUser().getLogin() : "Anonymous user";
        Label userNameLabel = new Label("userName", "Hi, " + userName);
        add(userNameLabel);
        
        add(new Link<Void>("logout") {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public void onClick() {
                CookieService cookieService = ErsyApplication.get().getCookieService();
                cookieService.removeCookieIfPresent(getRequest(), getResponse(), SessionProvider.REMEMBER_ME_LOGIN_COOKIE);
                cookieService.removeCookieIfPresent(getRequest(), getResponse(), SessionProvider.REMEMBER_ME_PASSWORD_COOKIE);

                UserSession.get().setUser(null);
                UserSession.get().invalidate();
            }
        });
	}

}
