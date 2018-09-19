package commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class variables {
	
	
	//turn ReportNG On or Off
	public static boolean report =  true;
	public static boolean verboseReport = true;
	
	//base domain to search
	public static String domain;
	
	//list of URLs that have been crawled
	public static List<String> master = new ArrayList<String>();
	public static List<String> crawled = new ArrayList<String>(); 
	
	//variable to be updated for each iteration
	public static String url;
	
	//incremental value for master list
	public static int i = 0;
	
	//excel location
	public static String FilePath = "src/main/resources/";
	public static String FileName = "results.xlsx";
	public static String sheetname = "result";
	
	//list of file extensions to ignore checking
	public static List<String> list = Arrays.asList("pdf", "jpg", "jpeg", "png", "css", "js" , "gif");

}
