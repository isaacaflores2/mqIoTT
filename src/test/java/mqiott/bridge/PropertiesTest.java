/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqiott.bridge;



import mqiott.configurations.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;


import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author iflores
 */
@SpringBootTest
@ContextConfiguration(classes = Properties.class)
@TestPropertySource(properties = {
        "mqiott.bridge.mqtttopics=topic1,topic2",
        "mqiott.bridge.deviceids=1,2",
        "mqiott.bridge.numdevices=2",
        "mqiott.bridge.mqttbrokeraddress=ssl://url.test:8883",
        "mqiott.bridge.mqttclientid=testId",
        "mqiott.bridge.mqttusername=testUser",
        "mqiott.bridge.mqttpassword=testPass"
})
public class PropertiesTest
{
   @Autowired
   Properties properties;
    
    @Test
     public void testMqttTopics()
    {
        String[] expResult = new String[2];
        expResult[0] = "topic1";
        expResult[1] = "topic2";
        assertArrayEquals(expResult, properties.mqttTopics);
    }
     
    @Test
    public void testDeviceIds()
    {
        String[] expResult = new String[2];
        expResult[0] = "1";
        expResult[1] = "2";
        assertArrayEquals(expResult, properties.deviceIds);
    }
    
    @Test
    public void testNumDevices()
    {
        assertEquals("2", properties.numDevices);
    }
    
    @Test
    public void testMqttBrokerAddress()
    {
        assertEquals("ssl://url.test:8883", properties.mqttBrokerAddress);
    }
    
    @Test
    public void testMqttClientId()
    {
        assertEquals("testId", properties.mqttClientId);
    }
    
    @Test
    public void testMqttBrokerUsername()
    {
        assertEquals("testUser", properties.mqttBrokerUsername);
    }
    
    @Test
    public void testMqttBrokerPassword()
    {
        assertEquals("testPass", properties.mqttBrokerPassword);
    }
}
