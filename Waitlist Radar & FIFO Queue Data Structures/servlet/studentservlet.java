package servlet;

import model.*;
import service.ModuleManager;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class StudentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String module = req.getParameter("module");

        Student s = new Student(id, name);

        Module m = ModuleManager.getModule(module);
        m.register(s);

        res.sendRedirect("dashboard.jsp");
    }
}