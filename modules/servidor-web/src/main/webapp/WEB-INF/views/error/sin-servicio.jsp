<%@ page pageEncoding="UTF-8" session="false" isELIgnored="false" import="ServidorWeb.ws.Central" %>
<%--
  Pagina de error (web.xml, error-page 500). Vive aparte del marco general (inicio-pagina.jspf) a proposito:
  ese marco usa datos de la base (institutos del lateral) y esta pagina no puede depender de ella.
  El status sigue siendo 500; solo cambia lo que ve el usuario. El motivo distingue si no responde el
  Servidor Central o si respondio con una falla (la base de datos caida).
--%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Servicio no disponible - edExt</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/webjars/bootstrap/5.3.3/css/bootstrap.min.css">
</head>
<body class="bg-body-tertiary">
<div class="container-xl my-5">
    <div class="card card-body p-4 mx-auto text-center" style="max-width: 36rem;">
        <h1 class="h3 text-primary">edExt</h1>
        <h2 class="h4 mt-3">El servicio no está disponible</h2>
        <p class="mb-4">En este momento no podemos atender tu peticion. Intentá nuevamente en unos minutos.</p>
        <p class="mb-4">(${Central.noResponde(requestScope['jakarta.servlet.error.exception']) ? 'Servidor Central no disponible' : 'Base de datos no disponible'})</p>
        <div><a class="btn btn-primary" href="${pageContext.request.contextPath}/inicio">Reintentar</a></div>
    </div>
</div>
</body>
</html>
