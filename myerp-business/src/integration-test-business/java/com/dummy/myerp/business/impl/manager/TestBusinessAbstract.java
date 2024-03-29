package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;


/**
 * Classe mère des classes de test d'intégration de la couche Business
 */
public abstract class TestBusinessAbstract {

    static {
        SpringRegistry.init();
    }

    /** {@link BusinessProxy} */
    private static final BusinessProxy BUSINESS_PROXY = SpringRegistry.getBusinessProxy();
    /** {@link TransactionManager} */
    private static final TransactionManager TRANSACTION_MANAGER = SpringRegistry.getTransactionManager();


    // ==================== Constructeurs ====================
    /**
     * Constructeur.
     */
    public TestBusinessAbstract() {
    }


    // ==================== Getters/Setters ====================
    public static BusinessProxy getBusinessProxy() {
        return BUSINESS_PROXY;
    }

    public static TransactionManager getTransactionManager() {
        return TRANSACTION_MANAGER;
    }
}
