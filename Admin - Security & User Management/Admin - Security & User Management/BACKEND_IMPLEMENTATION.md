# EduAll Backend Architectural Report

This document details the backend implementation for the EduAll University Portal, specifically mapping the core logic and OOP principles to the contributions of all 6 project members.

---

## 🏗️ Core Backend Framework
- **Architecture**: Layered (Controller -> Service -> Repository).
- **Persistence**: Flat-file database system using thread-safe I/O synchronization.
- **Patterns**:
  - **Factory Pattern**: Centralized object creation for polymorphic User types.
  - **Singleton Pattern**: Managed via Spring's `@Service` and `@Repository` annotations for safe, single-instance data access.
  - **Dependency Inversion**: High-level controllers depend on Service Interfaces rather than concrete implementations.

---

## 👨‍💻 Member 1: Umer D.R.S (Student - Enrollment)
**Responsibility**: Managing the lifecycle of course enrollment requests.

- **Create (Submit Request)**:
  - Logic: `StudentServiceImpl.submitEnrollmentRequest()` checks course capacity in real-time.
  - **Refinement**: The system counts both `APPROVED` and `PENDING` requests to determine capacity. This ensures that as soon as a student applies for the last seat, any subsequent applicants are immediately waitlisted, providing 100% accurate real-time sync.
  - Fail-Safe: If a module is full, the system automatically transitions the request to `WAITLISTED` status and creates a corresponding queue entry.
- **Read (Discovery)**:
  - Modules: `moduleRepository.findAll()` feeds the card-based marketplace.
  - History: `requestRepository.findByStudentId()` retrieves the specific audit trail for the logged-in student.
- **Update (Swap)**:
  - Implementation: `swapModule(requestId, newModuleCode)` allows students to update their pending requests before staff approval. It resets the internal timestamp to ensure FIFO integrity.
- **Delete (Withdraw)**:
  - Logic: `withdrawRequest(requestId)` removes pending records, ensuring no processed records can be deleted by users.

---

## 👨‍💻 Member 2: Weerasekara G.W.D.S (Student - Profile)
**Responsibility**: Secure management of student academic identity.

- **Create (Initialization)**:
  - Logic: `createProfile()` is triggered upon first login. It establishes a `StudentProfile` object linked to the primary `User` account.
- **Read (Academic View)**:
  - Retrieval: `getProfile(studentId)` fetches full academic details, including program and status.
- **Update (Contact Sync)**:
  - Implementation: `updateContactInfo()` performs atomic updates to email and phone fields, ensuring the profile stays synchronized with university records.
- **Delete (Deactivation)**:
  - Logic: `deactivateProfile()` implements "Soft Delete." It marks the profile as `active = false`, revoking portal access while maintaining historical data for audit purposes.

---

## 👨‍💻 Member 3: Kallora K.M.T.D (Moderator - Waitlist)
**Responsibility**: Implementation of FIFO (First-In-First-Out) priority logic for over-capacity modules.

- **Create (Queue Entry)**:
  - Logic: `addToWaitlist()` automatically calculates the `nextPosition` by analyzing existing entries for a specific module code.
- **Read (Priority View)**:
  - Implementation: `getWaitlistForModule()` filters the `WaitlistRepository` and provides a ranked list of students.
- **Update (Manual Reordering)**:
  - Logic: `updateWaitlistPosition()` allows staff to adjust ranks. After a rank change, `reindexWaitlist()` runs to ensure positions remain sequential (1, 2, 3...).
- **Delete (Resolution)**:
  - Logic: `removeFromWaitlist()` or `approveFromWaitlist()` removes the entry. Crucially, the system then triggers an automatic re-indexing for all remaining students in that queue.

---

## 👨‍💻 Member 4: Arachchi V.A.K.S.V (Moderator - Auditing)
**Responsibility**: Algorithmic sorting and batch processing of student requests.

- **Create (Batching)**:
  - Implementation: `generateBatchList()` uses a filter to isolate `PENDING` requests for concentrated moderator review.
- **Read (Algorithmic Sorting)**:
  - **Algorithm**: `SortingUtils.insertionSortRequests()` explicitly implements the **Insertion Sort** algorithm.
  - Logic: It sorts request objects chronologically by their Unix timestamps to prove fairness in staff decision-making.
- **Update (Override decision)**:
  - Logic: `approveRequest()` and `rejectRequest()` perform state transitions. Approval also triggers an atomic increment of the `currentEnrollment` count in the module registry.
- **Delete (Cleanup)**:
  - Logic: `clearProcessedRequests()` performs a global wipe of approved/rejected logs to keep the "Active History" view clean and efficient.

---

## 👨‍💻 Member 5: Jayathilaka W.M.U.S (Admin - Curriculum)
**Responsibility**: Maintaining the infrastructure and seating limits of the academic curriculum.

- **Create (Provisioning)**:
  - Logic: `addModule()` utilizes a DTO (Data Transfer Object) for safety. It enforces ID uniqueness to prevent registry corruption.
- **Read (Analytics)**:
  - Implementation: `getAllModules()` provides the data for the Admin "Health Widgets," calculating aggregate seating capacity across the system.
- **Update (Capacity Adjustment)**:
  - Logic: `updateModuleCapacity()` allows admins to dynamically expand or contract module limits based on resource availability.
- **Delete (Termination)**:
  - Logic: `removeModule()` permanently de-registers a module from the academic catalog.

---

## 👨‍💻 Member 6: Malaviarachchi M.U.T (Admin - Security)
**Responsibility**: System-wide user provisioning and security auditing using Design Patterns.

- **Create (Polymorphic Registration)**:
  - **Pattern**: Uses `UserFactory.createUser()`.
  - Logic: Based on the "Role" string (ADMIN, MODERATOR, STUDENT), the factory instantiates the correct subclass, demonstrating **Inheritance** and **Polymorphism**.
- **Read (Security Logging)**:
  - Implementation: `getAuthenticationLogs()` reads from a dedicated `logs.txt` file, providing a live feed of all successful and failed access attempts.
- **Update (Override)**:
  - Logic: `updateUserPassword()` provides a master override for security credentials, including the generation of temporary system tokens.
- **Delete (Revoke)**:
  - Logic: `deleteUser()` permanently removes security profiles from the root `UserRepository`.

---

## 🛠️ Data Integrity & Sync
- **Atomic Operations**: All write operations utilize `Files.write` with the `SYNC` option to ensure changes are immediately flushed to disk before the next UI refresh.
- **Error Handling**: Custom exceptions like `UserAlreadyExistsException` and `DataAccessException` prevent the system from entering an inconsistent state.
- **Polymorphism**: The `User` model uses abstract methods like `getDashboardUrl()`, allowing the frontend to redirect users based on their object type without `if/else` role checking.
