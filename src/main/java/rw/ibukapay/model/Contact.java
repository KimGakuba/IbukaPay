package rw.ibukapay.model;

import javax.faces.bean.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The Class Contact - a person without an account who is linked to
 * a FinancialRecord (borrower, lender, or IOU counterpart).
 * Attributes: contactId, contactName, contactPhoneNumber, contactEmail, notes
 *
 * @version 1.0
 */
@ManagedBean
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id_pk")
    private int contactId;

    @Column(name = "contact_code", nullable = false, unique = true, length = 5)
    @NotBlank(message = "Contact code is required")
    @Size(min = 5, max = 5, message = "Contact code must be exactly 5 characters")
    @Pattern(regexp = "[A-Z0-9]{5}", message = "Contact code must be 5 characters (A-Z, 0-9)")
    private String contactCode;

    @Column(name = "contact_name", nullable = false, length = 60)
    @NotBlank(message = "Contact name is required")
    @Size(min = 3, max = 60, message = "Contact name must be between 3 and 60 characters")
    private String contactName;

    @Column(name = "contact_phone_number", length = 15)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "[0-9+\\- ]{10,15}", message = "Phone number must be 10 to 15 digits")
    private String contactPhoneNumber;

    @Column(name = "contact_email", length = 60)
    @Email(message = "Please provide a valid email address")
    @Size(max = 60, message = "Email cannot exceed 60 characters")
    private String contactEmail;

    @Column(length = 255)
    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;

    public Contact() {
    }

    public Contact(String contactCode, String contactName, String contactPhoneNumber,
                   String contactEmail, String notes) {
        this.contactCode = contactCode;
        this.contactName = contactName;
        this.contactPhoneNumber = contactPhoneNumber;
        this.contactEmail = contactEmail;
        this.notes = notes;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactCode() {
        return contactCode;
    }

    public void setContactCode(String contactCode) {
        this.contactCode = contactCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}