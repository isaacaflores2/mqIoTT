package mqiott.controllers;

import mqiott.bridge.Bridge;
import mqiott.device.DeviceManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mqttbridge")
public class BridgeControllerImpl implements BridgeController
{
    private Bridge<String> bridge;
    private DeviceManager<String> deviceManager;

    @Autowired
    public BridgeControllerImpl(Bridge<String> bridge, DeviceManager<String> deviceManager)
    {
        this.bridge = bridge;
        this.deviceManager = deviceManager;
    }
    
    @Override
    public String index()
    {
        return "<h1>GarageDoor</h1>";
    }

    @Override
    public String getMqttBridgeClientStatus()
    {
        String status;
        JSONObject result = new JSONObject();

        if( bridge.isSubscribed()){
            status = "connected";
        }
        else                             
            status = "disconnected";

        result.put("result", status);
        return result.toString();
    }

    @Override
    public String publish(String id, String msg) {
        JSONObject result = bridge.publish(id, msg);
        return result.toString();
    }
    
    @Override
    public ResponseEntity<Exception> handleAllExceptions(RuntimeException e) {
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
