package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ComptabiliteManagerImplTest {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();


    //--------------------------------------TESTS FONCTIONS CRUD-------------------------------------------------//



    //----------------------------------TESTS CHECKS ECRITURE COMPTABLES--------------------------------------------//

    @Test
    @DisplayName("checkEcritureComptableUnitOK / all constraints respected")
    public void checkEcritureComptableUnitOk() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test
    @DisplayName("checkEcritureComptableUnitViolation / constraints non respected")
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();

        // JournalComptable is null
        vEcritureComptable.setJournal(null);
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));
        FunctionalException thrown1 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
        assertEquals("L'écriture comptable ne respecte pas les contraintes de validation. Le journal comptable ne doit pas être null.", thrown1.getCause().getMessage());


        // taille du libellé = 0
        vEcritureComptable.setJournal(new JournalComptable("AB", "journal fournisseurs"));
        vEcritureComptable.setLibelle("");
        FunctionalException thrown2 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
        assertEquals("L'écriture comptable ne respecte pas les contraintes de validation. Le libellé doit être compris entre 1 et 200 caractères.", thrown2.getCause().getMessage());


        // format des montants non respecté
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().clear();
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal("123.987"),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal("123.987")));

        FunctionalException thrown3 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
        assertEquals("L'écriture comptable ne respecte pas les contraintes de validation. " +
                "Le format du montant comptable est invalide: max 13 chiffres et 2 décimaux. " +
                "Le format du montant comptable est invalide: max 13 chiffres et 2 décimaux.",
                thrown3.getCause().getMessage());
    }

    @Test
    @DisplayName("checkEcritureComptableUnitRG2 / balance")
    public void checkEcritureComptableUnitRG2() throws Exception {

        // avec montants entiers
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(1234)));

        FunctionalException thrown1 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
        assertEquals("L'écriture comptable n'est pas équilibrée.", thrown1.getMessage());

        // avec montants décimaux
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal("123.90"),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal("12.39")));

        FunctionalException thrown2 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
        assertEquals("L'écriture comptable n'est pas équilibrée.", thrown2.getMessage());
    }


    @Test
    public void checkEcritureComptableUnitRG3() throws Exception {


        // 1 seule ligne d'écriture comptable
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));

        FunctionalException thrown1 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
        assertEquals("L'écriture comptable ne respecte pas les contraintes de validation. L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.", thrown1.getCause().getMessage());


        // 1 seule ligne d'écriture comptable avec montant au débit ET au crédit

        vEcritureComptable.getListLigneEcriture().clear();
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123), new BigDecimal(123)));

        FunctionalException thrown5 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
        assertEquals("L'écriture comptable ne respecte pas les contraintes de validation. L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.", thrown5.getCause().getMessage());


        // 2 lignes d'écriture au débit
        vEcritureComptable.getListLigneEcriture().clear();

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, new BigDecimal(123),
                null));

        FunctionalException thrown2 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
        assertEquals("L'écriture comptable n'est pas équilibrée.", thrown2.getMessage());


        // 2 lignes d'écritures au crédit
        vEcritureComptable.getListLigneEcriture().clear();

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null, new BigDecimal(123)));

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null, new BigDecimal(123)));

        FunctionalException thrown3 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
        assertEquals("L'écriture comptable n'est pas équilibrée.", thrown3.getMessage());


        // Une ligne d'écriture avec un montant au débit ET au crédit

        vEcritureComptable.getListLigneEcriture().clear();

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(100), new BigDecimal(123)));

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null,  new BigDecimal(123), null));

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(3),
                null, null, new BigDecimal(100)));

        FunctionalException thrown6 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
        assertEquals("Une écriture comptable ne doit pas avoir un montant au débit et au crédit.", thrown6.getMessage());


        // 2 lignes d'écritures sur le même compte
        //TODO: cela devrait être interdit. Rajouter une regle de gestion qui interdit ça?
//        vEcritureComptable.getListLigneEcriture().clear();
//
//        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
//                null, null, new BigDecimal(123)));
//
//        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
//                null,  new BigDecimal(123), null));
//
//        FunctionalException thrown4 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));
//        assertEquals("L'écriture comptable n'est pas équilibrée.", thrown4.getMessage());

    }

}
