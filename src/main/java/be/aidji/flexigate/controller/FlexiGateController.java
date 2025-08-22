package be.aidji.flexigate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class FlexiGateController {

    private final ApplicationEventPublisher publisher;

    @Autowired
    public FlexiGateController(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshRoutes() {
        publisher.publishEvent(new RefreshRoutesEvent(this));
        return ResponseEntity.ok("✅ Routes rechargées !");
    }
}
