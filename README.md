# Domain Crawler

Project is to find and resolve all webpages on a particular domain. <br />
Home page is loaded, then scanned for URLs. First URL is clicked on, then scanned for its own urls. This is then added a to a master list. Process repeats until no unique URLs are left.<br />
Additional actions can be added or removed from the main App class: <br />
1. checks URL for HTTP connection response (check for 404, 200, etc)
2. check for broken image links on page
3. spell check page for misspelled words

## Getting Started

Copy the project from BitBucket on to your local environment. <br />
Or wherever you want to run it.

### Prerequisites

1. Chrome browser installed (helpful if latest version)
2. Eclipse (or similar IDE)
3. TestNG plug-in installed
4. Maven plug-in installed (m2eclipse)

### Installing

##### TestNG

```
Open Eclipse
Go to Help -> Eclipse Marketplace...
Do search for TestNG (Type the text TestNG in Find text box > Click Go button)
After searching: Click Install button at TestNG for Eclipse area.
Follow the further instructions by eclipse.
```

Download latest ChromeDriver from: [here](https://sites.google.com/a/chromium.org/chromedriver/)  <br />
 Goes in 'src/main/resources/driver' folder
 
 ##### Dependencies
 All other dependencies are managed by Maven


## Setup the tests
1. Domain variable is stored in testng.xml . Update this to each domain to be scanned.
2. Comment out any methods you don't wish to run.
3. The Variables class contains a list (called list) of all file extensions you don't want the program to scan. Example: .pdfs or .jpg


## Running the tests

1. Right Click on testng.xml and select 'Run As' => 'TestNG Suite'
2. Test Case will run for each <test> specified in the xml
3. Excel report is saved to '/src/main/resources/results.xlsx'
4. HTML report is saved to '/test-output/html/index.html'

### How it works

Chrome browser is launched 'headless'. This means that a user will not see the browser window open in the UI. <br />
Once the page is loaded, the List 'WebElementLinks' gets all of the links on the page. These are then saved to the master list. <br />

Method 'verifyLink' is called: 
* all the links are checked to see if they resolve a valid header.
* All broken links are returned

Method 'checkImage' is called
 * all the 'img src' are checked to see if they resolve or are broken.
 * All broken links are returned
 
Method 'CheckSpelling' is called
* All the words in the body are loaded in to an array and looped through.
* Each word is checked against a dictionary '/src/main/resources/eng_com.dic' to see if its present. Any word missing is returned.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Known Issues

1. hyphenated words do not work
2. conjugations do not work

Since the program removes all special characters, it strips these aspects of the word.

## To Do:

* Find a better dictionary
* Speed up execution of spell checker by removing null values from array
* Make each webpage its own 'Test Case' in the TestNG framework so it reports as more than just one test scenario.

## Authors

* **Stephen Thompson** - *Initial work* - 

