package nl.knaw.dans.ersy.webui.pages;

import java.util.ArrayList;
import java.util.List;

import nl.knaw.dans.ersy.webui.pages.search.RecursivePanel;
import nl.knaw.dans.ersy.webui.pages.search.SearchPanel;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * Navigation panel for the examples project.
 * 
 * @author Eko Indarto
 */

public final class RecommendationPage extends Panel {
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
	public RecommendationPage(String id, String exampleTitle, WebPage page) {
		super(id);

//		final Label rec1 = new Label("rec1", new Model<String>(""));
//		rec1.setOutputMarkupId(true);
//		add(rec1);
//
//		final Label rec2 = new Label("rec2", new Model<String>(""));
//		rec2.setOutputMarkupId(true);
//		add(rec2);
//
//		final Label rec3 = new Label("rec3", new Model<String>(""));
//		rec3.setOutputMarkupId(true);
//		add(rec3);

		add(new SearchPanel("searchPanel"));
		List<Object> l1 = new ArrayList<Object>();
       
        RecursivePanel rp = new RecursivePanel("panels", l1);
        rp.setOutputMarkupId(true);
        add(rp);
        this.setOutputMarkupId(true);
	}

}