<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Inicio"/>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<c:choose>
    <c:when test="${rol.autenticado}">
        <h1 class="h3">Hola, <c:out value="${usuarioActual.nombre}"/> <c:out value="${usuarioActual.apellido}"/></h1>
        <p class="mb-0">Sesión iniciada como <strong><c:out value="${rol.etiqueta}"/></strong>.
            En el menú de la izquierda tenés todo lo que podés hacer.</p>
    </c:when>
    <c:otherwise>
        <h1 class="h3">Bienvenido a edExt</h1>
        <p class="mb-0">Estás navegando como <strong>Visitante</strong>.
            <a href="${raiz}/login">Iniciá sesión</a> para inscribirte a ediciones de cursos.</p>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
