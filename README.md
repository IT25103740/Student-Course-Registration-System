# SLIIT Module Registration & Waitlist Management System
  
  **An Advanced, Database-Less University Portal Powered by Custom Data Structures**

  [![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://java.com)
  [![JSP](https://img.shields.io/badge/JSP-007396?style=for-the-badge&logo=java&logoColor=white)](https://java.com)
  [![Bootstrap](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com)
  [![Security](https://img.shields.io/badge/Security-Advanced_Auth-red?style=for-the-badge&logo=shield)](https://github.com)
  [![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)](https://github.com)

</div>

<br>

## 🚀 Project Overview

Welcome to the **EduAll Central Registration Portal**. This enterprise-level web application is designed to handle high-volume student module enrollments, dynamic capacity forecasting, and automated waitlist management. 

Instead of relying on a traditional relational database, this system showcases core computer science concepts by utilizing **Advanced File I/O**, **FIFO Queues**, **Insertion Sort**, and **Binary Search Algorithms** to ensure persistent data storage and real-time processing.

<br>

<div align="center">
  <img src="https://media.giphy.com/media/SWoSkN6DxTszqIKEqv/giphy.gif" width="600px" style="border-radius:10px;">
</div>

<br>

## 💎 Premium Features

| Feature | Description | Access Level |
| :--- | :--- | :--- |
| 👨‍🎓 **Student Portal** | Direct enrollment gateway for students to select modules and view real-time queue status. | `Public` |
| 🛡️ **Secure Admin Console** | Staff authentication gateway utilizing `SweetAlert2` for dynamic security validation. | `Protected` |
| 📡 **Live Waitlist Radar** | Real-time monitoring of module capacities. It dynamically handles seat allocation using **FIFO Queues**. | `Protected` |
| 📊 **HOD Analytics** | Advanced predictive dashboard utilizing `Chart.js` to forecast next-semester capacity demands. | `Strict` |
| 🕵️‍♂️ **System Audit & Search** | Comprehensive system log tracking with **Binary Search** implementation for rapid student record retrieval. | `Strict` |

<br>

## 🧠 Core Algorithms & Architecture

This project strictly adheres to SLIIT's academic requirements by implementing complex Data Structures and Algorithms:

* **`Insertion Sort` for Admin Queue:** When student requests are loaded from `.txt` files, they are chronologically sorted using the Insertion Sort algorithm before being displayed on the Admin Dashboard to ensure fairness.
* **`FIFO Queue` for Waitlists:** If a module reaches its maximum capacity, new students are automatically enqueued. When an admin increases capacity, the system automatically dequeues and approves students based on First-In-First-Out principles.
* **`Binary Search` for Audit Trails:** Enables system administrators to quickly locate specific student logs from massive `.txt` data files using O(log n) time complexity.
* **`Database-Less I/O`:** Complete data persistence using high-speed Java File Handling (`BufferedReader` / `BufferedWriter`) across multiple text-based flat files.

<br>

## 👥 Meet the Team (Group 0X)

This project was collaboratively developed by our dedicated team of engineering students. Every member contributed to both Frontend (UI/UX) and Backend (Java OOP & Data Structures).

| Student ID | Primary Role & Contribution |
| :--- | :--- |
| `IT25xxxxxx` | System Architecture, Secure UI/UX & HOD Analytics |
| `IT25xxxxxx` | Student Portal & Enrollment Logic Development |
| `IT25xxxxxx` | Admin Console & Insertion Sort Implementation |
| `IT25xxxxxx` | Waitlist Radar & FIFO Queue Data Structures |
| `IT25xxxxxx` | System Audit Trail, Search Algorithms & Chart.js Integration |
| `IT25xxxxxx` | Core File I/O Handling, Backend Controllers & Text Database |

<br>

## 🛠️ Technology Stack

<p align="center">
  <img src="https://skillicons.dev/icons?i=java,html,css,js,bootstrap,idea,git,github" />
</p>

* **Frontend:** HTML5, CSS3, Bootstrap 5, Chart.js (Data Visualization)
* **Backend:** Core Java (JDK 17+), JavaServer Pages (JSP), Servlets
* **Logic:** Custom Utility Classes, Arrays, Queues, Searching & Sorting Algorithms
* **UI/UX Additions:** SweetAlert2 (Secure Modals), FontAwesome, Eduall Premium Styling

<br>

---
<div align="center">
  <p><b>Developed with ❤️ for the SLIIT Semester Evaluation</b></p>
  <img src="https://img.shields.io/badge/SLIIT-Faculty_of_Computing-002147?style=flat-square">
</div>
