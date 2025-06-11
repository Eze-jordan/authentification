package com.securite.authentification.mapper;

import com.securite.authentification.dto.UtilisateurRequest;
import com.securite.authentification.dto.UtilisateurResponse;
import com.securite.authentification.model.Role;
import com.securite.authentification.model.Utilisateur;

public class UtilisateurMapper {

    public static Utilisateur toEntity(UtilisateurRequest request) {
        Utilisateur user = new Utilisateur();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setMotDePasse(request.getMotDePasse());
        user.setSexe(request.getSexe());
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);
        return user;
    }

    public static UtilisateurResponse toDto(Utilisateur user) {
        UtilisateurResponse dto = new UtilisateurResponse();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setSexe(user.getSexe());
        dto.setRole(user.getRole());
        dto.setActif(user.isActif());
        return dto;
    }


}
