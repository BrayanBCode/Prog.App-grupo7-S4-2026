<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<c:set var="titulo" value="Usuarios"/>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<h1>Usuarios</h1>
<p>Elegí un usuario para ver su perfil.</p>

<div class="tarjetas">
    <c:forEach var="u" items="${usuarios}">
        <c:url var="url" value="/usuarios"><c:param name="nick" value="${u.nickname()}"/></c:url>
        <a class="tarjeta tarjeta-usuario" href="${url}">
            <e:imagen ruta="${u.imagen()}" alt="${u.nickname()}" clase="avatar"/>
            <span class="tarjeta-cuerpo">
                <strong><c:out value="${u.nickname()}"/></strong>
                <span class="tenue"><c:out value="${u.nombre()} ${u.apellido()}"/></span>
            </span>
        </a>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
