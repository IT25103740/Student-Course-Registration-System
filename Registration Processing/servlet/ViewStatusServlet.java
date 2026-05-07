package com.group.registration.servlet;

import com.group.registration.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/viewStatus")
public class ViewStatusServlet extends HttpServlet {
    private RegistrationService service = new RegistrationService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String studentId = req.getParameter("studentId");
        if (studentId == null || studentId.isEmpty()) {
            studentId = "STU001"; // Default for testing
        }

        req.setAttribute("registrations", service.getRegistrationsByStudent(studentId));
        req.getRequestDispatcher("viewStatus.jsp").forward(req, resp);
    }
}