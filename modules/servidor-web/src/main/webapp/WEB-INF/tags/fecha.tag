<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="valor" required="false" type="java.time.LocalDate" %>
<%-- Muestra una LocalDate como dd/MM/yyyy (JSTL fmt no sabe formatear java.time). --%>
<%= valor == null ? "-" : valor.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %>
