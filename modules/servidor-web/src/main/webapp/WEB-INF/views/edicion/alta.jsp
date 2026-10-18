<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Alta de Edicion de Curso (Docente). El curso ya viene elegido desde su pagina. El error del central
     (${error}) lo muestra el marco; los de cada campo, la validacion de Bootstrap (alta-edicion.js).
     El docente con sesion siempre participa (D3): su casilla viene marcada y no se puede desmarcar. --%>
<c:url var="uCurso" value="/cursos"><c:param name="nombre" value="${cursoAlta.nombre()}"/></c:url>

<div style="max-width: 640px;">
    <h1 class="h3 mb-1">Crear edición</h1>
    <p class="text-body-secondary mb-4">
        Curso <a href="${uCurso}"><c:out value="${cursoAlta.nombre()}"/></a> · <c:out value="${cursoAlta.instituto()}"/>
    </p>

    <form id="alta-edicion-form" action="${raiz}/edicion/alta" method="POST" enctype="multipart/form-data" novalidate>
        <input type="hidden" name="curso" value="<c:out value='${cursoAlta.nombre()}'/>">

        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre de la edición *</label>
            <input type="text" id="nombre" name="nombre" class="form-control" required maxlength="200"
                   value="<c:out value='${param.nombre}'/>">
            <div class="invalid-feedback">Ingresá el nombre de la edición.</div>
        </div>
        <div class="row g-3 mb-3">
            <div class="col-sm-4">
                <label for="fechaInicio" class="form-label">Fecha de inicio *</label>
                <input type="date" id="fechaInicio" name="fechaInicio" class="form-control" required
                       value="<c:out value='${param.fechaInicio}'/>">
                <div class="invalid-feedback">Elegí la fecha de inicio.</div>
            </div>
            <div class="col-sm-4">
                <label for="fechaFin" class="form-label">Fecha de fin *</label>
                <input type="date" id="fechaFin" name="fechaFin" class="form-control" required
                       value="<c:out value='${param.fechaFin}'/>">
                <div class="invalid-feedback">Elegí una fecha de fin igual o posterior al inicio.</div>
            </div>
            <div class="col-sm-4">
                <label for="cupo" class="form-label">Cupo</label>
                <input type="number" id="cupo" name="cupo" class="form-control" min="1" step="1" placeholder="Sin límite"
                       value="<c:out value='${param.cupo}'/>">
                <div class="invalid-feedback">Un número mayor que cero, o vacío.</div>
            </div>
        </div>

        <fieldset class="mb-3">
            <legend class="form-label fs-6 mb-1">Docentes que participan</legend>
            <p class="form-text mt-0 mb-2">Vos siempre participás en la edición que creás. Podés sumar otros docentes del instituto.</p>
            <c:forEach var="d" items="${docentesInstituto}">
                <c:set var="yo" value="${d.nickname() == usuarioActual.nickname}"/>
                <c:set var="elegido" value="${yo}"/>
                <c:forEach var="n" items="${docentesElegidos}"><c:if test="${n == d.nickname()}"><c:set var="elegido" value="${true}"/></c:if></c:forEach>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="docentes" id="doc-${d.nickname()}"
                           value="<c:out value='${d.nickname()}'/>" ${elegido ? 'checked' : ''} ${yo ? 'disabled' : ''}>
                    <label class="form-check-label" for="doc-${d.nickname()}">
                        <c:out value="${d.nombre()} ${d.apellido()}"/> (<c:out value="${d.nickname()}"/>)
                        <c:if test="${yo}"><span class="text-body-secondary small">— vos</span></c:if>
                    </label>
                </div>
            </c:forEach>
        </fieldset>

        <div class="mb-4">
            <label for="imagen" class="form-label">Imagen (opcional, JPG o PNG, hasta 2 MB)</label>
            <input type="file" id="imagen" name="imagen" class="form-control" accept=".jpg,.jpeg,.png">
            <div class="invalid-feedback">La imagen debe ser JPG o PNG y pesar hasta 2 MB.</div>
        </div>

        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary">Crear edición</button>
            <a href="${uCurso}" class="btn btn-outline-secondary">Cancelar</a>
        </div>
    </form>
</div>

<script src="${raiz}/js/alta-edicion.js"></script>
<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
