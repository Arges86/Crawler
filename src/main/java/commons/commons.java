/**
**********************************************************************
* @Project Name : Crawl Web Domain.
************
* @Function Name : getURLs()
* 	@Description : Gets initial list of URLs to start looping through.
* 				   Converts WebElement to String
*   @PreRequisite : Must already have loaded page, and WebElement of URLs
* 	@param : links - List of Links to check
*  	@param : domain - Domain of system to limit list to
* 	@Return : List<String> of new URLs
************
* @Function Name : AddUrls()
* 	@Description : Adds new/unique URLs to the master list
*   @PreRequisite : master List should already have initial set of values
* 	@param : links - List of Links to check
*  	@param : domain - Domain of system to limit list to
* 	@Return : void
************
* @Function Name : CheckSpelling()
* 	@Description : Gets all text in the body of the current webpage
* 					Splits DOM in to array at spaces & dashes
* 					Checks word with checkKirits() method
*   @PreRequisite : None
*   @param : driver - WebDriver object being used for test case
* 	@Return : void
************
* @Function Name : verifyLink()
* 	@Description : Gets the HTTP connection response for the page
* 				   Basically checking to see if a link on a page results in a 200 or 404
*   @PreRequisite : None
*   @param : urlLink - link to check its connection response
* 	@Return : HTTP Response Code
************
* @Function Name : checkImage()
* 	@Description : Gets list of all image source values on a page
* 				   Passes value to verifyLink() to check that image src returns value
*   @PreRequisite : Must be on a loaded webpage
*   @param : driver - WebDriver object being used for test case
* 	@Return : HashMap of Image's URL & HTTP Response Code. Only those that return 404
************
* @Function Name : getExtension()
* 	@Description : Gets the file extension from the URL
* 				   converts whatever the value is to lower case
*   @PreRequisite : None
*   @param : url
* 	@Return : the file extension. Ex: 'html', 'pdf', 'jpg'
************
* @Date : 2017
* @Author :  Stephen Thompson
***********************************************************************
*/

package commons;

import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import commons.variables;

public class commons {

	public static List<String> getURLs(List<WebElement> links) {
		 
		//sets new array to return
		 List<String> ListHref = new ArrayList<String>();
		 
		  //loops through all links
		  for(WebElement Webhref:links) {
			  
			  //if element isn't null
			  if (Webhref.getAttribute("href") != null) {
				  String StringHref  = (Webhref.getAttribute("href")).toString();

				  //if page isn't part of domain, ignore
				  if (StringHref.startsWith(variables.domain)) {
					  //System.out.println(StringHref);
					  
					  //if page doesn't have an octothorpe
					  if (!StringHref.contains("#")) {
						  
						//if page doesn't ends with one of the file extensions to not use
						  String extension = getExtension(StringHref);//gets TLD from URL
						  if(!variables.list.stream().anyMatch(extension::contains)) {
							  
							//if value isn't already part of this array or the already crawled array
							  if(!ListHref.contains(StringHref) && !variables.crawled.contains(StringHref)) {
								  ListHref.add(StringHref);
								  //System.out.println(StringHref);
		
							  }
						  }
					  }
				  }
			  }
		  }

		return ListHref;
	}
	
	public static void AddUrls (List<String> links) {

	 //loops through all links
	  for(String LoopedLinks:links) { 
	
		//if page isn't part of domain, ignore
		  if (LoopedLinks.startsWith(variables.domain)) {
	
			  //if page doesn't have an octothorpe
			  if (!LoopedLinks.contains("#")) {
				  
				  //if page doesn't ends with one of the file extensions to not use
				  String extension = getExtension(LoopedLinks);//gets file extension from URL
				  if(!variables.list.stream().anyMatch(extension::contains)) {

					  //if link isn't already part of master list, add it.
					  if(!variables.master.contains(LoopedLinks)) {
						  Reporter.log("Adding to master list "+LoopedLinks, variables.verboseReport);
						  variables.master.add(LoopedLinks);
					  }
				  }
			  }
		  }
	  }
	}
	
	public static ArrayList<String> CheckSpelling(WebDriver driver) {

		Reporter.log("Webpage getting spell checked is: "+ variables.url, variables.verboseReport);

		//Object of all test in body of webpage
		String DOM= driver.findElement(By.tagName("body")).getText();

		//Splits text into array by each dash and whitespace
		String[] search = DOM.split("[-–\\s]");

		//loops through array and prints words
		for( int i = 0; i < search.length; i++) {

			//Cleans up words
			search[i] = search[i].replaceAll("[^a-zA-Z0-9\\s+]", "");
			search[i] = search[i].replaceAll("\\+", "");
			search[i] = search[i].replaceAll("\\d","");
		}

		//Creates string of results
		String[] results = new String[search.length];

		//Creates string to return of failures
		ArrayList<String> returned = new ArrayList<String>();

		//loops through array checking to see if value is in dictionary
		//will return 'true' or the missing word
		for( int i = 0; i < search.length; i++) {
			String val = spelling(search[i].toLowerCase());

			//adds returned value from function to results[]
			results[i] = val;

			//if result is not true, displays misspelled word
			if (results[i] != "true") {
				Reporter.log("'"+results[i]+ "' is not a word!", variables.verboseReport);
				
				//add false results to returned list
				returned.add(results[i]);
			}
		}

		//If all words are spelled correctly (if all values in array are 'true')
		if (Arrays.stream(results).distinct().count() == 1) {
			Reporter.log("There are no spelling errors", variables.verboseReport);
		}
		
		return returned;
	}

	public static String spelling(String searchString) {
        boolean result = false;
        Scanner in = null;

        //Tries to get dictionary into memory from file
        //Checks to see if searchString is in 'Scanner'
        try {
            in = new Scanner(new FileReader("src/main/resources/eng_com.dic"));
            while(in.hasNextLine() && !result) {
                result = in.nextLine().indexOf(searchString) >= 0;
            }

        } catch(IOException e) {
            e.printStackTrace(); 
        //closes file out of memory
        } finally {
            try { in.close() ; } catch(Exception e) { /* ignore */ }  
        }

        //If result is true, then converts boolean to String type and returns
        if (result) {
        	String results = String.valueOf(result);
        	return results;

        //If result is false, then returns the 'word' that is not in dictionary.
        } else {
        	return searchString;
        }
 }
	
	public static int verifyLink(String urlLink) {
		//pulled from https://www.softwaretestingmaterial.com/broken-links-using-selenium/

        //Sometimes we may face exception "java.net.MalformedURLException". Keep the code in try catch block to continue the broken link analysis
        try {
			 //Use URL Class
			 URL link = new URL(urlLink);

			 // Create a connection using URL object (i.e., link)
			 HttpURLConnection httpConn =(HttpURLConnection)link.openConnection();

			 //Set the timeout for 2 seconds
			 httpConn.setConnectTimeout(2000);

			 //connect using connect method
			 httpConn.connect();

			 //use getResponseCode() to get the response code. 
			 if(httpConn.getResponseCode()== 200) { 
				 //System.out.println(urlLink+" - "+httpConn.getResponseMessage());
			 }

			 if(httpConn.getResponseCode()== 404) {
				 System.out.println(urlLink+" - "+httpConn.getResponseMessage());
			 }
			 
			 return httpConn.getResponseCode();

		 //getResponseCode method returns = IOException - if an error occurred connecting to the server. 
        } catch (Exception e) {
		 //e.printStackTrace();
		 }
		return 0;
    }
	
	public static HashMap<String, Integer> checkImage(WebDriver driver) {

		HashMap<String, Integer> myMap;
		myMap = new HashMap<String, Integer>();

		//gets list of all image links
		List<WebElement> allImages = driver.findElements(By.tagName("img"));

		for(WebElement imageFromList:allImages){

			//if image source is null
			if (imageFromList.getAttribute("src") != null) {

			     String ImageUrl=imageFromList.getAttribute("src");

			     //Reporter.log(ImageUrl, variables.verboseReport); //will get you all the image urls on the page

			     //verify the link to all the image URLs
			     int httpCode = verifyLink(ImageUrl);

			     if (httpCode == 404) {
			    	//adding values to map
				     myMap.put(ImageUrl, httpCode);
			     }
			}
		}

		return myMap;
	}

	private static String getExtension(String urlString) {
		if(urlString.contains(".")) {
			String extension = urlString.substring(urlString.lastIndexOf(".") + 1);
			extension =extension.toLowerCase();
		    
			//if URL is a get request, return the parts in between the . and ?
		    if (extension.contains("?")) {
	            return StringUtils.substringBetween(extension, ".", "?");
	        }

		    if (extension.length() == 0) {
	            return "";
	        }
		    
		    return extension;
		}
		
		return "";
	}
}
