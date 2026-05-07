<%@ page import="service.ModuleService,model.Module,java.util.*" %>

<%
    ModuleService service = new ModuleService();
    List<Module> modules = service.getModules();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Module Management</title>
    <link rel="stylesheet" href="style.css">
</head>

<body>

<div class="dashboard">

    <!-- SIDEBAR -->
    <div class="sidebar">
        <h2>Uni System</h2>
        <ul>
            <li class="active">Modules</li>
            <li>Students</li>
            <li>Reports</li>
        </ul>
    </div>

    <!-- MAIN CONTENT -->
    <div class="main">

        <h1>Module & Capacity Management</h1>

        <!-- STATS -->
        <div class="cards">

            <div class="card">
                <h3>Total Modules</h3>
                <p><%= modules.size() %></p>
            </div>

            <div class="card">
                <h3>Full Modules</h3>
                <p>
                    <%
                        int full = 0;
                        for(Module m : modules){
                            if(m.getEnrolled() >= m.getCapacity()) full++;
                        }
                        out.print(full);
                    %>
                </p>
            </div>

            <div class="card">
                <h3>Available Seats</h3>
                <p>
                    <%
                        int seats = 0;
                        for(Module m : modules){
                            seats += (m.getCapacity() - m.getEnrolled());
                        }
                        out.print(seats);
                    %>
                </p>
            </div>

        </div>

        <!-- FORM -->
        <div class="form-section">

            <h2>Manage Module</h2>

            <form action="ModuleServlet" method="post">

                <input type="text" name="id" placeholder="Module ID" required>
                <input type="text" name="name" placeholder="Module Name" required>
                <input type="number" name="capacity" placeholder="Capacity" required>

                <div class="buttons">
                    <button name="action" value="add" class="btn add">Add</button>
                    <button name="action" value="update" class="btn update">Update</button>
                    <button name="action" value="delete" class="btn delete">Delete</button>
                </div>

            </form>

        </div>

        <!-- TABLE -->
        <div class="table-section">

            <h2>Module List</h2>

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Capacity</th>
                    <th>Enrolled</th>
                    <th>Status</th>
                </tr>
                </thead>

                <tbody>

                <% for(Module m : modules){ %>

                <tr onclick="fillForm('<%=m.getId()%>','<%=m.getName()%>','<%=m.getCapacity()%>')">

                    <td><%= m.getId() %></td>
                    <td><%= m.getName() %></td>
                    <td><%= m.getCapacity() %></td>
                    <td><%= m.getEnrolled() %></td>

                    <td>
                        <% if(m.getEnrolled() >= m.getCapacity()){ %>
                        <span class="badge full">FULL</span>
                        <% } else { %>
                        <span class="badge available">Available</span>
                        <% } %>
                    </td>

                </tr>

                <% } %>

                </tbody>
            </table>

        </div>

    </div>
</div>

<script>
    function fillForm(id, name, capacity){
        document.getElementsByName("id")[0].value = id;
        document.getElementsByName("name")[0].value = name;
        document.getElementsByName("capacity")[0].value = capacity;
    }
</script>

</body>
</html>