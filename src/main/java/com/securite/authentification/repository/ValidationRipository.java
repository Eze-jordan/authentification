package com.securite.authentification.repository;
import com.securite.authentification.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationRipository extends JpaRepository<Validation, Long> {

    Optional<Validation> findByCode(String code);
}
