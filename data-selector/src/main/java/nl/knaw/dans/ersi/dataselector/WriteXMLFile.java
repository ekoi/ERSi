/**
 * 
 */
package nl.knaw.dans.ersi.dataselector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

/**
 * @author akmi
 * 
 */
public class WriteXMLFile {
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private Document doc;
	private Element rootElement;
	private Node repoElement;

	public WriteXMLFile() {
		docFactory = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = docFactory.newDocumentBuilder();
			// root elements
			doc = docBuilder.newDocument();
			rootElement = doc.createElement("repositories");
			doc.appendChild(rootElement);

			repoElement = createRepoElement();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void writeXML(File f) throws TransformerFactoryConfigurationError,
			FileNotFoundException, IOException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		DOMImplementationLS impl = (DOMImplementationLS) 
	            DOMImplementationRegistry.newInstance().getDOMImplementation("LS");

		LSSerializer writer = impl.createLSSerializer();
		DOMConfiguration config = writer.getDomConfig();
        config.setParameter("format-pretty-print", Boolean.TRUE);
       
        BufferedWriter out = new BufferedWriter(new FileWriter(f));
        out.write(writer.writeToString(doc));
        out.close();
//		OutputFormat format = new OutputFormat(doc);
//		format.setIndenting(true);
//
//		// to generate a file output use fileoutputstream instead of system.out
//		XMLSerializer serializer = new XMLSerializer(new FileOutputStream(f),
//				format);
//		serializer.serialize(doc);

		System.out.println("XML File saved! Location: " + f.getAbsolutePath());

	}

	private Element createRepoElement() {
		// repository element
		Element repo = doc.createElement("repository");
		repo.setAttribute("name", "easy");
		rootElement.appendChild(repo);
		return repo;
	}

	public void createDataElements(Map<String, List<Map<String, String>>> data,
			File output) {
		// data elements
		Set<String> langs = data.keySet();
		for (String lang : langs) {
			Element dataElement = doc.createElement("data");
			repoElement.appendChild(dataElement);
			dataElement.setAttribute("language", lang);
			List<Map<String, String>> pids = data.get(lang);
			for (Map<String, String> pidtext : pids) {
				Set<String> pid = pidtext.keySet();
				for (String p : pid) {
					// text
					Element textElement = doc.createElement("text");
					textElement.setAttribute("pid", p);
					textElement.appendChild(doc.createTextNode(pidtext.get(p)));
					dataElement.appendChild(textElement);
				}
			}
		}

		try {
			writeXML(output);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
