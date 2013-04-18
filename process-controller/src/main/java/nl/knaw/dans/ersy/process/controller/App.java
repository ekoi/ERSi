package nl.knaw.dans.ersy.process.controller;

import nl.knaw.dans.ersi.dataselector.util.DataExtractionExecutor;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World! " + args[0] + "- And Hello Eko! " + args[1]  );
        try {
			DataExtractionExecutor.main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println( "====== Hello World! " + args[0] + "- And Hello Eko! " + args[1]  );
    }
}
