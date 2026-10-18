<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Listar resultados de inscripciones (4.9, Estudiante): todas sus inscripciones a ediciones con su estado,
     de la mas nueva a la mas vieja. --%>
<h1 class="h3 mb-1">Resultados de mis inscripciones</h1>
<p class="text-body-secondary mb-4">Estado de cada inscripción a una edición de curso: Inscripto (todavía sin
    selección), Aceptada o Rechazada.</p>

<c:if test="${empty error and empty resultados}">
    <p class="text-body-secondary">Todavía no te inscribiste a ninguna edición. Elegí un instituto en el menú para ver
        sus cursos y ediciones.</p>
</c:if>
<c:if test="${not empty resultados}">
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Edición</th><th>Curso</th><th>Fecha de inscripción</th><th>Estado</th></tr></thead>
            <tbody>
            <c:forEach var="i" items="${resultados}">
                <c:url var="uEd" value="/ediciones"><c:param name="nombre" value="${i.edicion()}"/></c:url>
                <c:url var="uCur" value="/cursos"><c:param name="nombre" value="${i.curso()}"/></c:url>
                <tr>
                    <td><a href="${uEd}"><c:out value="${i.edicion()}"/></a></td>
                    <td><a href="${uCur}"><c:out value="${i.curso()}"/></a></td>
                    <td><e:fecha valor="${i.fechaInscripcion()}"/></td>
                    <td><e:estado valor="${i.estado()}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
