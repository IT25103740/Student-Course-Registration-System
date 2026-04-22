import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Maps this Servlet to the form submission action from index.jsp
@WebServlet("/LoginServlet") 
public class LoginServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Capture data submitted from the JSP form (Read inputs)
        String loginId = request.getParameter("userEmail"); 
        String loginPass = request.getParameter("userPassword");
        String targetPortal = request.getParameter("targetPortal"); // Identify which portal they are trying to access (Admin, HOD, Radar)

        // 2. Authenticate credentials against the text file using UserManager
        UserManager um = new UserManager();
        User loggedInUser = um.authenticateUser(loginId, loginPass);

        // 3. Determine login success or failure (Routing Logic)
        if (loggedInUser != null) {
            String role = loggedInUser.getRole();
            
            // Redirect user to the appropriate page based on their role and target portal
            if (role.equals("Admin") && (targetPortal.equals("admin") || targetPortal.equals("radar"))) {
                if(targetPortal.equals("admin")) response.sendRedirect("admin-dashboard.jsp");
                else response.sendRedirect("waitlist-status.jsp");
            } 
            else if (role.equals("Admin") && targetPortal.equals("hod")) {
                response.sendRedirect("hod-analytics.jsp"); // Grant access to the HOD portal
            } 
            else {
                // Block access and show warning if a Student tries to log into an administrative portal
                response.sendRedirect("index.jsp?error=unauthorized");
            }
        } else {
            // Redirect back to home with an error parameter if credentials (Email/Password) are incorrect
            response.sendRedirect("index.jsp?error=invalid");
        }
    }
}