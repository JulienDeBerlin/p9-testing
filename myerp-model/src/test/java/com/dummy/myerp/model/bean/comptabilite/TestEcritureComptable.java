package com.dummy.myerp.model.bean.comptabilite;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import static java.sql.Date.valueOf;
import static org.junit.jupiter.api.Assertions.*;




public class TestEcritureComptable {


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


    LigneEcritureComptable credit_positif_450 = new LigneEcritureComptable(new CompteComptable(), "libellé",null,  new BigDecimal("450"));
    LigneEcritureComptable credit_positif_100 = new LigneEcritureComptable(new CompteComptable(), "libellé",null,  new BigDecimal("100.00"));
    LigneEcritureComptable credit_positif_350 = new LigneEcritureComptable(new CompteComptable(), "libellé",null,  new BigDecimal("350.00"));
    LigneEcritureComptable credit_negatif_34_20 = new LigneEcritureComptable(new CompteComptable(), "libellé", null, new BigDecimal("-34.20"));



    @Test
    public void testGetTotalDebit() {

        EcritureComptable ecritureComptable1  = new EcritureComptable();
        ecritureComptable1.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable1.getListLigneEcriture().add(credit_positif_450);
        assertEquals(new BigDecimal("450.00"), ecritureComptable1.getTotalDebit(), "1 débit / 1 crédit, positif");


        EcritureComptable ecritureComptable2  = new EcritureComptable();
        ecritureComptable2.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable2.getListLigneEcriture().add(credit_positif_100);
        ecritureComptable2.getListLigneEcriture().add(credit_positif_350);
        assertEquals(new BigDecimal("450.00"), ecritureComptable2.getTotalDebit(), "1 débit / 2 crédit, positif");


        EcritureComptable ecritureComptable3  = new EcritureComptable();
        ecritureComptable3.getListLigneEcriture().add(debit_positif_400_50);
        ecritureComptable3.getListLigneEcriture().add(debit_positif_49_50);
        ecritureComptable3.getListLigneEcriture().add(credit_positif_450);
        assertEquals(new BigDecimal("450.00"), ecritureComptable3.getTotalDebit(), "2 débit / 1 crédit, positif");


        EcritureComptable ecritureComptable4  = new EcritureComptable();
        ecritureComptable4.getListLigneEcriture().add(debit_negatif_34_20);
        ecritureComptable4.getListLigneEcriture().add(credit_negatif_34_20);
        assertEquals(new BigDecimal("-34.20"), ecritureComptable4.getTotalDebit(), "1 débit / 1 crédit, négatif");

        EcritureComptable ecritureComptable5  = new EcritureComptable();
        ecritureComptable5.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable5.getListLigneEcriture().add(credit_positif_350);
        assertEquals(new BigDecimal("450.00"), ecritureComptable5.getTotalDebit(), "1 débit / 1 crédit, positif, non equilibré");


    }

    @Test
    public void testGetTotalCredit() {

        EcritureComptable ecritureComptable1  = new EcritureComptable();
        ecritureComptable1.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable1.getListLigneEcriture().add(credit_positif_450);
        assertEquals(new BigDecimal("450.00"), ecritureComptable1.getTotalCredit(), "1 débit / 1 crédit, positif, equilibré");


        EcritureComptable ecritureComptable2  = new EcritureComptable();
        ecritureComptable2.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable2.getListLigneEcriture().add(credit_positif_100);
        ecritureComptable2.getListLigneEcriture().add(credit_positif_350);
        assertEquals(new BigDecimal("450.00"), ecritureComptable2.getTotalCredit(), "1 débit / 2 crédit, positif, équilibré");


        EcritureComptable ecritureComptable3  = new EcritureComptable();
        ecritureComptable3.getListLigneEcriture().add(debit_positif_400_50);
        ecritureComptable3.getListLigneEcriture().add(debit_positif_49_50);
        ecritureComptable3.getListLigneEcriture().add(credit_positif_450);
        assertEquals(new BigDecimal("450.00"), ecritureComptable3.getTotalCredit(), "2 débit / 1 crédit, positif, équilibré");


        EcritureComptable ecritureComptable4  = new EcritureComptable();
        ecritureComptable4.getListLigneEcriture().add(debit_negatif_34_20);
        ecritureComptable4.getListLigneEcriture().add(credit_negatif_34_20);
        assertEquals(new BigDecimal("-34.20"), ecritureComptable4.getTotalCredit(), "1 débit / 1 crédit, négatif, équilibré");

        EcritureComptable ecritureComptable5  = new EcritureComptable();
        ecritureComptable5.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable5.getListLigneEcriture().add(credit_positif_350);
        assertEquals(new BigDecimal("350.00"), ecritureComptable5.getTotalCredit(), "1 débit / 1 crédit, positif, non equilibré");


    }


    @Test
    public void testIsEquilibree() {
        EcritureComptable ecritureComptable = new EcritureComptable();

        ecritureComptable.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable.getListLigneEcriture().add(credit_positif_100);
        ecritureComptable.getListLigneEcriture().add(credit_positif_350);
        ecritureComptable.getListLigneEcriture().add(debit_negatif_34_20);
        ecritureComptable.getListLigneEcriture().add(credit_negatif_34_20);
        assertTrue(ecritureComptable.isEquilibree(), "Equilibrée");

        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(debit_positif_450);
        ecritureComptable.getListLigneEcriture().add(credit_positif_100);
        ecritureComptable.getListLigneEcriture().add(credit_positif_350);
        ecritureComptable.getListLigneEcriture().add(debit_negatif_34_20);
        assertFalse(ecritureComptable.isEquilibree(), "Non Equilibrée");

        // avec et sans décimaux
        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(credit_positif_450);
        ecritureComptable.getListLigneEcriture().add(debit_positif_400_50);
        ecritureComptable.getListLigneEcriture().add(debit_positif_49_50);
        assertTrue(ecritureComptable.isEquilibree(), "Equilibrée");
    }


    @Test
    public void testToString(){


        LocalDate localDate = LocalDate.of(2020, 12, 28);
        Date date = valueOf(localDate);

        JournalComptable journalComptable = new JournalComptable();
        journalComptable.setCode("AX");
        journalComptable.setLibelle("Journal de banque");

        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setReference("AX-2020/00076");
        ecritureComptable.setDate(date);
        ecritureComptable.setId(23);
        ecritureComptable.setJournal(journalComptable);
        ecritureComptable.setLibelle("Libellé");

        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));



        assertEquals("EcritureComptable{id=23, journal=JournalComptable{code='AX', libelle='Journal de banque'}, reference='AX-2020/00076', date=2020-12-28, libelle='Libellé', totalDebit=123.00, totalCredit=123.00, listLigneEcriture=[\n" +
                "LigneEcritureComptable{compteComptable=CompteComptable{numero=401, libelle='null'}, libelle='null', debit=123, credit=null}\n" +
                "LigneEcritureComptable{compteComptable=CompteComptable{numero=411, libelle='null'}, libelle='null', debit=null, credit=123}\n" +
                "]}", ecritureComptable.toString());

    }

}
