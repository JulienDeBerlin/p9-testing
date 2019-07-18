package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static java.sql.Date.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/com/dummy/myerp/business/applicationContext.xml")

// La datasource est détectée automatiquement !
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/com/dummy/myerp/consumer/truncateDB.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/com/dummy/myerp/consumer/populateDB.sql")})


public class ComptabiliteManagerImplTestIntegration {

    private static BusinessProxy businessProxy;


    @BeforeAll
    private static void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("/com/dummy/myerp/business/applicationContext.xml");
        businessProxy = (BusinessProxyImpl) context.getBean("businessProxy");
    }


    @Test
    void sizeList() {
        assertEquals(5, businessProxy.getComptabiliteManager().getListEcritureComptable().size());
    }


    @Test
    void insertUpdateDelete() throws FunctionalException, NotFoundException {

        // Initial setting EcritureComptable
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("BQ", "Banque"));
        LocalDate localDate = LocalDate.of(2016, 12, 28);
        Date date = valueOf(localDate);
        ecritureComptable.setDate(date);
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401), null, new BigDecimal(123), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606), null, null, new BigDecimal(123)));

        businessProxy.getComptabiliteManager();

        businessProxy.getComptabiliteManager().addReference(ecritureComptable);
        assertEquals("BQ-2016/00052", ecritureComptable.getReference());

        // TEST INSERT
        List<EcritureComptable> ecritureComptableListBeforeInsertion = businessProxy.getComptabiliteManager().getListEcritureComptable();
        businessProxy.getComptabiliteManager().insertEcritureComptable(ecritureComptable);
        List<EcritureComptable> ecritureComptableListAfterInsertion = businessProxy.getComptabiliteManager().getListEcritureComptable();
        assertTrue(ecritureComptableListAfterInsertion.size() == ecritureComptableListBeforeInsertion.size() + 1);


        // TEST UPDATE

        assertEquals("Libelle", ecritureComptable.getLibelle());
        ecritureComptable.setLibelle("NouveauLibellé");
        businessProxy.getComptabiliteManager().updateEcritureComptable(ecritureComptable);

        List<EcritureComptable> ecritureComptableListAfterUpdate = businessProxy.getComptabiliteManager().getListEcritureComptable();

        String libelleAfterUpdate = "";
        Iterator<EcritureComptable> iterator = ecritureComptableListAfterUpdate.iterator();
        while (iterator.hasNext()) {
            EcritureComptable e = iterator.next();
            if (e.getReference().equals("BQ-2016/00052")) {
                libelleAfterUpdate += e.getLibelle();
            }
        }
        assertEquals("NouveauLibellé", libelleAfterUpdate);


        // TEST DELETE
        businessProxy.getComptabiliteManager().deleteEcritureComptable(ecritureComptable.getId());
        List<EcritureComptable> ecritureComptableListAfterDelete = businessProxy.getComptabiliteManager().getListEcritureComptable();
        assertTrue(ecritureComptableListAfterDelete.size() == ecritureComptableListBeforeInsertion.size());

        iterator = ecritureComptableListAfterDelete.iterator();

        boolean hasBeenDeleted = true;
        while (iterator.hasNext()) {
            EcritureComptable e = iterator.next();
            if (e.getReference().equals("BQ-2016/00052")) {
                hasBeenDeleted = false;
            }
        }
        assertTrue(hasBeenDeleted);
    }
}




