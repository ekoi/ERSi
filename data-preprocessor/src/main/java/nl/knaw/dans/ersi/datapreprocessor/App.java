package nl.knaw.dans.ersi.datapreprocessor;

import nl.knaw.dans.ersi.datapreprocessor.utils.DataCleansingExecutor;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World! " + System.getenv("ERSY_HOME"));
        try {
        	DataCleansingExecutor.main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
