<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Inicio"/>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<c:choose>
    <c:when test="${rol.autenticado}">
        <h1>Hola, <c:out value="${usuarioActual.nombre}"/> <c:out value="${usuarioActual.apellido}"/></h1>
        <p>Sesión iniciada como <strong><c:out value="${rol.etiqueta}"/></strong>.
            Usá la flecha de arriba a la izquierda para ver todo lo que podés hacer.</p>
    </c:when>
    <c:otherwise>
        <h1>Bienvenido a edExt</h1>
        <p>Estás navegando como <strong>Visitante</strong>. Iniciá sesión para inscribirte a ediciones de cursos.</p>
    </c:otherwise>
</c:choose>

<div class="tarjetas accesos">
    <a class="tarjeta" href="${raiz}/cursos"><span class="tarjeta-cuerpo"><strong>Cursos</strong>
        <span class="tenue">Por instituto</span></span></a>
    <a class="tarjeta" href="${raiz}/ediciones"><span class="tarjeta-cuerpo"><strong>Ediciones</strong>
        <span class="tenue">Fechas, cupos y docentes</span></span></a>
    <a class="tarjeta" href="${raiz}/usuarios"><span class="tarjeta-cuerpo"><strong>Usuarios</strong>
        <span class="tenue">Estudiantes y docentes</span></span></a>
</div>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
