package mqiott.dialogflow;

import mqiott.controllers.BridgeControllerImpl;
import mqiott.controllers.DeviceManagerControllerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class DialogFlowServiceTest {

    @InjectMocks
    private DialogFlowService dialogFlowService;

    @Mock
    private BridgeControllerImpl bridgeController;

    @Mock
    private DeviceManagerControllerImpl deviceManagerController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void processRequest() {
        Map<String, Object> map = new HashMap<>();
        String response, expectedResponse, id, msg = "toggle";

        id = "1";
        map.put("actions", DialogFlowService.ACTION_OPEN);
        when(bridgeController.publish(id, msg)).thenReturn("<h1> The garage door toggle. </h1>");
        response = dialogFlowService.processRequest(map);
        expectedResponse = DialogFlowService.RESPONSE_ACTION_OPEN;
        assertEquals(response, expectedResponse);

        id = "1";
        map.put("actions", DialogFlowService.ACTION_CLOSE);
        when(bridgeController.publish(id, msg)).thenReturn("<h1> The garage door toggle. </h1>");
        response = dialogFlowService.processRequest(map);
        expectedResponse = DialogFlowService.RESPONSE_ACTION_CLOSE;
        assertEquals(response, expectedResponse);

        id = "2";
        map.put("actions", DialogFlowService.ACTION_STATUS);
        when(deviceManagerController.getDeviceData(id)).thenReturn(DialogFlowService.DOOR_STATUS_OPEN);
        response = dialogFlowService.processRequest(map);
        expectedResponse = "Your garage is " +DialogFlowService.DOOR_STATUS_OPEN + ".";
        expectedResponse += " Would you like me to close your garage?";
        assertEquals(response, expectedResponse);

        id = "2";
        map.put("actions", DialogFlowService.ACTION_STATUS);
        when(deviceManagerController.getDeviceData(id)).thenReturn(DialogFlowService.DOOR_STATUS_CLOSED);
        response = dialogFlowService.processRequest(map);
        expectedResponse = "Your garage is " +DialogFlowService.DOOR_STATUS_CLOSED + ".";
        expectedResponse += " No need to worry, your house is safe.";
        assertEquals(response, expectedResponse);

    }
}