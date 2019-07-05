package com.dummy.myerp.model.bean.comptabilite;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class JournalComptableTest {

    @Test
    public void getByCode() {

        JournalComptable journalComptable1 = new JournalComptable("BQ", "journal de banque");
        JournalComptable journalComptable2 = new JournalComptable("CR", "journal des créditeur");
        JournalComptable journalComptable3 = new JournalComptable("DE", "journal des débiteurs");

        List<JournalComptable> journalComptableList = new ArrayList();
        journalComptableList.add(journalComptable1);
        journalComptableList.add(journalComptable2);
        journalComptableList.add(journalComptable3);

        assertEquals(journalComptable1, JournalComptable.getByCode(journalComptableList, "BQ"), "le code saisi correspond à un journal");
        assertEquals( null, JournalComptable.getByCode(journalComptableList, "ZZ"), "le code saisi ne correspond pas à un journal");
    }
}