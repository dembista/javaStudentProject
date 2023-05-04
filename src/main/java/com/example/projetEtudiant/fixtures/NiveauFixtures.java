package com.example.projetEtudiant.fixtures;

import com.example.projetEtudiant.model.Niveau;
import com.example.projetEtudiant.repositories.Niveaux;
import com.example.projetEtudiant.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NiveauFixtures {
    @Autowired
    private InscriptionService inscriptionService;
    @Autowired
    private Niveaux iniveau;

    public void addNiveau(){
        List<Niveau> niveaux = inscriptionService.listNiveau();

    }
    Niveau [] nives = {
           new Niveau(null,"Licence1") ,
            new Niveau(null,"Licence2") ,
            new Niveau(null,"Licence3") ,
            new Niveau(null,"Master1") ,
            new Niveau(null,"Master2")
    };
    //iniveau.in(nives);

}
