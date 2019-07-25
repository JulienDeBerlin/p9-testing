package com.dummy.myerp.model.bean.comptabilite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestSequenceEcritureComptable {

    @Test
    public void testToString() {

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setDerniereValeur(87);
        sequenceEcritureComptable.setAnnee(2023);
        sequenceEcritureComptable.setJournalCode("GF");

        assertEquals("SequenceEcritureComptable{annee=2023, journal=GF, derniereValeur=87}", sequenceEcritureComptable.toString());

    }
}