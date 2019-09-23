package mqiott.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface BridgeController {
    @RequestMapping("/")
    String index();

    @GetMapping("/connection")
    String getMqttBridgeClientStatus();

    @GetMapping("/publish")
    String publish(@RequestParam String id, @RequestParam String msg);

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Exception> handleAllExceptions(RuntimeException e);
}
