<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Busqueda (requerimiento 7.5): resultados del cuadro del cabezal, con las mismas tarjetas que la lista de
     cursos de un instituto (curso/lista.jsp). Cada tarjeta lleva a la Consulta de Curso. --%>
<c:choose>
    <c:when test="${empty busqueda}">
        <h1 class="h3 mb-1">Todos los cursos</h1>
    </c:when>
    <c:otherwise>
        <h1 class="h3 mb-1">Resultados para “<c:out value="${busqueda}"/>”</h1>
    </c:otherwise>
</c:choose>
<c:if test="${empty error}">
    <c:choose>
        <c:when test="${empty cursos}">
            <p class="text-body-secondary">No hay cursos que coincidan con la búsqueda.</p>
        </c:when>
        <c:otherwise>
            <p class="text-body-secondary mb-4">
                ${cursos.size()} ${cursos.size() == 1 ? 'curso' : 'cursos'}<c:if test="${not empty busqueda}"> con el texto en el nombre o la descripción</c:if>.
            </p>
        </c:otherwise>
    </c:choose>
</c:if>

<div class="row row-cols-1 row-cols-sm-2 row-cols-xl-3 g-3">
    <c:forEach var="cur" items="${cursos}">
        <c:url var="url" value="/cursos"><c:param name="nombre" value="${cur.nombre()}"/></c:url>
        <div class="col">
            <div class="card h-100 shadow-sm">
                <e:imagen ruta="${cur.imagen()}" alt="${cur.nombre()}" clase="card-img-top img-tarjeta"/>
                <div class="card-body">
                    <a class="stretched-link fw-semibold text-decoration-none" href="${url}"><c:out value="${cur.nombre()}"/></a>
                    <p class="small text-body-secondary mb-0 mt-1 recorte"><c:out value="${cur.descripcion()}"/></p>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
