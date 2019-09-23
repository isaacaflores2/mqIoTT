package garagedoor.controllers;

import garagedoor.device.Device;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

public interface DeviceManagerController {

    @GetMapping("/")
    String index();

    @GetMapping("/numdevices")
    @ResponseBody
    int getNumDevices();

    @GetMapping("/getdevices")
    @ResponseBody
    Collection<Device<String>> getDevices();

    @GetMapping("/get/{getId}")
    @ResponseBody
    Device getDevice(@PathVariable String id);

    @GetMapping("/contains/{getId}")
    @ResponseBody
    boolean containsDevice(@PathVariable String id);

    @GetMapping("/getStatus/{getId}")
    @ResponseBody
    String getDeviceStatus(@PathVariable String id);

    @PostMapping("/updatestatus")
    void updateDeviceStatus(@RequestParam String id, @RequestParam String status);

    @GetMapping("/device/data/{}")
    @ResponseBody
    String getDeviceData(@PathVariable String id);

    @PostMapping("/updatedata")
    void updateDeviceData(@RequestParam String id, @RequestParam String data);

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Exception> handleAllExceptions(RuntimeException e);
}
