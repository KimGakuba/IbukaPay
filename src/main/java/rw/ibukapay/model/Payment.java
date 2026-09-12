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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class Payment - individual payment towards a FinancialRecord.
 * Attributes: id, amount, paymentDate, notes
 *
 * @version 1.0
 */
@ManagedBean
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int id;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero")
    private BigDecimal amount;

    @Temporal(TemporalType.DATE)
    @Column(name = "payment_date", nullable = false)
    @NotNull(message = "Payment date is required")
    private Date paymentDate;

    @Column(length = 255)
    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "record_id", nullable = false)
    @NotNull(message = "Payment must be linked to a Financial Record")
    private FinancialRecord financialRecord;

    public Payment() {
    }

    public Payment(BigDecimal amount, Date paymentDate, String notes, FinancialRecord financialRecord) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.notes = notes;
        this.financialRecord = financialRecord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public FinancialRecord getFinancialRecord() {
        return financialRecord;
    }

    public void setFinancialRecord(FinancialRecord financialRecord) {
        this.financialRecord = financialRecord;
    }

    public String getFormattedPaymentDate() {
        if (paymentDate == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(paymentDate);
    }
}