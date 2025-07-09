package au.com.telstra.simcardactivator;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")

public class SimCardActivatorController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String ACTUATOR_URL = "http://localhost:8444/actuate";

    @PostMapping("/activate")
    public ResponseEntity<String> activate(@RequestBody SimCardRequest request){
        // Create the request body for the actuator
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonPayload = String.format("{\"iccid\": \"%s\"}", request.getIccid());
        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(ACTUATOR_URL, entity, String.class);
            System.out.println("Actuator responded with: " + response.getBody());
            return ResponseEntity.ok("Activation response: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace(); // 👈 also add this
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to activate SIM: " + e.getMessage());
        }
    }
}
