package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static java.sql.Date.valueOf;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/com/dummy/myerp/consumer/applicationContext.xml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional

// La datasource est détectée automatiquement !
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/com/dummy/myerp/consumer/truncateDB.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/com/dummy/myerp/consumer/populateDB.sql")})

public class ComptabiliteDaoImplTest extends AbstractDbConsumer {

//    @Autowired
//    private  DriverManagerDataSource dataSourceMYERP;

    private static ComptabiliteDaoImpl comptabiliteDaoImpl;

    private static LigneEcritureComptable debit_positif_400_50 = new LigneEcritureComptable(new CompteComptable(512), "libellé", new BigDecimal("400.50"), null);
    private static LigneEcritureComptable debit_positif_49_50 = new LigneEcritureComptable(new CompteComptable(512), "libellé", new BigDecimal("49.50"), null);
    private static LigneEcritureComptable debit_positif_450 = new LigneEcritureComptable(new CompteComptable(512), "libellé", new BigDecimal("450.00"), null);
    private static LigneEcritureComptable debit_negatif_34_20 = new LigneEcritureComptable(new CompteComptable(512), "libellé", new BigDecimal("-34.23"), null);

    private static LigneEcritureComptable credit_positif_450 = new LigneEcritureComptable(new CompteComptable(706), "libellé", null, new BigDecimal("450.00"));
    private static LigneEcritureComptable credit_positif_100 = new LigneEcritureComptable(new CompteComptable(401), "libellé", null, new BigDecimal("100.00"));
    private static LigneEcritureComptable credit_positif_350 = new LigneEcritureComptable(new CompteComptable(401), "libellé", null, new BigDecimal("350.00"));
    private static LigneEcritureComptable credit_negatif_34_20 = new LigneEcritureComptable(new CompteComptable(706), "libellé", null, new BigDecimal("-34.23"));


    @BeforeAll
    public static void setupBeforeAll() {
        comptabiliteDaoImpl = ComptabiliteDaoImpl.getInstance();
        ComptabiliteDaoImplTest instance = new ComptabiliteDaoImplTest();
    }

////    @BeforeEach
//    @Rollback(value = false)
//    public static void rebuildDatabase(DriverManagerDataSource dataSource) throws SQLException {
//
//        try (Connection connection = dataSource.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate("TRUNCATE db_myerp.myerp.journal_comptable CASCADE ");
//            statement.executeUpdate("TRUNCATE db_myerp.myerp.compte_comptable CASCADE ");
//
//            ScriptRunner runner = new ScriptRunner(connection, false, false);
//            //FIXME: utiliser un chemin relatif, mais comment faire?
//            String file = "/Users/admin/Documents/PROGRAMMING/OPENCLASSROOMS/P9/myErp/src/myerp-consumer/src/main/resources/com/dummy/myerp/consumer/populateDB.sql";
//            try {
//                runner.runScript(new BufferedReader(new FileReader(file)));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    @Test
    @Order(1)
    public void getListCompteComptable() {
        List<CompteComptable> list = comptabiliteDaoImpl.getListCompteComptable();
        assertEquals(7, list.size());
    }

    @Test
    @Order(2)
    public void getListEcritureComptable() {
        List<EcritureComptable> list = comptabiliteDaoImpl.getListEcritureComptable();
        assertEquals(5, list.size());
    }

    @Test
    @Order(3)
    public void getListJournalComptable() {
        List<JournalComptable> journalComptableList = comptabiliteDaoImpl.getListJournalComptable();
        assertEquals(4, journalComptableList.size());
    }

    @Test
    @Order(4)
    @DisplayName("getEcritureComptable / Id valid")
    public void getEcritureComptable1() throws NotFoundException {

        EcritureComptable ecritureComptable = comptabiliteDaoImpl.getEcritureComptable(-4);

        assertEquals(-4, ecritureComptable.getId(), "id matches");
        assertEquals("VE", ecritureComptable.getJournal().getCode(), "journal matches");
        assertEquals("VE-2016/00004", ecritureComptable.getReference(), "ref matches");

        LocalDate localDate = LocalDate.of(2016, 12, 28);
        Date date = valueOf(localDate);
        assertEquals(0, ecritureComptable.getDate().compareTo(date), "date matches");
        assertEquals("TMA Appli Yyy", ecritureComptable.getLibelle(), "libellé matches");
        assertEquals(3, ecritureComptable.getListLigneEcriture().size(), "size lignes ecriture matches");
        // TODO: éventuellement vérifier que les lignes d'écriture sont celles attendues
    }

    @Test
    @Order(5)
    @DisplayName("getEcritureComptable / Id non valid")
    public void getEcritureComptable2() throws NotFoundException {

        //TODO: étudier les expressions lambdas
        int id = -49;
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> comptabiliteDaoImpl.getEcritureComptable(id), "id non existant");
        assertEquals( "EcritureComptable non trouvée : id=" + id, thrown.getMessage());

    }


    @Test
    @Order(6)
    @DisplayName("getEcritureComptableByRef / ref valid")
    public void getEcritureComptableByRef1() throws NotFoundException {
        EcritureComptable ecritureComptable = comptabiliteDaoImpl.getEcritureComptableByRef("VE-2016/00004");

        assertEquals(-4, ecritureComptable.getId(), "id matches");
        assertEquals("VE", ecritureComptable.getJournal().getCode(), "journal matches");
        assertEquals("VE-2016/00004", ecritureComptable.getReference(), "ref matches");

        LocalDate localDate = LocalDate.of(2016, 12, 28);
        Date date = valueOf(localDate);
        assertEquals(0, ecritureComptable.getDate().compareTo(date), "date matches");
        assertEquals("TMA Appli Yyy", ecritureComptable.getLibelle(), "libellé matches");
    }

    @Test
    @Order(7)
    @DisplayName("getEcritureComptableByRef / ref non valid")
    public void getEcritureComptableByRef2() throws NotFoundException {

        //TODO: étudier les expressions lambdas
        String ref = "AA";
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> comptabiliteDaoImpl.getEcritureComptableByRef(ref), "référence non existante");
        assertEquals( "EcritureComptable non trouvée : reference=" + ref, thrown.getMessage());

    }

    @Test
    @Order(8)
    public void insertEcritureComptable() throws NotFoundException {

        EcritureComptable ecritureComptableToBeInserted = new EcritureComptable();

        ecritureComptableToBeInserted.setJournal(new JournalComptable("BQ", "Journal de Banque"));

        // FIXME: est-ce que la deuxième partie de la ref correspond à l'id ? incrémentation automatique?
        ecritureComptableToBeInserted.setReference("BQ-2016/23663");


        LocalDate localDate = LocalDate.of(2019, 01, 28);
        Date date = valueOf(localDate);
        ecritureComptableToBeInserted.setDate(date);

        ecritureComptableToBeInserted.setLibelle("Subvention Fondation LVMH");

        ecritureComptableToBeInserted.getListLigneEcriture().add(credit_positif_100);
        ecritureComptableToBeInserted.getListLigneEcriture().add(credit_positif_350);
        ecritureComptableToBeInserted.getListLigneEcriture().add(debit_positif_450);

        List<EcritureComptable> ecritureComptableListBeforeInsertion = comptabiliteDaoImpl.getListEcritureComptable();
        comptabiliteDaoImpl.insertEcritureComptable(ecritureComptableToBeInserted);

        // Teste qu'une écriture comptable a bien été ajouté en BDD
        assertEquals(ecritureComptableListBeforeInsertion.size() + 1, comptabiliteDaoImpl.getListEcritureComptable().size());

        // Teste que l'objet enregistré en BDD est complet et correct
        EcritureComptable ecritureComptableInserted = comptabiliteDaoImpl.getEcritureComptableByRef("BQ-2016/23663");
        assertEquals(date, ecritureComptableInserted.getDate());
        assertEquals("Subvention Fondation LVMH", ecritureComptableInserted.getLibelle());
        assertEquals(3, ecritureComptableInserted.getListLigneEcriture().size());

        // FIXME : tester que l'ID généré est bien le bon
        // FIXME: dans la couche DAO, pas de vérification que l'écriture soit bien équilibrée! A quel moment ce fait ce contrôle?
        // FIXME: que ce passe-t-il si on laisse des champs vides (qui ne sont pas censés l'être)
    }


    @Test
    @Order(9)
    @DisplayName("updateEcritureComptable / Id existant / update libellé")
    public void updateEcritureComptable1() throws NotFoundException {

        //Récuépère une écriture comptable de la BDD
        EcritureComptable ecritureComptableToBeUpdated = comptabiliteDaoImpl.getEcritureComptable(-1);

        // Test le libellé avant update
        assertEquals("Cartouches d’imprimante", ecritureComptableToBeUpdated.getLibelle());

        // Update du libellé
        ecritureComptableToBeUpdated.setLibelle("Subvention conseil régional");
        comptabiliteDaoImpl.updateEcritureComptable(ecritureComptableToBeUpdated);

        // Test le libellé après update
        assertEquals("Subvention conseil régional", ecritureComptableToBeUpdated.getLibelle());
    }


    @Test
    @Order(10)
    @DisplayName("updateEcritureComptable / Id existant / update lignes d'écriture")
    public void updateEcritureComptable2() throws NotFoundException {

        //Récuépère une écriture comptable de la BDD
        EcritureComptable ecritureComptableToBeUpdated = comptabiliteDaoImpl.getEcritureComptable(-1);

        // Test lignes d'écriture avant update
        assertEquals(3, ecritureComptableToBeUpdated.getListLigneEcriture().size());

        //Modification lignes d'écriture
        ecritureComptableToBeUpdated.getListLigneEcriture().clear();
        ecritureComptableToBeUpdated.getListLigneEcriture().add(debit_negatif_34_20);
        ecritureComptableToBeUpdated.getListLigneEcriture().add(credit_negatif_34_20);

        // Update
        comptabiliteDaoImpl.updateEcritureComptable(ecritureComptableToBeUpdated);

        // Test lignes d'écriture après update
        assertEquals(2, ecritureComptableToBeUpdated.getListLigneEcriture().size());
        BigDecimal bigDecimalTest = BigDecimal.valueOf(0);
        for (LigneEcritureComptable l : ecritureComptableToBeUpdated.getListLigneEcriture()) {
            bigDecimalTest = (l.getCredit() == null) ? bigDecimalTest.add(l.getDebit()) : bigDecimalTest.add(l.getCredit());
        }
        assertEquals(bigDecimalTest, BigDecimal.valueOf(-68.46));

    }

    @Test
    @Order(11)
    @DisplayName("updateEcritureComptable / Id non existant")
    public void updateEcritureComptable3() throws NotFoundException {

        //Récuépère une écriture comptable de la BDD
        EcritureComptable ecritureComptable = comptabiliteDaoImpl.getEcritureComptable(-1);

        //Change l'id
        ecritureComptable.setId(1000);

        //Test si on essaie de faire un update sur un id non existant
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> comptabiliteDaoImpl.updateEcritureComptable(ecritureComptable), "id non existant en BDD");
        assertEquals( "La BDD ne contient pas d'écriture comptable avec l'id="  + ecritureComptable.getId(), thrown.getMessage());

    }

    @Test
    @Order(12)
    public void deleteEcritureComptable() {
        fail();
    }

    @Test
    @Order(13)
    public void deleteListLigneEcritureComptable() {
        fail();
    }


}