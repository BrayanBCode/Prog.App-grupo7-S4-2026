<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Registrarse"/>
<c:set var="formularioSesion" value="${true}"/>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Alta de Usuario. El error del servidor (${error}) lo muestra el marco.
     Los errores de cada campo los marca registro.js debajo del campo (validacion de Bootstrap). --%>
<div class="mx-auto w-100" style="max-width: 560px;">
    <h1 class="h3 mb-4">Registrarse</h1>

    <form id="registro-form" action="${raiz}/registro" method="POST" enctype="multipart/form-data" novalidate
          data-disponible="${raiz}/registro/disponible">
        <div class="mb-3">
            <label for="nickname" class="form-label">Nickname *</label>
            <input type="text" id="nickname" name="nickname" class="form-control" required
                   pattern="[A-Za-z0-9_\-]{3,30}" maxlength="30" value="<c:out value='${nickname}'/>">
            <div class="invalid-feedback">Entre 3 y 30 caracteres: letras, números, _ o -.</div>
        </div>
        <div class="row g-3 mb-3">
            <div class="col-sm-6">
                <label for="nombre" class="form-label">Nombre *</label>
                <input type="text" id="nombre" name="nombre" class="form-control" required value="<c:out value='${nombre}'/>">
                <div class="invalid-feedback">Ingresá tu nombre.</div>
            </div>
            <div class="col-sm-6">
                <label for="apellido" class="form-label">Apellido *</label>
                <input type="text" id="apellido" name="apellido" class="form-control" required value="<c:out value='${apellido}'/>">
                <div class="invalid-feedback">Ingresá tu apellido.</div>
            </div>
        </div>
        <div class="mb-3">
            <label for="mail" class="form-label">Correo electrónico *</label>
            <input type="email" id="mail" name="mail" class="form-control" required value="<c:out value='${mail}'/>">
            <div class="invalid-feedback">Ingresá un correo válido (ej. usuario@correo.com).</div>
        </div>
        <div class="mb-3">
            <label for="fechaNac" class="form-label">Fecha de nacimiento *</label>
            <input type="date" id="fechaNac" name="fechaNac" class="form-control" required value="<c:out value='${fechaNac}'/>">
            <div class="invalid-feedback">Ingresá una fecha anterior a hoy.</div>
        </div>
        <div class="row g-3 mb-3">
            <div class="col-sm-6">
                <label for="password" class="form-label">Contraseña *</label>
                <input type="password" id="password" name="password" class="form-control" required autocomplete="new-password">
                <div class="invalid-feedback">Ingresá una contraseña.</div>
            </div>
            <div class="col-sm-6">
                <label for="confirmar" class="form-label">Confirmar contraseña *</label>
                <input type="password" id="confirmar" name="confirmar" class="form-control" required autocomplete="new-password">
                <div class="invalid-feedback">La contraseña y su confirmación no coinciden.</div>
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
            <div class="invalid-feedback">Un docente debe indicar su instituto.</div>
        </div>

        <div class="mb-4">
            <label for="imagen" class="form-label">Imagen (opcional, JPG o PNG, hasta 2 MB)</label>
            <input type="file" id="imagen" name="imagen" class="form-control" accept=".jpg,.jpeg,.png">
            <div class="invalid-feedback">La imagen debe ser JPG o PNG y pesar hasta 2 MB.</div>
        </div>

        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary flex-fill">Registrarme</button>
            <a href="${raiz}/inicio" class="btn btn-outline-secondary flex-fill">Cancelar</a>
        </div>
    </form>
</div>

<script src="${raiz}/js/registro.js"></script>
<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
