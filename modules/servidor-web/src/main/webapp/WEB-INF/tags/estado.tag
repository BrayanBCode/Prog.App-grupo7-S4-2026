<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="valor" required="true" type="ServidorCentral.Logica.entities.cursos.EstadoInscripcion" %>
<%-- Etiqueta de color para el estado de una inscripcion (Inscripto / Aceptada / Rechazada). --%>
<span class="badge ${valor == 'ACEPTADA' ? 'text-bg-success' : valor == 'RECHAZADA' ? 'text-bg-danger' : 'text-bg-warning'}">${valor.etiqueta}</span>
