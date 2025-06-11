package com.securite.authentification.repository;

import com.securite.authentification.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);

    List<Utilisateur> findByActif(boolean actif);

}
