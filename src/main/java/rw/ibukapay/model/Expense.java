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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class Expense - a payment obligation recorded by a member,
 * with a due date and an automatic reminder.
 *
 * @version 1.0
 */
@ManagedBean
@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "expense_id", nullable = false, unique = true, length = 5)
    @NotNull(message = "Expense ID is required")
    @Size(min = 5, max = 5, message = "Expense ID must be exactly 5 characters")
    @Pattern(regexp = "[A-Z0-9]{5}", message = "Expense ID must be 5 characters (A-Z, 0-9)")
    private String expenseId;

    @Column(nullable = false, length = 100)
    @NotNull(message = "Description is required")
    @Size(min = 5, max = 100, message = "Description must be between 5 and 100 characters")
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Temporal(TemporalType.DATE)
    @Column(name = "due_date", nullable = false)
    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private Date dueDate;

    @Column(nullable = false, length = 20)
    @NotNull(message = "Status is required")
    @Pattern(regexp = "PENDING|PAID|OVERDUE", message = "Status must be PENDING, PAID or OVERDUE")
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = true)
    private Member member;

    @Transient
    private String selectedMemberId;

    public Expense() {
    }

    public Expense(String expenseId, String description, BigDecimal amount, Date dueDate,
                   String status, Member member) {
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.status = status;
        this.member = member;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getSelectedMemberId() {
        return selectedMemberId;
    }

    public void setSelectedMemberId(String selectedMemberId) {
        this.selectedMemberId = selectedMemberId;
    }

    @Transient
    public boolean isOverdue() {
        if (status == null || status.equals("PAID")) {
            return false;
        }
        if (dueDate == null) {
            return false;
        }
        Date today = new Date();
        return dueDate.before(today);
    }

    @Transient
    public String getFormattedDueDate() {
        if (dueDate == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(dueDate);
    }
}