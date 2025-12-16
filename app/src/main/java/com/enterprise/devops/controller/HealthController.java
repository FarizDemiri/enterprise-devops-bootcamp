package com.enterprise.devops.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Enterprise DevOps Application is Running!";
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "UP");
        map.put("version", "1.0.0");
        return map;
    }
}
