package com.securite.authentification.controller;

import com.securite.authentification.dto.AuthentificationDTO;
import com.securite.authentification.dto.UtilisateurRequest;
import com.securite.authentification.dto.UtilisateurResponse;
import com.securite.authentification.dto.UtilisateurUpdateRequest;
import com.securite.authentification.securite.JwtService;
import com.securite.authentification.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs", description = "Opérations de gestion des utilisateurs et authentification")
public class UtilisateurController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private  UtilisateurService utilisateurService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    @Operation(summary = "Créer un utilisateur", description = "Permet de créer un nouvel utilisateur")
    public ResponseEntity<UtilisateurResponse> create(@Valid @RequestBody UtilisateurRequest request) {
        UtilisateurResponse response = utilisateurService.createUtilisateur(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/activation")
    @Operation(summary = "Activer un compte utilisateur", description = "Active un utilisateur via un token ou un code d’activation")
    public ResponseEntity<String> activation(@RequestBody Map<String, String> activation) {
        try {
            this.utilisateurService.activation(activation);
            return ResponseEntity.ok("Compte activé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/connexion")
    @Operation(summary = "Connexion", description = "Permet de se connecter et de récupérer un token JWT")
    public Map< String, String > connexion(@Valid @RequestBody AuthentificationDTO authentificationDTO) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (authentificationDTO.username(), authentificationDTO.motDePasse())
        );

        if(authenticate.isAuthenticated()) {
            return this.jwtService.generate(authentificationDTO.username());

        }
        return null;

    }


    @GetMapping("/all")
    @Operation(summary = "Récupérer tous les utilisateurs", description = "Retourne la liste de tous les utilisateurs")
    public ResponseEntity<List<UtilisateurResponse>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par ID", description = "Retourne les infos d’un utilisateur spécifique")
    public ResponseEntity<UtilisateurResponse> getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Récupérer un utilisateur par mail", description = "Retourne les infos d’un utilisateur par son mail")    public ResponseEntity<UtilisateurResponse> getUtilisateurByEmail(@PathVariable String email) {
        return utilisateurService.getUtilisateurByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/actif/{etat}")
    @Operation(summary = "Récupérer l'etat d'un utilisateur", description = "Retourne le statut d’un utilisateur par son actif (true ou false) ")
    public ResponseEntity<List<UtilisateurResponse>> getUtilisateursParEtat(@PathVariable boolean etat) {
        return ResponseEntity.ok(utilisateurService.getUtilisateursActifs(etat));
    }

    @PutMapping("/{id}/update")
    @Operation(summary = "Modifier un utilisateur", description = "Permet de modifier les infos d’un utilisateur existant")
    public ResponseEntity<UtilisateurResponse> updateUtilisateur(
            @PathVariable Long id,
            @Valid @RequestBody UtilisateurUpdateRequest request) {

        UtilisateurResponse response = utilisateurService.updateUtilisateur(id, request);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/desactiver")
    @Operation(summary = "Desactiver un utilisateur par ID", description = "desactiver un utilisateur par ID")
    public ResponseEntity<UtilisateurResponse> desactiver(@PathVariable Long id) {
        UtilisateurResponse response = utilisateurService.desactiverUtilisateur(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur", description = "Permet de supprimer un utilisateur par ID")
    public ResponseEntity<String> supprimer(@PathVariable Long id) {
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.ok("Utilisateur supprimé avec succès.");
    }

}
