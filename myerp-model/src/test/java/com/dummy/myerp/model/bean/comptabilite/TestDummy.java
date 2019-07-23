package com.dummy.myerp.model.bean.comptabilite;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDummy {


    @Test
    public void getByNumeroTest() {

        Integer numero1 = 1232;
        Integer numero2 = 92837;
        Integer numero3 = null;

        String libellé1 = "apports en capital";
        String libellé2 = null;
        String libellé3 = "produits exceptionnels";

        CompteComptable compteComptable1 = new CompteComptable(numero1, libellé1);
        CompteComptable compteComptable2 = new CompteComptable(numero2, libellé2);
        CompteComptable compteComptable3 = new CompteComptable(numero3, libellé3);

        List<CompteComptable> list3elements = new ArrayList<CompteComptable>(
                Arrays.asList(compteComptable1, compteComptable2, compteComptable3));


        assertEquals(compteComptable1, CompteComptable.getByNumero(list3elements, numero1), "3 elements, standard");
        assertEquals(compteComptable2, CompteComptable.getByNumero(list3elements, numero2), "3 elements, name is null");

        assertEquals(null, CompteComptable.getByNumero(list3elements, numero3), "3 elements, account is null");
        assertEquals(null, CompteComptable.getByNumero(list3elements, 5555), "3 elements, account not available");

    }
}