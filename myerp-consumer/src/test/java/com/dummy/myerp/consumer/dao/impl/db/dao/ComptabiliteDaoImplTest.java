package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
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
@Transactional

// La datasource est détectée automatiquement !
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/com/dummy/myerp/consumer/truncateDB.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/com/dummy/myerp/consumer/createDB.sql") })

public class ComptabiliteDaoImplTest extends AbstractDbConsumer {

    @Autowired
    private  DriverManagerDataSource dataSourceMYERP;

    private static ComptabiliteDaoImpl comptabiliteDaoImpl;

    private LigneEcritureComptable debit_positif_400_50 = new LigneEcritureComptable(new CompteComptable(100), "libellé", new BigDecimal("400.50"), null);
    private LigneEcritureComptable debit_positif_49_50 = new LigneEcritureComptable(new CompteComptable(200), "libellé", new BigDecimal("49.50"), null);
    private LigneEcritureComptable debit_positif_450 = new LigneEcritureComptable(new CompteComptable(200), "libellé", new BigDecimal("450.00"), null);
    private LigneEcritureComptable debit_negatif_34_20 = new LigneEcritureComptable(new CompteComptable(300), "libellé", new BigDecimal("-34.20"), null);

    private LigneEcritureComptable credit_positif_450 = new LigneEcritureComptable(new CompteComptable(44), "libellé", null, new BigDecimal("450.00"));
    private LigneEcritureComptable credit_positif_100 = new LigneEcritureComptable(new CompteComptable(876), "libellé", null, new BigDecimal("100.00"));
    private LigneEcritureComptable credit_positif_350 = new LigneEcritureComptable(new CompteComptable(4444), "libellé", null, new BigDecimal("350.00"));
    private LigneEcritureComptable credit_negatif_34_20 = new LigneEcritureComptable(new CompteComptable(300), "libellé", null, new BigDecimal("-34.20"));


    @BeforeAll
    public static void setup() {
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
//            String file = "/Users/admin/Documents/PROGRAMMING/OPENCLASSROOMS/P9/myErp/src/myerp-consumer/src/main/resources/com/dummy/myerp/consumer/createDB.sql";
//            try {
//                runner.runScript(new BufferedReader(new FileReader(file)));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    @Test
    public void getListCompteComptable() {
        List<CompteComptable> list = comptabiliteDaoImpl.getListCompteComptable();
        assertEquals(7, list.size());
    }

    @Test
    public void getListEcritureComptable() {
        List<EcritureComptable> list = comptabiliteDaoImpl.getListEcritureComptable();
        assertEquals(5, list.size());
    }

    @Test
    public void getListJournalComptable() {
        List<JournalComptable> journalComptableList = comptabiliteDaoImpl.getListJournalComptable();
        assertEquals(4, journalComptableList.size());
    }

    @Test
    @DisplayName("getEcritureComptable / Id valid")
    public void getEcritureComptable1() throws NotFoundException {

        EcritureComptable ecritureComptable = comptabiliteDaoImpl.getEcritureComptable(-4);

        assertEquals(-4, ecritureComptable.getId(), "id matches");
        assertEquals("VE", ecritureComptable.getJournal().getCode(), "journal matches");
        assertEquals("VE-2016/00004", ecritureComptable.getReference(), "ref matches");

        LocalDate localDate = LocalDate.of(2016, 12, 28);
        Date date = valueOf(localDate);
        assertTrue(ecritureComptable.getDate().compareTo(date) == 0, "date matches");
        assertEquals("TMA Appli Yyy", ecritureComptable.getLibelle(), "libellé matches");
    }

    @Test
    @DisplayName("getEcritureComptable / Id non valid")
    public void getEcritureComptable2() throws NotFoundException {

        //TODO: étudier les expressions lambdas
        int id = -49;
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> comptabiliteDaoImpl.getEcritureComptable(id), "id non existant");
        assertEquals(thrown.getMessage(), "EcritureComptable non trouvée : id=" + id);
    }


    @Test
    @DisplayName("getEcritureComptableByRef / ref valid")
    public void getEcritureComptableByRef1() throws NotFoundException {
        EcritureComptable ecritureComptable = comptabiliteDaoImpl.getEcritureComptableByRef("VE-2016/00004");

        assertEquals(-4, ecritureComptable.getId(), "id matches");
        assertEquals("VE", ecritureComptable.getJournal().getCode(), "journal matches");
        assertEquals("VE-2016/00004", ecritureComptable.getReference(), "ref matches");

        LocalDate localDate = LocalDate.of(2016, 12, 28);
        Date date = valueOf(localDate);
        assertTrue(ecritureComptable.getDate().compareTo(date) == 0, "date matches");
        assertEquals("TMA Appli Yyy", ecritureComptable.getLibelle(), "libellé matches");
    }

    @Test
    @DisplayName("getEcritureComptableByRef / ref non valid")
    public void getEcritureComptableByRef2() throws NotFoundException {

        //TODO: étudier les expressions lambdas
        String ref = "AA";
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> comptabiliteDaoImpl.getEcritureComptableByRef(ref), "référence non existante");
        assertEquals(thrown.getMessage(), "EcritureComptable non trouvée : reference=" + ref);
    }

    @Test
    public void insertEcritureComptable() {

        EcritureComptable ecritureComptable = new EcritureComptable();

        ecritureComptable.setId(122);
        ecritureComptable.setJournal(new JournalComptable("BQ", "Journal de Banque"));
        ecritureComptable.setReference("BQ-2016/23663");

        LocalDate localDate = LocalDate.of(2019, 01, 28);
        Date date = valueOf(localDate);
        ecritureComptable.setDate(date);

        ecritureComptable.setLibelle("Subvention Fondation LVMH");
        //FIXME: insertion d'une écriture sans ligne d'écriture, à quel moment se fait le lien entre écriture et lignes d'écriture???
//        ecritureComptable.getListLigneEcriture().add(credit_positif_100);
//        ecritureComptable.getListLigneEcriture().add(credit_positif_350);
//        ecritureComptable.getListLigneEcriture().add(debit_positif_450);

        List<EcritureComptable> ecritureComptableListBeforeInsertion = comptabiliteDaoImpl.getListEcritureComptable();
        comptabiliteDaoImpl.insertEcritureComptable(ecritureComptable);
        assertEquals(ecritureComptableListBeforeInsertion.size() + 1, comptabiliteDaoImpl.getListEcritureComptable().size());
    }


    @Test
    public void insertListLigneEcritureComptable() {
        fail();

        //FIXME : à quoi sert le pattern design singleton???

    }


    @Test
    public void loadListLigneEcriture() {
        fail();
    }


    @Test
    public void updateEcritureComptable() {
        fail();
    }

    @Test
    public void deleteEcritureComptable() {
        fail();
    }

    @Test
    public void deleteListLigneEcritureComptable() {
        fail();
    }


}