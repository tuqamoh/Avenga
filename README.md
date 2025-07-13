This project automates the testing of a RESTful Book API using Java, REST Assured, TestNG, and Faker library. It covers both positive and negative test scenarios using payloads and configuration from a JSON file.
Tests are built with Maven and run via GitHub Actions for continuous integration, with generated test reports.

**The following project structure includes**
The project contains two main packages:

**src/main/java** [wich includes all configurations and test implementations]

configurations.java      # Loads base URL from BookData.json
BaseClass.java           # contains prequisits for initialing global data and @beforeTest requirements
CRUDBooksEndpoints.java  # Book API methods (POST, GET, PUT, DELETE)

**src/tests/java** [wich includes Test Cases for happy and unhappy pathsfor All Endpoints [POST - GET - PUT - Delete]

POSTBooksEndpointTest.java    #that covers the test cases for POST Books Endpoint
GETBooksendpointTest.java    #that covers the test cases for GET(By Id and GET ALL) Books Endpoints
PUTBooksEndpointTest.java    #that covers the test cases for PUT Books Endpoint
DeleteBooksEndpointTest.java  #that covers the test cases for DELETE Books Endpoint
NegativeScenariosTest.java    #that covers the test cases Negative scenarios for all

BookData.json            # Test data and base URL
pom.xml                  # Maven dependencies
testng.xml               # Test suite that has all tests

**Prerequisites**
insure the following are installed:
Java (JDK 11+)	
Maven
IDE (I used Eclipse)
Internet connection for REST API access and dependency downloads



**Setup Instructions**
1-Clone the project git clone https://github.com/tuqamoh/Avenga.git
2-Import as Maven project in your IDE (or run via terminal)
3-Verify directory structure, ensure this file exists: BookData.json

**Continuous Integration**
This project uses GitHub Actions for CI:
Runs tests automatically on every push or pull request to the main branch
Caches Maven dependencies for faster builds
Uploads Surefire reports as build artifacts

**Workflow file: .github/workflows/maven.yml**

**Troubleshooting**
1- Tests failing due to ID mismatches?
The API auto-generates IDs on POST; BookId is printed before each test to make sure that I'm using the returned ID, not the sent one.

2- Surefire report not generated?
Ensure maven-surefire-report-plugin is configured in pom.xml. Run mvn surefire-report:report after tests.

3- GitHub Actions failing?
Check logs for Java version compatibility, Maven cache issues, or artifact upload plugin versions.
