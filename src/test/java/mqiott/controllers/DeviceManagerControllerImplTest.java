package mqiott.controllers;


import mqiott.device.Device;
import mqiott.device.DeviceManager;
import mqiott.device.MqttDevice;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class DeviceManagerControllerImplTest {

    @Mock
    private DeviceManager deviceManager;

    @InjectMocks
    private DeviceManagerController deviceManagerController = new DeviceManagerControllerImpl(deviceManager);

    private Device<String> expectedDevice;
    private Set<Device> expectedDeviceSet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        expectedDevice = new MqttDevice("topic", "1", "running", "data");
        expectedDeviceSet = new HashSet();
        expectedDeviceSet.add(expectedDevice);
    }

    @Test
    public void index() {
        String index = "MqttDevice Manager home";
        assertEquals(index, deviceManagerController.index());
    }

    @Test
    public void getNumDevices() {
        int expectedNumDevices = 2;
        when(deviceManager.numDevices()).thenReturn(expectedNumDevices);
        int numDevices = deviceManagerController.getNumDevices();
        verify(deviceManager).numDevices();
        assertEquals(expectedNumDevices, numDevices);
    }

    @Test
    public void getDevices() {
        when(deviceManager.devices()).thenReturn(expectedDeviceSet);
        Set<Device> deviceList = (Set) deviceManagerController.getDevices();
        verify(deviceManager).devices();
        assertEquals(expectedDeviceSet, deviceList);
    }

    @Test
    public void getDevice() {
        String id = "1";
        when(deviceManager.getDevice(id)).thenReturn(expectedDevice);
        Device device = deviceManagerController.getDevice(id);
        verify(deviceManager).getDevice(id);
        assertEquals(expectedDevice, device);
    }

    @Test
    public void containsDevice() {
        String id = "1";
        when(deviceManager.contains(id)).thenReturn(true);
        boolean returnedBool =  deviceManagerController.containsDevice(id);
        verify(deviceManager).contains(id);
        assert(returnedBool);
    }

    @Test
    public void getDeviceStatus() {
        String id = "1";
        String expectedStatus = "running";
        when(deviceManager.getDeviceStatus(id)).thenReturn(expectedStatus);
        String status  = deviceManagerController.getDeviceStatus(id);
        verify(deviceManager).getDeviceStatus(id);
        assertEquals(expectedStatus, status);
    }

    @Test
    public void updateDeviceStatus(){
        String id = "1";
        String expectedStatus = "running";

        doAnswer((i) -> {
            assertEquals(id, i.getArgument(0));
            assertEquals(expectedStatus, i.getArgument(1));
            return null;
        }).when(deviceManager).updateDeviceStatus(id, expectedStatus);

        when(deviceManager.getDeviceStatus(id)).thenReturn(expectedStatus);

        deviceManagerController.updateDeviceStatus(id, expectedStatus);
        assertEquals(expectedStatus, deviceManagerController.getDeviceStatus(id));
    }

    @Test
    public void getDeviceData() throws JSONException {
        String id = "1";
        String expectedData = "sample data";
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("result", expectedData);
        when(deviceManager.getDeviceData(id)).thenReturn(expectedData);

        String data  = deviceManagerController.getDeviceData(id);

        verify(deviceManager).getDeviceData(id);
        assertEquals(expectedResponse.toString(), data);
    }

    @Test
    public void updateDeviceData() throws JSONException {
        String id = "1";
        String expectedData = "running";
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("result", expectedData);

        doAnswer((i) -> {
            assertEquals(id, i.getArgument(0));
            assertEquals(expectedData, i.getArgument(1));
            return null;
        }).when(deviceManager).updateDeviceData(id, expectedData);

        when(deviceManager.getDeviceData(id)).thenReturn(expectedData);

        deviceManagerController.updateDeviceData(id, expectedData);
        assertEquals(expectedResponse.toString(), deviceManagerController.getDeviceData(id));
    }

    @Test
    public void handleAllExceptions() {
        RuntimeException e = new RuntimeException();
        assertEquals(new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR), deviceManagerController.handleAllExceptions(e));
    }
}