package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-test.xml")
public class ComptabiliteDaoImplTest {

    private static ComptabiliteDaoImpl comptabiliteDaoImpl;

    private LigneEcritureComptable debit_positif_400_50 = new LigneEcritureComptable(new CompteComptable(100), "libellé", new BigDecimal("400.50"), null);
    private LigneEcritureComptable debit_positif_49_50 = new LigneEcritureComptable(new CompteComptable(200), "libellé", new BigDecimal("49.50"), null);
    private LigneEcritureComptable debit_positif_450 = new LigneEcritureComptable(new CompteComptable(200), "libellé", new BigDecimal("450.00"), null);
    private LigneEcritureComptable debit_negatif_34_20 = new LigneEcritureComptable(new CompteComptable(300), "libellé", new BigDecimal("-34.20"), null);

    private LigneEcritureComptable credit_positif_450 = new LigneEcritureComptable(new CompteComptable(44), "libellé", null, new BigDecimal("450.00"));
    private LigneEcritureComptable credit_positif_100 = new LigneEcritureComptable(new CompteComptable(876), "libellé", null, new BigDecimal("100.00"));
    private LigneEcritureComptable credit_positif_350 = new LigneEcritureComptable(new CompteComptable(4444), "libellé", null, new BigDecimal("350.00"));
    private LigneEcritureComptable credit_negatif_34_20 = new LigneEcritureComptable(new CompteComptable(300), "libellé", null, new BigDecimal("-34.20"));


    @BeforeClass
    public static void setup() {
        comptabiliteDaoImpl = ComptabiliteDaoImpl.getInstance();

    }


    @Test
    public void testConnectionInMemoryDB() {

        List<CompteComptable> list = comptabiliteDaoImpl.getListCompteComptable();
        assertEquals("Test de connexion à la BDD test",7, list.size());

        /* FIXME : pourquoi lors du test ci-dessous j'obtiens un jdbc.CannotGetJdbcConnectionException
            alors que le test ci-dessus (getListCompteComptable) fonctionne très bien???
        */
        List<EcritureComptable> ecritureComptableList = comptabiliteDaoImpl.getListEcritureComptable();
        assertEquals("size before adding ecriture comptable" ,0, ecritureComptableList.size());


    }



    @Test
    public void insertAndGetEcritureComptable() {

        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setId(122);
        ecritureComptable.setJournal(new JournalComptable("BQ", "Journal de Banque"));
        ecritureComptable.setReference("BQ-00293");
        ecritureComptable.setDate(Date.from(LocalDate.of(2014, 2, 11).atStartOfDay(ZoneId.of("Africa/Tunis")).toInstant()));
        ecritureComptable.setLibelle("Subvention conseil général");
        ecritureComptable.getListLigneEcriture().add(credit_positif_100);
        ecritureComptable.getListLigneEcriture().add(credit_positif_350);
        ecritureComptable.getListLigneEcriture().add(debit_positif_450);


        List<EcritureComptable> ecritureComptableList = comptabiliteDaoImpl.getListEcritureComptable();
        assertEquals("size before adding ecriture comptable" ,0, ecritureComptableList.size());

//        comptabiliteDaoImpl.insertEcritureComptable(ecritureComptable);
//        ecritureComptableList = comptabiliteDaoImpl.getListEcritureComptable();
//        assertEquals("size before adding ecriture comptable" ,1, ecritureComptableList.size());

    }


    @Test
    public void insertListLigneEcritureComptable() {


        //FIXME : à quoi sert le pattern design singleton???


    }


    @Test
    public void getInstance() {
    }

    @Test
    public void getListCompteComptable() {
    }

    @Test
    public void getListJournalComptable() {
    }

    @Test
    public void getListEcritureComptable() {
    }

    @Test
    public void getEcritureComptable() {
    }

    @Test
    public void getEcritureComptableByRef() {
    }

    @Test
    public void loadListLigneEcriture() {
    }


    @Test
    public void updateEcritureComptable() {
    }

    @Test
    public void deleteEcritureComptable() {
    }

    @Test
    public void deleteListLigneEcritureComptable() {
    }
}