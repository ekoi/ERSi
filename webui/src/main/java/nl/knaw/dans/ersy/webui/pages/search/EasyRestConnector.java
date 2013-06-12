package nl.knaw.dans.ersy.webui.pages.search;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class EasyRestConnector {
	private static EasyRestConnector instance = null;
	private static final String host = "http://eof13.dans.knaw.nl:8080/rest";

	private Client client;
	private XPath xpath;

	private EasyRestConnector() {
		client = Client.create();
		xpath = XPathFactory.newInstance().newXPath();
	}

	public ArrayList<SearchHit> search(String q)
			throws XPathExpressionException {
		try {
			q = q.replace(" ", URLEncoder.encode("%", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<SearchHit> results = new ArrayList<SearchHit>();
		String xml = getResource("/search?q=" + q + "&limit=10");
		xml = xml.replace("&", "&amp;");
		NodeList hitNodes = (NodeList) xpath.evaluate("//hits/hit",
				new InputSource(new StringReader(xml)), XPathConstants.NODESET);
		for (int i = 0; i < hitNodes.getLength(); i++)
			results.add(new SearchHit(hitNodes.item(i)));
		return results;
	}

	public String getResource(String resource) {
		WebResource webResource = client.resource(host + resource);
		ClientResponse response = webResource.get(ClientResponse.class);
		return response.getStatus() == 200 ? response.getEntity(String.class)
				: null;
	}

	public static EasyRestConnector get() {
		if (instance == null)
			instance = new EasyRestConnector();
		return instance;
	}
}
