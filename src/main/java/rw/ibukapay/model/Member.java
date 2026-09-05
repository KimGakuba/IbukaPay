package rw.ibukapay.model;

import javax.faces.bean.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The Class Member - an IbukaPay user (person) who organizes and pays expenses.
 *
 * @version 1.0
 */
@ManagedBean
@Entity
@Table(name = "member")
public class Member extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "member_id", nullable = false, unique = true, length = 5)
    @NotNull(message = "Member ID is required")
    @Size(min = 5, max = 5, message = "Member ID must be exactly 5 characters")
    @Pattern(regexp = "[A-Z0-9]{5}", message = "Member ID must be 5 characters (A-Z, 0-9)")
    private String memberId;

    @Column(nullable = false, length = 60)
    @NotNull(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 60, message = "Email cannot exceed 60 characters")
    private String email;

    @Column(name = "phone_number", length = 15)
    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phoneNumber;

    public Member() {
    }

    public Member(String memberId, String firstName, String lastName, String gender,
                  int age, String email, String phoneNumber) {
        super(firstName, lastName, gender, age);
        this.memberId = memberId;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}