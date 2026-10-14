<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<h1>Alta de edición de curso</h1>

<%-- Paso 1: el docente indica el instituto y el sistema lista sus cursos --%>
<form class="formulario fila-campos" method="get" action="${raiz}/edicion/alta">
    <div class="form-group">
        <label for="instituto">Instituto que brinda el curso</label>
        <select id="instituto" name="instituto" onchange="this.form.submit()">
            <option value="">-- Seleccioná --</option>
            <c:forEach var="inst" items="${institutosMenu}">
                <option value="<c:out value='${inst}'/>" ${inst == param.instituto ? 'selected' : ''}><c:out value="${inst}"/></option>
            </c:forEach>
        </select>
    </div>
    <noscript><button type="submit" class="btn btn-secondary">Ver cursos</button></noscript>
</form>

<c:if test="${not empty param.instituto}">
    <c:choose>
        <c:when test="${empty cursosInstituto}">
            <p class="tenue">El instituto no tiene cursos.</p>
        </c:when>
        <c:otherwise>
            <%-- Paso 2: datos de la edicion --%>
            <form class="formulario" method="post" action="${raiz}/edicion/alta" enctype="multipart/form-data">
                <input type="hidden" name="instituto" value="<c:out value='${param.instituto}'/>">
                <div class="form-group">
                    <label for="curso">Curso *</label>
                    <select id="curso" name="curso" required>
                        <c:forEach var="cur" items="${cursosInstituto}">
                            <option value="<c:out value='${cur}'/>" ${cur == param.curso ? 'selected' : ''}><c:out value="${cur}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="nombre">Nombre de la edición *</label>
                    <input type="text" id="nombre" name="nombre" required value="<c:out value='${param.nombre}'/>">
                </div>
                <div class="fila-campos">
                    <div class="form-group">
                        <label for="fechaInicio">Fecha de inicio *</label>
                        <input type="date" id="fechaInicio" name="fechaInicio" required value="<c:out value='${param.fechaInicio}'/>">
                    </div>
                    <div class="form-group">
                        <label for="fechaFin">Fecha de fin *</label>
                        <input type="date" id="fechaFin" name="fechaFin" required value="<c:out value='${param.fechaFin}'/>">
                    </div>
                    <div class="form-group">
                        <label for="cupo">Cupo (opcional)</label>
                        <input type="number" id="cupo" name="cupo" min="1" value="<c:out value='${param.cupo}'/>">
                    </div>
                </div>
                <div class="form-group">
                    <label for="docentes">Docentes que participan * (Ctrl+click para elegir varios)</label>
                    <select id="docentes" name="docentes" multiple size="6" required>
                        <c:forEach var="d" items="${docentes}">
                            <c:set var="elegido" value="false"/>
                            <c:forEach var="x" items="${docentesElegidos}"><c:if test="${x == d.nickname()}"><c:set var="elegido" value="true"/></c:if></c:forEach>
                            <option value="<c:out value='${d.nickname()}'/>" ${elegido ? 'selected' : ''}>
                                <c:out value="${d.nombre()} ${d.apellido()} (${d.nickname()})"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="imagen">Imagen (opcional, JPG o PNG)</label>
                    <input type="file" id="imagen" name="imagen" accept=".jpg,.jpeg,.png">
                </div>
                <div class="button-group">
                    <button type="submit" class="btn btn-primary">Dar de alta</button>
                    <a href="${raiz}/inicio" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </c:otherwise>
    </c:choose>
</c:if>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
