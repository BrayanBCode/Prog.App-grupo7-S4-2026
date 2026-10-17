<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Alta de Curso (Docente). El error del central (${error}) lo muestra el marco; los de cada campo los
     marca el navegador con la validacion de Bootstrap (alta-curso.js). El docente es el que tiene la sesion. --%>
<div style="max-width: 640px;">
    <h1 class="h3 mb-1">Crear curso</h1>
    <p class="text-body-secondary mb-4">El curso queda a tu nombre como docente, con la fecha de hoy.</p>

    <form id="alta-curso-form" action="${raiz}/curso/alta" method="POST" enctype="multipart/form-data" novalidate>
        <div class="mb-3">
            <label for="instituto" class="form-label">Instituto que brinda el curso *</label>
            <select id="instituto" name="instituto" class="form-select" required>
                <option value="">-- Seleccioná --</option>
                <c:forEach var="i" items="${institutos}">
                    <option value="<c:out value='${i}'/>" ${i == institutoElegido ? 'selected' : ''}><c:out value="${i}"/></option>
                </c:forEach>
            </select>
            <div class="invalid-feedback">Elegí el instituto.</div>
        </div>
        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre *</label>
            <input type="text" id="nombre" name="nombre" class="form-control" required maxlength="200"
                   value="<c:out value='${param.nombre}'/>">
            <div class="invalid-feedback">Ingresá el nombre del curso.</div>
        </div>
        <div class="mb-3">
            <label for="descripcion" class="form-label">Descripción *</label>
            <textarea id="descripcion" name="descripcion" class="form-control" rows="4" required><c:out value="${param.descripcion}"/></textarea>
            <div class="invalid-feedback">Ingresá una descripción.</div>
        </div>
        <div class="row g-3 mb-3">
            <div class="col-sm-4">
                <label for="duracion" class="form-label">Duración *</label>
                <input type="number" id="duracion" name="duracion" class="form-control" required min="1" step="1"
                       value="<c:out value='${param.duracion}'/>">
                <div class="invalid-feedback">Un número mayor que cero.</div>
            </div>
            <div class="col-sm-4">
                <label for="cantHoras" class="form-label">Cantidad de horas *</label>
                <input type="number" id="cantHoras" name="cantHoras" class="form-control" required min="0.5" step="0.5"
                       value="<c:out value='${param.cantHoras}'/>">
                <div class="invalid-feedback">Un número mayor que cero.</div>
            </div>
            <div class="col-sm-4">
                <label for="cantCreditos" class="form-label">Créditos *</label>
                <input type="number" id="cantCreditos" name="cantCreditos" class="form-control" required min="1" step="1"
                       value="<c:out value='${param.cantCreditos}'/>">
                <div class="invalid-feedback">Un número mayor que cero.</div>
            </div>
        </div>
        <div class="mb-3">
            <label for="url" class="form-label">URL asociada</label>
            <input type="url" id="url" name="url" class="form-control" placeholder="https://..."
                   value="<c:out value='${param.url}'/>">
            <div class="invalid-feedback">Ingresá una URL válida (empieza con http:// o https://).</div>
        </div>
        <div class="mb-3">
            <label for="previas" class="form-label">Previas</label>
            <select id="previas" name="previas" class="form-select" multiple size="6">
                <c:forEach var="cur" items="${cursos}">
                    <c:set var="elegida" value="${false}"/>
                    <c:forEach var="p" items="${previasElegidas}"><c:if test="${p == cur}"><c:set var="elegida" value="${true}"/></c:if></c:forEach>
                    <option value="<c:out value='${cur}'/>" ${elegida ? 'selected' : ''}><c:out value="${cur}"/></option>
                </c:forEach>
            </select>
            <div class="form-text">Opcional. Ctrl + clic para elegir varias.</div>
        </div>
        <div class="mb-4">
            <label for="imagen" class="form-label">Imagen (opcional, JPG o PNG, hasta 2 MB)</label>
            <input type="file" id="imagen" name="imagen" class="form-control" accept=".jpg,.jpeg,.png">
            <div class="invalid-feedback">La imagen debe ser JPG o PNG y pesar hasta 2 MB.</div>
        </div>

        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">Crear curso</button>
            <a href="${raiz}/inicio" class="btn btn-outline-secondary">Cancelar</a>
        </div>
    </form>
</div>

<script src="${raiz}/js/alta-curso.js"></script>
<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
