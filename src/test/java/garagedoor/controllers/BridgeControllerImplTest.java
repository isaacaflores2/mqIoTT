package garagedoor.controllers;

import garagedoor.bridge.Bridge;

import garagedoor.device.DeviceManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BridgeControllerImplTest {

    @Mock
    private Bridge<String> bridge;
    @Mock
    private DeviceManager<String> deviceManager;

    @InjectMocks
    private BridgeController bridgeController = new BridgeControllerImpl(bridge, deviceManager);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void index() {
        String index = "<h1>GarageDoor</h1>";
        assertEquals(index, bridgeController.index());
    }

    @Test
    public void getMqttBridgeClientStatus() {
        String expecedResponse = "<h1> MQTT Client is running </h1>";
        when(bridge.isSubscribed()).thenReturn(true);
        String response = bridgeController.getMqttBridgeClientStatus();
        assertEquals(expecedResponse, response);

        expecedResponse = "<h1> MQTT Client is not running....FIX IT</h1>";
        when(bridge.isSubscribed()).thenReturn(false);
        response = bridgeController.getMqttBridgeClientStatus();
        assertEquals(expecedResponse, response);
    }

    @Test
    public void publish() throws JSONException {
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("result", "success");
        String id = "1";
        String msg = "toggle";
        when(bridge.publish(id, "toggle")).thenReturn(expectedResponse);
        String response = bridgeController.publish(id, msg);
        verify(bridge).publish(id, "toggle");
        assertEquals(expectedResponse.toString(), response.toString());
    }

    @Test
    public void handleAllExceptions() {
        RuntimeException e = new RuntimeException();
        assertEquals(new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR), bridgeController.handleAllExceptions(e));
    }
}