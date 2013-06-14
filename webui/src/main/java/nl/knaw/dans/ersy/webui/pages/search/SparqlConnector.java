package nl.knaw.dans.ersy.webui.pages.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SparqlConnector {
	private static SparqlConnector instance = null;
	private static final String host = "http://ns3096228.ovh.net:591/sparql";
	private static Logger LOG = LoggerFactory.getLogger(SparqlConnector.class);

	private SparqlConnector() {
	}

	public ArrayList<String> search(String pid) {
		// pid = "urn:nbn:nl:ui:13-7cb-hko";
    	String urlns = "SELECT ?dataset (SAMPLE(?pid) AS ?persistent_identifier) (MIN(bif:st_distance(?g, ?geo)) AS ?distance_km) { [] dc:identifier \"" + pid + "\"; geo:geometry ?g . ?dataset geo:geometry ?geo; dc:identifier ?pid . FILTER (bif:st_intersects (?geo, ?g, 30)) . FILTER (strStarts(?pid, \"urn\"))} GROUP BY ?dataset ORDER BY ?distance_km LIMIT 10";
    	String url = "";
    	try {
			url = URLEncoder.encode(urlns, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ArrayList<String> results = new ArrayList<String>();
		String jsonString = readUrl("?default-graph-uri=&query=" + url + "&format=application/json");
		JSON obj = JSONSerializer.toJSON( jsonString.trim() );  
		if (obj instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)obj;
			JSONObject jsonObjResults = (JSONObject)jsonObj.get("results");
			JSONArray jsonObjBindings = (JSONArray) jsonObjResults.get("bindings");
			LOG.debug("bindings size: " + jsonObjBindings.size());
			for (Object job : jsonObjBindings) {
				JSONObject joBinding = (JSONObject)job;
				JSONObject joPid = (JSONObject)joBinding.get("persistent_identifier");
				Object jopVal = joPid.get("value");
				if (jopVal!=null) {
					results.add(jopVal.toString());
				}
			}
			
		} else {
			
		}
		return results;
	}

	public static SparqlConnector get() {
		if (instance == null)
			instance = new SparqlConnector();
		return instance;
	}
	
	private static String readUrl(String urlString) {
        BufferedReader reader = null;
        StringBuffer buffer = null;
        try {
            URL url = new URL(host + urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read); 
            
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        return buffer.toString();
    }
}
