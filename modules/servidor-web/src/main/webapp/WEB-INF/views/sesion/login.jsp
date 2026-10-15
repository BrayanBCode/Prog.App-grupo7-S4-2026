<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Iniciar sesión"/>
<c:set var="formularioSesion" value="${true}"/>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Inicio de Sesion --%>
<div class="mx-auto w-100" style="max-width: 420px;">
    <h1 class="h3 mb-4">Iniciar sesión</h1>

    <!-- Credenciales invalidas: login.js lo muestra con ?error=true -->
    <div id="error-message" class="alert alert-danger d-none" role="alert">
        Nickname/Email o contraseña incorrectos. Por favor, reingrese sus datos.
    </div>

    <form id="login-form" action="${raiz}/login" method="POST">
        <div class="mb-3">
            <label for="usuario" class="form-label">Nickname o correo electrónico</label>
            <input type="text" id="usuario" name="usuario" class="form-control"
                   placeholder="Ej. jperez o usuario@correo.com" required autocomplete="username">
        </div>
        <div class="mb-4">
            <label for="password" class="form-label">Contraseña</label>
            <input type="password" id="password" name="password" class="form-control"
                   placeholder="Ingrese su contraseña" required autocomplete="current-password">
        </div>
        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary flex-fill">Ingresar</button>
            <a href="${raiz}/inicio" class="btn btn-outline-secondary flex-fill">Cancelar</a>
        </div>
    </form>
</div>

<script src="${raiz}/js/login.js"></script>
<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
