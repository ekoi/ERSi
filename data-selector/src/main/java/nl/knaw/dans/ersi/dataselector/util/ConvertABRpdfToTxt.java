package nl.knaw.dans.ersi.dataselector.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class ConvertABRpdfToTxt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileWriter fstream = new FileWriter("/Users/akmi/abrlist.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
            PdfReader reader = new PdfReader("/Users/akmi/Dropbox/THESIS/Sharing-thesis-project/TODO/ABR_website2.pdf");
            System.out.println("This PDF has "+reader.getNumberOfPages()+" pages.");
            int numberOfPages = reader.getNumberOfPages();
           for (int k=1; k<= numberOfPages; k++) {
            String page = PdfTextExtractor.getTextFromPage(reader, k);
            String[] str = page.split("\n");
//            System.out.println("Page Content:\n\n"+page+"\n\n");
//            System.out.println("=====");
            int i=0;
            for (String s : str) {
            	
            	if (s.contains("...")) {
            		System.out.println(i++ + " : " + s);
            		out.write(s);
            		out.newLine();
            	}
            }
           }
            System.out.println("Is this document encrypted: "+reader.isEncrypted());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

}
