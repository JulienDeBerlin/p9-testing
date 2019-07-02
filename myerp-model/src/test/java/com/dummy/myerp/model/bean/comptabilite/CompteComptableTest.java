package com.dummy.myerp.model.bean.comptabilite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CompteComptableTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getByNumeroTest() {

        Integer account1 = 1232;
        Integer account2 = 92837;
        Integer account3 = null;

        String name1 = "apports en capital";
        String name2 = null;
        String name3 = "produits exceptionnels";

        CompteComptable compteComptable1 = new CompteComptable(account1, name1);
        CompteComptable compteComptable2 = new CompteComptable(account2, name2);
        CompteComptable compteComptable3 = new CompteComptable(account3, name3);


        List<CompteComptable> list3elements = new ArrayList<CompteComptable>(
                Arrays.asList(compteComptable1, compteComptable2, compteComptable3));

        assertEquals("4 elements, standard", compteComptable1, CompteComptable.getByNumero(list3elements, account1));
        assertEquals("4 elements, name is null", compteComptable2, CompteComptable.getByNumero(list3elements, account2));
        assertEquals("4 elements, account is null", null, CompteComptable.getByNumero(list3elements, account3));
        assertEquals("4 elements, account not available", null, CompteComptable.getByNumero(list3elements, 5555));


    }
}