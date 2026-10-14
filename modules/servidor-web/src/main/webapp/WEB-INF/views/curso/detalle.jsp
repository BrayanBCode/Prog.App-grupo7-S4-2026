<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Consulta de Curso (Figura 3): datos del curso + ediciones y programas asociados --%>
<div class="ficha">
    <e:imagen ruta="${curso.imagen()}" alt="${curso.nombre()}" clase="ficha-img"/>
    <div>
        <h1><c:out value="${curso.nombre()}"/></h1>
        <c:url var="uInst" value="/cursos"><c:param name="instituto" value="${curso.instituto()}"/></c:url>
        <p class="tenue">Instituto: <a href="${uInst}"><c:out value="${curso.instituto()}"/></a></p>
        <c:if test="${not empty curso.url()}">
            <p><a href="<c:out value='${curso.url()}'/>" target="_blank" rel="noopener"><c:out value="${curso.url()}"/></a></p>
        </c:if>
    </div>
</div>

<p><c:out value="${curso.descripcion()}"/></p>

<dl class="datos">
    <dt>Duración</dt><dd><c:out value="${curso.duracion()}"/></dd>
    <dt>Cantidad de horas</dt><dd><c:out value="${curso.cantHoras()}"/></dd>
    <dt>Créditos</dt><dd><c:out value="${curso.cantCreditos()}"/></dd>
    <dt>Fecha de alta</dt><dd><e:fecha valor="${curso.fechaRegistro()}"/></dd>
    <c:if test="${curso.docente() != null}">
        <c:url var="uDoc" value="/usuarios"><c:param name="nick" value="${curso.docente().nickname()}"/></c:url>
        <dt>Docente</dt>
        <dd><a href="${uDoc}"><c:out value="${curso.docente().nombre()} ${curso.docente().apellido()}"/></a></dd>
    </c:if>
    <dt>Previas</dt>
    <dd>
        <c:if test="${empty curso.previas()}">Sin previas</c:if>
        <c:forEach var="pr" items="${curso.previas()}" varStatus="st">
            <c:url var="u" value="/cursos"><c:param name="nombre" value="${pr}"/></c:url>
            <a href="${u}"><c:out value="${pr}"/></a><c:if test="${not st.last}">, </c:if>
        </c:forEach>
    </dd>
</dl>

<c:if test="${puede.INSCRIPCION_EDICION_CURSO and not empty edicionVigente}">
    <c:url var="uInsc" value="/edicion/inscripcion"><c:param name="curso" value="${curso.nombre()}"/></c:url>
    <p><a class="btn btn-primary btn-auto" href="${uInsc}">Inscribirme a la edición vigente</a></p>
</c:if>

<h2>Ediciones del curso</h2>
<c:if test="${empty ediciones}"><p class="tenue">El curso todavía no tiene ediciones.</p></c:if>
<div class="tarjetas">
    <c:forEach var="ed" items="${ediciones}">
        <c:url var="u" value="/ediciones"><c:param name="nombre" value="${ed.nombre()}"/></c:url>
        <a class="tarjeta" href="${u}">
            <e:imagen ruta="${ed.imagen()}" alt="${ed.nombre()}" clase="tarjeta-img"/>
            <span class="tarjeta-cuerpo">
                <strong><c:out value="${ed.nombre()}"/></strong>
                <span class="tenue"><e:fecha valor="${ed.fechaInicio()}"/> al <e:fecha valor="${ed.fechaFin()}"/></span>
                <c:if test="${ed.nombre() == edicionVigente}"><span class="estado estado-ACEPTADA">Vigente</span></c:if>
            </span>
        </a>
    </c:forEach>
</div>

<h2>Programas de formación</h2>
<c:if test="${empty programas}"><p class="tenue">El curso no integra ningún programa.</p></c:if>
<%-- Solo los nombres: Consulta de Programa de Formacion no le corresponde a este grupo --%>
<ul class="lista">
    <c:forEach var="p" items="${programas}">
        <li><c:out value="${p}"/></li>
    </c:forEach>
</ul>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
