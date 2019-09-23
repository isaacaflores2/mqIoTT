# mqIoTT
## Mqtt Web Server implemented using Spring Boot. 

This project implements a MQTT Client and HTTPS Rest controller to serve as a MQTT/HTTPS Bridge server for my WiFi connected garage door opener. See my original [GarageDoor repository](https://github.com/isaacaflores2/GarageDoor) for details of the whole project. The MQTT/HTTPS Bridge takes web request and communicates with a ESP8266 WiFi module via MQTT to enable the door to be opened in a browser, Android app, or via a Google Assistant voice command. This project uses the following technologies: 
- Java 
- Sprint Boot
- Eclipse Paho MQTT Software
- Maven 

I have tried my best to make this code reusable to serve as a bridge for your own IoT solution. Please read the details below if you would like to use mqIoTT. 

---

## Detailed Project Description 
### Components
1. MqttBridge
   - This class implements the Eclipse Paho MQTT MqttCallback method to communicate with the Mosquitto Broker and update our Mqtt devices. 
2. MqttDeviceManager
   - This class encapsulates the MQTT devices to handle all read and update requests from the MqttBrigde class. 
3. Controllers
   - This classes (BrigdeController, DeviceManagerController, DialogFlowController) map web requests to MqttBridge and MqttDeviceManger functions to provide the status of the MqttBridge and access to read/write to the MQTT devices.
4. ConfigService
   - This class reads the values from the application.properties file and provides them to the MqttBridge and MqttDeviceManager classes. 

## Dependencies 
1. [Spring Boot](https://start.spring.io/)
   - Using [Spring Initializr][https://start.spring.io/] you can easily setup all requried spring boot dependencies for your SpringBoot project. This project utilized the SpringBoot starter Web and Security dependencies. 
2. [Eclipse Paho MQTT Software](https://www.eclipse.org/paho/clients/java/)
   - Follow the instructions to add the repository definition and dependency definition to the project pom.xml. 
3. [Java JDK 1.8 ](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
4. [Maven](https://maven.apache.org/install.html)
5. [Google Actions](https://developers.google.com/actions)
6. [Google Dialogflow](https://dialogflow.com)
7. See the [pom.xml](https://github.com/isaacaflores2/mqIoTT/blob/master/pom.xml) for all other dependencies.

## Build and Run
### 1. Application Properties File
   - The application properties file will provide the easiest out of the box solution. You will just need to specify your application properites for web server, login credentials, and the bridge application. 
   - All required fields can be found in the [example_application_properties file](https://github.com/isaacaflores2/mqIoTT/blob/master/example_%20application.properties). 
### 2. Build 
##### In the directory your pom.xml file use the following command:
      mvn clean install -Dspring.config.location=/full/path/to/application.properties
### 3. Test
##### The previous command also test your code during the build, but you can run the test without building from the same directory with the following command: 
      mvn test -Dspring.config.location=/full/path/to/application.properties
### 2. Run       
##### From the target directory you can run the bridge with the following command:
      java -jar mqIoTT-0.0.1.jar --spring.config.location=/full/path/to/application.properties
 
