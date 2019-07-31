package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.*;

import static com.dummy.myerp.technical.statictools.StaticTools.convertDateToCalendar;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================

    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) throws FunctionalException {

        int yearEcritureComptable = convertDateToCalendar(pEcritureComptable.getDate()).get(Calendar.YEAR);
        String referenceEcritureComptable = pEcritureComptable.getJournal().getCode() + "-" + yearEcritureComptable + "/";


        if (!getDaoProxy().getComptabiliteDao().isCodeJournalValid(pEcritureComptable.getJournal().getCode())) {
            throw new FunctionalException("Le code journal n'existe pas en base de donnée.");
        } else {

            try {
                SequenceEcritureComptable sequenceEcritureComptable = getDaoProxy().getComptabiliteDao().getSequenceJournal(pEcritureComptable);

                if (sequenceEcritureComptable.getDerniereValeur() == 99999) {
                    throw new FunctionalException("Nombre maximal d'écritures atteint. Veuillez choisir un nouveau journal.");
                } else {
                    referenceEcritureComptable += (StringUtils.leftPad(String.valueOf(sequenceEcritureComptable.getDerniereValeur() + 1), 5, "0"));
                    getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(sequenceEcritureComptable);
                }

            } catch (NotFoundException notFoundException) {
                referenceEcritureComptable += "00001";
                getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(yearEcritureComptable, pEcritureComptable.getJournal().getCode());
            }
            pEcritureComptable.setReference(referenceEcritureComptable);
        }
    }

    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion.
     *
     * @param pEcritureComptable -
     * @throws FunctionalException si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {

        checkEcritureComptableUnit_Contraintes(pEcritureComptable);
        checkEcritureComptableUnit_RG2(pEcritureComptable);
        checkEcritureComptableUnit_RG3(pEcritureComptable);
        checkEcritureComptableUnit_Debit_or_Credit(pEcritureComptable);
        checkEcritureComptableUnit_RG5(pEcritureComptable);
        checkEcritureComptable_RG6(pEcritureComptable);
        checkEcritureComptable_CompteComptable_Unique(pEcritureComptable);
    }

    /**
     * Cette méthode lance une exception si les contraintes unitaires sur les attributs de l'écriture comptable ne sont pas respectées.
     * Vérification des annotations de validation.
     *
     * @param pEcritureComptable -
     * @throws FunctionalException
     */
    protected void checkEcritureComptableUnit_Contraintes(EcritureComptable pEcritureComptable) throws FunctionalException {
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            Iterator<ConstraintViolation<EcritureComptable>> it = vViolations.iterator();
            String customMessage = "L'écriture comptable ne respecte pas les contraintes de validation.";
            while (it.hasNext()) {
                customMessage = customMessage + " " + it.next().getMessage();
            }
            throw new FunctionalException(customMessage);
        }
    }

    /**
     * Cette méthode lance une exception si l'écriture comptable n'est pas équilibrée.
     * Vérification de la règle de gestion nr. 2.
     *
     * @param pEcritureComptable -
     * @throws FunctionalException
     */
    protected void checkEcritureComptableUnit_RG2(EcritureComptable pEcritureComptable) throws FunctionalException {
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }
    }

    /**
     * Cette méthode lance une exception si l'écriture comptable ne compte pas au minimum 2 lignes d'écriture, une au débit et une au crédit.
     * Vérification de la règle de gestion nr. 3.
     *
     * @param pEcritureComptable -
     * @throws FunctionalException
     */
    protected void checkEcritureComptableUnit_RG3(EcritureComptable pEcritureComptable) throws FunctionalException {
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        if (pEcritureComptable.getListLigneEcriture().size() < 2
                || vNbrCredit < 1
                || vNbrDebit < 1) {
            throw new FunctionalException(
                    "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }
    }

    /**
     * Cette méthode lance une exception si une ligne d'écriture comptable a un montant au débit et au crédit.
     *
     * @param pEcritureComptable -
     * @throws FunctionalException
     */
    protected void checkEcritureComptableUnit_Debit_or_Credit(EcritureComptable pEcritureComptable) throws FunctionalException {
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(), BigDecimal.ZERO)) != 0
                    && (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(), BigDecimal.ZERO)) != 0)) {
                throw new FunctionalException(
                        "Une écriture comptable ne doit pas avoir un montant au débit et au crédit.");
            }
        }
    }

    /**
     * Cette méthode lance une exception si le format de la référence de ecriture comptable n'est pas respecté (XX-AAAA/#####)
     * Vérification de la règle de gestion nr. 5.
     *
     * @param pEcritureComptable -
     * @throws FunctionalException
     */
    protected void checkEcritureComptableUnit_RG5(EcritureComptable pEcritureComptable) throws FunctionalException {

        // Vérifie que la référence n'est pas nulle
        if (pEcritureComptable.getReference() != null) {

            // Vérifie que le code du journal dans la référence correspond au journal de l'écriture
            String codeJournalInReference = pEcritureComptable.getReference().substring(0, 2);
            if (!codeJournalInReference.equals(pEcritureComptable.getJournal().getCode())) {
                throw new FunctionalException("Format de la référence incorrect : le code journal ne correspond pas au journal de l'écriture.");
            }

            // vérifie que l'année dans la référence correspond à la date de l'écriture
            String yearInReference = pEcritureComptable.getReference().substring(3, 7);
            if (!yearInReference.equals(Integer.toString(convertDateToCalendar(pEcritureComptable.getDate()).get(Calendar.YEAR)))) {
                throw new FunctionalException("Format de la référence incorrect : l'année indiquée dans la référence ne correspond pas à la date de l'écriture.");
            }

            // vérifie que le numéro de séquence est représenté sur 5 chiffres
            String sequenceInReference = pEcritureComptable.getReference().substring(8);
            if (sequenceInReference.length() != 5) {
                throw new FunctionalException("Format de la référence incorrect : le numéro de séquence doit être représenté avec 5 chiffres.");
            }

            // Vérifie les caractères fixes
            if (!pEcritureComptable.getReference().substring(2, 3).equals("-") || (!pEcritureComptable.getReference().substring(7, 8).equals("/"))) {
                throw new FunctionalException("Format de la référence incorrect : le code journal est suivi d'un - et l'année d'un /");
            }

        } else {
            throw new FunctionalException("La référence ne peut pas être null.");
        }

    }

    /**
     * Cette méthode lance une exception si une écriture comptable débite et crédite un même compte.
     *
     * @param pEcritureComptable -
     * @throws FunctionalException
     */
    protected void checkEcritureComptable_CompteComptable_Unique(EcritureComptable pEcritureComptable) throws FunctionalException {

        List<Integer> NumeroComptesDebit = new ArrayList<>();
        List<Integer> NumeroComptesCredit = new ArrayList<>();

        for (LigneEcritureComptable ligneEcriture : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(ligneEcriture.getCredit(), BigDecimal.ZERO)) != 0) {
                NumeroComptesCredit.add(ligneEcriture.getCompteComptable().getNumero());
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(ligneEcriture.getDebit(), BigDecimal.ZERO)) != 0) {
                NumeroComptesDebit.add(ligneEcriture.getCompteComptable().getNumero());
            }
        }
        NumeroComptesDebit.retainAll(NumeroComptesCredit);
        if (NumeroComptesDebit.size() != 0) {
            throw new FunctionalException("Une écriture comptable ne peut pas débiter et créditer un même compte.");
        }
    }


    /**
     * Cette méthode lance une exception si la référence d'une écriture n'est pas unique.
     * Vérification de la règle de gestion nr. 6.
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les règles de gestion
     */
    protected void checkEcritureComptable_RG6(EcritureComptable pEcritureComptable) throws FunctionalException {
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(pEcritureComptable.getReference());

                if (pEcritureComptable.getId() == null || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("La BDD contient une autre écriture comptable avec la même référence.");
                }
            } catch (NotFoundException vEx){
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {

        checkEcritureComptable(pEcritureComptable);
        TransactionManager transactionManager = getTransactionManager();

        TransactionStatus vTS = transactionManager.beginTransactionMyERP();

        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } catch (Exception e){
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException, NotFoundException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } catch (Exception e) {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } catch (Exception e) {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
}