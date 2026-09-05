# IbukaPay — Project Documentation

---

## 1. Abstract

**IbukaPay** is a Java web application built with **JSF (JavaServer Faces)** and the **Hibernate ORM framework** that helps individuals organise their expenses and avoid late payments through automated reminders. A user first signs in (or creates an account), then records payment obligations — an *Expense* — with an amount, description, due date and status, and manages the contributors to those payments (*Members*). Whenever a pending expense is saved, the system automatically schedules a payment reminder before the due date, and any expense that remains unpaid after its due date is flagged as **OVERDUE**.

The system implements **full CRUD** (Create, Read, Update, Delete) on the business entities **Member** and **Expense**, plus authentication and registration for the **User** entity, following the layered architecture taught in class: `model` (JPA entities with a `@MappedSuperclass` base), `dao` (raw Hibernate session/transaction handling), `bean` (JSF managed beans acting as controllers), and JSF Facelets views. It applies **three types of validation** (JSF declarative validators, Bean Validation annotations, and programmatic server-side validation) and **three types of CSS** (external, internal/embedded and inline).

---

## 2. Problem Statement

In everyday life, people juggle many payment obligations — school fees, rent, electricity, water, loans, subscriptions. Payments are often forgotten simply because there is **no central place** to:

1. record every expense with its due date,
2. see at a glance which payments are still pending or already late,
3. be **reminded automatically** before a due date passes.

Spreadsheets or memory are unreliable: a pending bill is only noticed when it is already **overdue**, which can lead to late-payment penalties, service disconnections, or damaged credit standings. In addition, existing solutions are either complex (heavy accounting packages) or do not notify the user before the deadline. Finally, there is no controlled access to such a system — anyone should sign in before viewing or changing financial data.

**IbukaPay addresses this gap** with a protected web application where a user signs in or registers, records expenses with due dates and amounts, links them to members, receives automatic reminders, and has overdue expenses flagged automatically.

---

## 3. Scope of the Project

**In scope (implemented):**
- **Authentication and registration**: sign-in, sign-up, logout, session-based access control (every page is protected by an `AuthFilter`; only the login and register pages are public). All accounts share a single **USER** scope and the system has no privileged accounts.
- Register, view, update and delete **Members** (a person who organises and pays expenses) using full CRUD.
- Register, view, update and delete **Expenses** (payment obligations) with amount, description, due date and status.
- Automatic **reminder scheduling** simulation for pending expenses and **overdue detection**.
- Three-layer validation: JSF validators, Bean Validation annotations, and programmatic server-side checks.
- Styling with external, internal and inline CSS.
- Secure password storage using salted **PBKDF2** hashing (`PasswordUtil`), seeded default account `user/user123` (`DataInitializer`), and a delete-integrity guard.
- Persistence with Hibernate + PostgreSQL (`hbm2ddl.auto=update`), following the course reference architecture.

**Out of scope (future work / vision):**
- Real email/SMS delivery of reminders (currently simulated with a notification message).
- Role-based access control, admins and user management screens.
- Multiple currencies, exchange rates and monthly budgets/statistics.
- Invoicing, receipts and integration with payment gateways such as Mobile Money.
- A REST API, microservices (reminder/notification service) and mobile client.
- Deployment on Docker/Kubernetes.

The **distributed** nature of the project is its long-term target: a central *expense-management service*, a *reminder/notification service* and a *reporting service* communicating through REST APIs, so the reminder engine can scale and run independently of the user-facing application.

---

## 4. AS-IS Model (Current Situation)

Today, payment tracking and access to such tools are manual and fragmented:

```
Person receives a bill (paper/SMS/loans)
        |
        v
Writes it in a notebook / remembers it / ignores it
        |
        v
No record of who to contact for the payment
        |
        v
Due date passes unnoticed  ->  late fees, disconnection, bad credit
```

**Characteristics of the AS-IS situation:**
- No single repository of the person's payment obligations.
- No deadline tracking; bills are only recalled when a reminder letter or penalty arrives.
- No way to know at a glance how many expenses are pending, paid or overdue.
- Contact details of who owes what are scattered across receipts and messages.
- Anyone with the spreadsheet or the machine could view or alter financial data — no sign-in, no accountability.
- Late payments cause avoidable financial damage.

**Limitations:** incomplete information, no automation, no visibility, no access control, purely reactive.

---

## 5. TO-BE Model (Proposed Solution)

With IbukaPay, the same flow becomes digital, protected and proactive:

```
Person signs in (or creates an account)  [AuthFilter / SQLite-like User store in PostgreSQL]
        |
        v
Records each expense (amount, due date, status)
        |
        v
System schedules an automated reminder before the due date
        |
        v
Expense passes due date unpaid  ->  automatically flagged OVERDUE
        |
        v
User sees the flag and marks the expense PAID after settling
```

**Benefits delivered:**
- A centralised, always-accessible list of all expenses and their status.
- Only authenticated users can enter the system; the session is invalidated on logout.
- Proactive reminders instead of reactive penalties.
- Automatic overdue detection so nothing is silently forgotten.
- Update/delete capabilities keep records accurate.
- Clear, validated input at every boundary keeps the data trustworthy.

---

## 6. Business Requirements

### 6.1 Functional Requirements

| ID  | Requirement |
|-----|-------------|
| FR-1 | The system shall require the user to **sign in or register** before accessing any page. |
| FR-2 | The system shall register a **User** account with a unique username and a password hashed with PBKDF2. |
| FR-3 | The system shall **logout** the user and invalidate the session, redirecting to the login page. |
| FR-4 | The system shall allow a person to register as a **Member** with a unique Member ID (5 chars), names, gender, age, email and phone. |
| FR-5 | The system shall allow **member data** to be viewed, edited and deleted. |
| FR-6 | A member with registered expenses shall **not** be deleted (integrity guard). |
| FR-7 | The system shall allow an **Expense** to be registered with a unique Expense ID (5 chars), description, amount, due date and status. |
| FR-8 | The system shall allow **expense data** to be viewed, edited and deleted. |
| FR-9 | The system shall **flag** any non-paid expense whose due date has passed as **OVERDUE**. |
| FR-10 | The system shall **schedule a reminder** (email/SMS simulation) for pending expenses before their due date. |
| FR-11 | The system shall **persist all data** in PostgreSQL through Hibernate. |
| FR-12 | All input shall be validated using JSF validators, Bean Validation and programmatic checks. |

### 6.2 Non-Functional (Quality) Requirements

- **Usability:** intuitive pages, consistent navigation and clear validation messages.
- **Reliability:** transactions on all Hibernate operations with rollback on failure.
- **Performance:** the `SessionFactory` is a singleton, not re-created per request.
- **Maintainability:** clear layer separation (`model` / `dao` / `bean` / view).
- **Portability:** packaged as a `.war` deployable on any Servlet 4.0 container (Tomcat 9).

---

## 7. All Software Qualities Applied in the System

| Quality | How it is applied |
|---------|-------------------|
| **Maintainability** | Layered structure (`rw.ibukapay.model`, `.dao`, `.bean`, `.filter`, `.servlet`, `.util`) separates persistence, controllers, security and views. |
| **Reusability** | `Person` is a `@MappedSuperclass` reused by every person-like entity without duplication; `HibernateUtil` and `PasswordUtil` are singletons/utilities reused across DAOs and beans. |
| **Data Integrity** | Unique constraints on business keys (`memberId`, `expenseId`, `username`), auto-generated primary keys, foreign-key relation `Expense -> Member`, and a delete guard. |
| **Security** | Session-based `AuthFilter` gate on all pages, PBKDF2 password hashing with random salt, no secrets committed, data-binding restricted to beans only, and a dedicated `LogoutServlet` that invalidates the session. |
| **Usability** | Clear forms, mandatory-field markers, field-level and summary error messages, confirmation page. |
| **Reliability** | Every DAO operation runs in a transaction with rollback on error and session closure in `finally`. |
| **Performance** | `SessionFactory` built once as a singleton; EAGER fetch on the `Member` association keeps list views lazy-proxy free. |
| **Testability** | DAOs and beans are plain Java objects that can be unit-tested against a test database. |

---

## 8. Initial Class Diagram for All Entities

```mermaid
classDiagram
    class Person {
        <<MappedSuperclass>>
        -String firstName
        -String lastName
        -String gender
        -int age
    }
    class Member {
        <<Entity>> <<ManagedBean>>
        -int id PK
        -String memberId (unique, len 5)
        -String email
        -String phoneNumber
    }
    class Expense {
        <<Entity>> <<ManagedBean>>
        -int id PK
        -String expenseId (unique, len 5)
        -String description
        -BigDecimal amount
        -Date dueDate
        -String status
        -Member member (optional, @ManyToOne EAGER)
        +boolean isOverdue()
    }
    class User {
        <<Entity>>
        -int id PK
        -String username (unique)
        -String password (PBKDF2 hash)
    }

    Person <|-- Member : extends (@MappedSuperclass)

    class MemberDao { +save() +findById() +findAll() +update() +delete() +countByMemberId() }
    class ExpenseDao { +save() +findById() +findAll() +update() +delete() }
    class UserDao { +findByUsername() +save() }
    class HibernateUtil { +getSessionFactory() }
    class PasswordUtil { +hashPassword() +verifyPassword() }

    class MemberManagementBean { +saveMemberRecord() +findMemberRecords() +updateMemberRecord() +deleteMemberRecord() }
    class ExpenseManagementBean { +saveExpenseRecord() +findExpenseRecords() +updateExpenseRecord() +deleteExpenseRecord() }
    class UserManagementBean { +login() +register() +logout() +getCurrentUser() }
    class AuthFilter { +doFilter() }
    class LogoutServlet { +doGet() }
    class DataInitializer { +contextInitialized() }

    Member "1" <-- "0..*" Expense : owns (@ManyToOne EAGER)
    Expense ..> Member
    MemberDao ..> Member
    ExpenseDao ..> Expense
    MemberDao ..> HibernateUtil
    ExpenseDao ..> HibernateUtil
    UserDao ..> User
    UserDao ..> HibernateUtil
    PasswordUtil ..> User
    MemberManagementBean ..> MemberDao
    ExpenseManagementBean ..> ExpenseDao
    UserManagementBean ..> UserDao
    UserManagementBean ..> PasswordUtil
    AuthFilter ..> UserDao
    LogoutServlet --> (session invalidate)
    DataInitializer ..> UserDao
    DataInitializer ..> PasswordUtil
```

Plain-text representation:

```
                    Person  (@MappedSuperclass)
                        ▲
                        │  extends
              ┌─────────┴──────────┐
              │ Member (Entity)    │ 1 ────────<  0..*  Expense (Entity)
              │  id, memberId,     │   owns          id, expenseId, description,
              │  email, phone      │                 amount, dueDate, status, member
              └─────────┬──────────┘                 (ManyToOne -> Member, optional)

        User (Entity): id, username (unique), password (PBKDF2 hash)

        HibernateUtil ──► SessionFactory singleton
        MemberDao / ExpenseDao / UserDao ──► HibernateUtil
        UserDao / PasswordUtil ──► User
        MemberManagementBean / ExpenseManagementBean / UserManagementBean ──► respective DAO
        AuthFilter (protects all pages)  •  LogoutServlet (/logout)  •  DataInitializer (seeds user/user123)

        JSF Facelets views: login, register, index, memberRegistration, memberList, memberUpdate,
                            expenseRegistration, expenseList, expenseUpdate, confirmationPage
```

**Database schema** (created by `hbm2ddl.auto=update` on PostgreSQL):

```
ibukapay_user ( id PK identity, username VARCHAR UNIQUE, password VARCHAR(255) )

member ( id PK identity, member_id UNIQUE(5), firstname, lastname,
         gender, age CHECK (0..150), email, phone_number )

expense ( id PK identity, expense_id UNIQUE(5), description, amount NUMERIC(12,2),
          due_date DATE, status, owner_id FK -> member.id (nullable) )
```

---

## Technology Stack

| Layer        | Technology |
|--------------|------------|
| View         | JSF 2.3 (Mojarra) + Facelets XHTML |
| Controller   | JSF `@ManagedBean` + `@SessionScoped` beans |
| Persistence  | Hibernate ORM 5.6 (`Session` / `Transaction`) |
| Database     | PostgreSQL 17 |
| Security     | `AuthFilter` (session gate) + PBKDF2 password hashing |
| Build        | Maven (`.war`) |
| Container    | Apache Tomcat 9.0 (Servlet 4.0) using CDI/Weld |
| Validation   | JSF validators + Bean Validation (Hibernate Validator) |

## How to Run

1. Create the database: `CREATE DATABASE ibukapay;`
2. Adjust `src/main/resources/hibernate.cfg.xml` if your PostgreSQL credentials differ (default `postgres` / `12345`).
3. Build: `mvn package`
4. Deploy `target/IbukaPay-1.0-SNAPSHOT.war` on Apache Tomcat 9.
5. Open `http://localhost:8080/ibukapay/` (or your IDE's exploded context, e.g. `http://localhost:8082/IbukaPay_war_exploded/`).
6. Sign in with the seeded default account **user / user123** or create a new account on the register page.