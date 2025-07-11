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
