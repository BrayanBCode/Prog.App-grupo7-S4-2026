<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Ingresar resultados de evaluaciones (Tarea 3): encabezado de la edicion y los aceptados con su nota (1 a 12).
     Mientras la edicion esta abierta se pueden guardar y corregir; "Cerrar edicion" guarda lo escrito y la cierra.
     Cerrada, el servlet lleva al acta (edicion/acta.jsp). --%>
<c:set var="det" value="${resultados.edicion}"/>
<%@ include file="/WEB-INF/views/edicion/encabezado.jspf" %>

<h2 class="h6 fw-bold">Calificaciones</h2>
<c:if test="${empty resultados.inscripciones}">
    <p class="text-body-secondary">No hay estudiantes aceptados en esta edición.</p>
</c:if>

<c:if test="${not empty resultados.inscripciones}">
    <form method="POST" action="${raiz}/edicion/resultados"
          onsubmit="return event.submitter == null || event.submitter.value != 'cerrar'
                    || confirm('¿Cerrar la edición? Después no se van a poder modificar las notas, y las inscripciones pendientes quedan rechazadas.');">
        <input type="hidden" name="edicion" value="<c:out value='${ed.nombre}'/>">
        <div class="table-responsive" style="max-width: 760px;">
            <table class="table table-sm align-middle">
                <thead class="table-light"><tr><th>Estudiante</th><th>Nombre</th><th style="width: 9rem;">Nota (1 a 12)</th></tr></thead>
                <tbody>
                <c:forEach var="i" items="${resultados.inscripciones}">
                    <c:url var="uEst" value="/usuarios"><c:param name="nick" value="${i.nickname}"/></c:url>
                    <tr>
                        <td><a href="${uEst}"><c:out value="${i.nickname}"/></a></td>
                        <td><c:out value="${i.nombre} ${i.apellido}"/></td>
                        <td>
                            <input type="number" min="1" max="12" step="1" class="form-control form-control-sm"
                                   name="nota.<c:out value='${i.nickname}'/>" value="<c:out value='${i.nota}'/>"
                                   aria-label="Nota de ${i.nickname}">
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="d-flex gap-2">
            <button type="submit" name="accion" value="guardar" class="btn btn-primary">Guardar notas</button>
            <button type="submit" name="accion" value="cerrar" class="btn btn-outline-danger">Cerrar edición</button>
        </div>
        <p class="form-text">Para cerrar, todos los estudiantes tienen que tener nota. Al cerrar, las inscripciones que sigan pendientes quedan rechazadas.</p>
    </form>
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
