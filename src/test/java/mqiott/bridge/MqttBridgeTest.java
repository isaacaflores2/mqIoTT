/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqiott.bridge;

import mqiott.configurations.Properties;
import mqiott.device.Device;
import mqiott.device.DeviceManager;
import mqiott.device.MqttDeviceManager;
import mqiott.device.MqttDevice;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 *
 * @author iflores
 */
public class MqttBridgeTest
{

    private Properties properties;
    
    @InjectMocks
    private  MqttBridge mqttBridge;

    @Mock
    MqttClient mqttClient;

    private DeviceManager<String> deviceManager;
    private MqttBroker mqttBroker;

    
    public Device[] expMqttDevices;
    public String topic1  = "garage/door/toggle";
    public String topic2  = "garage/sensor/door";
    public String id1 = "1";
    public String id2 = "2";
    public String status = "default";
    public String data = "open";
    public int numDevices = 2;

    @BeforeEach
    public void setUp() 
    {

        expMqttDevices = new MqttDevice[2];
        expMqttDevices[0] = new MqttDevice(topic1 , id1 ,status, data);
        expMqttDevices[1] = new MqttDevice(topic2 , id2 ,status, data);
        properties = new Properties();
        properties.mqttTopics = new String[]{topic1, topic2};
        properties.deviceIds = new String[]{id1, id2};
        properties.numDevices = "2";
        properties.mqttBrokerAddress = "ssl://localhost";
        properties.mqttClientId = "bridgeTest";
        properties.mqttBrokerUsername = "iflores";
        properties.mqttBrokerPassword = "smarthomeoptimis13!";


        deviceManager = new MqttDeviceManager<>(properties);
        mqttBroker = new MqttBroker(properties);
        mqttBridge = new MqttBridge(properties, deviceManager, mqttBroker);
        mqttBridge.loadProperties();

        MockitoAnnotations.initMocks(this);
    }      
    
    @Test
    public void testMqttClientSetup()
    {
        System.out.println("setup Test:");
        when(mqttClient.isConnected()).thenReturn(true);
        Boolean response =  mqttBridge.getMqttClient().isConnected();
        verify(mqttClient).isConnected();
        assertEquals(true, response );
    }
    
    @Test
    public void testPublish() throws MqttException, JSONException {
        System.out.println("publish Test:");
        String message = "test";
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("result", "success");

        MqttMessage mqttMsg = new MqttMessage(message.getBytes());
        mqttMsg.setQos(2);

        doAnswer(invocationOnMock -> {
            assertEquals(topic2, invocationOnMock.getArgument(0));
            assertEquals(mqttMsg, invocationOnMock.getArgument(1));
            return null;
        }).when(mqttClient).publish(topic2, mqttMsg);

        when(mqttClient.isConnected()).thenReturn(true);
        JSONObject response = mqttBridge.publish(id2, message);
        assertEquals(expectedResponse.toString(), response.toString());
    }
    
    @Test
    public void testSubscribe() throws MqttException {
        System.out.println("subscribe Test:");

        doAnswer(invocationOnMock -> {
            assertEquals(topic1, invocationOnMock.getArgument(0));
            return null;
        }).when(mqttClient).subscribe(topic1);

        when(mqttClient.isConnected()).thenReturn(true);
        mqttBridge.subscribe();
        assertTrue(mqttBridge.isSubscribed());
        assertTrue(mqttBridge.isSubscribed);
    }
    
    @Test
    public void testConnectionLost() 
    {
        System.out.println("connectionLost Test:");
        Throwable thrwbl = new Throwable();

        when(mqttClient.isConnected()).thenReturn(true);
        mqttBridge.connectionLost(thrwbl);

        assertTrue(mqttBridge.isSubscribed());
    }

    @Test
    public void testMessageArrived() throws Exception 
    {
        System.out.println("messageArrived test");
        String payload = "test test";
        MqttMessage message = new MqttMessage(payload.getBytes());

        /*doAnswer(invocationOnMock -> {
            assertTrue(topic1.equals(invocationOnMock.getArgument(0)));
            assertTrue(message.equals(invocationOnMock.getArgument(1)));
            return null;
        }).when(mqttBridge).messageArrived(topic1, message);
        */

        mqttBridge.messageArrived(topic2, message);
        assertEquals(payload, deviceManager.getDeviceData(id2));
        
    }

    @Test
    public void testIsConnected() 
    {
        System.out.println("test isConnected: ");
        assertFalse(mqttBridge.isSubscribed());
        mqttBridge.isSubscribed = true;
        assertTrue(mqttBridge.isSubscribed());
    }
}
