package garagedoor.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DialogFlowController {
    @PostMapping("/dialogFlowWebHook")
    ResponseEntity<?> smartGarageIntent(@RequestBody String requestStr);
}
