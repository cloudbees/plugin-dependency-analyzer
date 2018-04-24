package com.cloudbees.ce.plugins.injector.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/ping")
public class PingAPI {
    @GetMapping
    public String ping() {
        return "pong";
    }
}
