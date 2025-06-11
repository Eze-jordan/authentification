package com.securite.authentification.service;

import com.securite.authentification.dto.UtilisateurRequest;
import com.securite.authentification.dto.UtilisateurResponse;
import com.securite.authentification.dto.UtilisateurUpdateRequest;
import com.securite.authentification.mapper.UtilisateurMapper;
import com.securite.authentification.model.Role;
import com.securite.authentification.model.Utilisateur;
import com.securite.authentification.model.Validation;
import com.securite.authentification.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurService  implements UserDetailsService {

    private  final UtilisateurRepository utilisateurRepository;
    private final  BCryptPasswordEncoder passwordEncoder;
    private  final ValidationService validationService;


    public UtilisateurService(UtilisateurRepository utilisateurRepository, BCryptPasswordEncoder passwordEncoder, ValidationService validationService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.validationService = validationService;

    }


    public UtilisateurResponse createUtilisateur(UtilisateurRequest request) {
        // Rôle par défaut
        if (request.getRole() == null) {
            request.setRole(Role.USER);
        }

        // Mapper le DTO en entité
        Utilisateur utilisateur = UtilisateurMapper.toEntity(request);

        // Encoder le mot de passe
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));

        // Sauvegarder l'utilisateur
        Utilisateur saved = utilisateurRepository.save(utilisateur);
        this.validationService.enregister(utilisateur);

        // Retourner la réponse DTO
        return UtilisateurMapper.toDto(saved);

    }


    public List<UtilisateurResponse> getAllUtilisateurs() {
        return utilisateurRepository.findAll()
                .stream()
                .map(UtilisateurMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<UtilisateurResponse> getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .map(UtilisateurMapper::toDto);
    }

    public Optional<UtilisateurResponse> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .map(UtilisateurMapper::toDto);
    }

    public List<UtilisateurResponse> getUtilisateursActifs(boolean actif) {
        return utilisateurRepository.findByActif(actif)
                .stream()
                .map(UtilisateurMapper::toDto)
                .collect(Collectors.toList());
    }

    public void activation(Map<String, String> activation) {
        Validation validation = validationService.lireEnFonctionDuCode(activation.get("code"));
        if (Instant.now().isAfter(validation.getExpiration())) {
            throw new RuntimeException("Votre code a expiré");
        }

        Utilisateur utilisateurActiver = utilisateurRepository.findById(validation.getUtilisateur().getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));

        utilisateurActiver.setActif(true);
        utilisateurRepository.save(utilisateurActiver);
    }

    public UtilisateurResponse updateUtilisateur(Long id, UtilisateurUpdateRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());

        // Encodage du mot de passe si modifié
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));

        Utilisateur updated = utilisateurRepository.save(utilisateur);
        return UtilisateurMapper.toDto(updated);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.utilisateurRepository.findByEmail(username).orElseThrow (()
               -> new UsernameNotFoundException(
                       "Aucun utilisateur ne conrespond à cet identifiant"
        ));
    }
    public UtilisateurResponse desactiverUtilisateur(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        utilisateur.setActif(false);
        Utilisateur updated = utilisateurRepository.save(utilisateur);
        return UtilisateurMapper.toDto(updated);
    }

    public void supprimerUtilisateur(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur introuvable");
        }
        utilisateurRepository.deleteById(id);
    }

}
