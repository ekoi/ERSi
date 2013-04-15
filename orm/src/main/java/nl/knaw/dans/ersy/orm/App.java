package nl.knaw.dans.ersy.orm;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
//      //Create AdminBase instance for MySQL
//        AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
//                "3306", "ersy", "root", "");
//        ExtractionReportTable er = new ExtractionReportTable(System.currentTimeMillis(), 11, 22, 33, 44, 55, "Akmi");
//
//        //Store Customer Object in the Database
//        boolean b = admin.save(er);
//        
//        ExtractionReportTable ert = new ExtractionReportTable();
//        
////        ArrayList<ExtractionReportTable> e = admin.obtain(ert).where("executionTime").equal(object).findAll();
////        if (!e.isEmpty())
////        	System.out.println(e.size());
////     
//        System.out.println( "END " + b  );
    }
}
