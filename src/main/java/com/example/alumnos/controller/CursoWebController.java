package com.example.alumnos.controller;

import com.example.alumnos.entity.Curso;
import com.example.alumnos.entity.Profesor;
import com.example.alumnos.repository.CursoRepository;
import com.example.alumnos.repository.ProfesorRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cursos")
public class CursoWebController {

    private final CursoRepository cursoRepository;
    private final ProfesorRepository profesorRepository;

    public CursoWebController(CursoRepository cursoRepository,
                               ProfesorRepository profesorRepository) {
        this.cursoRepository = cursoRepository;
        this.profesorRepository = profesorRepository;
    }

    @GetMapping
    public String listarCursos(Model model) {
        model.addAttribute("cursos", cursoRepository.findAll());
        return "cursos/CursoListingView";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("curso", new Curso());
        model.addAttribute("profesores", profesorRepository.findAll());
        model.addAttribute("accion", "Añadir");
        return "cursos/CursoFormView";
    }

    @PostMapping("/nuevo")
    public String guardarNuevo(@Valid @ModelAttribute("curso") Curso curso,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("profesores", profesorRepository.findAll());
            model.addAttribute("accion", "Añadir");
            return "cursos/CursoFormView";
        }
        resolverProfesor(curso);
        cursoRepository.save(curso);
        return "redirect:/cursos";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado: " + id));
        model.addAttribute("curso", curso);
        model.addAttribute("profesores", profesorRepository.findAll());
        model.addAttribute("accion", "Editar");
        return "cursos/CursoFormView";
    }

    @PostMapping("/{id}/editar")
    public String guardarEdicion(@PathVariable Long id,
                                 @Valid @ModelAttribute("curso") Curso curso,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("profesores", profesorRepository.findAll());
            model.addAttribute("accion", "Editar");
            return "cursos/CursoFormView";
        }
        curso.setId(id);
        resolverProfesor(curso);
        cursoRepository.save(curso);
        return "redirect:/cursos";
    }

    private void resolverProfesor(Curso curso) {
        if (curso.getProfesor() != null && curso.getProfesor().getId() != null) {
            Profesor profesor = profesorRepository.findById(curso.getProfesor().getId()).orElse(null);
            curso.setProfesor(profesor);
        } else {
            curso.setProfesor(null);
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        cursoRepository.deleteById(id);
        return "redirect:/cursos";
    }
}
