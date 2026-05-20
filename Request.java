<%@ page import="service.RequestService" %>
<%@ page import="model.Request" %>
<%@ page import="java.util.*" %>

<%
    RequestService service = new RequestService();
    List<Request> requests = new ArrayList<>();

    try {
        requests = service.getSortedRequests();
    } catch (Exception e) {
        out.println("Error loading requests");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Request Dashboard</title>
    <link rel="stylesheet" href="requestStyle.css">
</head>

<body>

<div class="dashboard">

    <!-- SIDEBAR -->
    <div class="sidebar">
        <h2>Admin Panel</h2>

        <ul>
            <li class="active">Requests</li>
            <li>Modules</li>
            <li>Students</li>
        </ul>
    </div>

    <!-- MAIN -->
    <div class="main">

        <h1>Student Request Sorting & Approvals</h1>

        <!-- STAT CARDS -->
        <div class="cards">

            <div class="card">
                <h3>Total Requests</h3>
                <p><%= requests.size() %></p>
            </div>

            <div class="card">
                <h3>Pending</h3>
                <p>
                    <%
                        int pending = 0;

                        for(Request r : requests){
                            if(r.getStatus().equalsIgnoreCase("PENDING"))
                                pending++;
                        }

                        out.print(pending);
                    %>
                </p>
            </div>

            <div class="card">
                <h3>Processed</h3>
                <p>
                    <%
                        int processed = 0;

                        for(Request r : requests){
                            if(!r.getStatus().equalsIgnoreCase("PENDING"))
                                processed++;
                        }

                        out.print(processed);
                    %>
                </p>
            </div>

        </div>

        <!-- REQUEST TABLE -->
        <div class="table-section">

            <div class="table-header">
                <h2>Incoming Requests</h2>

                <form action="RequestServlet" method="post">
                    <button class="clear-btn"
                            name="action"
                            value="clear">
                        Clear Processed
                    </button>
                </form>
            </div>

            <table>

                <thead>
                <tr>
                    <th>Request ID</th>
                    <th>Student ID</th>
                    <th>Request Type</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>Action</th>
                </tr>
                </thead>

                <tbody>

                <% for(Request r : requests){ %>

                <tr>

                    <td><%= r.getRequestId() %></td>
                    <td><%= r.getStudentId() %></td>
                    <td><%= r.getRequestType() %></td>

                    <td>
                        <% if(r.getStatus().equalsIgnoreCase("PENDING")){ %>

                        <span class="pending">
                                PENDING
                            </span>

                        <% } else if(r.getStatus().equalsIgnoreCase("APPROVED")){ %>

                        <span class="approved">
                                APPROVED
                            </span>

                        <% } else { %>

                        <span class="rejected">
                                REJECTED
                            </span>

                        <% } %>
                    </td>

                    <td><%= r.getDate() %></td>

                    <td>

                        <% if(r.getStatus().equalsIgnoreCase("PENDING")){ %>

                        <form action="RequestServlet"
                              method="post"
                              class="action-form">

                            <input type="hidden"
                                   name="requestId"
                                   value="<%= r.getRequestId() %>">

                            <button class="approve-btn"
                                    name="action"
                                    value="approve">
                                Approve
                            </button>

                            <button class="reject-btn"
                                    name="action"
                                    value="reject">
                                Reject
                            </button>

                        </form>

                        <% } else { %>

                        Completed

                        <% } %>

                    </td>

                </tr>

                <% } %>

                </tbody>

            </table>

        </div>

    </div>

</div>

</body>
</html>