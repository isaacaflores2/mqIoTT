package garagedoor.Controllers;

import garagedoor.iot.device.Device;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

public interface DeviceManagerController {

    @GetMapping("/")
    public String index();

    @GetMapping("/numdevices")
    @ResponseBody
    public int getNumDevices();

    @GetMapping("/getdevices")
    @ResponseBody
    public Collection<Device<String>> getDevices();

    @GetMapping("/get/{getId}")
    @ResponseBody
    public Device getDevice(@PathVariable String id);

    @GetMapping("/contains/{getId}")
    @ResponseBody
    public boolean containsDevice(@PathVariable String id);

    @GetMapping("/getStatus/{getId}")
    @ResponseBody
    public String getDeviceStatus(@PathVariable String id);

    @PostMapping("/updatestatus")
    public void updateDeviceStatus(@RequestParam String id, @RequestParam String status);

    @GetMapping("/device/data/{}")
    @ResponseBody
    public String getDeviceData(@PathVariable String id);

    @PostMapping("/updatedata")
    public void updateDeviceData(@RequestParam String id, @RequestParam String data);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Exception> handleAllExceptions(RuntimeException e);
}
