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
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class Reminder - simulated reminder attached to a FinancialRecord.
 * Attributes: id, reminderDate, message, status
 *
 * @version 1.0
 */
@ManagedBean
@Entity
@Table(name = "reminder")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminder_id")
    private int id;

    @Temporal(TemporalType.DATE)
    @Column(name = "reminder_date", nullable = false)
    @NotNull(message = "Reminder date is required")
    @FutureOrPresent(message = "Reminder date cannot be in the past")
    private Date reminderDate;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Message is required")
    @Size(min = 5, max = 255, message = "Message must be between 5 and 255 characters")
    private String message;

    @Column(nullable = false, length = 15)
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "ACTIVE|SENT|DISABLED", message = "Status must be ACTIVE, SENT or DISABLED")
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "record_id", nullable = false)
    @NotNull(message = "Reminder must be linked to a Financial Record")
    private FinancialRecord financialRecord;

    public Reminder() {
    }

    public Reminder(Date reminderDate, String message, String status, FinancialRecord financialRecord) {
        this.reminderDate = reminderDate;
        this.message = message;
        this.status = status;
        this.financialRecord = financialRecord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FinancialRecord getFinancialRecord() {
        return financialRecord;
    }

    public void setFinancialRecord(FinancialRecord financialRecord) {
        this.financialRecord = financialRecord;
    }

    public String getFormattedReminderDate() {
        if (reminderDate == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(reminderDate);
    }
}