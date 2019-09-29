package mqiott.controllers;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import mqiott.device.Device;
import mqiott.device.DeviceManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeviceManagerControllerImpl implements DeviceManagerController
{
    private DeviceManager<String> deviceManager;

    @Autowired
    public DeviceManagerControllerImpl(DeviceManager<String> deviceManager)
    {
        this.deviceManager = deviceManager;
    }

    @Override
    public String index()
    {
        return "MqttDevice Manager home";
    }

    @Override
    public int getNumDevices()
    {
        return deviceManager.numDevices();
    }

    @Override
    public Collection<Device<String>> getDevices()
    {
        return deviceManager.devices();
    }

    @Override
    public Device getDevice(@PathVariable String id)
    {
        return deviceManager.getDevice(id);
    }

    @Override
    public boolean containsDevice(@PathVariable String id)
    {
        return deviceManager.contains(id);
    }

    @Override
    public String getDeviceStatus(@PathVariable String id)
    {
        return deviceManager.getDeviceStatus(id);
    }

    @Override
    public void updateDeviceStatus(@PathVariable String id, @PathVariable String status)
    {
        deviceManager.updateDeviceStatus(id, status);
    }

    @Override
    public String getDeviceData(@PathVariable String id) {
        JSONObject result = new JSONObject();
        String data  = deviceManager.getDeviceData(id);

        if(data == null) {
            data = "Sorry. There is no available status on your garage right now.";
        }

        result.put("result", data);
        return result.toString();
    }

    @Override
    public void updateDeviceData(@RequestParam String id, @RequestParam String data) {
        deviceManager.updateDeviceData(id, data);
    }

    @Override
    public final ResponseEntity<Exception> handleAllExceptions(RuntimeException e)
    {
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
