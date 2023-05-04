package com.example.projetEtudiant.controller;

import com.example.projetEtudiant.exception.BadRequestException;
import com.example.projetEtudiant.model.*;
import com.example.projetEtudiant.service.InscriptionService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
@Log
@RequestMapping("/api")
public class InscriptionController {
   // private static final String STRUCTUREID_REQUIRED = "structureId required" ;
    private final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    @Autowired
    private InscriptionService inscriptionService;

    @PostMapping("/inscrire")
    public ResponseEntity<?> faireInscription(@RequestBody Inscription inscription) throws ParseException {

        try{
            Etudiant etudiant = null;
            LocalDate localDate = inscription.getEtudiant().getDateNaissance();
            LocalDate dateJour = LocalDate.now();
            String  anneeSolaire = String.valueOf(inscription.getAnneeScolaire());

            int age = (int) ChronoUnit.YEARS.between(localDate,dateJour);
            if (age < 18)
                throw  new BadRequestException("L'étudiant est trop jeune pour s'inscrire.");
            if (inscription.getEtudiant().getId()==null) {
               etudiant = new Etudiant();
                inscription.setEtudiant(etudiant);
            }else
            {
                etudiant = inscriptionService.findByIdEtudiant(inscription.getEtudiant().getId());
                if (etudiant == null)
                    throw new RuntimeException("L'etudiant n'existe pas");
                    inscription.setEtudiant(etudiant);
            }

            Classe  classe = inscriptionService.findByIdClasses(inscription.getClasse().getId());
            Double mensualite = Double.valueOf(classe.getMensualite());
            Double fraisInscription = Double.valueOf(classe.getFraisInscription());
            Double minimumDeposit = fraisInscription + mensualite;

            Double initialDeposit = inscription.getInitialDeposit();

            if (initialDeposit < minimumDeposit) {
                throw new IllegalArgumentException("Le montant minimum a déposé est " + minimumDeposit);
            }
                inscription.setClasse(classe);
          Inscription insrire =  inscriptionService.inscrire(inscription);
          //Paiement [] payer =  inscriptionService.proceedPayments(insrire, initialDeposit, minimumDeposit , mensualite);
            return ResponseEntity.status(HttpStatus.CREATED).body(insrire);
        }catch(Exception e){
            throw e;
        }
    }

    @PostMapping("/ajoutEtudiant")
    public Etudiant ajoutEtudiant(@RequestBody Etudiant etudiant ) throws Exception {
        try{
            if(etudiant.getId()==null)throw new BadRequestException("L'etudiant n'existe pas");
            if (etudiant == null || etudiant.getAge() < 18)
                throw new BadRequestException("L'etudiant n'est pas majeur");
            return inscriptionService.ajoutEtudiant(etudiant);
        }catch(Exception e){
            throw e;
        }
    }
    @PostMapping("/ajoutClasse")
    public Classe ajoutClasse(@RequestBody Classe classe ){
        try{
            return inscriptionService.ajoutClasse(classe);
        }catch(Exception e){
            throw e;
        }
    }
    @PostMapping("/ajoutFiliere")
    public Filiere ajoutFiliere(@RequestBody Filiere filiere ){
        try{
            return inscriptionService.ajoutFiliere(filiere);
        }catch(Exception e){
            throw e;
        }
    }
    @PostMapping("/ajoutNiveau")
    public Niveau ajoutNiveau(@RequestBody Niveau niveau ){
        try{
            return inscriptionService.ajoutNiveau(niveau);
        }catch(Exception e){
            throw e;
        }
    }

    /*@PostMapping("/payer")
    public Paiement fairePaiement(@ModelAttribute("paiement")PaiementDto paiementDto){
        Paiement paiement = inscriptionService.fairePaiement(paiementDto.getPaiement());
        paiement.setInscription(paiementDto.getInscription());
        paiement.setAmount(paiementDto.getAmount());
        paiement.setMois(paiementDto.getMois());
        return inscriptionService.fairePaiement(paiement);
    }*/
}
