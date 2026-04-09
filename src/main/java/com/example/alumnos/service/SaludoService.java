package com.example.alumnos.service;

import org.springframework.stereotype.Service;

@Service
public class SaludoService {
    public String obtenerSaludo() {
        return "Hola desde el servicioooooooo";
    }

}
