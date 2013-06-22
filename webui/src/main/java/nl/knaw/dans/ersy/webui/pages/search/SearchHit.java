package nl.knaw.dans.ersy.webui.pages.search;

import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SearchHit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9001971158886116179L;
	private String title;
	private String storeId;
	private String creator;
	private String dateCreated;
	private String pid;
	private ArrayList<String> description;
	private ArrayList<String> identifiers;
	private ArrayList<String> coverage;
	private String accessCategory;

	public SearchHit(Node hitNode) {
		description = new ArrayList<String>();
		identifiers = new ArrayList<String>();
		coverage = new ArrayList<String>();
		NodeList nodes = hitNode.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++)
			parseNode(nodes.item(i));
	}

	private void parseNode(Node node) {
		String name = node.getNodeName();
		String content = node.getTextContent();
		if (name.equals("title")) {
			title = content;
		} else if (name.equals("storeId")) {
			storeId = content;
		} else if (name.equals("creator")) {
			creator = content;
		} else if (name.equals("dateCreated")) {
			dateCreated = content;
		} else if (name.equals("description")) {
			description.add(content);
		} else if (name.equals("identifier")) {
			identifiers.add(content);
		} else if (name.equals("coverage")) {
			coverage.add(content);
		} else if (name.equals("accessCategory")) {
			accessCategory = content;
		}
	}

	public String getTitle() {
		return title;
	}

	public String getStoreId() {
		for (String s : identifiers) {
			if (s.startsWith("urn:nbn:"))
				return s;
		}
		return storeId;
	}

	public String getCreator() {
		return creator;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public ArrayList<String> getIdentifiers() {
		return identifiers;
	}

	public ArrayList<String> getCoverage() {
		return coverage;
	}

	public String getAccessCategory() {
		return accessCategory;
	}

	/**
	 * @return the pid
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * @param pid the pid to set
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}

}
