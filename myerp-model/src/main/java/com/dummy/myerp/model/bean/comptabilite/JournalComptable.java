package com.dummy.myerp.model.bean.comptabilite;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;


/**
 * Bean représentant un Journal Comptable
 */
public class JournalComptable {

    // ==================== Attributs ====================
    /** code */
    @NotNull (message = "Le code ne doit pas être null.")
    @Size(min = 2, max = 2, message = "Le code journal doit être représenté par 2 caractères.")
    private String code;

    /** libelle */
    @NotNull (message = "Le libellé ne doit pas être null.")
    @Size(message = "Le libellé doit être compris entre 1 et 150 caractères.", min = 1, max = 150)
    private String libelle;


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Journal comptable.
     */
    public JournalComptable() {
    }

    /**
     * Instantiates a new Journal comptable.
     *
     * @param pCode the p code
     * @param pLibelle the p libelle
     */
    public JournalComptable(String pCode, String pLibelle) {
        code = pCode;
        libelle = pLibelle;
    }


    // ==================== Getters/Setters ====================
    public String getCode() {
        return code;
    }
    public void setCode(String pCode) {
        code = pCode;
    }
    public String getLibelle() {
        return libelle;
    }
    public void setLibelle(String pLibelle) {
        libelle = pLibelle;
    }


    // ==================== Méthodes ====================
    @Override
    public String toString() {
        final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
        final String vSEP = ", ";
        vStB.append("{")
            .append("code='").append(code).append('\'')
            .append(vSEP).append("libelle='").append(libelle).append('\'')
            .append("}");
        return vStB.toString();
    }


    // ==================== Méthodes STATIC ====================
    /**
     * Renvoie le {@link JournalComptable} de code {@code pCode} s'il est présent dans la liste
     *
     * @param pList la liste où chercher le {@link JournalComptable}
     * @param pCode le code du {@link JournalComptable} à chercher
     * @return {@link JournalComptable} ou {@code null}
     */
    public static JournalComptable getByCode(List<? extends JournalComptable> pList, String pCode) {
        JournalComptable vRetour = null;
        for (JournalComptable vBean : pList) {
            if (vBean != null && Objects.equals(vBean.getCode(), pCode)) {
                vRetour = vBean;
                break;
            }
        }
        return vRetour;
    }
}
