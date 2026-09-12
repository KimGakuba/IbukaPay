package rw.ibukapay.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import rw.ibukapay.model.FinancialRecord;

import java.util.List;

/**
 * The Class FinancialRecordDao - raw Hibernate CRUD operations for the
 * FinancialRecord entity, written in the same style as the reference
 * project's PatientDao.
 *
 * @version 1.0
 */
public class FinancialRecordDao {

    HibernateUtil hibernateUtil = new HibernateUtil();

    // CRUD db operations
    // CREATE
    public FinancialRecord registerFinancialRecord(FinancialRecord theRecord) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: create transaction
        Transaction tr = ss.beginTransaction();
        // step 3: perform action: CRUD
        ss.save(theRecord);
        // step 4: commit transaction
        tr.commit();
        // step 5: close session
        ss.close();
        return theRecord;
    }

    // READ - one record by business key
    public FinancialRecord findByRecordCode(String recordCode) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: perform action
        List<FinancialRecord> records = ss
                .createQuery("SELECT r FROM FinancialRecord r WHERE r.recordCode = :code",
                        FinancialRecord.class)
                .setParameter("code", recordCode)
                .list();
        ss.close();
        return records.isEmpty() ? null : records.get(0);
    }

    // READ - one record by primary key
    public FinancialRecord findById(int id) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: perform action
        FinancialRecord record = ss.get(FinancialRecord.class, id);
        ss.close();
        return record;
    }

    // READ - all records
    public List<FinancialRecord> findAllFinancialRecords() {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: perform action
        List<FinancialRecord> records = ss
                .createQuery("SELECT r FROM FinancialRecord r ORDER BY r.dueDate",
                        FinancialRecord.class).list();
        ss.close();
        return records;
    }

    // READ - number of records linked to a contact (used by the delete guard)
    public long countByContactId(int contactId) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: perform action
        Long count = (Long) ss
                .createQuery("SELECT count(r) FROM FinancialRecord r WHERE r.contact.contactId = :cid")
                .setParameter("cid", contactId)
                .uniqueResult();
        ss.close();
        return count == null ? 0 : count;
    }

    // UPDATE
    public FinancialRecord updateFinancialRecord(FinancialRecord theRecord) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: create transaction
        Transaction tr = ss.beginTransaction();
        // step 3: perform action: CRUD
        ss.update(theRecord);
        // step 4: commit transaction
        tr.commit();
        // step 5: close session
        ss.close();
        return theRecord;
    }

    // DELETE
    public void deleteFinancialRecord(FinancialRecord theRecord) {
        // step 1: create session
        Session ss = hibernateUtil.getSessionFactory().openSession();
        // step 2: create transaction
        Transaction tr = ss.beginTransaction();
        // step 3: perform action: CRUD
        ss.delete(theRecord);
        // step 4: commit transaction
        tr.commit();
        // step 5: close session
        ss.close();
    }
}