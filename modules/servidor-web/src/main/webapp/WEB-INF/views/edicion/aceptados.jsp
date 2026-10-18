<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Listar aceptados a una edicion de curso: encabezado de la edicion (como la Figura 4) y las inscripciones
     en estado Aceptada. --%>
<c:set var="det" value="${aceptados.edicion()}"/>
<%@ include file="/WEB-INF/views/edicion/encabezado.jspf" %>

<h2 class="h6 fw-bold">Estudiantes aceptados (${aceptados.inscripciones().size()})</h2>
<c:if test="${empty aceptados.inscripciones()}">
    <p class="text-body-secondary">Todavía no hay estudiantes aceptados en esta edición.</p>
</c:if>
<c:if test="${not empty aceptados.inscripciones()}">
    <div class="table-responsive" style="max-width: 760px;">
        <table class="table table-sm align-middle">
            <thead class="table-light"><tr><th>Estudiante</th><th>Nombre</th><th>Fecha inscripción</th></tr></thead>
            <tbody>
            <c:forEach var="i" items="${aceptados.inscripciones()}">
                <c:url var="uEst" value="/usuarios"><c:param name="nick" value="${i.nickname()}"/></c:url>
                <tr>
                    <td><a href="${uEst}"><c:out value="${i.nickname()}"/></a></td>
                    <td><c:out value="${i.nombre()} ${i.apellido()}"/></td>
                    <td><e:fecha valor="${i.fechaInscripcion()}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
