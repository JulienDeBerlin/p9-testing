package com.dummy.myerp.model.bean.comptabilite;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class CompteComptableTest {



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

        assertEquals(compteComptable1, CompteComptable.getByNumero(list3elements, account1), "3 elements, standard");
        assertEquals(compteComptable2, CompteComptable.getByNumero(list3elements, account2), "3 elements, name is null");

        //FIXME: ça ne devrait pas être possible de passer null en paramètre
        assertEquals(null, CompteComptable.getByNumero(list3elements, account3), "3 elements, account is null");
        assertEquals(null, CompteComptable.getByNumero(list3elements, 5555), "3 elements, account not available");


    }
}