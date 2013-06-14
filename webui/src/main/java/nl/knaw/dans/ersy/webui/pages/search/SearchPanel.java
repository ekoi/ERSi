package nl.knaw.dans.ersy.webui.pages.search;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import nl.knaw.dans.ersy.orm.Recommendation;
import nl.knaw.dans.ersy.orm.RecommendationFromDual.DRM;
import nl.knaw.dans.ersy.orm.RecommendationPid;
import nl.knaw.dans.ersy.webui.pages.RecommendationPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6892480325207277638L;
	private static Logger LOG = LoggerFactory.getLogger(SearchPanel.class);

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
			            AjaxLink<Void> al = new AjaxLink<Void>("c1-link")
			                    {
	                        /**
									 * 
									 */
									private static final long serialVersionUID = -4091661966857519945L;

							@Override
	                        public void onClick(AjaxRequestTarget target)
	                        {
	                        	String pid = "";
	                            Page p = this.getPage();
	                            Component obj = this.get("SearchResultPanel");
	                            if (obj instanceof SearchResultPanel) {
	                            	SearchResultPanel srp = (SearchResultPanel)obj;
	                            	Component so = srp.get("storeId");
	                            	pid = so.getDefaultModelObjectAsString();
	                            	
	                            	
	                            }
	                            Component c = p.get(1);
	                            if (c instanceof RecommendationPage) {
	                            	RecommendationPage rp = (RecommendationPage)c;
	                            	Component recommendationPanels = rp.get("recommendationPanels");
	                            	
	                            	Component stdRecCheckbox = rp.get("stdRecCheckbox");
	                            	boolean os = (Boolean) stdRecCheckbox.getDefaultModelObject();
	                            	
	                            	Component abrRecCheckbox = rp.get("abrRecCheckbox");
	                            	boolean oa = (Boolean) abrRecCheckbox.getDefaultModelObject();
	                            	
	                            	Component locationRecCheckbox = rp.get("locationRecCheckbox");
	                            	boolean ol = (Boolean) locationRecCheckbox.getDefaultModelObject();
	                            	
	                            	List<String> pids = new ArrayList<String>();
	                            	if (oa) {
	                            		List<RecommendationPid> rl = Recommendation.findRelevancePids(DRM.STANDARD, pid);
		                            	LOG.info("Number of recommendations: " + rl.size());
		                            	for (RecommendationPid r : rl) {
		                            		pids.add(r.getPid().toString());
		                            	}
	                            	} else if (os) {
	                            		
	                            	} else if (ol){
										pids = SparqlConnector.get().search(pid);
	                            	} else {
	                            		
	                            	}
	                            	List<Object> recs = new ArrayList<Object>();
	                            	recs.add(retrieveRecommendationDatasets(pids));
	                            	rp.remove(recommendationPanels);
	                            	rp.addOrReplace(new RecursivePanel("recommendationPanels", recs));
	                            	 target.add(rp);
	                            }
	                           
	                        }

							/**
							 * @param recs
							 * @param pids
							 * @throws XPathExpressionException
							 */
							private List<Object> retrieveRecommendationDatasets(List<String> pids) {
								List<Object> recs = new ArrayList<Object>();
								try {
									if (!pids.isEmpty()) {
										ArrayList<SearchHit> hits = EasyRestConnector.get().pidsSearch(pids);
										recs.add(hits);
									}
								} catch (XPathExpressionException e) {
									e.printStackTrace();
								}
								return recs;
							}
	                    };
			            item.add(al);
			            al.add(new SearchResultPanel("SearchResultPanel", new Model<SearchHit>(hit)));
						searchResults.add(item);
					}
					target.add(searchResults.getParent());
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
//	
//	private static String shorten(String s, int size) {
//		return s.length() > (size - 1) ? s.substring(0, size) + " ..." : s;
//	}
//
//	private static String head(ArrayList<String> list) {
//		return list.size() > 0 ? list.get(0) : "";
//	}

}
