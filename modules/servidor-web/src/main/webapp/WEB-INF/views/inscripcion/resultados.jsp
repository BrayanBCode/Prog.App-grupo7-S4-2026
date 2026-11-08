<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Listar resultados de inscripciones (4.9, Estudiante): todas sus inscripciones a ediciones con su estado,
     de la mas nueva a la mas vieja. Debajo, "Listar resultados de evaluaciones" (Tarea 3): las ediciones cerradas
     con la nota obtenida (el central solo manda la nota si la edicion esta cerrada). --%>
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
                <c:url var="uEd" value="/ediciones"><c:param name="nombre" value="${i.edicion}"/></c:url>
                <c:url var="uCur" value="/cursos"><c:param name="nombre" value="${i.curso}"/></c:url>
                <tr>
                    <td><a href="${uEd}"><c:out value="${i.edicion}"/></a></td>
                    <td><a href="${uCur}"><c:out value="${i.curso}"/></a></td>
                    <td><e:fecha valor="${i.fechaInscripcion}"/></td>
                    <td><e:estado valor="${i.estado}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<c:set var="hayCerradas" value="${false}"/>
<c:forEach var="i" items="${resultados}">
    <c:if test="${i.cerrada and i.estado == 'ACEPTADA'}"><c:set var="hayCerradas" value="${true}"/></c:if>
</c:forEach>
<h2 class="h4 mt-4 mb-1">Resultados de evaluaciones</h2>
<p class="text-body-secondary mb-3">Nota de 1 a 12 en las ediciones que el docente ya cerró.</p>
<c:if test="${empty error and not hayCerradas}">
    <p class="text-body-secondary">Todavía no tenés ediciones cerradas con resultado.</p>
</c:if>
<c:if test="${hayCerradas}">
    <div class="table-responsive" style="max-width: 760px;">
        <table class="table table-hover align-middle">
            <thead class="table-light"><tr><th>Edición</th><th>Curso</th><th class="text-end">Nota</th></tr></thead>
            <tbody>
            <c:forEach var="i" items="${resultados}">
                <c:if test="${i.cerrada and i.estado == 'ACEPTADA'}">
                    <c:url var="uEd" value="/ediciones"><c:param name="nombre" value="${i.edicion}"/></c:url>
                    <c:url var="uCur" value="/cursos"><c:param name="nombre" value="${i.curso}"/></c:url>
                    <tr>
                        <td><a href="${uEd}"><c:out value="${i.edicion}"/></a></td>
                        <td><a href="${uCur}"><c:out value="${i.curso}"/></a></td>
                        <td class="text-end fw-semibold"><c:out value="${i.nota}"/></td>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
