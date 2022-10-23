# Assignment2 251 159.251
## Lydia Price, ID: 20004521

### To Run Tests:
JUnit tests can be run via the  `mvn clean test` or `mvn test`  from the 
command line or via the maven tab in your IDE. This will automatically run
all tests excluding StressTests.java - this is because running the Stress Tests
with maven took far too long, and if left running for too long, could potentially 
crash the IDE due to the large scale of the Stress Tests.

It is best to run the Stress Tests from within your IDE. For example, within
Intellij IDEA, the tests can be run from either the gutter beside the declaration
of the class name, or by selecting and running the run configuration. An example 
of this is shown below. 
![IntelliJ Example](src/main/resources/IDE_JUnitRun_Example.jpg)


