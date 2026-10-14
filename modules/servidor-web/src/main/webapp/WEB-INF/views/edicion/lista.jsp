<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<h1>Ediciones de cursos</h1>

<c:choose>
    <c:when test="${not empty curso}">
        <c:url var="uCurso" value="/cursos"><c:param name="nombre" value="${curso}"/></c:url>
        <p>Ediciones de <a href="${uCurso}"><c:out value="${curso}"/></a>:</p>
        <c:if test="${empty ediciones}"><p class="tenue">El curso todavía no tiene ediciones.</p></c:if>
        <div class="tarjetas">
            <c:forEach var="ed" items="${ediciones}">
                <c:url var="u" value="/ediciones"><c:param name="nombre" value="${ed.nombre()}"/></c:url>
                <a class="tarjeta" href="${u}">
                    <e:imagen ruta="${ed.imagen()}" alt="${ed.nombre()}" clase="tarjeta-img"/>
                    <span class="tarjeta-cuerpo">
                        <strong><c:out value="${ed.nombre()}"/></strong>
                        <span class="tenue"><e:fecha valor="${ed.fechaInicio()}"/> al <e:fecha valor="${ed.fechaFin()}"/></span>
                    </span>
                </a>
            </c:forEach>
        </div>
    </c:when>
    <c:when test="${not empty filtro}">
        <p>Cursos de <strong><c:out value="${filtro}"/></strong>. Elegí uno para ver sus ediciones.</p>
        <c:if test="${empty cursos}"><p class="tenue">No hay cursos asociados.</p></c:if>
        <ul class="lista">
            <c:forEach var="cur" items="${cursos}">
                <c:url var="u" value="/ediciones"><c:param name="curso" value="${cur}"/></c:url>
                <li><a href="${u}"><c:out value="${cur}"/></a></li>
            </c:forEach>
        </ul>
    </c:when>
    <c:otherwise>
        <p>Elegí el instituto que brinda el curso.</p>
        <h2>Institutos</h2>
        <div class="chips">
            <c:forEach var="inst" items="${institutosMenu}">
                <c:url var="u" value="/ediciones"><c:param name="instituto" value="${inst}"/></c:url>
                <a class="chip" href="${u}"><c:out value="${inst}"/></a>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
