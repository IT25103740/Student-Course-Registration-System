# Member 6: Malaviarachchi M.U.T - Viva Technical Breakdown
**Role**: Admin - Security & User Management
**Theme**: Advanced OOP Patterns & Security Infrastructure

This document provides a line-by-line explanation of the backend logic implemented by Member 6 to prove technical proficiency during the Viva.

---

## 1. `User.java` (Abstract Base Class)
*Demonstrates: Inheritance, Abstraction, and Polymorphism.*

- **`public abstract class User`**: Defines a generic template that cannot be instantiated directly.
- **`private String userId, username, passwordHash, role`**: Encapsulates common identity data shared by all system actors.
- **`public abstract String getDashboardUrl()`**: A polymorphic method. Each subclass (Admin, Moderator, Student) provides its own specific implementation to tell the system where to redirect after login.
- **`public abstract String[] getPermissions()`**: Forces subclasses to define their own security access levels.

---

## 2. `UserFactory.java` (Factory Design Pattern)
*Demonstrates: Creational Design Patterns & Decoupling.*

- **`public static User createUser(String role)`**: A static method that acts as a central hub for creating objects.
- **`switch (cleanRole)`**: Inspects the input string.
  - If "ADMIN", it returns `new Admin()`.
  - If "STUDENT", it returns `new Student()`.
- **Why this matters**: The rest of the system doesn't need to know *how* to create different users; it just asks the Factory. This makes the system extremely easy to extend (e.g., adding a "Faculty" role).

---

## 3. `UserManagementController.java` (The Gateway)
*Demonstrates: MVC Pattern & Request Handling.*

- **`@RequestMapping("/admin/users")`**: Sets the base URL for this controller.
- **`usersPage(Model model)`**: 
  - Calls `adminService.getAllUsers()` to fetch data.
  - Adds the user list and logs to the `model` for display in the frontend.
- **`addUser(...)`**:
  - Receives form data via `@RequestParam`.
  - Performs validation (ensuring fields aren't blank).
  - Passes data to the Service layer for persistence.
- **`updatePassword(...)`**: Handles credential reset requests.
- **`deleteUser(...)`**: Revokes system access and triggers the backend delete logic.

---

## 4. `AdminServiceImpl.java` (Security Logic)
*Demonstrates: Dependency Inversion & Business Rules.*

- **`registerUser(...)`**:
  - **Uniqueness Check**: Queries the repository to ensure the ID isn't already taken.
  - **Factory Call**: Uses `UserFactory.createUser()` to build the correct object type dynamically.
  - **Auto-Linking**: If the created user is a `Student`, it automatically generates an empty `StudentProfile` so the student doesn't have to manualy link accounts.
- **`updateUserPassword(...)`**: Finds the user by ID and performs an atomic update of the `passwordHash` field.
- **`getAuthenticationLogs()`**: Acts as a bridge between the controller and the security log database.

---

## 5. `AuthenticationLog.java` (Security Model)
*Demonstrates: Data Modeling & String Parsing.*

- **`private String action`**: Stores the event type (e.g., `LOGIN_SUCCESS`).
- **`getStatus()`**: A helper method that parses the action string to return a clean "SUCCESS" or "FAILURE" token for the UI.
- **`toFileString()`**: Formats the object into a pipe-delimited string (`ID|NAME|ACTION|TIME|IP`) for storage in the `.txt` file.
- **`fromFileString()`**: The inverse logic—splits a line from the text file back into a Java object for real-time display in the Admin Monitor.

---

## 🎓 Viva Key-Takeaway for Member 6:
*"My implementation focuses on **Infrastructure Security**. By using the **Factory Pattern**, I ensured that user creation is centralized and polymorphic. I also implemented a **Real-Time Security Log** that parses flat-file data to provide the Admin with a live audit trail of every login attempt."*
