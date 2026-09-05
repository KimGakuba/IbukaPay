package rw.ibukapay.bean;

import rw.ibukapay.dao.ExpenseDao;
import rw.ibukapay.dao.MemberDao;
import rw.ibukapay.model.Expense;
import rw.ibukapay.model.Member;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The Class ExpenseManagementBean - JSF controller for Expense CRUD
 * including the automated reminder simulation.
 *
 * @version 1.0
 */
@ManagedBean
@SessionScoped
public class ExpenseManagementBean {

    private final ExpenseDao expenseDao = new ExpenseDao();
    private final MemberDao memberDao = new MemberDao();
    private Expense expenseToUpdate;

    public Expense getExpenseToUpdate() {
        return expenseToUpdate;
    }

    public void setExpenseToUpdate(Expense expenseToUpdate) {
        this.expenseToUpdate = expenseToUpdate;
    }

    public List<Member> getMembers() {
        return memberDao.findAll();
    }

    public List<String> getStatuses() {
        return Arrays.asList("PENDING", "PAID", "OVERDUE");
    }

    // CREATE - applies programmatic validation on top of JSF / Bean Validation
    public String saveExpenseRecord(Expense theExpense) {
        // programmatic validation: business key uniqueness
        Expense existing = expenseDao.findByExpenseId(theExpense.getExpenseId());
        if (existing != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Duplicate Expense ID",
                            "Expense ID " + theExpense.getExpenseId() + " already exists."));
            return null;
        }
        // programmatic validation: business rules on top of JSF / Bean Validation
        if (theExpense.getDueDate().before(new java.sql.Date(System.currentTimeMillis()))) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid due date",
                            "The due date cannot be earlier than today."));
            return null;
        }
        if (theExpense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid amount",
                            "The amount must be greater than zero."));
            return null;
        }
        try {
            expenseDao.save(theExpense);
            scheduleReminder(theExpense);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed",
                            "Could not save the expense. " + ex.getMessage()));
            return null;
        }
        return "confirmationPage";
    }

    // READ - all expenses for the list page
    public List<Expense> findExpenseRecords() {
        return expenseDao.findAll();
    }

    // READ - prepare the expense copy bound to the update form
    public String prepareUpdate(Expense theExpense) {
        this.expenseToUpdate = theExpense;
        if (theExpense.getMember() != null) {
            this.expenseToUpdate.setSelectedMemberId(theExpense.getMember().getMemberId());
        }
        return "expenseUpdate";
    }

    // UPDATE
    public String updateExpenseRecord() {
        try {
            expenseDao.update(expenseToUpdate);
            scheduleReminder(expenseToUpdate);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update failed",
                            "Could not update the expense. " + ex.getMessage()));
            return null;
        }
        return "expenseList";
    }

    // DELETE
    public String deleteExpenseRecord(Expense theExpense) {
        try {
            expenseDao.delete(theExpense);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete failed",
                            "Could not delete the expense. " + ex.getMessage()));
            return null;
        }
        return "expenseList";
    }

    // automated reminder simulation for PENDING expenses
    private void scheduleReminder(Expense theExpense) {
        if ("PENDING".equals(theExpense.getStatus()) && theExpense.getMember() != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Reminder scheduled",
                            "A payment reminder will be sent to "
                                    + theExpense.getMember().getEmail() + " before due date "
                                    + theExpense.getFormattedDueDate() + "."));
        }
    }
}