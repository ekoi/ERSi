package nl.knaw.dans.ersy.webui.secure.view;

import static nl.knaw.dans.ersy.webui.service.SessionProvider.REMEMBER_ME_DURATION_IN_DAYS;
import static nl.knaw.dans.ersy.webui.service.SessionProvider.REMEMBER_ME_LOGIN_COOKIE;
import static nl.knaw.dans.ersy.webui.service.SessionProvider.REMEMBER_ME_PASSWORD_COOKIE;
import nl.knaw.dans.ersy.webui.secure.ErsyApplication;
import nl.knaw.dans.ersy.webui.secure.UserSession;
import nl.knaw.dans.ersy.webui.secure.model.User;
import nl.knaw.dans.ersy.webui.service.CookieService;
import nl.knaw.dans.ersy.webui.service.UserService;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class LoginPanel extends Panel {

    private String login;
    private String password;
    private boolean rememberMe;

    public LoginPanel(String id) {
    	super(id);
        Form<Void> loginForm = new Form<Void>("form");
        add(loginForm);

        loginForm.add(new FeedbackPanel("feedback"));
        loginForm.add(new RequiredTextField<String>("login", new PropertyModel<String>(this, "login")));
        loginForm.add(new PasswordTextField("password", new PropertyModel<String>(this, "password")));
        loginForm.add(new CheckBox("rememberMe", new PropertyModel<Boolean>(this, "rememberMe")));

        Button submit = new Button("submit") {
            @Override
            public void onSubmit() {
                UserService userService = ErsyApplication.get().getUserService();

                User user = userService.findByLoginAndPassword(login, password);

                if(user == null) {
                    error("Invalid login and/or password. Please try again.");
                }
                else {
                    UserSession.get().setUser(user);

                    if(rememberMe) {
                        CookieService cookieService = ErsyApplication.get().getCookieService();
                        cookieService.saveCookie(getResponse(), REMEMBER_ME_LOGIN_COOKIE, user.getLogin(), REMEMBER_ME_DURATION_IN_DAYS);
                        cookieService.saveCookie(getResponse(), REMEMBER_ME_PASSWORD_COOKIE, user.getPassword(), REMEMBER_ME_DURATION_IN_DAYS);
                    }

                    setResponsePage(AdminPage.class);
                }
            }
        };

        loginForm.add(submit);
    }
}
