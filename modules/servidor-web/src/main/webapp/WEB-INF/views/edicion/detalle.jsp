<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Consulta de Edicion de Curso (Figura 4): imagen, nombre e instituto con los links "Inscribirme" y
     "Ver informacion del Curso"; debajo fechas, cupo, publicacion y los docentes asignados. --%>
<c:set var="ed" value="${detalleEdicion.edicion()}"/>

<div class="d-flex flex-column flex-sm-row gap-3 mb-4">
    <e:imagen ruta="${ed.imagen()}" alt="${ed.nombre()}" clase="img-curso rounded"/>
    <div>
        <h1 class="h3 mb-1"><c:out value="${ed.nombre()}"/></h1>
        <p class="text-body-secondary mb-2"><c:out value="${detalleEdicion.instituto()}"/>
            <c:if test="${detalleEdicion.vigente()}"><span class="badge text-bg-success ms-1">Vigente</span></c:if>
        </p>
        <%-- TODO 4.5: link "Inscribirme" (Ajax) si puede.INSCRIPCION_EDICION_CURSO y la edicion es la vigente --%>
        <c:url var="uCurso" value="/cursos"><c:param name="nombre" value="${ed.nombreCurso()}"/></c:url>
        <a href="${uCurso}">Ver información del Curso</a>
    </div>
</div>

<dl class="row mb-4" style="max-width: 480px;">
    <dt class="col-sm-6">Fecha inicio</dt><dd class="col-sm-6"><e:fecha valor="${ed.fechaInicio()}"/></dd>
    <dt class="col-sm-6">Fecha fin</dt><dd class="col-sm-6"><e:fecha valor="${ed.fechaFin()}"/></dd>
    <dt class="col-sm-6">Cupo</dt><dd class="col-sm-6">${ed.cupo() == 0 ? 'Sin límite' : ed.cupo()}</dd>
    <dt class="col-sm-6">Fecha publicación</dt><dd class="col-sm-6"><e:fecha valor="${ed.fechaPublicacion()}"/></dd>
</dl>

<h2 class="h6 fw-bold">Docentes</h2>
<c:if test="${empty detalleEdicion.docentes()}"><p class="text-body-secondary">No tiene docentes asignados.</p></c:if>
<ul class="list-unstyled mb-0">
    <c:forEach var="d" items="${detalleEdicion.docentes()}">
        <c:url var="uDoc" value="/usuarios"><c:param name="nick" value="${d.nickname()}"/></c:url>
        <li class="mb-1">› <c:out value="${d.nombre()} ${d.apellido()}"/> - <a href="${uDoc}"><c:out value="${d.nickname()}"/></a></li>
    </c:forEach>
</ul>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
