<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<h1>Alta de curso</h1>

<form class="formulario" method="post" action="${raiz}/curso/alta" enctype="multipart/form-data">
    <div class="form-group">
        <label for="instituto">Instituto que brinda el curso *</label>
        <select id="instituto" name="instituto" required>
            <option value="">-- Seleccioná --</option>
            <c:forEach var="inst" items="${institutosMenu}">
                <option value="<c:out value='${inst}'/>" ${inst == param.instituto ? 'selected' : ''}><c:out value="${inst}"/></option>
            </c:forEach>
        </select>
    </div>
    <div class="form-group">
        <label for="nombre">Nombre *</label>
        <input type="text" id="nombre" name="nombre" required value="<c:out value='${param.nombre}'/>">
    </div>
    <div class="form-group">
        <label for="descripcion">Descripción *</label>
        <textarea id="descripcion" name="descripcion" rows="4" required><c:out value="${param.descripcion}"/></textarea>
    </div>
    <div class="fila-campos">
        <div class="form-group">
            <label for="duracion">Duración *</label>
            <input type="number" id="duracion" name="duracion" min="1" required value="<c:out value='${param.duracion}'/>">
        </div>
        <div class="form-group">
            <label for="horas">Cantidad de horas *</label>
            <input type="number" id="horas" name="horas" min="1" step="any" required value="<c:out value='${param.horas}'/>">
        </div>
        <div class="form-group">
            <label for="creditos">Créditos *</label>
            <input type="number" id="creditos" name="creditos" min="1" required value="<c:out value='${param.creditos}'/>">
        </div>
    </div>
    <div class="form-group">
        <label for="url">URL</label>
        <input type="url" id="url" name="url" placeholder="https://" value="<c:out value='${param.url}'/>">
    </div>
    <div class="form-group">
        <label for="previas">Previas (opcional, Ctrl+click para elegir varias)</label>
        <select id="previas" name="previas" multiple size="6">
            <c:forEach var="cur" items="${todosLosCursos}">
                <c:set var="elegida" value="false"/>
                <c:forEach var="p" items="${previasElegidas}"><c:if test="${p == cur}"><c:set var="elegida" value="true"/></c:if></c:forEach>
                <option value="<c:out value='${cur}'/>" ${elegida ? 'selected' : ''}><c:out value="${cur}"/></option>
            </c:forEach>
        </select>
    </div>
    <div class="form-group">
        <label for="imagen">Imagen (opcional, JPG o PNG)</label>
        <input type="file" id="imagen" name="imagen" accept=".jpg,.jpeg,.png">
    </div>
    <div class="button-group">
        <button type="submit" class="btn btn-primary">Dar de alta</button>
        <a href="${raiz}/inicio" class="btn btn-secondary">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
