This project automates the testing of a RESTful Book API using Java, REST Assured, TestNG, and Faker library. It covers both positive and negative test scenarios using payloads and configuration from a JSON file.

**The following project structure includes**
The project contains two main packages:

src/main/java [wich includes all configurations and test implementations]

configurations.java      # Loads base URL from BookData.json

CRUDBooksEndpoints.java  # Book API methods (POST, GET, PUT, DELETE)

src/tests/java [wich includes Test Cases for happy and unhappy paths]

BooksTest.java           # Main TestNG test class

BookData.json            # Test data and base URL

pom.xml                  # Maven dependencies

**Prerequisites**
insure the following are installed:
Java (JDK 11+)	
Maven
IDE (I used Eclipse)

**Setup Instructions**
1-Clone the project
2-Import as Maven project in your IDE (or run via terminal)
3-Verify directory structure, ensure this file exists: BookData.json

The project also contains TestReport.html that provides provides a clear summary of the test executions, and shows the status of each test case.

Finally you'll find .yaml file inside .github\workflows folder that handles the CI/CD Configurations of the project and you will be able to see it running under Actions Tab in the GIT repo.

TestNG reports will be generated during the GitHub Actions CI process and are uploaded as artifacts:

willc automatically run with every push/pull request
and will be available in `target/site/surefire-report.html`(you'll find the report in Actions > click on the latest run workflow> scroll down to Artifacts)
 
