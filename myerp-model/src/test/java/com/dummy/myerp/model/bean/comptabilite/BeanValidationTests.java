package com.dummy.myerp.model.bean.comptabilite;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * This class is dedicated to the testing of constraint annotations set on the beans
 */
public class BeanValidationTests {

    private static Validator validator;


    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    /**
     * Test d'une annotation prédéfinie (@Size sur l'attribut code de {@link JournalComptable})
     */
    @Test
    public void JournalComptableConstraintsTest(){

        JournalComptable journalComptable = new JournalComptable("2", "journal de banque");
        Set<ConstraintViolation<JournalComptable>> constraintViolations = validator.validate(journalComptable);
        assertEquals(  0, constraintViolations.size() , "toutes contraintes respectées");

        journalComptable = new JournalComptable("200987", "journal de banque");
        constraintViolations = validator.validate(journalComptable);
        assertEquals(  1, constraintViolations.size() , "code journal > 5 chiffres");
        assertEquals( "la taille doit être entre 1 et 5",  constraintViolations.iterator().next().getMessage(), "code journal > 5 chiffres");

    }

    /**
     * Test de l'annotation personnalisée (@MontantComptable), utilisée dans des attributs de {@link LigneEcritureComptable}
     */
    @Test
    public void ContraintMontantComptableTest(){

        CompteComptable compteComptable = new CompteComptable(1232, "Subventions publiques");
        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable( compteComptable, "Subvention Conseil Général", new BigDecimal("150000"), null );
        Set<ConstraintViolation<LigneEcritureComptable>> constraintViolations = validator.validate(ligneEcritureComptable);
        assertEquals( 0, constraintViolations.size() , "toutes contraintes respectées");

        ligneEcritureComptable = new LigneEcritureComptable( compteComptable, "Subvention Conseil Général", new BigDecimal("150000.432"), null );
        constraintViolations = validator.validate(ligneEcritureComptable);
        assertEquals(  1, constraintViolations.size(), "nb décimaux > 2" );
        assertEquals( "Le format du montant comptable est invalide: max 13 chiffres et 2 décimaux.",  constraintViolations.iterator().next().getMessage(), "nb décimaux > 2");

        ligneEcritureComptable = new LigneEcritureComptable( compteComptable, "Subvention Conseil Général", new BigDecimal("22226222222223.23"), null );
        constraintViolations = validator.validate(ligneEcritureComptable);
        assertEquals( 1, constraintViolations.size(), "nb entiers > 13" );
        assertEquals( "Le format du montant comptable est invalide: max 13 chiffres et 2 décimaux.",  constraintViolations.iterator().next().getMessage(), "nb entiers > 13");

        ligneEcritureComptable = new LigneEcritureComptable( compteComptable, "Subvention Conseil Général", new BigDecimal("250"), null );
        constraintViolations = validator.validate(ligneEcritureComptable);
        assertEquals( 1, constraintViolations.size(), "nombre saisi sans décimaux" );


    }




}
