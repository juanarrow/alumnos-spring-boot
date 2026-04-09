package com.example.alumnos.controller;

import com.example.alumnos.service.SaludoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private final SaludoService saludoService;

    public HomeController(SaludoService saludoService) {
        this.saludoService = saludoService;
    }

    @GetMapping("/saludo")
    public String saludo(Model model) {
        model.addAttribute("mensaje", saludoService.obtenerSaludo());
        return "saludo";
    }


}
