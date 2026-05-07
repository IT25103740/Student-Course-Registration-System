package servlet;

import model.Module;
import service.ModuleService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class ModuleServlet extends HttpServlet {

    ModuleService service = new ModuleService();

    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("add".equals(action)) {

            service.addModule(new Module(
                    req.getParameter("id"),
                    req.getParameter("name"),
                    Integer.parseInt(req.getParameter("capacity")),
                    0
            ));
        }

        else if ("update".equals(action)) {

            service.updateModule(
                    req.getParameter("id"),
                    req.getParameter("name"),
                    Integer.parseInt(req.getParameter("capacity"))
            );
        }

        else if ("delete".equals(action)) {

            service.deleteModule(req.getParameter("id"));
        }

        res.sendRedirect("module.jsp");
    }
}