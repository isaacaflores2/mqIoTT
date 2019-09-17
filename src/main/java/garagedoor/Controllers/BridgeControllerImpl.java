package garagedoor.Controllers;

import garagedoor.MqttHttpsBridge.Bridge;
import garagedoor.iot.device.Device;
import garagedoor.iot.device.DeviceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                        
        if( bridge.isSubscribed()){
            status = "<h1> MQTT Client is running </h1>";
        }
        else                             
            status = "<h1> MQTT Client is not running....FIX IT</h1>";
                        
        return status;
    }

    @Override
    public String publish(String id, String msg) {
        String response = "success";
        bridge.publish(id, msg);
        return response;
    }
    
    @Override
    public ResponseEntity<Exception> handleAllExceptions(RuntimeException e) {
        return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}