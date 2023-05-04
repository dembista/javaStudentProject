package com.example.projetEtudiant.controller.dto;

import com.example.projetEtudiant.model.Inscription;
import com.example.projetEtudiant.model.Paiement;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Builder
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaiementDto {
    private Paiement paiement;
    private Inscription inscription;
    private String mois;
    private Double amount;
}
