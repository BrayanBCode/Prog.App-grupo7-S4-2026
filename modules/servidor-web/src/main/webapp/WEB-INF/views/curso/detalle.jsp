<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %><%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Consulta de Curso (Figura 3): a la izquierda imagen y datos del curso; a la derecha sus
     ediciones y los programas de formacion que lo incluyen. --%>
<c:set var="cur" value="${detalle.curso()}"/>

<div class="row g-4">
    <div class="col-lg-7">
        <div class="d-flex flex-column flex-sm-row gap-3 mb-3">
            <e:imagen ruta="${cur.imagen()}" alt="${cur.nombre()}" clase="img-curso rounded"/>
            <div>
                <h1 class="h3 mb-1"><c:out value="${cur.nombre()}"/></h1>
                <c:url var="uInst" value="/cursos"><c:param name="instituto" value="${cur.instituto()}"/></c:url>
                <p class="mb-2"><a class="text-body-secondary" href="${uInst}"><c:out value="${cur.instituto()}"/></a></p>
                <c:if test="${not empty cur.url()}">
                    <a href="<c:out value='${cur.url()}'/>" target="_blank" rel="noopener" class="text-break"><c:out value="${cur.url()}"/></a>
                </c:if>
            </div>
        </div>

        <p><c:out value="${cur.descripcion()}"/></p>

        <dl class="row small mb-0">
            <dt class="col-sm-5">Duración</dt><dd class="col-sm-7"><c:out value="${cur.duracion()}"/></dd>
            <dt class="col-sm-5">Cantidad de horas</dt><dd class="col-sm-7">${cur.cantHoras() % 1 == 0 ? cur.cantHoras().intValue() : cur.cantHoras()}</dd>
            <dt class="col-sm-5">Créditos</dt><dd class="col-sm-7"><c:out value="${cur.cantCreditos()}"/></dd>
            <dt class="col-sm-5">Fecha de alta</dt><dd class="col-sm-7"><e:fecha valor="${cur.fechaRegistro()}"/></dd>
            <c:if test="${not empty cur.docente()}">
                <c:url var="uDoc" value="/usuarios"><c:param name="nick" value="${cur.docente().nickname()}"/></c:url>
                <dt class="col-sm-5">Docente</dt>
                <dd class="col-sm-7"><a href="${uDoc}"><c:out value="${cur.docente().nombre()} ${cur.docente().apellido()}"/></a></dd>
            </c:if>
            <dt class="col-sm-5">Previas</dt>
            <dd class="col-sm-7">
                <c:if test="${empty cur.previas()}">Sin previas</c:if>
                <c:forEach var="p" items="${cur.previas()}" varStatus="st">
                    <c:url var="uPrev" value="/cursos"><c:param name="nombre" value="${p}"/></c:url>
                    <a href="${uPrev}"><c:out value="${p}"/></a><c:if test="${not st.last}">, </c:if>
                </c:forEach>
            </dd>
        </dl>
    </div>

    <div class="col-lg-5">
        <h2 class="h6 fw-bold">Ediciones del curso</h2>
        <c:if test="${empty detalle.ediciones()}"><p class="small text-body-secondary">Todavía no tiene ediciones.</p></c:if>
        <ul class="list-unstyled mb-4">
            <c:forEach var="ed" items="${detalle.ediciones()}">
                <li class="d-flex align-items-center gap-2 mb-2">
                    <e:imagen ruta="${ed.imagen()}" alt="${ed.nombre()}" clase="img-mini rounded"/>
                    <div class="small">
                        <c:url var="uEd" value="/ediciones"><c:param name="nombre" value="${ed.nombre()}"/></c:url>
                        <a class="fw-semibold d-block" href="${uEd}"><c:out value="${ed.nombre()}"/></a>
                        <div class="text-body-secondary"><e:fecha valor="${ed.fechaInicio()}"/> – <e:fecha valor="${ed.fechaFin()}"/></div>
                    </div>
                </li>
            </c:forEach>
        </ul>

        <h2 class="h6 fw-bold">Programas de formación</h2>
        <c:if test="${empty detalle.programas()}"><p class="small text-body-secondary">No forma parte de ningún programa.</p></c:if>
        <ul class="small ps-3 mb-0">
            <c:forEach var="p" items="${detalle.programas()}">
                <li><c:out value="${p}"/></li>
            </c:forEach>
        </ul>
    </div>
</div>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
