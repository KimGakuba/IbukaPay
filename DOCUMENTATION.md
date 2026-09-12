# IbukaPay — Project Documentation

**Author:** NGABONZIZA Kim Gakuba (ID: 27670)
**Course:** Web Technology and Internet
**Date:** 11-09-2026
**GitHub Repository:** https://github.com/KimGakuba/IbukaPay
**Video Recording:** *(Google Vid link — to be inserted after recording)*

---

## 1. Abstract

**IbukaPay** is a personal financial obligation and bill management system that helps one person manage all their financial responsibilities in one place. The system records money the user owes to others, money others owe to the user, and personal and household bills.

Unlike expense-sharing applications, IbukaPay does not require the other person to create an account — anyone can simply be saved as a **Contact**, making the system suitable for informal loans, IOUs, rent, electricity, water, school fees, and similar real-life situations.

The system is built with **JSF 2.3 (JavaServer Faces)** and the **Hibernate ORM framework** on **PostgreSQL**, following the layered architecture taught in class: `model` (JPA entities), `dao` (raw Hibernate session/transaction handling), managed beans acting as controllers, and JSF Facelets XHTML views — exactly mirroring the logic of the reference project **PatientManagementSystem Hibernate V3**.

Two entities — **Contact** and **FinancialRecord** — are implemented with full CRUD (Create, Read, Update, Delete) operations. The system applies **three types of validation** (JSF declarative validators, Bean Validation annotations, and programmatic server-side checks) and **three types of CSS** (external stylesheet, internal/embedded `<style>` blocks, and inline `style="..."` attributes). Like the V3 reference, the project uses **no filter and no listener** — neither is required by the assignment.

---

## 2. Problem Statement

Many people manage borrowed money, lent money, and household bills using memory, notebooks, or chat messages. This creates several problems:

- Due dates are forgotten.
- Partial payments are difficult to track.
- There is no single place to see all obligations.
- Existing expense apps often require every person to register, even when they are not actual users.

IbukaPay solves this by allowing one registered user to record financial obligations involving people or organizations that do not have accounts.

---

## 3. Scope of the Project

### In Scope (Implemented)

- **Contact** — full CRUD: register, view, update, delete any person linked to records
- **FinancialRecord** — full CRUD: record money I owe, money owed to me, or bills, with amount, remaining amount, due date, status, and optional Contact/Category/BillProvider associations
- All 7 JPA entities from the class diagram are defined: User, Contact, FinancialRecord, Category, BillProvider, Payment, Reminder
- Partial payment tracking (remainingAmount field), overdue detection, and simulated reminders
- Input validation across three layers
- External, internal and inline CSS styling

### Out of Scope (Future Work)

- Real SMS and email notifications
- Monthly budgeting, spending statistics
- Automatic recurring payments
- Exporting records, multiple currencies
- REST API, microservices

---

## 4. AS-IS Model

Before IbukaPay, people:

1. Lend or borrow money and rely on memory
2. Keep bills as paper receipts or SMS messages
3. Forget due dates and lose track of remaining balances
4. Have no complete financial overview

**Limitations:** information scattered, high chance of mistakes, no reminders, not searchable, no overall financial picture.

---

## 5. TO-BE Model

With IbukaPay, the process becomes digital, protected and proactive:

1. User opens the application (no login needed — no filter/listener, matching V3)
2. Optionally adds Contacts and Bills Providers
3. Creates a Financial Record (I Owe / Owed to Me / Bill)
4. Links the record to a Contact, Category or BillProvider
5. Enters amount, remaining amount, due date, category and description
6. System schedules an automated reminder before the due date
7. System automatically updates status (pending → overdue / paid)
8. User searches and filters records from the dashboard

---

## 6. All Software Qualities Applied in the System

| Quality | How it is applied |
|---------|-------------------|
| **Maintainability** | Layered structure (`rw.ibukapay.model`, `.dao`, managed beans in `rw.ibukapay`) mirroring V3 |
| **Data Integrity** | Unique constraints on business keys (contactCode, recordCode), FK relations, delete guards |
| **Security** | No secrets in code, strict input validation, referential-integrity delete guards (assignment needs no filter/listener — V3 implements none) |
| **Usability** | Clear forms, mandatory-field markers, field-level and summary error messages, confirmation page |
| **Reliability** | Every DAO operation runs in the manual transaction pattern (open session → begin → CRUD → commit → close) |
| **Performance** | SessionFactory built once and cached; EAGER fetch on associations keeps list views free of lazy proxy issues |

---

## 7. Initial Class Diagram for All Entities

```
Person is NOT used in the new entity model (the PDF has no Person superclass).

Entities (all @Entity, @ManagedBean):
  User         - userId, userName, passwordHash   (defined in the diagram; no CRUD UI in this assignment)
  Contact      - contactId, contactCode, contactName, contactPhoneNumber, contactEmail, notes
  FinancialRecord - recordId, recordCode, type, description, amount, remainingAmount,
                    dueDate, status, contact(FK), category(FK), billProvider(FK)
  Category     - id, name, description
  BillProvider - id, name, phoneNumber, email, notes
  Payment      - id, amount, paymentDate, notes, financialRecord(FK)
  Reminder     - id, reminderDate, message, status, financialRecord(FK)

Relationships:
  Contact       0..* ──< 0..* FinancialRecord (optional contact on a record)
  Category      0..* ──< 0..* FinancialRecord (optional category on a record)
  BillProvider  0..* ──< 0..* FinancialRecord (optional provider on a record)
  FinancialRecord 1 ──< 0..* Payment          (one record has many payments)
  FinancialRecord 1 ──< 0..* Reminder         (one record has many reminders)

Full CRUD implemented for: Contact and FinancialRecord.
```

---

## 8. Three Types of Validation

### Type 1 — JSF Declarative Validators (in XHTML views)

Every form uses `<f:validateLength>`, `<f:validateRegex>`, `<f:validateDoubleRange>`,
`<f:convertDateTime>` and `required="true"` with `requiredMessage`.

Examples:
```xml
<f:validateLength minimum="5" maximum="5"/>
<f:validateRegex pattern="[A-Z0-9]{5}"/>
<f:validateDoubleRange minimum="0.01"/>
<f:convertDateTime pattern="yyyy-MM-dd"/>
```

### Type 2 — Bean Validation Annotations (on entities)

Every entity field carries `@NotNull`, `@NotBlank`, `@Size`, `@Pattern`, `@Email`,
`@DecimalMin`, `@FutureOrPresent` etc., validated automatically when the entity is persisted.

Examples:
```java
@NotBlank(message = "Contact code is required")
@Size(min = 5, max = 5, message = "Contact code must be exactly 5 characters")
@Pattern(regexp = "[A-Z0-9]{5}", message = "Contact code must be 5 characters (A-Z, 0-9)")

@NotNull(message = "Amount is required")
@DecimalMin(value = "0.01", message = "Amount must be greater than zero")

@FutureOrPresent(message = "Due date cannot be in the past")
```

### Type 3 — Programmatic Server-Side Validation (in management beans)

Both `ContactManagementBean.saveContactRecord()` and
`FinancialRecordManagementBean.saveFinancialRecord()` perform checks that JSF and Bean
Validation alone cannot catch:

- **Duplicate business key**: `contactDao.findByContactCode()` / `financialRecordDao.findByRecordCode()`
- **Due date not in the past**: `theRecord.getDueDate().before(new java.sql.Date(...))`
- **Amount must be positive**: `theRecord.getAmount().compareTo(BigDecimal.ZERO) <= 0`
- **Remaining amount within bounds**: remainingAmount ≥ 0 and ≤ total amount
- **Delete guard (referential integrity)**: a Contact linked to any FinancialRecord cannot be deleted

---

## 9. Three Types of CSS

### External CSS
`src/main/webapp/resources/css/style.css` — shared across all pages via:
```xml
<h:outputStylesheet library="css" name="style.css"/>
```
Contains: `.navbar`, `.container`, `.card`, `.data-table`, `.btn`, `.form-grid`, `.field`, `.error-box`, `.info-box`, `.footer`, `.section-note`.

### Internal (Embedded) CSS
Every XHTML page has a `<style type="text/css">` block in `<h:head>` with page-specific rules:
```xml
<style type="text/css">
    /* internal (embedded) CSS - only used on this page */
    .panel-box { background: #f8fafc; ... }
</style>
```
Used on: `contactRegistration`, `contactList`, `contactUpdate`, `financialRecordRegistration`,
`financialRecordList`, `financialRecordUpdate`, `confirmationPage`, `index`.

### Inline CSS
Individual elements carry `style="..."` attributes throughout:
```xml
<h:commandLink action="#{contactManagementBean.deleteContactRecord(ctc)}" value="Delete"
               style="color:#dc2626"/>
```

---

## 10. Technology Stack

| Layer        | Technology |
|--------------|------------|
| View         | JSF 2.3 (Mojarra) + Facelets XHTML |
| Controller   | JSF `@ManagedBean` + `@SessionScoped` beans in `rw.ibukapay` |
| Persistence  | Hibernate ORM 5.6 (`Session` / `Transaction`) |
| Database     | PostgreSQL 17 |
| Security     | Not required for this assignment — no filter or listener (same as V3) |
| Validation   | JSF validators + Bean Validation (Hibernate Validator) + programmatic checks |
| CSS          | External stylesheet + internal `<style>` blocks + inline `style="..."` |
| Build        | Maven (`.war`) |
| Container    | Apache Tomcat 9.0 (Servlet 4.0) using CDI/Weld |

---

## 11. How to Run

1. Create the database:
   ```sql
   CREATE DATABASE ibukapay;
   ```
2. Edit `src/main/resources/hibernate.cfg.xml` if your PostgreSQL credentials differ (default `postgres` / `12345`).
3. Build:
   ```bash
   mvn clean package
   ```
4. Deploy `target/IbukaPay-1.0-SNAPSHOT.war` on Apache Tomcat 9.
5. Open:
   ```
   http://localhost:8080/IbukaPay-1.0-SNAPSHOT/
   ```
   The index page loads directly — there is no login page because the assignment
   requires no filter or listener (and, like the V3 reference project, none is implemented).

---

## 12. GitHub & Video Links

| Item | Link |
|------|------|
| **GitHub Repository** | https://github.com/KimGakuba/IbukaPay |
| **Video Recording** | *(Record using Google Vid with screen + camera (5–10 min), paste the share link here)* |

---

*Document prepared by NGABONZIZA Kim Gakuba — ID 27670, Web Technology and Internet, 11 September 2026.*