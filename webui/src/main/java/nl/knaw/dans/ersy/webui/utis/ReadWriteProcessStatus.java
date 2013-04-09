/**
 * 
 */
package nl.knaw.dans.ersy.webui.utis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author akmi
 *
 */
public class ReadWriteProcessStatus {

	private static final String FILE_NAME= "/tmp/ersy/status/data-extraction.status";
	/**
	 * @param args
	 */
	public static boolean write() {

        try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(FILE_NAME);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

            //write start time
            long startTime = System.currentTimeMillis();
            bufferedWriter.write(Long.toString(startTime));
            bufferedWriter.close();
            return true;
        }
        catch(IOException ex) {
        
        }
        return false;
	}

	public static long read() {
		
        BufferedReader bufferedReader = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(FILE_NAME);

            // Always wrap FileReader in BufferedReader.
            bufferedReader = new BufferedReader(fileReader);
            long endTime = 0;
            String str = bufferedReader.readLine();
            if (str != null) 
            	endTime = Long.parseLong(str);
            return endTime;
        }
        catch(FileNotFoundException ex) {
            			
        }catch(IOException ex) {
           
        } finally {
        	if ( bufferedReader != null)
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
        }
		return 0;
	}
}
