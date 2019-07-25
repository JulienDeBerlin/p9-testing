package com.dummy.myerp.model.bean.comptabilite;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TestLigneEcritureComptable {

    @Test
    public void testToString() {

        CompteComptable compteComptable = new CompteComptable();
        compteComptable.setLibelle("Fournisseurs");
        compteComptable.setNumero(325);

        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable();
        ligneEcritureComptable.setCredit(new BigDecimal("123.98"));
        ligneEcritureComptable.setCompteComptable(compteComptable);
        ligneEcritureComptable.setLibelle("Location salle de réunion, Board Meeting Février 2019");

        assertEquals( "LigneEcritureComptable{compteComptable=CompteComptable{numero=325, libelle='Fournisseurs'}, libelle='Location salle de réunion, Board Meeting Février 2019', debit=null, credit=123.98}", ligneEcritureComptable.toString());


    }
}