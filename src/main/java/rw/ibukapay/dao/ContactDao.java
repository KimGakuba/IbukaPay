package rw.ibukapay.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import rw.ibukapay.model.Contact;

import java.util.List;

/**
 * The Class ContactDao - raw Hibernate CRUD operations for the Contact entity,
 * written in the same style as the reference project's PatientDao.
 *
 * @version 1.0
 */
public class ContactDao {

    HibernateUtil hibernateUtil = new HibernateUtil();

    // CRUD db operations
    // CREATE
    public Contact registerContact(Contact theContact) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: create transaction
        Transaction tr = ss.beginTransaction();
        // step 3: perform action: CRUD
        ss.save(theContact);
        // step 4: commit transaction
        tr.commit();
        // step 5: close session
        ss.close();
        return theContact;
    }

    // READ - one record by business key
    public Contact findByContactCode(String contactCode) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: perform action
        List<Contact> contacts = ss
                .createQuery("SELECT c FROM Contact c WHERE c.contactCode = :code", Contact.class)
                .setParameter("code", contactCode)
                .list();
        ss.close();
        return contacts.isEmpty() ? null : contacts.get(0);
    }

    // READ - all records
    public List<Contact> findAllContacts() {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: perform action
        List<Contact> contacts = ss
                .createQuery("SELECT c FROM Contact c ORDER BY c.contactName", Contact.class).list();
        ss.close();
        return contacts;
    }

    // UPDATE
    public Contact updateContact(Contact theContact) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: create transaction
        Transaction tr = ss.beginTransaction();
        // step 3: perform action: CRUD
        ss.update(theContact);
        // step 4: commit transaction
        tr.commit();
        // step 5: close session
        ss.close();
        return theContact;
    }

    // DELETE
    public void deleteContact(Contact theContact) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: create transaction
        Transaction tr = ss.beginTransaction();
        // step 3: perform action: CRUD
        ss.delete(theContact);
        // step 4: commit transaction
        tr.commit();
        // step 5: close session
        ss.close();
    }
}