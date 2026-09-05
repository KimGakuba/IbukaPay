# IbukaPay

A **distributed Java application for organizing expenses and avoiding late payments through automated reminders** — implemented as an academic practical project with **JSF + Hibernate + PostgreSQL**, following the layered architecture of the course reference project.

## Entities (full CRUD)

- **Member** (extends a `@MappedSuperclass` `Person`) — the person who organizes and pays expenses.
- **Expense** — a payment obligation (`@ManyToOne` to Member) with amount, due date and status, plus overdue detection and reminder scheduling.

## Features

- Full CRUD for **Member** and **Expense** through JSF views.
- **Three types of validation**: JSF validators, Bean Validation annotations, and programmatic server-side checks (duplicates, amount, dates, delete-integrity guard).
- **Three types of CSS**: external (`resources/css/style.css`), internal (`<style>` in each page), inline (`style="..."`).
- **Automated reminder simulation** for pending expenses and automatic **OVERDUE** flagging.
- Sessions/transactions managed manually in the DAO layer with rollback + clean closure.

## Stack

JSF 2.3 (Mojarra) · Hibernate ORM 5.6 · PostgreSQL 17 · Maven WAR · Apache Tomcat 9 · CDI/Weld · Hibernate Validator

## Running

```bash
# 1. create the database
psql -U postgres -c "CREATE DATABASE ibukapay;"

# 2. (optional) edit src/main/resources/hibernate.cfg.xml if your DB credentials differ
#    default: user=postgres password=12345 host=localhost:5432 db=ibukapay

# 3. build
mvn clean package

# 4. deploy target/IbukaPay-1.0-SNAPSHOT.war to Tomcat 9 (context: /ibukapay),
#    then open
#    http://localhost:8080/ibukapay/
```

## Structure

```
rw.ibukapay.model   Person (@MappedSuperclass), Member, Expense
rw.ibukapay.dao     HibernateUtil (singleton), MemberDao, ExpenseDao
rw.ibukapay.bean    MemberManagementBean, ExpenseManagementBean
src/main/webapp     Facelets views + resources/css/style.css
```

See **PROPOSAL.md** for the full assignment documentation (abstract, problem statement, scope, AS-IS, TO-BE, business requirements, software qualities and class diagram).