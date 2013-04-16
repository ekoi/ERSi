package nl.knaw.dans.ersy.webui;

import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * Base class for all Ersy pages.
 * 
 * @author Eko Indarto
 * 
 */
public class ErsyBasePage extends WebPage
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final CssResourceReference MYPAGE_CSS = new CssResourceReference(ErsyBasePage.class, "styles.css");
	
	/**
	 * Constructor
	 */
	public ErsyBasePage()
	{
		this(new PageParameters());
	}

	/**
	 * Constructor
	 * 
	 * @param pageParameters
	 */
	public ErsyBasePage(final PageParameters pageParameters)
	{
		super(pageParameters);
		add(new ErsyPageHeader("header", this));
	}


	/**
	 * Construct.
	 * 
	 * @param model
	 */
	public ErsyBasePage(IModel<?> model)
	{
		super(model);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
	  response.render(CssReferenceHeaderItem.forReference(MYPAGE_CSS));
	}

}
