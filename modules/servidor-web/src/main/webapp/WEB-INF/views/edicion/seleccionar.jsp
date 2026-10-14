<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<h1>Seleccionar estudiantes</h1>

<c:choose>
    <c:when test="${sinVigente}">
        <p>El curso <strong><c:out value="${curso}"/></strong> no tiene una edición vigente.</p>
        <p><a href="${raiz}/edicion/seleccionar">Elegir otro curso</a></p>
    </c:when>
    <c:when test="${not empty edicion}">
        <div class="ficha">
            <e:imagen ruta="${edicion.imagen()}" alt="${edicion.nombre()}" clase="ficha-img"/>
            <div>
                <h2><c:out value="${edicion.nombre()}"/></h2>
                <c:url var="uCur" value="/cursos"><c:param name="nombre" value="${edicion.nombreCurso()}"/></c:url>
                <p><a href="${uCur}">Ver información del curso</a></p>
                <dl class="datos">
                    <dt>Inicio</dt><dd><e:fecha valor="${edicion.fechaInicio()}"/></dd>
                    <dt>Fin</dt><dd><e:fecha valor="${edicion.fechaFin()}"/></dd>
                    <dt>Cupo</dt><dd><c:out value="${edicion.cupo() > 0 ? edicion.cupo() : 'Sin límite'}"/></dd>
                    <dt>Publicación</dt><dd><e:fecha valor="${edicion.fechaPublicacion()}"/></dd>
                </dl>
            </div>
        </div>

        <c:if test="${inscripciones != null}">
            <h2>Inscripciones</h2>
            <c:if test="${empty inscripciones}"><p class="tenue">La edición no tiene inscripciones.</p></c:if>
            <c:if test="${not empty inscripciones}">
                <form method="post" action="${raiz}/edicion/seleccionar">
                    <input type="hidden" name="edicion" value="<c:out value='${edicion.nombre()}'/>">
                    <table class="tabla">
                        <thead><tr><th>Estudiante</th><th>Fecha de inscripción</th><th>Estado</th><th>Selección</th></tr></thead>
                        <tbody>
                        <c:forEach var="i" items="${inscripciones}">
                            <c:url var="uEst" value="/usuarios"><c:param name="nick" value="${i.nickname()}"/></c:url>
                            <tr>
                                <td><a href="${uEst}"><c:out value="${i.nombre()} ${i.apellido()}"/></a></td>
                                <td><e:fecha valor="${i.fechaInscripcion()}"/></td>
                                <td><e:estado valor="${i.estado()}"/></td>
                                <td>
                                    <%-- Solo se puede pasar de Inscripto a Aceptada o Rechazada (Tabla 1) --%>
                                    <c:if test="${i.estado() == 'INSCRIPTO'}">
                                        <label class="radio"><input type="radio" name="estado_${i.nickname()}" value="ACEPTADA"> Aceptar</label>
                                        <label class="radio"><input type="radio" name="estado_${i.nickname()}" value="RECHAZADA"> Rechazar</label>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div class="button-group">
                        <button type="submit" class="btn btn-primary">Guardar selección</button>
                        <a class="btn btn-secondary" href="${raiz}/edicion/seleccionar">Cancelar</a>
                    </div>
                </form>
            </c:if>
        </c:if>
    </c:when>
    <c:otherwise>
        <%@ include file="/WEB-INF/views/comun/elegir-curso.jspf" %>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
