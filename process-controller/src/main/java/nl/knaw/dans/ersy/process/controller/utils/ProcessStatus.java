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
import java.io.Serializable;
import java.util.TimeZone;

/**
 * @author akmi
 *
 */
public class ProcessStatus implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8078077586437754670L;
	final private String fileNameCurrent;
	final private String fileNameLast;
	final private String processName;

	public enum ProcessName {
		   DATA_EXTRACTION, DATA_CLEANING, DATA_MINING
		 }

	public ProcessStatus (ProcessName pn) {
		switch (pn) {
		case DATA_EXTRACTION: 
				fileNameCurrent =  "/tmp/ersy/status/data-extraction.current";
				fileNameLast =  "/tmp/ersy/status/data-extraction.last";
				processName = "Data Extraction process";
			break;
		
		case DATA_CLEANING: 
			fileNameCurrent =  "/tmp/ersy/status/data-cleansing.current";
			fileNameLast =  "/tmp/ersy/status/data-cleansing.last";
			processName = "Data Cleaning process";
			break;
		default:
			fileNameCurrent =  "/tmp/ersy/status/data-extraction.current";
			fileNameLast =  "/tmp/ersy/status/data-extraction.last";
			processName = "Data Extraction process";
			break;
		}
	}
	
	public boolean writeCurrentStatus() {
		return writeStatus(fileNameCurrent, false);
	}
	
	public boolean writeLastStatus() {
		return writeStatus(fileNameLast, false);
	}
	
	public boolean writeDoneStatus() {
		return writeStatus(fileNameCurrent, true);
	}
	
	public String giveCurrentStatus(){
		long currTime = readStatus(fileNameCurrent);
		if (currTime == 0) {
			return processName + " is not running.";
		} 
		return processName + " is running.";
	}
	
	public String giveTimeLastProcess() {
		long lastTime = readStatus(fileNameLast);
		return "Last " + processName + " is finished at " + convertToHumanReadableTime(lastTime);
	}
	public boolean isRunning() {
		return readStatus(fileNameCurrent) != 0;
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
