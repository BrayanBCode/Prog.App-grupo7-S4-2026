<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<h1>Inscripción a edición de curso</h1>

<c:choose>
    <c:when test="${empty curso}">
        <%@ include file="/WEB-INF/views/comun/elegir-curso.jspf" %>
    </c:when>
    <c:when test="${empty edicion}">
        <p>El curso <strong><c:out value="${curso}"/></strong> no tiene una edición vigente en este momento.</p>
        <p><a href="${raiz}/edicion/inscripcion">Elegir otro curso</a></p>
    </c:when>
    <c:otherwise>
        <div class="ficha">
            <e:imagen ruta="${edicion.imagen()}" alt="${edicion.nombre()}" clase="ficha-img"/>
            <div>
                <h2><c:out value="${edicion.nombre()}"/></h2>
                <p class="tenue">Edición vigente de <c:out value="${curso}"/></p>
                <dl class="datos">
                    <dt>Inicio</dt><dd><e:fecha valor="${edicion.fechaInicio()}"/></dd>
                    <dt>Fin</dt><dd><e:fecha valor="${edicion.fechaFin()}"/></dd>
                    <dt>Cupo</dt><dd><c:out value="${edicion.cupo() > 0 ? edicion.cupo() : 'Sin límite'}"/></dd>
                </dl>
            </div>
        </div>
        <c:choose>
            <c:when test="${not empty yaInscripto}">
                <div class="alert-danger">Ya estás inscripto a esta edición (estado:
                    <e:estado valor="${yaInscripto.estado()}"/>). Podés elegir otro curso o cancelar.</div>
                <div class="button-group">
                    <a class="btn btn-primary" href="${raiz}/edicion/inscripcion">Elegir otro curso</a>
                    <a class="btn btn-secondary" href="${raiz}/inicio">Cancelar</a>
                </div>
            </c:when>
            <c:otherwise>
                <form method="post" action="${raiz}/edicion/inscripcion">
                    <input type="hidden" name="edicion" value="<c:out value='${edicion.nombre()}'/>">
                    <input type="hidden" name="curso" value="<c:out value='${curso}'/>">
                    <div class="button-group">
                        <button type="submit" class="btn btn-primary">Confirmar inscripción</button>
                        <a class="btn btn-secondary" href="${raiz}/edicion/inscripcion">Cancelar</a>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
