package com.dummy.myerp.business.impl.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Classe de test de l'initialisation du contexte Spring
 */
public class ITinitSpring extends TestBusinessAbstract {

    /**
     * Constructeur.
     */
    public ITinitSpring() {
        super();
    }


    /**
     * Teste l'initialisation du contexte Spring
     */
    @Test
    public void testInit() {
        SpringRegistry.init();
        assertNotNull(SpringRegistry.getBusinessProxy());
        assertNotNull(SpringRegistry.getTransactionManager());
    }
}
