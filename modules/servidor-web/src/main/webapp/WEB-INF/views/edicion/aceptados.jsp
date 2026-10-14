<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<h1>Estudiantes aceptados</h1>

<c:choose>
    <c:when test="${not empty edicion}">
        <c:url var="uEd" value="/ediciones"><c:param name="nombre" value="${edicion.nombre()}"/></c:url>
        <h2><a href="${uEd}"><c:out value="${edicion.nombre()}"/></a></h2>
        <p class="tenue"><e:fecha valor="${edicion.fechaInicio()}"/> al <e:fecha valor="${edicion.fechaFin()}"/></p>
        <c:if test="${aceptados != null}">
            <c:if test="${empty aceptados}"><p class="tenue">Todavía no hay inscripciones aceptadas.</p></c:if>
            <c:if test="${not empty aceptados}">
                <table class="tabla">
                    <thead><tr><th>Estudiante</th><th>Nickname</th><th>Correo</th><th>Fecha de inscripción</th></tr></thead>
                    <tbody>
                    <c:forEach var="i" items="${aceptados}">
                        <c:url var="uEst" value="/usuarios"><c:param name="nick" value="${i.nickname()}"/></c:url>
                        <tr>
                            <td><c:out value="${i.nombre()} ${i.apellido()}"/></td>
                            <td><a href="${uEst}"><c:out value="${i.nickname()}"/></a></td>
                            <td><c:out value="${i.mail()}"/></td>
                            <td><e:fecha valor="${i.fechaInscripcion()}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </c:if>
    </c:when>
    <c:when test="${not empty curso}">
        <p>Ediciones de <strong><c:out value="${curso}"/></strong>:
            <a href="${raiz}/edicion/aceptados" class="tenue">(cambiar)</a></p>
        <c:if test="${empty ediciones}"><p class="tenue">El curso no tiene ediciones.</p></c:if>
        <table class="tabla">
            <thead><tr><th>Edición</th><th>Inicio</th><th>Fin</th><th>Cupo</th></tr></thead>
            <tbody>
            <c:forEach var="ed" items="${ediciones}">
                <c:url var="u" value="/edicion/aceptados"><c:param name="edicion" value="${ed.nombre()}"/></c:url>
                <tr>
                    <td><a href="${u}"><c:out value="${ed.nombre()}"/></a></td>
                    <td><e:fecha valor="${ed.fechaInicio()}"/></td>
                    <td><e:fecha valor="${ed.fechaFin()}"/></td>
                    <td><c:out value="${ed.cupo() > 0 ? ed.cupo() : 'Sin límite'}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <%@ include file="/WEB-INF/views/comun/elegir-curso.jspf" %>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
