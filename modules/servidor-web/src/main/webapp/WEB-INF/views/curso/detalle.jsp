<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %><%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Consulta de Curso (Figura 3): a la izquierda imagen y datos del curso, con el resumen de valoraciones abajo
     (7.6, Figura 7); a la derecha sus ediciones y los programas de formacion que lo incluyen. --%>
<c:set var="cur" value="${detalle.curso}"/>

<div class="row g-4">
    <div class="col-lg-7">
        <div class="d-flex flex-column flex-sm-row gap-3 mb-3">
            <e:imagen ruta="${cur.imagen}" alt="${cur.nombre}" clase="img-curso rounded"/>
            <div>
                <h1 class="h3 mb-1"><c:out value="${cur.nombre}"/></h1>
                <c:url var="uInst" value="/cursos"><c:param name="instituto" value="${cur.instituto}"/></c:url>
                <p class="mb-2"><a class="text-body-secondary" href="${uInst}"><c:out value="${cur.instituto}"/></a></p>
                <c:if test="${not empty cur.url}">
                    <a href="<c:out value='${cur.url}'/>" target="_blank" rel="noopener" class="text-break"><c:out value="${cur.url}"/></a>
                </c:if>
            </div>
        </div>

        <p><c:out value="${cur.descripcion}"/></p>

        <dl class="row small mb-0">
            <dt class="col-sm-5">Duración</dt><dd class="col-sm-7"><c:out value="${cur.duracion}"/></dd>
            <dt class="col-sm-5">Cantidad de horas</dt><dd class="col-sm-7">${cur.cantHoras % 1 == 0 ? cur.cantHoras.intValue() : cur.cantHoras}</dd>
            <dt class="col-sm-5">Créditos</dt><dd class="col-sm-7"><c:out value="${cur.cantCreditos}"/></dd>
            <dt class="col-sm-5">Fecha de alta</dt><dd class="col-sm-7"><e:fecha valor="${cur.fechaRegistro}"/></dd>
            <c:if test="${not empty cur.docente}">
                <c:url var="uDoc" value="/usuarios"><c:param name="nick" value="${cur.docente.nickname}"/></c:url>
                <dt class="col-sm-5">Docente</dt>
                <dd class="col-sm-7"><a href="${uDoc}"><c:out value="${cur.docente.nombre} ${cur.docente.apellido}"/></a></dd>
            </c:if>
            <dt class="col-sm-5">Previas</dt>
            <dd class="col-sm-7">
                <c:if test="${empty cur.previas}">Sin previas</c:if>
                <c:forEach var="p" items="${cur.previas}" varStatus="st">
                    <c:url var="uPrev" value="/cursos"><c:param name="nombre" value="${p}"/></c:url>
                    <a href="${uPrev}"><c:out value="${p}"/></a><c:if test="${not st.last}">, </c:if>
                </c:forEach>
            </dd>
        </dl>

        <%-- Resumen de valoraciones (7.6, Figura 7), debajo de los datos: barras de 5 a 1 estrellas con la cantidad
             y, al lado, el promedio --%>
        <c:if test="${not empty valoracion}">
            <section class="resumen-valoraciones card card-body p-3 mt-4" style="max-width: 34rem;" aria-label="Valoraciones del curso">
                <h2 class="h6 fw-bold mb-2">Valoraciones</h2>
                <div class="d-flex align-items-center gap-3">
                    <div class="flex-grow-1">
                        <c:forEach var="k" begin="0" end="4">
                            <c:set var="n" value="${5 - k}"/>
                            <c:set var="cant" value="${valoracion.porPuntaje[n - 1]}"/>
                            <div class="d-flex align-items-center gap-2 small">
                                <span class="text-nowrap" style="width: 1.8rem;">${n} <span class="text-warning">★</span></span>
                                <div class="progress flex-grow-1" style="height: .55rem;" role="presentation">
                                    <div class="progress-bar barra-${n}"
                                         style="width: ${valoracion.cantidad == 0 ? 0 : cant * 100 / valoracion.cantidad}%"></div>
                                </div>
                                <span class="text-end text-body-secondary" style="width: 1.5rem;">${cant}</span>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="text-center border-start ps-3">
                        <div class="small text-body-secondary">Promedio</div>
                        <div class="fs-2 fw-semibold lh-1 my-1">
                            <c:choose>
                                <c:when test="${valoracion.cantidad == 0}">–</c:when>
                                <c:otherwise><fmt:formatNumber value="${valoracion.promedio}" maxFractionDigits="1" minFractionDigits="1"/></c:otherwise>
                            </c:choose>
                        </div>
                        <e:estrellas valor="${valoracion.promedio}"/>
                        <div class="small text-body-secondary text-nowrap mt-1">${valoracion.cantidad} ${valoracion.cantidad == 1 ? 'valoración' : 'valoraciones'}</div>
                    </div>
                </div>
                <c:if test="${not empty valoracion.miPuntaje}">
                    <div class="small mt-2 pt-2 border-top">Tu puntaje: <e:estrellas valor="${valoracion.miPuntaje}"/></div>
                </c:if>
            </section>
        </c:if>
    </div>

    <div class="col-lg-5">
        <div class="d-flex justify-content-between align-items-center mb-2">
            <h2 class="h6 fw-bold mb-0">Ediciones del curso</h2>
            <c:if test="${puede.ALTA_EDICION_CURSO}">
                <c:url var="uAltaEd" value="/edicion/alta"><c:param name="curso" value="${cur.nombre}"/></c:url>
                <a class="btn btn-outline-primary btn-sm" href="${uAltaEd}">+ Crear edición</a>
            </c:if>
        </div>
        <c:if test="${empty detalle.ediciones}"><p class="small text-body-secondary">Todavía no tiene ediciones.</p></c:if>
        <%-- Cada edicion es una tarjeta: se puede tocar en cualquier parte para ir a la edicion --%>
        <div class="d-flex flex-column gap-2 mb-4">
            <c:forEach var="ed" items="${detalle.ediciones}">
                <c:url var="uEd" value="/ediciones"><c:param name="nombre" value="${ed.nombre}"/></c:url>
                <a class="tarjeta-edicion card card-body p-2 d-flex flex-row align-items-center gap-2 text-body text-decoration-none" href="${uEd}">
                    <e:imagen ruta="${ed.imagen}" alt="${ed.nombre}" clase="img-mini rounded"/>
                    <div class="small">
                        <span class="fw-semibold text-primary"><c:out value="${ed.nombre}"/></span>
                        <c:if test="${ed.nombre == detalle.edicionVigente}"><span class="badge text-bg-success ms-1">Vigente</span></c:if>
                        <div class="text-body-secondary"><e:fecha valor="${ed.fechaInicio}"/> – <e:fecha valor="${ed.fechaFin}"/></div>
                    </div>
                </a>
            </c:forEach>
        </div>

        <h2 class="h6 fw-bold">Programas de formación</h2>
        <c:if test="${empty detalle.programas}"><p class="small text-body-secondary">No forma parte de ningún programa.</p></c:if>
        <ul class="small ps-3 mb-0">
            <c:forEach var="p" items="${detalle.programas}">
                <li><c:out value="${p}"/></li>
            </c:forEach>
        </ul>
    </div>
</div>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
