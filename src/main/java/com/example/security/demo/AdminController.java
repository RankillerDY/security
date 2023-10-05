package com.example.security.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @GetMapping
    public String get() {
        return "GET:: admin controller";
    }
    @PostMapping
    public String post() {
        return "GET:: admin controller";
    }
    @PutMapping
    public String put() {
        return "GET:: admin controller";
    }


}
