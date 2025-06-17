package com.kvote.backend.test;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/*
    * This controller is for testing purposes only.
    * It provides an endpoint to request KLAY from the Baobab testnet faucet.
    * It should not be used in production environments.
    * Make sure to remove or disable this controller in production.
 */
@RestController
@RequestMapping("/test")
public class TestFaucetController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/fund")
    public ResponseEntity<String> fund(@RequestParam String address) {
        String jsonBody = "{\"address\":\"" + address + "\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        String faucetUrl = "https://baobab.wallet.klaytn.foundation/faucet";
        ResponseEntity<String> response = restTemplate.postForEntity(faucetUrl, request, String.class);

        return ResponseEntity.ok("Requested KLAY for: " + address + "\n" + response.getBody());
    }
}
