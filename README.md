# IbukaPay

A **personal financial obligation and bill tracking system** — implemented as an academic practical project with **JSF + Hibernate + PostgreSQL**, following the layered architecture of the course reference project.

**Author:** NGABONZIZA Kim Gakuba (ID: 27670)
**GitHub:** https://github.com/KimGakuba/IbukaPay

## Entities (from the class diagram, all defined)

- **User** — account holder (`userId`, `userName`, `passwordHash`)
- **Contact** — person without an account linked to records ✅ full CRUD
- **FinancialRecord** — main obligation record ✅ full CRUD
- **Category**, **BillProvider**, **Payment**, **Reminder** — supporting entities

## Features

- Full CRUD for **Contact** and **FinancialRecord** through JSF views.
- **Three types of validation**: JSF declarative validators, Bean Validation annotations, and programmatic server-side checks (duplicates, amount, dates, remaining-amount bounds, delete-integrity guard).
- **Three types of CSS**: external (`resources/css/style.css`), internal (`<style>` in each page), inline (`style="..."`).
- Automated reminder simulation for pending records and automatic **OVERDUE** flagging.
- Sessions/transactions managed manually in the DAO layer following the reference project style (`HibernateUtil` instance → `openSession` → `beginTransaction` → CRUD → `commit` → `close`).

## Implementation style

Built to mirror **PatientManagementSystem Hibernate V3** logic (the class reference project):

- Model classes are `@ManagedBean @Entity` JPA beans, so Facelets bind directly to `#{contact.contactName}`, `#{financialRecord.recordCode}`, etc.
- Managed beans live in the root package `rw.ibukapay` and instantiate `new XxxDao()` per method.
- DAOs hold a `HibernateUtil` instance and call `getSessionFactory().openSession()`.
- Implicit navigation (action methods return the next page name).
- **No filter, no listener, no login** — the assignment (full CRUD + 3 validation types + 3 CSS types) does not require them, and the V3 reference implements none.

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

# 4. deploy target/IbukaPay-1.0-SNAPSHOT.war to Tomcat 9,
#    then open
#    http://localhost:8080/IbukaPay-1.0-SNAPSHOT/
```

## Structure

```
rw.ibukapay             ContactManagementBean, FinancialRecordManagementBean (JSF managed beans)
rw.ibukapay.model       User, Contact, FinancialRecord, Category, BillProvider, Payment, Reminder
rw.ibukapay.dao         HibernateUtil (V3-style SessionFactory access), ContactDao, FinancialRecordDao,
                        CategoryDao, BillProviderDao
src/main/webapp         Facelets views + resources/css/style.css
```

See **DOCUMENTATION.md** for the full assignment documentation (abstract, problem statement, scope, AS-IS, TO-BE, software qualities, class diagram, validation, CSS, and links to GitHub / video).