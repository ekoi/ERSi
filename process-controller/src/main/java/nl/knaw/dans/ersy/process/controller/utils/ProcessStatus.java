/**
 * 
 */
package nl.knaw.dans.ersy.process.controller.utils;

import hirondelle.date4j.DateTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimeZone;

/**
 * @author akmi
 *
 */
public class ProcessStatus {

	private static final String FILE_NAME_CURRENT= "/tmp/ersy/status/data-extraction.current";
	private static final String FILE_NAME_LAST= "/tmp/ersy/status/data-extraction.last";
	
	public static boolean writeCurrentStatus() {
		return writeStatus(FILE_NAME_CURRENT, false);
	}
	
	public static boolean writeLastStatus() {
		return writeStatus(FILE_NAME_LAST, false);
	}
	
	public static boolean writeDoneStatus() {
		return writeStatus(FILE_NAME_CURRENT, true);
	}
	
	public static String giveStatus(){
		long currTime = readStatus(FILE_NAME_CURRENT);
		long lastTime = readStatus(FILE_NAME_LAST);
		String lastProccess = "Last process is finished at " + convertToHumanReadableTime(lastTime);
		if (currTime == 0) {
			return "No process is running. " + lastProccess;
		} 
		return "Current process is still running. " + lastProccess;
	}
	
	public static boolean isRunning() {
		return readStatus(FILE_NAME_CURRENT) != 0;
	}
	/**
	 * @param args
	 */
	private static boolean writeStatus(final String fileName, boolean done) {

        try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(fileName);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

            //write start time
            if (done) {
            	bufferedWriter.write("0");
            } else {
	            bufferedWriter.write(Long.toString(System.currentTimeMillis()));
            }
            bufferedWriter.close();
            return true;
        }
        catch(IOException ex) {
        
        }
        return false;
	}

	private static long readStatus(final String fileName) {
		
        BufferedReader bufferedReader = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

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
        
        }catch(NumberFormatException e) {
           
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
	
	private static String convertToHumanReadableTime(long millis) {
		DateTime fromMilliseconds = DateTime.forInstant(millis,  TimeZone.getTimeZone("Europe/Amsterdam"));
		return fromMilliseconds.format("DD-MM-YYYY hh:mm:ss");
	}
}
