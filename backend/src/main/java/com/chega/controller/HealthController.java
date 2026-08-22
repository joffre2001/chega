package com.chega.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public Map<String, String> verificarStatus() {
        return Map.of(
                "aplicacao", "CHEGA",
                "status", "online",
                "mensagem", "Você não precisa descobrir tudo sozinho."
        );
    }
}