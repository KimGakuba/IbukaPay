package rw.ibukapay;

import rw.ibukapay.dao.ContactDao;
import rw.ibukapay.dao.FinancialRecordDao;
import rw.ibukapay.model.Contact;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * The Class ContactManagementBean - JSF controller for Contact full CRUD,
 * written in the same style as the reference project's PatientManagementBean.
 *
 * @version 1.0
 */
@ManagedBean
@SessionScoped
public class ContactManagementBean {

    private Contact contactToUpdate;

    public Contact getContactToUpdate() {
        return contactToUpdate;
    }

    public void setContactToUpdate(Contact contactToUpdate) {
        this.contactToUpdate = contactToUpdate;
    }

    // CREATE - with programmatic server-side validation (business key uniqueness)
    public String saveContactRecord(Contact theContact) {
        ContactDao contactDao = new ContactDao();
        // programmatic validation: the business key must not already exist
        Contact existing = contactDao.findByContactCode(theContact.getContactCode());
        if (existing != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Duplicate Contact Code",
                            "Contact code " + theContact.getContactCode() + " is already registered."));
            return null;
        }
        try {
            // calling method to save record in database
            contactDao.registerContact(theContact);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed",
                            "Could not save the contact. " + ex.getMessage()));
            return null;
        }
        return "confirmationPage";
    }

    // READ - all contacts for the list page
    public List<Contact> findContactRecords() {
        ContactDao contactDao = new ContactDao();
        return contactDao.findAllContacts();
    }

    // READ - prepare the contact copy bound to the update form
    public String prepareUpdate(Contact theContact) {
        this.contactToUpdate = theContact;
        return "contactUpdate";
    }

    // UPDATE
    public String updateContactRecord() {
        ContactDao contactDao = new ContactDao();
        try {
            contactDao.updateContact(contactToUpdate);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update failed",
                            "Could not update the contact. " + ex.getMessage()));
            return null;
        }
        return "contactList";
    }

    // DELETE - guarded so a contact linked to a FinancialRecord is not deleted
    public String deleteContactRecord(Contact theContact) {
        FinancialRecordDao financialRecordDao = new FinancialRecordDao();
        if (financialRecordDao.countByContactId(theContact.getContactId()) > 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Contact not deleted",
                            "Contact " + theContact.getContactCode()
                                    + " is still linked to financial records. Delete those records first."));
            return null;
        }
        ContactDao contactDao = new ContactDao();
        try {
            contactDao.deleteContact(theContact);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete failed",
                            "Could not delete the contact. " + ex.getMessage()));
            return null;
        }
        return "contactList";
    }
}