<%@ page import="service.ModuleManager, model.Module" %>

<html>
<body>

<h2>System Dashboard</h2>

<%
    for (Module m : ModuleManager.getAllModules().values()) {
%>

<h3><%= m.getName() %></h3>
<p>Capacity: <%= m.getCapacity() %></p>
<p>Enrolled: <%= m.getEnrolled() %></p>
<p>Waitlist: <%= m.getWaitlistCount() %></p>

<hr>

<%
    }
%>

</body>
</html>