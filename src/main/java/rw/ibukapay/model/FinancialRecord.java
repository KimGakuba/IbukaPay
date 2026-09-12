package rw.ibukapay.model;

import javax.faces.bean.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class FinancialRecord - the main obligation record.
 * Attributes: recordId, type, amount, remainingAmount, dueDate, status
 * Optionally linked to a Contact, a Category or a BillProvider.
 *
 * @version 1.0
 */
@ManagedBean
@Entity
@Table(name = "financial_record")
public class FinancialRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id_pk")
    private int recordId;

    @Column(name = "record_code", nullable = false, unique = true, length = 5)
    @NotBlank(message = "Record code is required")
    @Size(min = 5, max = 5, message = "Record code must be exactly 5 characters")
    @Pattern(regexp = "[A-Z0-9]{5}", message = "Record code must be 5 characters (A-Z, 0-9)")
    private String recordCode;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Type is required")
    @Pattern(regexp = "I_OWE|OWED_TO_ME|BILL", message = "Type must be I_OWE, OWED_TO_ME or BILL")
    private String type;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 100, message = "Description must be between 5 and 100 characters")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Column(name = "remaining_amount", nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Remaining amount is required")
    @DecimalMin(value = "0.00", message = "Remaining amount can never be negative")
    private BigDecimal remainingAmount;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date", nullable = false)
    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private Date dueDate;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "PENDING|PARTIALLY_PAID|PAID|OVERDUE", message = "Status must be PENDING, PARTIALLY_PAID, PAID or OVERDUE")
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_id", nullable = true)
    private Contact contact;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bill_provider_id", nullable = true)
    private BillProvider billProvider;

    @Transient
    private String selectedContactId;

    @Transient
    private String selectedCategoryId;

    @Transient
    private String selectedBillProviderId;

    public FinancialRecord() {
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BillProvider getBillProvider() {
        return billProvider;
    }

    public void setBillProvider(BillProvider billProvider) {
        this.billProvider = billProvider;
    }

    public String getSelectedContactId() {
        return selectedContactId;
    }

    public void setSelectedContactId(String selectedContactId) {
        this.selectedContactId = selectedContactId;
    }

    public String getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(String selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public String getSelectedBillProviderId() {
        return selectedBillProviderId;
    }

    public void setSelectedBillProviderId(String selectedBillProviderId) {
        this.selectedBillProviderId = selectedBillProviderId;
    }

    @Transient
    public String getFormattedDueDate() {
        if (dueDate == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(dueDate);
    }

    @Transient
    public String getTypeLabel() {
        if (type == null) {
            return "";
        }
        switch (type) {
            case "I_OWE":
                return "Money I Owe";
            case "OWED_TO_ME":
                return "Money Owed to Me";
            case "BILL":
                return "Bill";
            default:
                return type;
        }
    }
}