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
import javax.validation.constraints.Size;

/**
 * The Class BillProvider - organization issuing bills.
 * Attributes: id, name, phoneNumber, email, notes
 *
 * @version 1.0
 */
@ManagedBean
@Entity
@Table(name = "bill_provider")
public class BillProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_provider_id")
    private int id;

    @Column(nullable = false, unique = true, length = 60)
    @NotBlank(message = "Provider name is required")
    @Size(min = 3, max = 60, message = "Provider name must be between 3 and 60 characters")
    private String name;

    @Column(name = "phone_number", length = 15)
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phoneNumber;

    @Column(nullable = false, length = 60)
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Column(length = 255)
    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;

    public BillProvider() {
    }

    public BillProvider(String name, String phoneNumber, String email, String notes) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}