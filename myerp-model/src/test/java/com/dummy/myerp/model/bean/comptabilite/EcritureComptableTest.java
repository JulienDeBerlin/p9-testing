package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class EcritureComptableTest {

    /*FIXME
        n'est-ce pas problématique de créer ainsi une nouvelle méthode au sein d'une classe de test? Quid de la séparation code et test?
        déplacer cette méthode dans la classe LigneEcritureComptable?
     */

//    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
//        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
//        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
//        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
//                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
//        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
//                                                                    vLibelle,
//                                                                    vDebit, vCredit);
//        return vRetour;
//    }


    LigneEcritureComptable debit_positif_400_50 = new LigneEcritureComptable(new CompteComptable(), "libellé", new BigDecimal("400.50"), null);
    LigneEcritureComptable debit_positif_49_50 = new LigneEcritureComptable(new CompteComptable(), "libellé", new BigDecimal("49.50"), null);
    LigneEcritureComptable debit_positif_450 = new LigneEcritureComptable(new CompteComptable(), "libellé", new BigDecimal("450.00"), null);
    LigneEcritureComptable debit_negatif_34_20 = new LigneEcritureComptable(new CompteComptable(), "libellé", new BigDecimal("-34.20"), null);


    LigneEcritureComptable credit_positif_450 = new LigneEcritureComptable(new CompteComptable(), "libellé",null,  new BigDecimal("450.00"));
    LigneEcritureComptable credit_positif_100 = new LigneEcritureComptable(new CompteComptable(), "libellé",null,  new BigDecimal("100.00"));
    LigneEcritureComptable credit_positif_350 = new LigneEcritureComptable(new CompteComptable(), "libellé",null,  new BigDecimal("350.00"));
    LigneEcritureComptable credit_negatif_34_20 = new LigneEcritureComptable(new CompteComptable(), "libellé", null, new BigDecimal("-34.20"));



    @Test
    public void getTotalDebit() {

        EcritureComptable ecritureComptable1  = new EcritureComptable();
        ecritureComptable1.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable1.getListLigneEcriture().add(credit_positif_450);
        assertEquals("1 débit / 1 crédit, positif", new BigDecimal("450.00"), ecritureComptable1.getTotalDebit());


        EcritureComptable ecritureComptable2  = new EcritureComptable();
        ecritureComptable2.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable2.getListLigneEcriture().add(credit_positif_100);
        ecritureComptable2.getListLigneEcriture().add(credit_positif_350);
        assertEquals("1 débit / 2 crédit, positif", new BigDecimal("450.00"), ecritureComptable2.getTotalDebit());


        EcritureComptable ecritureComptable3  = new EcritureComptable();
        ecritureComptable3.getListLigneEcriture().add(debit_positif_400_50);
        ecritureComptable3.getListLigneEcriture().add(debit_positif_49_50);
        ecritureComptable3.getListLigneEcriture().add(credit_positif_450);
        assertEquals("2 débit / 1 crédit, positif", new BigDecimal("450.00"), ecritureComptable3.getTotalDebit());


        EcritureComptable ecritureComptable4  = new EcritureComptable();
        ecritureComptable4.getListLigneEcriture().add(debit_negatif_34_20);
        ecritureComptable4.getListLigneEcriture().add(credit_negatif_34_20);
        assertEquals("1 débit / 1 crédit, négatif", new BigDecimal("-34.20"), ecritureComptable4.getTotalDebit());

        EcritureComptable ecritureComptable5  = new EcritureComptable();
        ecritureComptable5.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable5.getListLigneEcriture().add(credit_positif_350);
        assertEquals("1 débit / 1 crédit, positif, non equilibré", new BigDecimal("450.00"), ecritureComptable5.getTotalDebit());


    }

    @Test
    public void getTotalCredit() {

        EcritureComptable ecritureComptable1  = new EcritureComptable();
        ecritureComptable1.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable1.getListLigneEcriture().add(credit_positif_450);
        assertEquals("1 débit / 1 crédit, positif, equilibré", new BigDecimal("450.00"), ecritureComptable1.getTotalCredit());


        EcritureComptable ecritureComptable2  = new EcritureComptable();
        ecritureComptable2.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable2.getListLigneEcriture().add(credit_positif_100);
        ecritureComptable2.getListLigneEcriture().add(credit_positif_350);
        assertEquals("1 débit / 2 crédit, positif, équilibré", new BigDecimal("450.00"), ecritureComptable2.getTotalCredit());


        EcritureComptable ecritureComptable3  = new EcritureComptable();
        ecritureComptable3.getListLigneEcriture().add(debit_positif_400_50);
        ecritureComptable3.getListLigneEcriture().add(debit_positif_49_50);
        ecritureComptable3.getListLigneEcriture().add(credit_positif_450);
        assertEquals("2 débit / 1 crédit, positif, équilibré", new BigDecimal("450.00"), ecritureComptable3.getTotalCredit());


        EcritureComptable ecritureComptable4  = new EcritureComptable();
        ecritureComptable4.getListLigneEcriture().add(debit_negatif_34_20);
        ecritureComptable4.getListLigneEcriture().add(credit_negatif_34_20);
        assertEquals("1 débit / 1 crédit, négatif, équilibré", new BigDecimal("-34.20"), ecritureComptable4.getTotalCredit());

        EcritureComptable ecritureComptable5  = new EcritureComptable();
        ecritureComptable5.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable5.getListLigneEcriture().add(credit_positif_350);
        assertEquals("1 débit / 1 crédit, positif, non equilibré", new BigDecimal("350.00"), ecritureComptable5.getTotalCredit());


    }


    @Test
    public void isEquilibree() {
        EcritureComptable ecritureComptable = new EcritureComptable();

        ecritureComptable.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable.getListLigneEcriture().add(credit_positif_100);
        ecritureComptable.getListLigneEcriture().add(credit_positif_350);
        ecritureComptable.getListLigneEcriture().add(debit_negatif_34_20);
        ecritureComptable.getListLigneEcriture().add(credit_negatif_34_20);
        Assert.assertTrue("Equilibrée", ecritureComptable.isEquilibree());

        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable.getListLigneEcriture().add(credit_positif_100);
        ecritureComptable.getListLigneEcriture().add(credit_positif_350);
        ecritureComptable.getListLigneEcriture().add(debit_negatif_34_20);
        Assert.assertFalse("Non Equilibrée", ecritureComptable.isEquilibree());
    }


}
