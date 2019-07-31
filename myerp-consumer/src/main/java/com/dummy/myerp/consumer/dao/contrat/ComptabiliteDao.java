package com.dummy.myerp.consumer.dao.contrat;

import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;

import java.util.List;


/**
 * Interface de DAO des objets du package Comptabilite
 */
public interface ComptabiliteDao {

    /**
     * Renvoie la liste des Comptes Comptables
     *
     * @return {@link List}
     */
    List<CompteComptable> getListCompteComptable();


    /**
     * Renvoie la liste des Journaux Comptables
     *
     * @return {@link List}
     */
    List<JournalComptable> getListJournalComptable();


    // ==================== EcritureComptable ====================

    /**
     * Renvoie la liste des Écritures Comptables
     *
     * @return {@link List}
     */
    List<EcritureComptable> getListEcritureComptable();

    /**
     * Renvoie la liste des lignes d'écriture comptable correspondant à l'id d'une écriture donnée
     *
     * @param ecritureId l'id de l'écriture dont on veut récupérer les lignes d'écriture
     * @return
     */
    List<LigneEcritureComptable> getListLignesEcritureComptable(int ecritureId);

    /**
     * Renvoie l'Écriture Comptable d'id {@code pId}.
     *
     * @param pId l'id de l'écriture comptable
     * @return {@link EcritureComptable}
     * @throws NotFoundException : Si l'écriture comptable n'est pas trouvée
     */
    EcritureComptable getEcritureComptable(Integer pId) throws NotFoundException;

    /**
     * Renvoie l'Écriture Comptable de référence {@code pRef}.
     *
     * @param pReference la référence de l'écriture comptable
     * @return {@link EcritureComptable}
     * @throws NotFoundException : Si l'écriture comptable n'est pas trouvée
     */
    EcritureComptable getEcritureComptableByRef(String pReference) throws NotFoundException;


    /**
     * Charge la liste des lignes d'écriture de l'écriture comptable {@code pEcritureComptable}
     *
     * @param pEcritureComptable -
     */
    void loadListLigneEcriture(EcritureComptable pEcritureComptable);

    /**
     * Insert une nouvelle écriture comptable.
     *
     * @param pEcritureComptable -
     */
    void insertEcritureComptable(EcritureComptable pEcritureComptable);

    /**
     * Met à jour l'écriture comptable.
     *
     * @param pEcritureComptable -
     */
    void updateEcritureComptable(EcritureComptable pEcritureComptable) throws NotFoundException;

    /**
     * Supprime l'écriture comptable d'id {@code pId}.
     *
     * @param pId l'id de l'écriture
     */
    void deleteEcritureComptable(Integer pId);

    /**
     * Récupère la dernière valeur de la séquence du journal dans lequelle l'écriture comptable doit être enregistrée.
     *
     * @param pEcritureComptable -
     * @return
     */
    SequenceEcritureComptable getSequenceJournal(EcritureComptable pEcritureComptable) throws NotFoundException;

    /**
     * Un code journal est considéré valide s'il est référencé en BDD (table journal_comptable)
     *
     * @param codeJournal le code journal dont on vérifie la validité
     * @return
     */
    boolean isCodeJournalValid(String codeJournal);

    /**
     * Insert une nouvelle séquence
     *
     * @param year        année de la séquence
     * @param codeJournal code journal de la séquence
     */
    void insertSequenceEcritureComptable(int year, String codeJournal);


    /**
     * Met à jour une séquence en incrémentant le champ derniere_valeur
     *
     * @param sequenceEcritureComptable la séquence à mettre à jour
     * @return la sequence mise à jour
     */
    SequenceEcritureComptable updateSequenceEcritureComptable(SequenceEcritureComptable sequenceEcritureComptable) throws NotFoundException;

    /**
     * @return La liste des séquences d'ecritures comptables en BDD
     */
    List<SequenceEcritureComptable> getListSequenceEcritureComptable();

    /**
     * Récupère un SequenceEcritureComptable
     *
     * @param year        PK de la SequenceEcritureComptable
     * @param codeJournal PK de la SequenceEcritureComptable
     * @return la SequenceEcritureComptable
     */
    SequenceEcritureComptable getSequenceEcritureComptable(int year, String codeJournal) throws NotFoundException;


}
