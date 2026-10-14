<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Consulta de Edicion de Curso (Figura 4) --%>
<div class="ficha">
    <e:imagen ruta="${edicion.imagen()}" alt="${edicion.nombre()}" clase="ficha-img"/>
    <div>
        <h1><c:out value="${edicion.nombre()}"/></h1>
        <p class="tenue"><c:out value="${curso.instituto()}"/></p>
        <c:url var="uCurso" value="/cursos"><c:param name="nombre" value="${curso.nombre()}"/></c:url>
        <p><a href="${uCurso}">Ver información del curso</a></p>
        <c:if test="${puede.INSCRIPCION_EDICION_CURSO and esVigente}">
            <c:url var="uInsc" value="/edicion/inscripcion"><c:param name="curso" value="${curso.nombre()}"/></c:url>
            <p><a class="btn btn-primary btn-auto" href="${uInsc}">Inscribirme</a></p>
        </c:if>
    </div>
</div>

<dl class="datos">
    <dt>Fecha de inicio</dt><dd><e:fecha valor="${edicion.fechaInicio()}"/></dd>
    <dt>Fecha de fin</dt><dd><e:fecha valor="${edicion.fechaFin()}"/></dd>
    <dt>Cupo</dt><dd><c:out value="${edicion.cupo() > 0 ? edicion.cupo() : 'Sin límite'}"/></dd>
    <dt>Fecha de publicación</dt><dd><e:fecha valor="${edicion.fechaPublicacion()}"/></dd>
</dl>

<h2>Docentes</h2>
<ul class="lista">
    <c:forEach var="d" items="${docentes}">
        <c:url var="u" value="/usuarios"><c:param name="nick" value="${d.nickname()}"/></c:url>
        <li><c:out value="${d.nombre()} ${d.apellido()}"/> - <a href="${u}"><c:out value="${d.nickname()}"/></a></li>
    </c:forEach>
</ul>

<c:if test="${participa}">
    <h2>Opciones del docente</h2>
    <p>
        <c:url var="uSel" value="/edicion/seleccionar"><c:param name="edicion" value="${edicion.nombre()}"/></c:url>
        <c:url var="uAcep" value="/edicion/aceptados"><c:param name="edicion" value="${edicion.nombre()}"/></c:url>
        <a href="${uSel}">Seleccionar estudiantes</a> &middot; <a href="${uAcep}">Ver aceptados</a>
    </p>
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
