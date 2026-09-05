package rw.ibukapay.bean;

import rw.ibukapay.dao.ExpenseDao;
import rw.ibukapay.dao.MemberDao;
import rw.ibukapay.model.Member;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * The Class MemberManagementBean - JSF controller for Member CRUD.
 *
 * @version 1.0
 */
@ManagedBean
@SessionScoped
public class MemberManagementBean {

    private final MemberDao memberDao = new MemberDao();
    private final ExpenseDao expenseDao = new ExpenseDao();
    private Member memberToUpdate;

    public Member getMemberToUpdate() {
        return memberToUpdate;
    }

    public void setMemberToUpdate(Member memberToUpdate) {
        this.memberToUpdate = memberToUpdate;
    }

    // CREATE - with programmatic server-side validation
    public String saveMemberRecord(Member theMember) {
        // programmatic validation: the business key must not already exist
        Member existing = memberDao.findByMemberId(theMember.getMemberId());
        if (existing != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Duplicate Member ID",
                            "Member ID " + theMember.getMemberId() + " is already registered."));
            return null;
        }
        try {
            memberDao.save(theMember);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed",
                            "Could not save the member. " + ex.getMessage()));
            return null;
        }
        return "confirmationPage";
    }

    // READ - all members for the list page
    public List<Member> findMemberRecords() {
        return memberDao.findAll();
    }

    // READ - prepare the member copy bound to the update form
    public String prepareUpdate(Member theMember) {
        this.memberToUpdate = theMember;
        return "memberUpdate";
    }

    // UPDATE
    public String updateMemberRecord() {
        try {
            memberDao.update(memberToUpdate);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update failed",
                            "Could not update the member. " + ex.getMessage()));
            return null;
        }
        return "memberList";
    }

    // DELETE - guarded so a member with expenses is not deleted (FK integrity)
    public String deleteMemberRecord(Member theMember) {
        if (expenseDao.countByMemberId(theMember.getId()) > 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Member not deleted",
                            "Member " + theMember.getMemberId()
                                    + " still owns registered expenses. Delete those expenses first."));
            return null;
        }
        try {
            memberDao.delete(theMember);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete failed",
                            "Could not delete the member. " + ex.getMessage()));
            return null;
        }
        return "memberList";
    }
}