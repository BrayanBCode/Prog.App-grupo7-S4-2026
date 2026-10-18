<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Seleccionar estudiantes para una edicion de curso (Figura 5): encabezado de la edicion como en la
     Figura 4 y la tabla de inscripciones. Cada inscripcion en Inscripto tiene un selector
     Pendiente / Aceptar / Rechazar; las ya resueltas muestran su estado. Solo en la edicion vigente (D2). --%>
<c:set var="det" value="${seleccion.edicion()}"/>
<%@ include file="/WEB-INF/views/edicion/encabezado.jspf" %>

<h2 class="h6 fw-bold">Inscripciones</h2>
<c:if test="${not det.vigente()}">
    <div class="alert alert-info small">Esta edición no es la vigente del curso: solo se pueden seleccionar estudiantes en la edición vigente.</div>
</c:if>
<c:if test="${empty seleccion.inscripciones()}">
    <p class="text-body-secondary">Todavía no hay inscripciones a esta edición.</p>
</c:if>

<c:if test="${not empty seleccion.inscripciones()}">
    <form method="POST" action="${raiz}/edicion/seleccionar">
        <input type="hidden" name="edicion" value="<c:out value='${ed.nombre()}'/>">
        <div class="table-responsive" style="max-width: 760px;">
            <table class="table table-sm align-middle">
                <thead class="table-light">
                <tr><th>Nombre</th><th>Fecha inscripción</th><th>Inscripción</th><th>Estado</th></tr>
                </thead>
                <tbody>
                <c:forEach var="i" items="${seleccion.inscripciones()}">
                    <c:url var="uEst" value="/usuarios"><c:param name="nick" value="${i.nickname()}"/></c:url>
                    <tr>
                        <td><a href="${uEst}"><c:out value="${i.nombre()} ${i.apellido()}"/></a></td>
                        <td><e:fecha valor="${i.fechaInscripcion()}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${i.estado() == 'INSCRIPTO' and det.vigente()}">
                                    <select name="seleccion" class="form-select form-select-sm" style="width: auto;"
                                            aria-label="Selección para ${i.nickname()}">
                                        <option value="">Pendiente</option>
                                        <option value="<c:out value='${i.nickname()}'/>:ACEPTADA">Aceptar</option>
                                        <option value="<c:out value='${i.nickname()}'/>:RECHAZADA">Rechazar</option>
                                    </select>
                                </c:when>
                                <c:otherwise><span class="text-body-secondary small">—</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td><e:estado valor="${i.estado()}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <c:if test="${det.vigente()}">
            <button type="submit" class="btn btn-primary">Guardar selección</button>
        </c:if>
    </form>
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
