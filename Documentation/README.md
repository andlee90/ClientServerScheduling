# ClientServerScheduling
CPSC445 class project
Andrew Smith & Timothy Kelly

# To Build Project In Eclipse
* Clone repository.
* Unzip project files.
* Open Eclipse and create a new Java project. 
* Project name should be 'ClientServerScheduling'.
* Project must use JavaSE-1.8 execution environment.
* Select 'Use project folder as root...' for Project Layout
* Select all folders from cloned repo and drag into eclipse package explorer under 'ClientServerScheduling.
* Right click 'Source Code/src' -> Build Path -> Use as Source Folder.
* Right click on project directory -> Configure -> Convert to Maven Project
* Open 'pom.xml' and add between version and build tags:
```
   <dependencies>
  	<!-- https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc -->
	<dependency>
    	<groupId>org.xerial</groupId>
    	<artifactId>sqlite-jdbc</artifactId>
    	<version>3.16.1</version>
	</dependency>
  </dependencies>
```
* File -> Save
* Navigate to Main.java, right click -> Run as -> Java Application

# To Create a Useable .jar In Eclipse
* File -> Export -> Java -> Runnable JAR
* Launch Configuration must be Main - Client Server Scheduling
* Set Export Destination to appropriate directory and name to 'ClientSeverScheduling.jar'
* Create a new folder tree as such 'ClientSeverScheduling/sqlite/db' and add .jar to parent directory
