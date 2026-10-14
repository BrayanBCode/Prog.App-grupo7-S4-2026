<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Consulta de Usuario --%>
<div class="ficha">
    <e:imagen ruta="${perfil.imagen()}" alt="${perfil.nickname()}" clase="ficha-img avatar"/>
    <div>
        <h1><c:out value="${perfil.nombre()} ${perfil.apellido()}"/></h1>
        <p class="tenue">
            <c:out value="${perfil.nickname()}"/> / <c:out value="${perfil.mail()}"/>
            <span class="rol rol-${esDocente ? 'DOCENTE' : 'ESTUDIANTE'}">${esDocente ? 'Docente' : 'Estudiante'}</span>
        </p>
        <%-- Seguir a un Usuario: solo con sesion, y no a uno mismo --%>
        <c:if test="${puede.SEGUIR_USUARIO and not propio}">
            <c:choose>
                <c:when test="${loSigo}">
                    <p><span class="estado estado-ACEPTADA">Lo seguís</span></p>
                </c:when>
                <c:otherwise>
                    <form method="post" action="${raiz}/usuarios/seguir">
                        <input type="hidden" name="nick" value="<c:out value='${perfil.nickname()}'/>">
                        <button type="submit" class="btn btn-primary btn-auto">Seguir</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </c:if>
    </div>
</div>

<h2>Datos básicos</h2>
<dl class="datos">
    <dt>Nickname</dt><dd><c:out value="${perfil.nickname()}"/></dd>
    <dt>Nombre</dt><dd><c:out value="${perfil.nombre()}"/></dd>
    <dt>Apellido</dt><dd><c:out value="${perfil.apellido()}"/></dd>
    <dt>Correo electrónico</dt><dd><c:out value="${perfil.mail()}"/></dd>
    <dt>Fecha de nacimiento</dt><dd><e:fecha valor="${perfil.fechaNac()}"/></dd>
    <c:if test="${esDocente}">
        <dt>Instituto</dt><dd><c:forEach var="i" items="${institutos}" varStatus="st"><c:out value="${i}"/><c:if test="${not st.last}">, </c:if></c:forEach></dd>
    </c:if>
</dl>

<c:choose>
    <c:when test="${esDocente}">
        <h2>Ediciones de cursos en las que participa</h2>
        <c:if test="${empty ediciones}"><p class="tenue">No participa en ninguna edición.</p></c:if>
        <c:if test="${not empty ediciones}">
            <table class="tabla">
                <thead><tr><th>Edición</th><th>Curso</th><th>Inicio</th><th>Fin</th>
                    <c:if test="${propio}"><th>Selección de estudiantes</th></c:if></tr></thead>
                <tbody>
                <c:forEach var="ed" items="${ediciones}">
                    <c:url var="uEd" value="/ediciones"><c:param name="nombre" value="${ed.nombre()}"/></c:url>
                    <c:url var="uCur" value="/cursos"><c:param name="nombre" value="${ed.nombreCurso()}"/></c:url>
                    <tr>
                        <td><a href="${uEd}"><c:out value="${ed.nombre()}"/></a></td>
                        <td><a href="${uCur}"><c:out value="${ed.nombreCurso()}"/></a></td>
                        <td><e:fecha valor="${ed.fechaInicio()}"/></td>
                        <td><e:fecha valor="${ed.fechaFin()}"/></td>
                        <c:if test="${propio}">
                            <c:url var="uAcep" value="/edicion/aceptados"><c:param name="edicion" value="${ed.nombre()}"/></c:url>
                            <td><a href="${uAcep}">Ver aceptados</a></td>
                        </c:if>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
    </c:when>
    <c:otherwise>
        <h2>Inscripciones a ediciones de cursos</h2>
        <c:if test="${empty inscripciones}"><p class="tenue">Sin inscripciones.</p></c:if>
        <c:if test="${not empty inscripciones}">
            <table class="tabla">
                <thead><tr><th>Edición</th><th>Curso</th><th>Fecha</th><th>Estado</th></tr></thead>
                <tbody>
                <c:forEach var="i" items="${inscripciones}">
                    <c:url var="uEd" value="/ediciones"><c:param name="nombre" value="${i.edicion()}"/></c:url>
                    <c:url var="uCur" value="/cursos"><c:param name="nombre" value="${i.curso()}"/></c:url>
                    <tr>
                        <td><a href="${uEd}"><c:out value="${i.edicion()}"/></a></td>
                        <td><a href="${uCur}"><c:out value="${i.curso()}"/></a></td>
                        <td><e:fecha valor="${i.fechaInscripcion()}"/></td>
                        <td><e:estado valor="${i.estado()}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

        <h2>Programas de formación</h2>
        <c:if test="${empty programasInscripto}"><p class="tenue">No está inscripto a ningún programa.</p></c:if>
        <ul class="lista">
            <c:forEach var="p" items="${programasInscripto}">

                <li><c:out value="${p.programa()}"/> <span class="tenue">(desde <e:fecha valor="${p.fechaInscripcion()}"/>)</span></li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<div class="columnas">
    <section>
        <h2>Seguidores (${seguidores.size()})</h2>
        <ul class="lista">
            <c:forEach var="s" items="${seguidores}">
                <c:url var="uS" value="/usuarios"><c:param name="nick" value="${s}"/></c:url>
                <li><a href="${uS}"><c:out value="${s}"/></a></li>
            </c:forEach>
        </ul>
    </section>
    <section>
        <h2>Sigue a (${seguidos.size()})</h2>
        <ul class="lista">
            <c:forEach var="s" items="${seguidos}">
                <c:url var="uS" value="/usuarios"><c:param name="nick" value="${s}"/></c:url>
                <li><a href="${uS}"><c:out value="${s}"/></a></li>
            </c:forEach>
        </ul>
    </section>
</div>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
