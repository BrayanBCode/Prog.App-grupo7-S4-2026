<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Ver resultados de evaluaciones (Tarea 3): acta de una edicion cerrada, con los estudiantes que participaron
     (aceptados) y la nota de cada uno. Solo lectura. --%>
<c:set var="det" value="${acta.edicion}"/>
<%@ include file="/WEB-INF/views/edicion/encabezado.jspf" %>

<h2 class="h6 fw-bold">Acta de resultados <span class="badge text-bg-secondary ms-1">Edición cerrada</span></h2>
<c:if test="${empty acta.inscripciones}">
    <p class="text-body-secondary">La edición no tuvo estudiantes aceptados.</p>
</c:if>
<c:if test="${not empty acta.inscripciones}">
    <div class="table-responsive" style="max-width: 760px;">
        <table class="table table-sm align-middle">
            <thead class="table-light"><tr><th>Estudiante</th><th>Nombre</th><th class="text-end">Nota (1 a 12)</th></tr></thead>
            <tbody>
            <c:forEach var="i" items="${acta.inscripciones}">
                <c:url var="uEst" value="/usuarios"><c:param name="nick" value="${i.nickname}"/></c:url>
                <tr>
                    <td><a href="${uEst}"><c:out value="${i.nickname}"/></a></td>
                    <td><c:out value="${i.nombre} ${i.apellido}"/></td>
                    <td class="text-end fw-semibold"><c:out value="${i.nota}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <p class="form-text">${acta.inscripciones.size()} estudiante(s) en el acta.</p>
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
