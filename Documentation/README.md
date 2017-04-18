# ClientServerScheduling
CPSC445 Class Project  
by Andrew Smith and Timothy Kelly

## To Build Project In Eclipse
* Clone [repository](https://www.github.com/andlee90/ClientServerScheduling) [https://www.github.com/andlee90/ClientServerScheduling]
* Unzip project files
* Open Eclipse and create a new Java project.
* Project name should be 'ClientServerScheduling'
* Project must use JavaSE-1.8 execution environment
* Select 'Use project folder as root...' for Project Layout
* Select all folders from cloned repo and drag into eclipse package explorer under 'ClientServerScheduling
* Right click 'Source Code/src' -> Build Path -> Use as Source Folder
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

## To Create a Useable .jar In Eclipse
* File -> Export -> Java -> Runnable JAR
* Launch Configuration must be Main - Client Server Scheduling
* Set Export Destination to appropriate directory and name to 'ClientSeverScheduling.jar'
* Create a new folder tree as such 'ClientSeverScheduling/sqlite/db' and add .jar to parent directory

## To Build Project In IDEA
* Clone [repository](https://www.github.com/andlee90/ClientSeverScheduling) [https://www.github.com/andlee90/ClientServerScheduling]
* Unzip project files
* Open IDEA and create a new Java project 
* Project name should be 'ClientServerScheduling'
* Project SDK must be 1.8
* Select all folders from cloned repo and drag into IDEA project explorer under 'ClientServerScheduling'
* Delete the default src/ folder
* File -> Project Structure... -> Modules tab
* Select Source Code/src, right click -> Sources
* Navigate to the Libraries tab and click the green '+'
* Select 'from Maven' and search 'sqlite jdbc'
* Select org.xerial:sqlite-jdbc:3.16.1 and click 'ok'
* Click 'ok' again followed by 'apply' and 'ok' once more
* Run -> Run Main

## To Create a Useable .jar In IDEA
* File -> Project Structure -> Artifacts tab
* Click the green '+' and select JAR -> From modules with dependencies
* Select Main as the main class and check 'extract to the target jar'
* Click 'ok' then 'apply' followed by 'ok' again
* Build -> Build Artifacts -> Build
* JAR will be located in out/artifacts
