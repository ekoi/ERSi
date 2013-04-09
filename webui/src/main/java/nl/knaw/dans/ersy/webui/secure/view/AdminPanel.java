/**
 * 
 */
package nl.knaw.dans.ersy.webui.secure.view;

import java.util.ArrayList;
import java.util.List;

import nl.knaw.dans.ersy.webui.secure.ErsyApplication;
import nl.knaw.dans.ersy.webui.secure.UserSession;
import nl.knaw.dans.ersy.webui.service.CookieService;
import nl.knaw.dans.ersy.webui.service.SessionProvider;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

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
        
        

		final String filePath = "/tmp/ersy/conf/configuration.xml";
        
		// create a list of ITab objects used to feed the tabbed panel
		List<ITab> tabs = new ArrayList<ITab>();
		
		tabs.add(new AbstractTab(new Model<String>("Statistics"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new TabStatisticsPanel(panelId);
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Configuration"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new TabConfigurationPanel(panelId, filePath);
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Data Extraction"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new TabDataExtractionPanel(panelId, filePath);
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("Data Cleaning"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new TabDataCleaningPanel(panelId, filePath);
			}
		});

		tabs.add(new AbstractTab(new Model<String>("Execute TM Process"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new TabDataMiningPanel(panelId);
			}
		});
		
		tabs.add(new AbstractTab(new Model<String>("TM in Progress"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new TabProcessInProgressPanel(panelId);
			}
		});
		

		add(new AjaxTabbedPanel("tabs", tabs));
	}

}
