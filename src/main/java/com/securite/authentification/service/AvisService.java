package com.securite.authentification.service;

import com.securite.authentification.model.Avis;
import com.securite.authentification.repository.AvisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvisService {
    private  final AvisRepository avisRepository;


    public AvisService(AvisRepository avisRepository) {
        this.avisRepository = avisRepository;
    }

    public void creer(Avis avis) {
        this.avisRepository.save(avis);

    }
}
