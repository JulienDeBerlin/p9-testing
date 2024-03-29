package com.dummy.myerp.model.bean.comptabilite;


/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
public class SequenceEcritureComptable {

    // ==================== Attributs ====================
    /**
     * L'année
     */
    private Integer annee;
    /**
     * Le code du journal
     */
    private String journalCode;
    /**
     * La dernière valeur utilisée
     */
    private Integer derniereValeur;
    // ==================== Constructeurs ====================

    /**
     * Constructeur
     */
    public SequenceEcritureComptable() {
    }

    /**
     * Constructeur
     *
     * @param pAnnee          -
     * @param pDerniereValeur -
     */
    public SequenceEcritureComptable(Integer pAnnee, Integer pDerniereValeur) {
        annee = pAnnee;
        derniereValeur = pDerniereValeur;
    }

    public SequenceEcritureComptable(Integer annee, String journalCode, Integer derniereValeur) {
        this.annee = annee;
        this.journalCode = journalCode;
        this.derniereValeur = derniereValeur;
    }

    // ==================== Getters/Setters ====================
    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer pAnnee) {
        annee = pAnnee;
    }

    public Integer getDerniereValeur() {
        return derniereValeur;
    }

    public void setDerniereValeur(Integer pDerniereValeur) {
        derniereValeur = pDerniereValeur;
    }

    public String getJournalCode() {
        return journalCode;
    }

    public void setJournalCode(String journalCode) {
        this.journalCode = journalCode;
    }

    // ==================== Méthodes ====================
    @Override
    public String toString() {
        final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
        final String vSEP = ", ";
        vStB.append("{")
                .append("annee=").append(annee)
                .append(vSEP).append("journal=").append(journalCode)
                .append(vSEP).append("derniereValeur=").append(derniereValeur)
                .append("}");
        return vStB.toString();
    }
}
