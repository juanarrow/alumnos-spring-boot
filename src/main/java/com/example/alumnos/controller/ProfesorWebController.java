package com.example.alumnos.controller;

import com.example.alumnos.entity.Profesor;
import com.example.alumnos.repository.ProfesorRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profesores")
public class ProfesorWebController {

    private final ProfesorRepository profesorRepository;

    public ProfesorWebController(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @GetMapping
    public String listarProfesores(Model model) {
        model.addAttribute("profesores", profesorRepository.findAll());
        return "profesores/ProfesoresListView";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("profesor", new Profesor());
        model.addAttribute("accion", "Añadir");
        return "profesores/ProfessorFormView";
    }

    @PostMapping("/nuevo")
    public String guardarNuevo(@Valid @ModelAttribute("profesor") Profesor profesor,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("accion", "Añadir");
            return "profesores/ProfessorFormView";
        }
        profesorRepository.save(profesor);
        return "redirect:/profesores";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Profesor profesor = profesorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado: " + id));
        model.addAttribute("profesor", profesor);
        model.addAttribute("accion", "Editar");
        return "profesores/ProfessorFormView";
    }

    @PostMapping("/{id}/editar")
    public String guardarEdicion(@PathVariable Long id,
                                 @Valid @ModelAttribute("profesor") Profesor profesor,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("accion", "Editar");
            return "profesores/ProfessorFormView";
        }
        profesor.setId(id);
        profesorRepository.save(profesor);
        return "redirect:/profesores";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        profesorRepository.deleteById(id);
        return "redirect:/profesores";
    }
}
