<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edExt</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <%@ include file="/WEB-INF/views/comun/cabezal.jspf" %>

    <main class="contenido">
        <c:choose>
            <c:when test="${rol.autenticado}">
                <h1>Hola, <c:out value="${usuarioActual.nombre}"/> <c:out value="${usuarioActual.apellido}"/></h1>
                <p>Sesión iniciada como <strong><c:out value="${rol.etiqueta}"/></strong>.</p>
            </c:when>
            <c:otherwise>
                <h1>Bienvenido a edExt</h1>
                <p>Estás navegando como <strong>Visitante</strong>. Iniciá sesión para acceder a más funcionalidades.</p>
            </c:otherwise>
        </c:choose>
    </main>
</body>
</html>
