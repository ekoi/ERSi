package nl.knaw.dans.ersi.dataselector.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class ConvertABRpdfToTxt {
	
	private static Logger LOG = LoggerFactory.getLogger(ConvertABRpdfToTxt.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileWriter fstream = new FileWriter(System.getenv("ERSY_HOME") + "/abrlist.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
            PdfReader reader = new PdfReader(System.getenv("ERSY_HOME") + "/ABR_website2.pdf");
            LOG.debug("This PDF has "+reader.getNumberOfPages()+" pages.");
            int numberOfPages = reader.getNumberOfPages();
           for (int k=1; k<= numberOfPages; k++) {
            String page = PdfTextExtractor.getTextFromPage(reader, k);
            String[] str = page.split("\n");
//            LOG.debug("Page Content:\n\n"+page+"\n\n");
//            LOG.debug("=====");
            int i=0;
            for (String s : str) {
            	
            	if (s.contains("...")) {
            		LOG.debug(i++ + " : " + s);
            		out.write(s);
            		out.newLine();
            	}
            }
           }
            LOG.debug("Is this document encrypted: "+reader.isEncrypted());
            out.close();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

	}

}
