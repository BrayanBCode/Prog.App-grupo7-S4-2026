<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="valor" required="false" %>
<%-- Muestra una fecha del central (texto ISO yyyy-MM-dd) como dd/MM/yyyy. --%>
<%= valor == null || valor.isBlank() ? "-" : java.time.LocalDate.parse(valor).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %>
