<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Modificar Datos de Usuario (4.11). Los datos que se editan son los de los lapices de la Figura 6 (nombre,
     apellido y fecha de nacimiento); nickname y correo se muestran pero no se modifican. El error del servidor
     (${error}) lo muestra el marco. --%>
<div class="w-100" style="max-width: 560px;">
    <h1 class="h3 mb-4">Modificar mis datos</h1>

    <form action="${raiz}/usuarios/modificar" method="POST">
        <div class="row g-3 mb-3">
            <div class="col-sm-6">
                <label for="nickname" class="form-label">Nickname</label>
                <input type="text" id="nickname" class="form-control" value="<c:out value='${usuarioActual.nickname}'/>" disabled>
            </div>
            <div class="col-sm-6">
                <label for="mail" class="form-label">Correo electrónico</label>
                <input type="email" id="mail" class="form-control" value="<c:out value='${usuarioActual.mail}'/>" disabled>
            </div>
        </div>
        <div class="row g-3 mb-3">
            <div class="col-sm-6">
                <label for="nombre" class="form-label">Nombre *</label>
                <input type="text" id="nombre" name="nombre" class="form-control" required autofocus value="<c:out value='${nombre}'/>">
            </div>
            <div class="col-sm-6">
                <label for="apellido" class="form-label">Apellido *</label>
                <input type="text" id="apellido" name="apellido" class="form-control" required value="<c:out value='${apellido}'/>">
            </div>
        </div>
        <div class="mb-4">
            <label for="fechaNac" class="form-label">Fecha de nacimiento *</label>
            <input type="date" id="fechaNac" name="fechaNac" class="form-control" required value="<c:out value='${fechaNac}'/>">
        </div>

        <c:url var="uPerfil" value="/usuarios"><c:param name="nick" value="${usuarioActual.nickname}"/></c:url>
        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">Guardar</button>
            <a class="btn btn-outline-secondary" href="${uPerfil}">Cancelar</a>
        </div>
    </form>
</div>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
