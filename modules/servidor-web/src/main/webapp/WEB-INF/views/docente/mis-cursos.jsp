<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- "Mis cursos y ediciones" del docente: acceso rapido a sus cursos y a las ediciones en las que participa,
     con las acciones de cada una (Crear edicion, Seleccionar estudiantes en la vigente). --%>
<div class="d-flex justify-content-between align-items-start gap-3 mb-3">
    <h1 class="h3 mb-0">Mis cursos y ediciones</h1>
    <a class="btn btn-outline-primary btn-sm text-nowrap" href="${raiz}/curso/alta">+ Crear curso</a>
</div>

<h2 class="h5 mt-2">Mis cursos</h2>
<c:if test="${empty misCursos.cursos()}"><p class="text-body-secondary">Todavía no dictás ningún curso.</p></c:if>
<div class="row row-cols-1 row-cols-sm-2 row-cols-xl-3 g-3 mb-4">
    <c:forEach var="cur" items="${misCursos.cursos()}">
        <c:url var="uCur" value="/cursos"><c:param name="nombre" value="${cur.nombre()}"/></c:url>
        <c:url var="uAltaEd" value="/edicion/alta"><c:param name="curso" value="${cur.nombre()}"/></c:url>
        <div class="col">
            <div class="card h-100 shadow-sm">
                <e:imagen ruta="${cur.imagen()}" alt="${cur.nombre()}" clase="card-img-top img-tarjeta"/>
                <div class="card-body d-flex flex-column">
                    <a class="fw-semibold text-decoration-none" href="${uCur}"><c:out value="${cur.nombre()}"/></a>
                    <p class="small text-body-secondary mt-1 mb-3 recorte"><c:out value="${cur.descripcion()}"/></p>
                    <a class="btn btn-outline-primary btn-sm mt-auto align-self-start" href="${uAltaEd}">+ Crear edición</a>
                </div>
            </div>
        </div>
    </c:forEach>
</div>

<h2 class="h5">Mis ediciones</h2>
<c:if test="${empty misCursos.ediciones()}"><p class="text-body-secondary">Todavía no participás en ninguna edición.</p></c:if>
<c:if test="${not empty misCursos.ediciones()}">
    <div class="table-responsive">
        <table class="table table-hover align-middle">
            <thead class="table-light">
            <tr><th>Edición</th><th>Curso</th><th>Inicio</th><th>Fin</th><th></th></tr>
            </thead>
            <tbody>
            <c:forEach var="me" items="${misCursos.ediciones()}">
                <c:set var="ed" value="${me.edicion()}"/>
                <c:url var="uEd" value="/ediciones"><c:param name="nombre" value="${ed.nombre()}"/></c:url>
                <c:url var="uCur" value="/cursos"><c:param name="nombre" value="${ed.nombreCurso()}"/></c:url>
                <tr>
                    <td><a href="${uEd}"><c:out value="${ed.nombre()}"/></a>
                        <c:if test="${me.vigente()}"><span class="badge text-bg-success ms-1">Vigente</span></c:if></td>
                    <td><a href="${uCur}"><c:out value="${ed.nombreCurso()}"/></a></td>
                    <td><e:fecha valor="${ed.fechaInicio()}"/></td>
                    <td><e:fecha valor="${ed.fechaFin()}"/></td>
                    <td class="text-end text-nowrap">
                        <c:if test="${me.vigente()}">
                            <c:url var="uSel" value="/edicion/seleccionar"><c:param name="edicion" value="${ed.nombre()}"/></c:url>
                            <a class="btn btn-outline-primary btn-sm" href="${uSel}">Seleccionar estudiantes</a>
                        </c:if>
                        <c:url var="uAcep" value="/edicion/aceptados"><c:param name="edicion" value="${ed.nombre()}"/></c:url>
                        <a class="btn btn-outline-secondary btn-sm" href="${uAcep}">Ver aceptados</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
