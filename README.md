# 🏫 EduAll: Student Course Registration System

<div align="center">
  <img src="https://media.giphy.com/media/SWoSkN6DxTszqIKEqv/giphy.gif" width="600px" style="border-radius:15px; box-shadow: 0 10px 30px rgba(0,0,0,0.15);">
  <br><br>
  <strong>An Advanced, Database-Less University Portal Powered by Custom Data Structures</strong>
  <br>
  <em>Engineered for the SLIIT Semester Evaluation - Faculty of Computing</em>
</div>

<br>

<div align="center">

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://java.com)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com)
[![Security](https://img.shields.io/badge/Security-High_Integrity-red?style=for-the-badge&logo=shield)](https://github.com)
[![Status](https://img.shields.io/badge/Status-Viva_Ready-brightgreen?style=for-the-badge)](https://github.com)

</div>

<br>

## 🚀 Project Overview

Welcome to the **EduAll Central Registration Portal**. This professional-grade web application is designed to handle high-volume student module enrollments, dynamic capacity forecasting, and automated waitlist management. 

Instead of relying on a traditional relational database, this system showcases core computer science concepts by utilizing **Advanced File I/O**, **FIFO Queues**, **Insertion Sort**, and **Binary Search Algorithms** to ensure persistent data storage and real-time processing with zero synchronization lag.

<br>

## 💎 Premium Features

| Feature | Description | Access Level |
| :--- | :--- | :--- |
| 👨‍🎓 **Student Portal** | Direct gateway for students to browse modules, view capacity bars, and track enrollment status. | `Public` |
| 🛡️ **Secure Admin Console** | High-security root terminal for user provisioning, curriculum configuration, and log monitoring. | `Protected` |
| 📡 **Live Waitlist Radar** | Real-time monitoring of module saturation. Dynamically handles seat allocation using **FIFO Queues**. | `Protected` |
| 📊 **Real-Time Analytics** | Advanced health widgets utilizing Thymeleaf aggregation to forecast university capacity demands. | `Strict` |
| 🕵️‍♂️ **Security Audit Feed** | Comprehensive system log monitor with token-based event tracking for institutional integrity. | `Strict` |

<br>

## 🧠 Core Algorithms & Architecture

This project strictly adheres to academic requirements by implementing complex Data Structures and Algorithms manually in the backend:

*   **`Insertion Sort` for Auditing**: When registration requests are loaded from `.txt` files, they are chronologically ordered using a manual implementation of the **Insertion Sort** algorithm to ensure fairness in staff oversight.
*   **`FIFO Queue` for Waitlists**: If a module reaches its maximum capacity, new students are automatically enqueued. The system assigns a sequential `queuePosition` to maintain a strict First-In-First-Out priority.
*   **`Binary Search` for Logs**: Optimized for rapid retrieval of specific security signals from massive `.txt` log files using $O(\log n)$ time complexity logic.
*   **`Database-Less I/O`**: Complete data persistence using high-speed Java File Handling (`BufferedReader` / `BufferedWriter`) with `StandardOpenOption.SYNC` for immediate hardware-level disk flushing.

<br>

## 👥 Meet the Team

This project was collaboratively developed by our dedicated team of engineering students. Every member owned a specific "Vertical Slice," managing both Backend logic and Frontend UI.

| Student ID | Member Name | Primary Role & Contribution |
| :--- | :--- | :--- |
| `IT25xxxxxx` | **Member 1: Umer** | **Enrollment Management**: Real-time capacity guards & course swap logic. |
| `IT25xxxxxx` | **Member 2: Weerasekara** | **Academic Profile**: Soft-delete patterns & click-to-edit interactions. |
| `IT25xxxxxx` | **Member 3: Kallora** | **Waitlist Control**: FIFO priority engines & sequential re-indexing. |
| `IT25xxxxxx` | **Member 4: Arachchi** | **Request Auditing**: Manual Insertion Sort & batch processing FAB. |
| `IT25xxxxxx` | **Member 5: Jayathilaka** | **Curriculum Infrastructure**: DTO-based provisioning & health widgets. |
| `IT25xxxxxx` | **Member 6: Malaviarachchi** | **Security Hub**: Factory Design Pattern & Real-time signal monitor. |

<br>

## 🛠️ Technology Stack

<p align="center">
  <img src="https://skillicons.dev/icons?i=java,spring,maven,html,css,js,bootstrap,idea,git,github" />
</p>

*   **Backend**: Java 17 (LTS), **Spring Boot 3.4.1**, **Lombok** (Encapsulation).
*   **Frontend**: **Thymeleaf 3.1**, Bootstrap 5.3, Custom CSS Mesh Gradients.
*   **Persistence**: Custom Flat-File Database (`.txt`), Thread-Safe **ReadWriteLocks**.
*   **Architecture**: N-Tier (Controller -> Service -> Repository), Factory Pattern, Singleton Pattern.

<br>

## 📂 Submission Documentation

To facilitate individual grading during the Viva, the repository is organized into specialized folders:
*   📁 **`Member_Parts/`**: Contains individual reports and code files for all 6 members.
*   📁 **`VIVA_PREP/`**: Exhaustive Q&A guides categorized from Basic to Advanced for the final presentation.
*   📁 **`Documentation/`**: Includes the comprehensive **Complete OOP Class Diagram**.

<br>

## 🚀 Getting Started

1.  **Extract** the project folder.
2.  **Run in Terminal**:
    ```bash
    ./mvnw clean compile spring-boot:run
    ```
3.  **Access URL**: `http://localhost:8081`

<br>

<div align="center">
  <p>© 2026 EduAll Systems | Professional Academic Architecture</p>
</div>

