/**
 * 
 */
package nl.knaw.dans.ersy.webui;

import java.io.Serializable;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * @author akmi
 *
 */
public class ErsyNavigationLabel extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2684675680904090457L;
	private static final String LABEL_STYLE = "color:red";

	/**
	 * @param id
	 */
	public ErsyNavigationLabel(String id) {
		super(id);
	}

	/**
	 * @param id
	 * @param label
	 */
	public ErsyNavigationLabel(String id, String label) {
		super(id, label);
	}

	/**
	 * @param id
	 * @param label
	 */
	public ErsyNavigationLabel(String id, Serializable label) {
		super(id, label);
	}

	/**
	 * @param id
	 * @param model
	 */
	public ErsyNavigationLabel(String id, IModel<?> model) {
		super(id, model);
	}
	@Override
	protected void onComponentTag(ComponentTag tag) {
		String s = this.getPage().getClassRelativePath();
		String id = tag.getId();
		if (id.equals("homeLabel") && s.contains("HomePage")) {
			tag.put("style", LABEL_STYLE);
		} else if (id.equals("adminLabel") && s.contains("AdminPage")) {
			tag.put("style", LABEL_STYLE);
		} else if (id.equals("howLabel") && s.contains("HowItWorkPage")) {
			tag.put("style", LABEL_STYLE);
		} else if (id.equals("apiLabel") && s.contains("ApiPage")) {
			tag.put("style", LABEL_STYLE);
		} else if (id.equals("pubLabel") && s.contains("PublicationPage")) {
			tag.put("style", LABEL_STYLE);
		} else if (id.equals("contactLabel") && s.contains("ContactPage")) {
		tag.put("style", LABEL_STYLE);
	}
		super.onComponentTag(tag);
	}
}
