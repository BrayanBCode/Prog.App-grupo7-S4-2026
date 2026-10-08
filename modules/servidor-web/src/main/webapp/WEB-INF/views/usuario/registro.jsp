<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrarse - edExt</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<%@ include file="/WEB-INF/views/comun/cabezal.jspf" %>

<main class="contenido">
  <div class="login-card ancha">
    <h2>Registrarse</h2>

    <c:if test="${not empty error}">
        <div class="alert-danger" role="alert"><c:out value="${error}"/></div>
    </c:if>
    <div id="error-cliente" class="alert-danger hidden" role="alert"></div>

    <form id="registro-form" action="${pageContext.request.contextPath}/registro"
          method="POST" enctype="multipart/form-data">

        <div class="form-group">
            <label for="nickname">Nickname *</label>
            <input type="text" id="nickname" name="nickname" required maxlength="30"
                   value="<c:out value='${nickname}'/>">
        </div>
        <div class="form-group">
            <label for="nombre">Nombre *</label>
            <input type="text" id="nombre" name="nombre" required value="<c:out value='${nombre}'/>">
        </div>
        <div class="form-group">
            <label for="apellido">Apellido *</label>
            <input type="text" id="apellido" name="apellido" required value="<c:out value='${apellido}'/>">
        </div>
        <div class="form-group">
            <label for="mail">Correo electrónico *</label>
            <input type="email" id="mail" name="mail" required value="<c:out value='${mail}'/>">
        </div>
        <div class="form-group">
            <label for="fechaNac">Fecha de nacimiento *</label>
            <input type="date" id="fechaNac" name="fechaNac" required value="<c:out value='${fechaNac}'/>">
        </div>
        <div class="form-group">
            <label for="password">Contraseña *</label>
            <input type="password" id="password" name="password" required autocomplete="new-password">
        </div>
        <div class="form-group">
            <label for="confirmar">Confirmar contraseña *</label>
            <input type="password" id="confirmar" name="confirmar" required autocomplete="new-password">
        </div>

        <div class="form-group">
            <label for="tipo">Tipo de usuario *</label>
            <select id="tipo" name="tipo">
                <option value="ESTUDIANTE" ${tipo != 'DOCENTE' ? 'selected' : ''}>Estudiante</option>
                <option value="DOCENTE"    ${tipo == 'DOCENTE' ? 'selected' : ''}>Docente</option>
            </select>
        </div>
        <div class="form-group hidden" id="grupo-instituto">
            <label for="instituto">Instituto *</label>
            <select id="instituto" name="instituto">
                <option value="">-- Seleccioná --</option>
                <c:forEach var="i" items="${institutos}">
                    <option value="<c:out value='${i}'/>" ${i == institutoElegido ? 'selected' : ''}>
                        <c:out value="${i}"/>
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="imagen">Imagen (opcional, JPG o PNG)</label>
            <input type="file" id="imagen" name="imagen" accept=".jpg,.jpeg,.png">
        </div>

        <div class="button-group">
            <button type="submit" class="btn btn-primary">Registrarme</button>
            <a href="${pageContext.request.contextPath}/inicio" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
  </div>
</main>

<script src="${pageContext.request.contextPath}/js/registro.js"></script>
</body>
</html>