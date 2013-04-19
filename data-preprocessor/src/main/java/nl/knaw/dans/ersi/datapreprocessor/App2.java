package nl.knaw.dans.ersi.datapreprocessor;

import nl.knaw.dans.ersi.datapreprocessor.utils.DataAbrCleansingExecutor;


/**
 * Hello world!
 *
 */
public class App2 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World! " + System.getenv("ERSY_HOME"));
        try {
        	DataAbrCleansingExecutor.main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
