package OSS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import commons.*;

public class App {
	
	private WebDriver driver;

	@BeforeMethod
	@Parameters("domain")
	public void setUp(String domain) throws Exception {

		variables.domain = domain;

		//Options to start Chrome browser
		System.setProperty("webdriver.chrome.driver","src/main/resources/driver/chromedriver.exe");
	    ChromeOptions cOptions = new ChromeOptions();
	    cOptions.addArguments("--allow-running-insecure-content"); //Allows insecure content
	    cOptions.addArguments("disable-infobars"); //removes yellow warning bar
	    cOptions.addArguments("headless");
	    driver = new ChromeDriver(cOptions);
	    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	    //adds first page to crawled list
	    variables.crawled.add(variables.domain);

	    Reporter.log ("Starting at page "+variables.domain, variables.report);
	    driver.get(variables.domain);
	    
	    Reporter.log("Getting list of all links on the page", variables.verboseReport);

		  //Creates list of links to click on, except for those in crawled list
		  List<WebElement> WebElementLinks = driver.findElements(By.tagName("a"));
		  
		  //sets initial values of master list to be added to over iterations
		  variables.master = commons.getURLs(WebElementLinks);
		  Reporter.log("First URL in the list: "+variables.master.get(0), variables.report);
		  
		  //checks URL for http connection response
		  commons.verifyLink(variables.url);
		  
		  //sets first url to go to
		  variables.url = variables.domain;
		  
		  //opens the excel file to be written
		  WriteExcel.setExcelFile();

	}

	@Test
	
	public void Spell_Check() throws Exception {

		driver.get(variables.url);
		WriteExcel.setCellData(variables.url, (variables.i+1), 0);

		/*
		 * This is where any methods to check the page for additional features goes
		 * 
		 */

		//checks URL for HTTP connection response
			int response;
			response = commons.verifyLink(variables.url);
			String responseString = Integer.toString(response);
			WriteExcel.setCellData(responseString, (variables.i+1), 1);

		//checks for all images for broken links
			HashMap<String, Integer> brokenImages;
			String ImagesString = "";
			brokenImages = commons.checkImage(driver);
			//loops through key value pair and adds to string to print to file
			for (Entry<String, Integer> entry : brokenImages.entrySet()) {
			    String key = entry.getKey();
			    Integer value = entry.getValue();
			    ImagesString.concat(key);
			    ImagesString.concat("=>");
			    ImagesString.concat(Integer.toString(value));
			    ImagesString.concat(" : ");
			}
			WriteExcel.setCellData(ImagesString, (variables.i+1), 2);

		//checks the spelling of all words on the page
			ArrayList<String> Spelling = new ArrayList<String>();
			Spelling = commons.CheckSpelling(driver);
			//Turns list in to a csv
			String csv = String.join(",", Spelling);
			WriteExcel.setCellData(csv, (variables.i+1), 3);

		/*
		 * End of additional methods to extend functionality of crawler
		 */

		//Gets a string of all links
		List<WebElement> Links = driver.findElements(By.tagName("a"));
		List<String> links = commons.getURLs(Links);

		//if there are any new links, adds it to the master list.
		commons.AddUrls(links);

		try {
			//sets URL as next item in the list
			variables.url = variables.master.get(variables.i);
		
			Reporter.log("Going to => "+variables.url, variables.report);
			variables.i++;
		  
			//iterates through test again
			Spell_Check();

		} catch (IndexOutOfBoundsException e) {
			//does nothing
		}

	}

	@AfterMethod
   	public void tearDown() throws Exception {
	//Sets stop time for each test case
	    String EndTime = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
	    Reporter.log("Test Case finished at: "+EndTime, variables.report);
	    
//	    try {
//			//sets URL as next item in the list
//			variables.url = variables.master.get(variables.i);
//		
//			Reporter.log("Going to => "+variables.url, variables.report);
//			variables.i++;
//		  
//			//iterates through test again
//			Spell_Check();
//
//		} catch (IndexOutOfBoundsException e) {
//			//does nothing
//		}

    //Closes browser in between each test
	    //driver.quit();
   }
}
