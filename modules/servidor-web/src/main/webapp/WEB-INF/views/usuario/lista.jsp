<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<c:set var="titulo" value="Usuarios"/>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<h1 class="h3 mb-1">Usuarios</h1>
<p class="text-body-secondary mb-4">Elegí un usuario para ver su perfil.</p>

<%-- Grilla responsive: 1 columna en celular, 2 en tablet, 3 en escritorio --%>
<div class="row row-cols-1 row-cols-sm-2 row-cols-xl-3 g-3">
    <c:forEach var="u" items="${usuarios}">
        <c:url var="url" value="/usuarios"><c:param name="nick" value="${u.nickname()}"/></c:url>
        <div class="col">
            <div class="card h-100 shadow-sm">
                <div class="card-body d-flex align-items-center gap-3">
                    <e:imagen ruta="${u.imagen()}" alt="${u.nickname()}" clase="avatar avatar-md"/>
                    <div class="text-truncate">
                        <%-- stretched-link hace clickeable toda la tarjeta --%>
                        <a class="stretched-link fw-semibold text-decoration-none" href="${url}"><c:out value="${u.nickname()}"/></a>
                        <div class="small text-body-secondary text-truncate"><c:out value="${u.nombre()} ${u.apellido()}"/></div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
