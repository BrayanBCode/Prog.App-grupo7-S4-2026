<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio de Sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body class="centrado">

    <div class="login-card">
        <h2>Iniciar Sesión</h2>

        <!-- Mensaje de advertencia para credenciales inválidas (login.js lo muestra con ?error=true) -->
        <div id="error-message" class="alert-danger hidden" role="alert">
            Nickname/Email o contraseña incorrectos. Por favor, reingrese sus datos.
        </div>

        <form id="login-form" action="${pageContext.request.contextPath}/login" method="POST">

            <!-- Identificador: Nickname o Email -->
            <div class="form-group">
                <label for="usuario">Nickname o Correo Electrónico</label>
                <input
                    type="text"
                    id="usuario"
                    name="usuario"
                    placeholder="Ej. jperez o usuario@correo.com"
                    required
                    autocomplete="username"
                >
            </div>

            <!-- Contraseña -->
            <div class="form-group">
                <label for="password">Contraseña</label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Ingrese su contraseña"
                    required
                    autocomplete="current-password"
                >
            </div>

            <!-- Botones de Acción -->
            <div class="button-group">
                <button type="submit" class="btn btn-primary">Ingresar</button>
                <a href="${pageContext.request.contextPath}/inicio" class="btn btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>

    <script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html>
