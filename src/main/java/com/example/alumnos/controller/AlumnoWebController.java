package com.example.alumnos.controller;

import com.example.alumnos.entity.Alumno;
import com.example.alumnos.entity.Curso;
import com.example.alumnos.repository.AlumnoRepository;
import com.example.alumnos.repository.CursoRepository;
import com.example.alumnos.service.CloudinaryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/alumnos")
public class AlumnoWebController {

    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;
    private final CloudinaryService cloudinaryService;

    public AlumnoWebController(AlumnoRepository alumnoRepository,
                               CursoRepository cursoRepository,
                               CloudinaryService cloudinaryService) {
        this.alumnoRepository = alumnoRepository;
        this.cursoRepository = cursoRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public String listarAlumnos(Model model) {
        model.addAttribute("alumnos", alumnoRepository.findAll());
        return "alumnos/AlumnoListingView";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("alumno", new Alumno());
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("accion", "Añadir");
        return "alumnos/AlumnoFormView";
    }

    @PostMapping("/nuevo")
    public String guardarNuevo(@Valid @ModelAttribute("alumno") Alumno alumno,
                               BindingResult result,
                               @RequestParam("foto") MultipartFile foto,
                               Model model) throws Exception {
        if (result.hasErrors()) {
            model.addAttribute("cursos", cursoRepository.findAll());
            model.addAttribute("accion", "Añadir");
            return "alumnos/AlumnoFormView";
        }
        if (!foto.isEmpty()) {
            alumno.setFotoUrl(cloudinaryService.subirImagen(foto, "alumnos"));
        }
        resolverCurso(alumno);
        alumnoRepository.save(alumno);
        return "redirect:/alumnos";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado: " + id));
        model.addAttribute("alumno", alumno);
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("accion", "Editar");
        return "alumnos/AlumnoFormView";
    }

    @PostMapping("/{id}/editar")
    public String guardarEdicion(@PathVariable Long id,
                                 @Valid @ModelAttribute("alumno") Alumno alumno,
                                 BindingResult result,
                                 @RequestParam("foto") MultipartFile foto,
                                 Model model) throws Exception {
        if (result.hasErrors()) {
            model.addAttribute("cursos", cursoRepository.findAll());
            model.addAttribute("accion", "Editar");
            return "alumnos/AlumnoFormView";
        }
        alumno.setId(id);
        if (!foto.isEmpty()) {
            alumno.setFotoUrl(cloudinaryService.subirImagen(foto, "alumnos"));
        } else {
            alumnoRepository.findById(id).ifPresent(a -> alumno.setFotoUrl(a.getFotoUrl()));
        }
        resolverCurso(alumno);
        alumnoRepository.save(alumno);
        return "redirect:/alumnos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        alumnoRepository.deleteById(id);
        return "redirect:/alumnos";
    }

    private void resolverCurso(Alumno alumno) {
        if (alumno.getCurso() != null && alumno.getCurso().getId() != null) {
            Curso curso = cursoRepository.findById(alumno.getCurso().getId()).orElse(null);
            alumno.setCurso(curso);
        } else {
            alumno.setCurso(null);
        }
    }
}
