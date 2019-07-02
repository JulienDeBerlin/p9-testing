package com.dummy.myerp.model.bean.comptabilite;


import com.dummy.myerp.model.validation.constraint.MontantComptable;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;


/**
 * This class is dedicated to the testing of constraint annotations set on the beans
 */
public class BeanValidationTests {

    private static Validator validator;


    @BeforeClass
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
        assertEquals( "toutes contraintes respectées", 0, constraintViolations.size() );

        journalComptable = new JournalComptable("200987", "journal de banque");
        constraintViolations = validator.validate(journalComptable);
        assertEquals( "code journal > 5 chiffres", 1, constraintViolations.size() );
        assertEquals("code journal > 5 chiffres", "la taille doit être entre 1 et 5",  constraintViolations.iterator().next().getMessage());

    }

    /**
     * Test de l'annotation personnalisée (@MontantComptable), utilisée dans des attributs de {@link LigneEcritureComptable}
     */
    @Test
    public void ContraintMontantComptableTest(){

        CompteComptable compteComptable = new CompteComptable(1232, "Subventions publiques");
        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable( compteComptable, "Subvention Conseil Général", new BigDecimal("150000"), null );
        Set<ConstraintViolation<LigneEcritureComptable>> constraintViolations = validator.validate(ligneEcritureComptable);
        assertEquals( "toutes contraintes respectées", 0, constraintViolations.size() );

        ligneEcritureComptable = new LigneEcritureComptable( compteComptable, "Subvention Conseil Général", new BigDecimal("150000.432"), null );
        constraintViolations = validator.validate(ligneEcritureComptable);
        assertEquals( "nb décimaux > 2", 1, constraintViolations.size() );
        assertEquals("nb décimaux > 2", "Le format du montant comptable est invalide: max 13 chiffres et 2 décimaux",  constraintViolations.iterator().next().getMessage());

        ligneEcritureComptable = new LigneEcritureComptable( compteComptable, "Subvention Conseil Général", new BigDecimal("22226222222223.23"), null );
        constraintViolations = validator.validate(ligneEcritureComptable);
        assertEquals( "nb entiers > 13", 1, constraintViolations.size() );
        assertEquals("nb entiers > 13", "Le format du montant comptable est invalide: max 13 chiffres et 2 décimaux",  constraintViolations.iterator().next().getMessage());


    }




}
