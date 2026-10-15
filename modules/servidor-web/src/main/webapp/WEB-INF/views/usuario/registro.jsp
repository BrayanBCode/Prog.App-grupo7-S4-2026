<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Registrarse"/>
<c:set var="formularioSesion" value="${true}"/>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Alta de Usuario. El error del servidor (${error}) lo muestra el marco. --%>
<div class="mx-auto w-100" style="max-width: 560px;">
    <h1 class="h3 mb-4">Registrarse</h1>

    <div id="error-cliente" class="alert alert-danger d-none" role="alert"></div>

    <form id="registro-form" action="${raiz}/registro" method="POST" enctype="multipart/form-data">
        <div class="mb-3">
            <label for="nickname" class="form-label">Nickname *</label>
            <input type="text" id="nickname" name="nickname" class="form-control" required maxlength="30"
                   value="<c:out value='${nickname}'/>">
        </div>
        <div class="row g-3 mb-3">
            <div class="col-sm-6">
                <label for="nombre" class="form-label">Nombre *</label>
                <input type="text" id="nombre" name="nombre" class="form-control" required value="<c:out value='${nombre}'/>">
            </div>
            <div class="col-sm-6">
                <label for="apellido" class="form-label">Apellido *</label>
                <input type="text" id="apellido" name="apellido" class="form-control" required value="<c:out value='${apellido}'/>">
            </div>
        </div>
        <div class="mb-3">
            <label for="mail" class="form-label">Correo electrónico *</label>
            <input type="email" id="mail" name="mail" class="form-control" required value="<c:out value='${mail}'/>">
        </div>
        <div class="mb-3">
            <label for="fechaNac" class="form-label">Fecha de nacimiento *</label>
            <input type="date" id="fechaNac" name="fechaNac" class="form-control" required value="<c:out value='${fechaNac}'/>">
        </div>
        <div class="row g-3 mb-3">
            <div class="col-sm-6">
                <label for="password" class="form-label">Contraseña *</label>
                <input type="password" id="password" name="password" class="form-control" required autocomplete="new-password">
            </div>
            <div class="col-sm-6">
                <label for="confirmar" class="form-label">Confirmar contraseña *</label>
                <input type="password" id="confirmar" name="confirmar" class="form-control" required autocomplete="new-password">
            </div>
        </div>

        <div class="mb-3">
            <label for="tipo" class="form-label">Tipo de usuario *</label>
            <select id="tipo" name="tipo" class="form-select">
                <option value="ESTUDIANTE" ${tipo != 'DOCENTE' ? 'selected' : ''}>Estudiante</option>
                <option value="DOCENTE"    ${tipo == 'DOCENTE' ? 'selected' : ''}>Docente</option>
            </select>
        </div>
        <div class="mb-3 d-none" id="grupo-instituto">
            <label for="instituto" class="form-label">Instituto *</label>
            <select id="instituto" name="instituto" class="form-select">
                <option value="">-- Seleccioná --</option>
                <c:forEach var="i" items="${institutos}">
                    <option value="<c:out value='${i}'/>" ${i == institutoElegido ? 'selected' : ''}>
                        <c:out value="${i}"/>
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-4">
            <label for="imagen" class="form-label">Imagen (opcional, JPG o PNG)</label>
            <input type="file" id="imagen" name="imagen" class="form-control" accept=".jpg,.jpeg,.png">
        </div>

        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary flex-fill">Registrarme</button>
            <a href="${raiz}/inicio" class="btn btn-outline-secondary flex-fill">Cancelar</a>
        </div>
    </form>
</div>

<script src="${raiz}/js/registro.js"></script>
<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
