package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.DatosDemoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private final DatosDemoService datosDemoService;

    public AdminController(
            DatosDemoService datosDemoService) {

        this.datosDemoService = datosDemoService;
    }

    @PostMapping("/reset")
    public String resetearBase() {

        datosDemoService.resetearBase();

        return "Base restaurada";
    }
}