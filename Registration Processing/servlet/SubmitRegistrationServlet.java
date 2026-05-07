package com.group.registration.servlet;

import com.group.registration.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/submitRegistration")
public class SubmitRegistrationServlet extends HttpServlet {
    private RegistrationService service = new RegistrationService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String studentId = req.getParameter("studentId");
        String[] modules = req.getParameterValues("modules");

        if (studentId == null || modules == null || modules.length == 0) {
            resp.sendRedirect("registerModules.jsp?error=1");
            return;
        }

        List<String> moduleList = Arrays.asList(modules);
        String requestId = service.submitRegistration(studentId, moduleList);

        resp.sendRedirect("viewStatus.jsp?studentId=" + studentId);
    }
}