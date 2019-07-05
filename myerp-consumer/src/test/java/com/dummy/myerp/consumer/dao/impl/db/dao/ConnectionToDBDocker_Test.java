package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.db.AbstractDbConsumer;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/com/dummy/myerp/consumer/applicationContext.xml")

public class ConnectionToDBDocker_Test extends AbstractDbConsumer {


    @Test
    public void getListCompteComptable() {

        ComptabiliteDaoImpl comptabiliteDaoImpl = new ComptabiliteDaoImpl();
        List<CompteComptable> list = comptabiliteDaoImpl.getListCompteComptable();
        assertEquals("Test de connexion à la BDD test",7, list.size());

        /*
        FIXME
            pour vérifier la connexion, on vérifie si le nombre de CompteComptable en BDD.
            Si les données sont modifiées en BDD, le test ne pasera plus.
            Est-ce qu'il y a une meilleure manière de faire?
            Je ne veux pas utiliser un mock ici, puisque le but est justement de tester la connexion à la BDD.
        */

    }
}