package com.securite.authentification.controller;

import com.securite.authentification.model.Avis;
import com.securite.authentification.service.AvisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avis")
@Tag(name = "Avis", description = "Donner vos avis")
public class AvisController {
    private final AvisService avisService;

    public AvisController(AvisService avisService) {
        this.avisService = avisService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Ajouter un avis ", description = "Ajouter un avis sur les authentifications")
    public void creer(@RequestBody Avis avis) {
        this.avisService.creer(avis);
    }
}
