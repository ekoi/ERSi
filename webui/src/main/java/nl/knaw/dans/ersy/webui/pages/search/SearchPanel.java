package nl.knaw.dans.ersy.webui.pages.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import nl.knaw.dans.ersy.orm.Recommendation;
import nl.knaw.dans.ersy.orm.RecommendationFromDual;
import nl.knaw.dans.ersy.orm.RecommendationPid;
import nl.knaw.dans.ersy.orm.RecommendationFromDual.DRM;
import nl.knaw.dans.ersy.webui.pages.RecommendationPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

public class SearchPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6892480325207277638L;

	public SearchPanel(String id) {
		super(id);
		setOutputMarkupId(true);
		
		Form<Void> form = new Form<Void>("form");
		add(form);

		final TextField<String> field = new TextField<String>("query",
				new Model<String>(""));
		form.add(field);
		
		final RepeatingView searchResults = new RepeatingView("searchResults");
		searchResults.setOutputMarkupId(true);
		add(searchResults);
		
		form.add(new IndicatingAjaxButton("submitSearch", form) {
			private static final long serialVersionUID = -8657955624468662411L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				String query = field.getModelObject();
				try {
					searchResults.removeAll();
					ArrayList<SearchHit> hits = EasyRestConnector.get().search(query);
					for (SearchHit hit : hits) {
			            AbstractItem item = new AbstractItem(searchResults.newChildId());
			            AjaxLink al = new AjaxLink<Void>("c1-link")
			                    {
	                        @Override
	                        public void onClick(AjaxRequestTarget target)
	                        {
	                            Page p = this.getPage();
	                            Component c = p.get(1);
	                            if (c instanceof RecommendationPage) {
	                            	RecommendationPage rp = (RecommendationPage)c;
	                            	Component cl = rp.get("panels");
	                            	List<RecommendationPid> rl = RecommendationFromDual.findRelevancePids(DRM.STANDARD.toString(), "");
	                            	List<Object> l = new ArrayList<Object>();
	                            	for (RecommendationPid r : rl) {
	                            		l.add(r.getPid());
	                            	}
	                            	
	                            	try {
										ArrayList<SearchHit> hits = SparqlConnector.get().search("");
									} catch (XPathExpressionException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	                            	
	                            	rp.remove(cl);
	                            	rp.addOrReplace(new RecursivePanel("panels", l));
	                            	 target.add(rp);
	                            }
	                           
	                        }
	                    };
			            item.add(al);
			            al.add(new Label("storeId", hit.getStoreId()));
						al.add(new Label("title", hit.getTitle()));
						al.add(new Label("creator", hit.getCreator()));
						al.add(new Label("dateCreated", hit.getDateCreated()));
						al.add(new Label("description", shorten(head(hit.getDescription()), 150)));
						searchResults.add(item);
					}
					target.add(searchResults.getParent());
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	private static String shorten(String s, int size) {
		return s.length() > (size - 1) ? s.substring(0, size) + " ..." : s;
	}

	private static String head(ArrayList<String> list) {
		return list.size() > 0 ? list.get(0) : "";
	}

}
