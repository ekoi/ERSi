package nl.knaw.dans.ersy.webui.pages.search;

import java.util.List;

import nl.knaw.dans.ersy.orm.Recommendation;
import nl.knaw.dans.ersy.orm.RecommendationFromDual.DRM;
import nl.knaw.dans.ersy.orm.RecommendationPid;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * This example list knows how to display sublists. It expects a list where each
 * element is either a string or another list.
 * 
 */
public final class RecursivePanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3951206917295739260L;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            The id of this component
	 * @param list
	 *            a list where each element is either a string or another list
	 */
	public RecursivePanel(final String id, List<Object> list, String pid) {
		super(id);
		add(new Rows("rows", list, pid));
		setVersioned(false);
	}

	/**
	 * The list class.
	 */
	private static class Rows extends ListView<Object> {
		private static final long serialVersionUID = -4393927920778239416L;

		private String pid;

		/**
		 * Construct.
		 * 
		 * @param name
		 *            name of the component
		 * @param list
		 *            a list where each element is either a string or another
		 *            list
		 */
		public Rows(String name, List<Object> list, String pid) {
			super(name, list);
			this.pid = pid;
		}

		/**
		 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected void populateItem(ListItem<Object> listItem) {
			Object modelObject = listItem.getDefaultModelObject();

			if (modelObject instanceof List) {
				// create a panel that renders the sub list
				RecursivePanel nested = new RecursivePanel("nested",
						(List<Object>) modelObject, pid);
				listItem.add(nested);
				// if the current element is a list, we create a dummy row/
				// label element
				// as we have to confirm to our HTML definition, and set it's
				// visibility
				// property to false as we do not want LI tags to be rendered.
				WebMarkupContainer row = new WebMarkupContainer("row");
				row.setVisible(false);
				row.add(new WebMarkupContainer("label"));
				listItem.add(row);
			} else {
				// if the current element is not a list, we create a dummy panel
				// element
				// to confirm to our HTML definition, and set this panel's
				// visibility
				// property to false to avoid having the UL tag rendered
				RecursivePanel nested = new RecursivePanel("nested", null, pid);
				nested.setVisible(false);
				listItem.add(nested);
				// add the row (with the LI element attached, and the label with
				// the
				// row's actual value to display
				final WebMarkupContainer row = new WebMarkupContainer("row");
				row.setOutputMarkupId(true);
				final SearchHit sh = (SearchHit) modelObject;
				row.add(new SearchResultPanel("label", new Model<SearchHit>(sh)));

				final Model<Integer> idModel = new Model<Integer>();
				final Model<Integer> votesModel = new Model<Integer>(0);

				List<RecommendationPid> recPids = Recommendation
						.findRelevancePids(DRM.STANDARD, pid);
				for (RecommendationPid recPid : recPids) {
					if (recPid.getPid().equals(sh.getPid())) {
						idModel.setObject(recPid.getId());
						votesModel.setObject(Recommendation.getRating(recPid.getId()));
						break;
					}
					//System.out.println(sh.getPid() + " " + recPid.getPid());
				}

				row.add(new Label("votes", votesModel));

				row.add(new AjaxLink<Void>("upvote") {
					private static final long serialVersionUID = -2304057805873427370L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Recommendation.updateRating(idModel.getObject(), true);
						target.add(row);
					}
				});

				row.add(new AjaxLink<Void>("downvote") {
					private static final long serialVersionUID = -7029145553763006348L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						if (votesModel == null || votesModel.getObject() == null) {
							votesModel.setObject(-1);
						} else {
							votesModel.setObject(votesModel.getObject() - 1);
						}
						Recommendation.updateRating(idModel.getObject(), false);
						target.add(row);
					}
				});

				listItem.add(row);
			}
		}
	}
}