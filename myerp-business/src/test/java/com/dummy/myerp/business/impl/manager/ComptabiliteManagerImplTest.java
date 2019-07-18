package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.sql.Date.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:transactionContextTest.xml")
public class ComptabiliteManagerImplTest extends AbstractBusinessManager {


    private static ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    private static DaoProxy daoProxyMock = mock(DaoProxy.class, Mockito.RETURNS_DEEP_STUBS);
    private static TransactionManager transactionManager = TransactionManager.getInstance();


    @BeforeAll
    private static void setUp() {
        AbstractBusinessManager.configure(null, daoProxyMock, transactionManager);
    }

    //----------------------------------TESTS CHECKS ECRITURE COMPTABLES--------------------------------------------//

    @Test
    void checkEcritureComptableUnit_Contraintes() throws FunctionalException {
        EcritureComptable ecritureComptable = new EcritureComptable();

        // Toutes contraintes validées
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));

        LocalDate localDate = LocalDate.of(2020, 12, 28);
        Date date = valueOf(localDate);
        ecritureComptable.setDate(date);

        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));
        manager.checkEcritureComptableUnit_Contraintes(ecritureComptable);

        // JournalComptable  null
        ecritureComptable.setJournal(null);
        ecritureComptable.setDate(date);

        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));
        FunctionalException thrown1 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_Contraintes(ecritureComptable));
        assertEquals("L'écriture comptable ne respecte pas les contraintes de validation. Le journal comptable ne doit pas être null.", thrown1.getMessage());


        // Taille du libellé = 0
        ecritureComptable.setJournal(new JournalComptable("AB", "journal fournisseurs"));
        ecritureComptable.setLibelle("");
        FunctionalException thrown2 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_Contraintes(ecritureComptable));
        assertEquals("L'écriture comptable ne respecte pas les contraintes de validation. Le libellé doit être compris entre 1 et 200 caractères.", thrown2.getMessage());


        // Format des montants non respecté
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal("123.987"),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal("123.987")));

        FunctionalException thrown3 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_Contraintes(ecritureComptable));
        assertEquals("L'écriture comptable ne respecte pas les contraintes de validation. " +
                        "Le format du montant comptable est invalide: max 13 chiffres et 2 décimaux. " +
                        "Le format du montant comptable est invalide: max 13 chiffres et 2 décimaux.",
                thrown3.getMessage());
    }


    @Test
    void checkEcritureComptableUnit_RG2() throws FunctionalException {
        EcritureComptable ecritureComptable = new EcritureComptable();

        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        LocalDate localDate = LocalDate.of(2020, 12, 28);
        Date date = valueOf(localDate);
        ecritureComptable.setDate(date);

        ecritureComptable.setLibelle("Libelle");

        // Ecriture équilibrée
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));
        manager.checkEcritureComptableUnit_RG2(ecritureComptable);

        // Ecriture équilibrée avec montants décimaux et non décimaux
        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal("123.20"),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, new BigDecimal("123"),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(3),
                null, null,
                new BigDecimal("246.20")));
        manager.checkEcritureComptableUnit_RG2(ecritureComptable);


        // Ecriture non-équilibrée avec montants entiers
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(1234)));

        FunctionalException thrown1 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG2(ecritureComptable));
        assertEquals("L'écriture comptable n'est pas équilibrée.", thrown1.getMessage());

        // Ecriture non-équilibrée avec montants décimaux
        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal("123.90"),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal("12.39")));

        FunctionalException thrown2 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG2(ecritureComptable));
        assertEquals("L'écriture comptable n'est pas équilibrée.", thrown2.getMessage());

    }

    @Test
    void checkEcritureComptableUnit_RG3() {

        // 0 ligne d'écriture au crédit
        EcritureComptable ecritureComptable = new EcritureComptable();

        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        LocalDate localDate = LocalDate.of(2020, 12, 28);
        Date date = valueOf(localDate);
        ecritureComptable.setDate(date);

        ecritureComptable.setLibelle("Libelle");

        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2), null, new BigDecimal(300), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(3), null, new BigDecimal(300), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4), null, new BigDecimal(222), null));

        FunctionalException thrown1 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG3(ecritureComptable));
        assertEquals("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.", thrown1.getMessage());


        // 0 ligne d'écriture au débit
        ecritureComptable.getListLigneEcriture().clear();

        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, null, new BigDecimal(123)));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(300)));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(3), null, null, new BigDecimal(300)));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4), null, null, new BigDecimal(222)));

        FunctionalException thrown2 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG3(ecritureComptable));
        assertEquals("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.", thrown2.getMessage());


        // 1 seule ligne d'écriture comptable avec montant au débit ET au crédit

        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), new BigDecimal(123)));

        FunctionalException thrown3 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG3(ecritureComptable));
        assertEquals("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.", thrown3.getMessage());
    }

    @Test
    void checkEcritureComptable_CompteComptable_Unique() {
        EcritureComptable ecritureComptable = new EcritureComptable();

        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        LocalDate localDate = LocalDate.of(2020, 12, 28);
        Date date = valueOf(localDate);
        ecritureComptable.setDate(date);

        ecritureComptable.setLibelle("Libelle");

        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null, new BigDecimal(123)));

        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123), null));

        FunctionalException thrown = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptable_CompteComptable_Unique(ecritureComptable));
        assertEquals("Une écriture comptable ne peut pas débiter et créditer un même compte.", thrown.getMessage());
    }


    @Test
    void checkEcritureComptableUnit_Debit_or_Credit() {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        LocalDate localDate = LocalDate.of(2020, 12, 28);
        Date date = valueOf(localDate);
        ecritureComptable.setDate(date);

        ecritureComptable.setLibelle("Libelle");

        // Une ligne d'écriture avec un montant au débit ET au crédit

        ecritureComptable.getListLigneEcriture().clear();

        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(100), new BigDecimal(123)));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2), null, new BigDecimal(123), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(3), null, null, new BigDecimal(100)));

        FunctionalException thrown = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_Debit_or_Credit(ecritureComptable));
        assertEquals("Une écriture comptable ne doit pas avoir un montant au débit et au crédit.", thrown.getMessage());
    }

    @Test
    void checkEcritureComptableUnit_RG5() throws FunctionalException {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        LocalDate localDate = LocalDate.of(2020, 12, 28);
        Date date = valueOf(localDate);
        ecritureComptable.setDate(date);

        // Reference null
        FunctionalException thrown5 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG5(ecritureComptable));
        assertEquals("La référence ne peut pas être null.", thrown5.getMessage());

        // Correct
        ecritureComptable.setReference("AC-2020/00003");
        manager.checkEcritureComptableUnit_RG5(ecritureComptable);

        // Code journal incorrect
        ecritureComptable.setReference("AAC-2020/00003");
        FunctionalException thrown = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG5(ecritureComptable));
        assertEquals("Format de la référence incorrect : le code journal ne correspond pas au journal de l'écriture.", thrown.getMessage());

        // Année incorrecte
        ecritureComptable.setReference("AC-2019/00003");
        FunctionalException thrown1 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG5(ecritureComptable));
        assertEquals("Format de la référence incorrect : l'année indiquée dans la référence ne correspond pas à la date de l'écriture.", thrown1.getMessage());

        // Séquence incorrecte
        ecritureComptable.setReference("AC-2020/3");
        FunctionalException thrown2 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG5(ecritureComptable));
        assertEquals("Format de la référence incorrect : le numéro de séquence doit être représenté avec 5 chiffres.", thrown2.getMessage());

        // Année incorrecte
        ecritureComptable.setReference("AC-20/00003");
        FunctionalException thrown3 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG5(ecritureComptable));
        assertEquals("Format de la référence incorrect : l'année indiquée dans la référence ne correspond pas à la date de l'écriture.", thrown3.getMessage());

        // Séparateurs incorrects
        ecritureComptable.setReference("AC/2020/00003");
        FunctionalException thrown4 = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptableUnit_RG5(ecritureComptable));
        assertEquals("Format de la référence incorrect : le code journal est suivi d'un - et l'année d'un /", thrown4.getMessage());


    }

    @Test
    void checkEcritureComptable_RG6() throws NotFoundException, FunctionalException {

        // Nouvelle écriture pas encore persistée et donc sans ID
        EcritureComptable ecritureNonPersistee = new EcritureComptable();
        ecritureNonPersistee.setJournal(new JournalComptable("AC", "Achat"));
        LocalDate localDate1 = LocalDate.of(2020, 12, 28);
        Date date1 = valueOf(localDate1);
        ecritureNonPersistee.setDate(date1);
        ecritureNonPersistee.setReference("AC-2020/00987");

        // Ecriture en BDD
        EcritureComptable ecritureBDD = new EcritureComptable();
        ecritureBDD.setId(100);
        ecritureBDD.setJournal(new JournalComptable("AC", "Achat"));
        LocalDate localDate2 = LocalDate.of(2020, 03, 23);
        Date date2 = valueOf(localDate2);
        ecritureBDD.setDate(date2);
        ecritureBDD.setReference("AC-2020/00987");

        // Une écriture non persistée à la même référence qu'une autre écriture en BDD
        when(getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(Mockito.anyString())).thenReturn(ecritureBDD);
        FunctionalException thrown = assertThrows(FunctionalException.class, () -> manager.checkEcritureComptable_RG6(ecritureNonPersistee));
        assertEquals("La BDD contient une autre écriture comptable avec la même référence.", thrown.getMessage());

        // La recherche d'une écriture persistée par son ID ne retourne qu'elle même : pas d'exception
        when(getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(Mockito.anyString())).thenReturn(ecritureBDD);
        manager.checkEcritureComptable_RG6(ecritureBDD);

        // La recherche d'une écriture non persistée par son ID ne retourne rien : pas d'exception
        when(getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(Mockito.anyString())).thenThrow(new NotFoundException());
        manager.checkEcritureComptable_RG6(ecritureNonPersistee);
    }


    //--------------------------------------TESTS FONCTIONS CRUD-------------------------------------------------//


    @Test
    @DisplayName("addReference / tests if reference is added")
    public void addReference() throws FunctionalException, NotFoundException {

        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("TR", "Achat"));

        LocalDate localDate = LocalDate.of(2020, 12, 28);
        Date date = valueOf(localDate);
        ecritureComptable.setDate(date);

        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));


        // Code journal inexistant en BDD
        when(getDaoProxy().getComptabiliteDao().isCodeJournalValid(Mockito.anyString())).thenReturn(false);
        FunctionalException thrown = assertThrows(FunctionalException.class, () -> manager.addReference(ecritureComptable));
        assertEquals("Le code journal n'existe pas en base de donnée.", thrown.getMessage());

        // Code journal et année existants en BDD
        when(getDaoProxy().getComptabiliteDao().isCodeJournalValid(Mockito.anyString())).thenReturn(true);

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setJournalCode("TR");
        sequenceEcritureComptable.setAnnee(2020);
        sequenceEcritureComptable.setDerniereValeur(342);

        when(getDaoProxy().getComptabiliteDao().getSequenceJournal(any(EcritureComptable.class))).thenReturn(sequenceEcritureComptable);

        assertEquals(null, ecritureComptable.getReference());
        manager.addReference(ecritureComptable);
        assertEquals("TR-2020/00343", ecritureComptable.getReference());

        // Code journal existant et année inexistante en BDD
        when(getDaoProxy().getComptabiliteDao().getSequenceJournal(any(EcritureComptable.class))).thenThrow(NotFoundException.class);
        manager.addReference(ecritureComptable);
        assertEquals("TR-2020/00001", ecritureComptable.getReference());

        // Code journal et année existants en BDD mais lastReference = 99999
        sequenceEcritureComptable.setDerniereValeur(99999);
        when(getDaoProxy().getComptabiliteDao().getSequenceJournal(any(EcritureComptable.class))).thenReturn(sequenceEcritureComptable);
        FunctionalException thrown2 = assertThrows(FunctionalException.class, () -> manager.addReference(ecritureComptable));
        assertEquals("Nombre maximal d'écritures atteint. Veuillez choisir un nouveau journal.", thrown2.getMessage());

        // FIXME : test de l'insert ou update de la table SequenceEcritureComptable fait dans la couche DAO. Ok?

    }

    @Test
    public void getListEcritureComptable() {
        List<EcritureComptable> ecritureComptableListMock =
                new ArrayList<>(Arrays.asList(new EcritureComptable(),
                        new EcritureComptable(),
                        new EcritureComptable()));

        when(getDaoProxy().getComptabiliteDao().getListEcritureComptable()).thenReturn(ecritureComptableListMock);

        List<EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();
        assertEquals(3, ecritureComptableList.size());

    }

    @Test
    public void getListCompteComptable() {
        List<CompteComptable> compteComptableListMock =
                new ArrayList<>(Arrays.asList(new CompteComptable(12, "libellé"),
                        new CompteComptable(13, "libellé"),
                        new CompteComptable(14, "libellé")));
        when(getDaoProxy().getComptabiliteDao().getListCompteComptable()).thenReturn(compteComptableListMock);

        List<CompteComptable> compteComptableList = manager.getListCompteComptable();
        assertEquals(3, compteComptableList.size());
        assertEquals(12, compteComptableList.get(0).getNumero());

    }

    @Test
    public void getListJournalComptable() {
        List<JournalComptable> journalComptableListMock =
                new ArrayList<>(Arrays.asList(new JournalComptable("AB", "journal de banque"),
                        new JournalComptable("JU", "journal fournisseurs")));
        when(getDaoProxy().getComptabiliteDao().getListJournalComptable()).thenReturn(journalComptableListMock);

        List<JournalComptable> journalComptableList = manager.getListJournalComptable();
        assertEquals(2, journalComptableList.size());
        assertEquals("journal fournisseurs", journalComptableList.get(1).getLibelle());

    }

    @Test
    @DisplayName("Insert denied")
    void insertEcritureComptableException() throws FunctionalException, NotFoundException {

        // Initial setting EcritureComptable
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("BQ", "Banque"));
        LocalDate localDate = LocalDate.of(2016, 02, 25);
        Date date = valueOf(localDate);
        ecritureComptable.setDate(date);
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401), null, new BigDecimal(123), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606), null, null, new BigDecimal(123)));

        // Référence null
        FunctionalException thrown1 = assertThrows(FunctionalException.class, () -> manager.insertEcritureComptable(ecritureComptable));
        assertEquals("La référence ne peut pas être null.", thrown1.getMessage());


        // Ecriture non-équilibrée
        ecritureComptable.setReference("BQ-2016/00052");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(3), null, null, new BigDecimal(12300)));
        FunctionalException thrown3 = assertThrows(FunctionalException.class, () -> manager.insertEcritureComptable(ecritureComptable));
        assertEquals("L'écriture comptable n'est pas équilibrée.", thrown3.getMessage());


        // Date null
        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401), null, new BigDecimal(123), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606), null, null, new BigDecimal(123)));
        ecritureComptable.setDate(null);
        FunctionalException thrown2 = assertThrows(FunctionalException.class, () -> manager.insertEcritureComptable(ecritureComptable));
        assertEquals("L'écriture comptable ne respecte pas les contraintes de validation. La date ne doit pas être null.", thrown2.getMessage());

    }



}
