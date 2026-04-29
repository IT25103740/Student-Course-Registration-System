package servlet;

import model.Module;
import service.ModuleManager;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AdminServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String module = req.getParameter("module");
        int seats = Integer.parseInt(req.getParameter("seats"));

        Module m = ModuleManager.getModule(module);
        m.addSeats(seats);

        res.sendRedirect("dashboard.jsp");
    }
}