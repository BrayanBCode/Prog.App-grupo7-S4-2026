<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Valorar un Curso (Tarea 3): a la izquierda los cursos en los que participa el estudiante; a la derecha, el
     elegido con su promedio y el puntaje propio, o las 5 estrellas para valorarlo (una sola vez). --%>
<h1 class="h3 mb-1">Valorar cursos</h1>
<p class="text-body-secondary mb-4">Valorá de 1 a 5 estrellas los cursos en los que participaste. Cada curso se
    valora una sola vez, y tu puntaje cuenta para el promedio del curso.</p>

<c:if test="${empty error and empty cursos}">
    <p class="text-body-secondary">Todavía no participaste en ningún curso: vas a poder valorarlo cuando te acepten
        en alguna de sus ediciones.</p>
</c:if>

<c:if test="${not empty cursos}">
    <div class="row g-4">
        <div class="col-lg-5">
            <div class="list-group">
                <c:forEach var="cu" items="${cursos}">
                    <c:url var="uVal" value="/cursos/valorar"><c:param name="curso" value="${cu}"/></c:url>
                    <a class="list-group-item list-group-item-action ${cu == curso ? 'active' : ''}" href="${uVal}"
                       ${cu == curso ? 'aria-current="true"' : ''}><c:out value="${cu}"/></a>
                </c:forEach>
            </div>
        </div>

        <div class="col-lg-7">
            <c:choose>
                <c:when test="${empty valoracion}">
                    <p class="text-body-secondary">Elegí un curso de la lista.</p>
                </c:when>
                <c:otherwise>
                    <c:url var="uCurso" value="/cursos"><c:param name="nombre" value="${curso}"/></c:url>
                    <h2 class="h5 mb-3"><a class="text-body" href="${uCurso}"><c:out value="${curso}"/></a></h2>

                    <div class="d-flex align-items-center gap-2 mb-1">
                        <e:estrellas valor="${valoracion.promedio}"/>
                        <c:choose>
                            <c:when test="${valoracion.cantidad == 0}"><span class="text-body-secondary">Sin valoraciones todavía</span></c:when>
                            <c:otherwise>
                                <span class="fw-semibold"><fmt:formatNumber value="${valoracion.promedio}" maxFractionDigits="1"/></span>
                                <span class="text-body-secondary">de 5 · ${valoracion.cantidad} ${valoracion.cantidad == 1 ? 'valoración' : 'valoraciones'}</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <p class="small text-body-secondary mb-4">Promedio del curso, de todas sus ediciones.</p>

                    <c:choose>
                        <c:when test="${not empty valoracion.miPuntaje}">
                            <div class="d-flex align-items-center gap-2">
                                <span>Tu puntaje:</span> <e:estrellas valor="${valoracion.miPuntaje}"/>
                            </div>
                            <p class="small text-body-secondary mt-1">Ya valoraste este curso.</p>
                        </c:when>
                        <c:otherwise>
                            <form method="POST" action="${raiz}/cursos/valorar">
                                <input type="hidden" name="curso" value="<c:out value='${curso}'/>">
                                <fieldset class="mb-3">
                                    <legend class="fs-6 mb-1">Tu puntaje</legend>
                                    <%-- De 5 a 1 en el HTML: con row-reverse se ven de 1 a 5 y el CSS pinta la elegida y las anteriores --%>
                                    <div class="elegir-estrellas">
                                        <c:forEach var="n" begin="1" end="5">
                                            <c:set var="p" value="${6 - n}"/>
                                            <input type="radio" class="btn-check" name="puntaje" id="estrella-${p}" value="${p}" required>
                                            <label for="estrella-${p}" title="${p} ${p == 1 ? 'estrella' : 'estrellas'}">★</label>
                                        </c:forEach>
                                    </div>
                                </fieldset>
                                <button type="submit" class="btn btn-primary"
                                        onclick="return confirm('¿Confirmás tu valoración? Después no se puede cambiar.');">Valorar</button>
                                <p class="form-text">Cada curso se valora una sola vez.</p>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
