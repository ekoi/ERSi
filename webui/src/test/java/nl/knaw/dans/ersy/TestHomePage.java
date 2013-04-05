package nl.knaw.dans.ersy;

import nl.knaw.dans.ersy.webui.ErsyBasePage;
import nl.knaw.dans.ersy.webui.pages.ErsyHomeApplication;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage
{
	private WicketTester tester;

	@Before
	public void setUp()
	{
		tester = new WicketTester(new ErsyHomeApplication());
	}

	@Test
	public void homepageRendersSuccessfully()
	{
		//start and render the test page
		tester.startPage(ErsyBasePage.class);

		//assert rendered page class
		tester.assertRenderedPage(ErsyBasePage.class);
	}
}
