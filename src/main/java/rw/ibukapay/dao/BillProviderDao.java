package rw.ibukapay.dao;

import org.hibernate.Session;
import rw.ibukapay.model.BillProvider;

import java.util.List;

/**
 * The Class BillProviderDao - read operations for the BillProvider lookup list.
 *
 * @version 1.0
 */
public class BillProviderDao {

    HibernateUtil hibernateUtil = new HibernateUtil();

    // READ - all bill providers (used to populate the record form dropdown)
    public List<BillProvider> findAllBillProviders() {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: perform action
        List<BillProvider> providers = ss
                .createQuery("SELECT b FROM BillProvider b ORDER BY b.name", BillProvider.class).list();
        ss.close();
        return providers;
    }

    // READ - one bill provider by primary key
    public BillProvider findById(int id) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: perform action
        BillProvider provider = ss.get(BillProvider.class, id);
        ss.close();
        return provider;
    }
}