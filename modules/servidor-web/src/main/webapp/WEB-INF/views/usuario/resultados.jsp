<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<h1>Resultados de mis inscripciones</h1>

<c:if test="${empty inscripciones}"><p class="tenue">Todavía no te inscribiste a ninguna edición.</p></c:if>
<c:if test="${not empty inscripciones}">
    <table class="tabla">
        <thead><tr><th>Edición</th><th>Curso</th><th>Fecha de inscripción</th><th>Estado</th></tr></thead>
        <tbody>
        <c:forEach var="i" items="${inscripciones}">
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
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
