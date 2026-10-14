<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<h1><c:out value="${titulo}"/></h1>

<c:choose>
    <c:when test="${elegir}">
        <%-- Primer paso de Consulta de Curso: el usuario indica el instituto --%>
        <p>Elegí un instituto para ver sus cursos.</p>
        <h2>Institutos</h2>
        <div class="chips">
            <c:forEach var="inst" items="${institutosMenu}">
                <c:url var="u" value="/cursos"><c:param name="instituto" value="${inst}"/></c:url>
                <a class="chip" href="${u}"><c:out value="${inst}"/></a>
            </c:forEach>
        </div>
    </c:when>
    <c:otherwise>
        <h2>Cursos</h2>
        <c:if test="${empty cursos}"><p class="tenue">No hay cursos asociados.</p></c:if>
        <div class="tarjetas">
            <c:forEach var="cur" items="${cursos}">
                <c:url var="u" value="/cursos"><c:param name="nombre" value="${cur.nombre()}"/></c:url>
                <a class="tarjeta" href="${u}">
                    <e:imagen ruta="${cur.imagen()}" alt="${cur.nombre()}" clase="tarjeta-img"/>
                    <span class="tarjeta-cuerpo">
                        <strong><c:out value="${cur.nombre()}"/></strong>
                        <span class="tenue"><c:out value="${cur.instituto()}"/></span>
                        <span class="recorte"><c:out value="${cur.descripcion()}"/></span>
                    </span>
                </a>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
