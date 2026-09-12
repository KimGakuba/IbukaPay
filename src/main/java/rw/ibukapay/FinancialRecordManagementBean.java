package rw.ibukapay;

import rw.ibukapay.dao.BillProviderDao;
import rw.ibukapay.dao.CategoryDao;
import rw.ibukapay.dao.ContactDao;
import rw.ibukapay.dao.FinancialRecordDao;
import rw.ibukapay.model.BillProvider;
import rw.ibukapay.model.Category;
import rw.ibukapay.model.Contact;
import rw.ibukapay.model.FinancialRecord;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * The Class FinancialRecordManagementBean - JSF controller for FinancialRecord
 * full CRUD (money I owe, money owed to me, and bills), written in the same
 * style as the reference project's PatientManagementBean.
 *
 * @version 1.0
 */
@ManagedBean
@SessionScoped
public class FinancialRecordManagementBean {

    private FinancialRecord recordToUpdate;

    public FinancialRecord getRecordToUpdate() {
        return recordToUpdate;
    }

    public void setRecordToUpdate(FinancialRecord recordToUpdate) {
        this.recordToUpdate = recordToUpdate;
    }

    // dropdown sources for the record form
    public List<Contact> getContacts() {
        ContactDao contactDao = new ContactDao();
        return contactDao.findAllContacts();
    }

    public List<Category> getCategories() {
        CategoryDao categoryDao = new CategoryDao();
        return categoryDao.findAllCategories();
    }

    public List<BillProvider> getBillProviders() {
        BillProviderDao billProviderDao = new BillProviderDao();
        return billProviderDao.findAllBillProviders();
    }

    public List<String> getTypes() {
        return Arrays.asList("I_OWE", "OWED_TO_ME", "BILL");
    }

    public List<String> getStatuses() {
        return Arrays.asList("PENDING", "PARTIALLY_PAID", "PAID", "OVERDUE");
    }

    // CREATE - applies programmatic validation on top of JSF / Bean Validation
    public String saveFinancialRecord(FinancialRecord theRecord) {
        // programmatic validation: business key uniqueness
        FinancialRecordDao financialRecordDao = new FinancialRecordDao();
        FinancialRecord existing = financialRecordDao.findByRecordCode(theRecord.getRecordCode());
        if (existing != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Duplicate Record Code",
                            "Record code " + theRecord.getRecordCode() + " already exists."));
            return null;
        }
        // programmatic validation: business rules on top of JSF / Bean Validation
        if (theRecord.getDueDate() == null
                || theRecord.getDueDate().before(new java.sql.Date(System.currentTimeMillis()))) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid due date",
                            "The due date cannot be earlier than today."));
            return null;
        }
        if (theRecord.getAmount() == null || theRecord.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid amount",
                            "The amount must be greater than zero."));
            return null;
        }
        if (theRecord.getRemainingAmount() == null
                || theRecord.getRemainingAmount().compareTo(BigDecimal.ZERO) < 0
                || theRecord.getRemainingAmount().compareTo(theRecord.getAmount()) > 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid remaining amount",
                            "The remaining amount can never be negative and cannot exceed the total amount."));
            return null;
        }
        // link the selected dropdown values to real entities
        resolveSelectedRelations(theRecord);
        try {
            // calling method to save record in database
            financialRecordDao.registerFinancialRecord(theRecord);
            scheduleReminder(theRecord);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed",
                            "Could not save the financial record. " + ex.getMessage()));
            return null;
        }
        return "confirmationPage";
    }

    // READ - all financial records for the list page
    public List<FinancialRecord> findFinancialRecords() {
        FinancialRecordDao financialRecordDao = new FinancialRecordDao();
        return financialRecordDao.findAllFinancialRecords();
    }

    // READ - prepare the record copy bound to the update form
    public String prepareUpdate(FinancialRecord theRecord) {
        this.recordToUpdate = theRecord;
        if (theRecord.getContact() != null) {
            this.recordToUpdate.setSelectedContactId(theRecord.getContact().getContactCode());
        }
        if (theRecord.getCategory() != null) {
            this.recordToUpdate.setSelectedCategoryId(String.valueOf(theRecord.getCategory().getId()));
        }
        if (theRecord.getBillProvider() != null) {
            this.recordToUpdate.setSelectedBillProviderId(String.valueOf(theRecord.getBillProvider().getId()));
        }
        return "financialRecordUpdate";
    }

    // UPDATE
    public String updateFinancialRecord() {
        FinancialRecordDao financialRecordDao = new FinancialRecordDao();
        try {
            resolveSelectedRelations(recordToUpdate);
            financialRecordDao.updateFinancialRecord(recordToUpdate);
            scheduleReminder(recordToUpdate);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update failed",
                            "Could not update the financial record. " + ex.getMessage()));
            return null;
        }
        return "financialRecordList";
    }

    // DELETE
    public String deleteFinancialRecord(FinancialRecord theRecord) {
        FinancialRecordDao financialRecordDao = new FinancialRecordDao();
        try {
            financialRecordDao.deleteFinancialRecord(theRecord);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete failed",
                            "Could not delete the financial record. " + ex.getMessage()));
            return null;
        }
        return "financialRecordList";
    }

    // turns the transient dropdown strings ("selected...") into real entity links
    private void resolveSelectedRelations(FinancialRecord theRecord) {
        if (theRecord.getSelectedContactId() != null && !theRecord.getSelectedContactId().isEmpty()) {
            ContactDao contactDao = new ContactDao();
            Contact contact = contactDao.findByContactCode(theRecord.getSelectedContactId());
            theRecord.setContact(contact);
        }
        if (theRecord.getSelectedCategoryId() != null && !theRecord.getSelectedCategoryId().isEmpty()) {
            CategoryDao categoryDao = new CategoryDao();
            Category category = categoryDao.findById(Integer.parseInt(theRecord.getSelectedCategoryId()));
            theRecord.setCategory(category);
        }
        if (theRecord.getSelectedBillProviderId() != null && !theRecord.getSelectedBillProviderId().isEmpty()) {
            BillProviderDao billProviderDao = new BillProviderDao();
            BillProvider provider = billProviderDao.findById(Integer.parseInt(theRecord.getSelectedBillProviderId()));
            theRecord.setBillProvider(provider);
        }
    }

    // automated reminder simulation for PENDING records
    private void scheduleReminder(FinancialRecord theRecord) {
        if ("PENDING".equals(theRecord.getStatus()) || "PARTIALLY_PAID".equals(theRecord.getStatus())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Reminder scheduled",
                            "A payment reminder will be sent before due date "
                                    + theRecord.getFormattedDueDate() + "."));
        }
    }
}