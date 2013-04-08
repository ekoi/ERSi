package nl.knaw.dans.ersy.webui.secure.view;

import nl.knaw.dans.ersy.webui.ErsyBasePage;
import nl.knaw.dans.ersy.webui.secure.ErsyApplication;
import nl.knaw.dans.ersy.webui.secure.UserSession;
import nl.knaw.dans.ersy.webui.service.CookieService;
import nl.knaw.dans.ersy.webui.service.SessionProvider;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AdminPage extends ErsyBasePage {
    private static final long serialVersionUID = 1L;

    public AdminPage(final PageParameters parameters) {
        super(parameters);
       boolean userLogin =  UserSession.get().userLoggedIn();
       boolean userNotLogin = UserSession.get().userNotLoggedIn();

        LoginPanel loginPanel = new LoginPanel("loginPanel");
        loginPanel.setVisible(userNotLogin);
        add(loginPanel);
        
        AdminPanel adminPanel = new AdminPanel("adminPanel");
        adminPanel.setVisible(userLogin);
        add(adminPanel);
    }

}
