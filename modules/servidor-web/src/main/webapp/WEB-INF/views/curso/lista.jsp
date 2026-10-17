<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Consulta de Curso, paso 1: el usuario eligio un instituto en el lateral y ve sus cursos. --%>
<c:choose>
    <c:when test="${empty instituto}">
        <h1 class="h3 mb-1">Cursos</h1>
        <p class="text-body-secondary mb-0">Elegí un instituto en el menú de la izquierda para ver sus cursos.</p>
    </c:when>
    <c:otherwise>
        <div class="d-flex justify-content-between align-items-start gap-3">
            <h1 class="h3 mb-1">Cursos de <c:out value="${instituto}"/></h1>
            <c:if test="${puede.ALTA_CURSO}">
                <c:url var="uAlta" value="/curso/alta"><c:param name="instituto" value="${instituto}"/></c:url>
                <a class="btn btn-outline-primary btn-sm text-nowrap" href="${uAlta}">+ Crear curso</a>
            </c:if>
        </div>
        <p class="text-body-secondary mb-4">Elegí un curso para ver sus datos, ediciones y programas.</p>

        <c:if test="${empty cursos}">
            <p class="text-body-secondary">Este instituto todavía no tiene cursos.</p>
        </c:if>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-xl-3 g-3">
            <c:forEach var="cur" items="${cursos}">
                <c:url var="url" value="/cursos"><c:param name="nombre" value="${cur.nombre()}"/></c:url>
                <div class="col">
                    <div class="card h-100 shadow-sm">
                        <e:imagen ruta="${cur.imagen()}" alt="${cur.nombre()}" clase="card-img-top img-tarjeta"/>
                        <div class="card-body">
                            <%-- stretched-link hace clickeable toda la tarjeta --%>
                            <a class="stretched-link fw-semibold text-decoration-none" href="${url}"><c:out value="${cur.nombre()}"/></a>
                            <p class="small text-body-secondary mb-0 mt-1 recorte"><c:out value="${cur.descripcion()}"/></p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
