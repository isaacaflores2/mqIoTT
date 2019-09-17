package garagedoor.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface BridgeController {
    @RequestMapping("/")
    public String index();

    @GetMapping("/connection")
    public String getMqttBridgeClientStatus();

    @PostMapping("/publish")
    public String publish(@RequestParam String id, @RequestParam String msg);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Exception> handleAllExceptions(RuntimeException e);
}
