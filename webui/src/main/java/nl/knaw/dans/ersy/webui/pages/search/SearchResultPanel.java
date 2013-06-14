/**
 * 
 */
package nl.knaw.dans.ersy.webui.pages.search;

import java.util.ArrayList;

import nl.knaw.dans.ersy.orm.Recommendation;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author akmi
 *
 */
public class SearchResultPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8702476579493755108L;
	private static Logger LOG = LoggerFactory.getLogger(SearchResultPanel.class);

	public SearchResultPanel(String id, IModel<SearchHit> model) {
		super(id, model);
		SearchHit hit = model.getObject();
		 add(new Label("storeId", hit.getStoreId()));
			add(new Label("title", hit.getTitle()));
			add(new Label("creator", hit.getCreator()));
			add(new Label("dateCreated", hit.getDateCreated()));
			Label desc = new Label("description", shorten(head(hit.getDescription()), 150));
			desc.setEscapeModelStrings(false);
			add(desc);
	}
	
	private static String shorten(String s, int size) {
		int len = s.length();
		if (len> (size - 1))
			return s.substring(0, size) + " ...";
		if (len < 75) {
			StringBuffer sb = new StringBuffer(s);
			for (int i=75; i>len; i--){
				sb.append("&nbsp;");
			}
			return sb.toString();
		}
		return s.length() > (size - 1) ? s.substring(0, size) + " ..." : s;
	}

	private static String head(ArrayList<String> list) {
		return list.size() > 0 ? list.get(0) : "";
	}
}
