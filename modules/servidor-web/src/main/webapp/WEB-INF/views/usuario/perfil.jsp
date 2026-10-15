<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="e" tagdir="/WEB-INF/tags" %>
<%@ include file="/WEB-INF/views/comun/inicio-pagina.jspf" %>

<%-- Consulta de Usuario --%>
<div class="d-flex flex-column flex-sm-row align-items-sm-center gap-4 mb-4">
    <e:imagen ruta="${perfil.imagen()}" alt="${perfil.nickname()}" clase="avatar avatar-lg"/>
    <div>
        <h1 class="h3 mb-1"><c:out value="${perfil.nombre()} ${perfil.apellido()}"/></h1>
        <p class="text-body-secondary mb-2">
            <c:out value="${perfil.nickname()}"/> · <c:out value="${perfil.mail()}"/>
            <span class="badge ${esDocente ? 'text-bg-primary' : 'text-bg-success'} ms-1">${esDocente ? 'Docente' : 'Estudiante'}</span>
        </p>
        <%-- TODO 4.6: boton Seguir --%>
    </div>
</div>

<%-- Pestañas (letra 7.3): datos basicos / ediciones o inscripciones / seguidores --%>
<ul class="nav nav-tabs mb-3" role="tablist">
    <li class="nav-item" role="presentation">
        <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#tab-datos" type="button" role="tab">Datos</button>
    </li>
    <li class="nav-item" role="presentation">
        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#tab-cursos" type="button" role="tab">
            ${esDocente ? 'Ediciones' : 'Inscripciones'}
        </button>
    </li>
    <c:if test="${not esDocente}">
        <li class="nav-item" role="presentation">
            <button class="nav-link" data-bs-toggle="tab" data-bs-target="#tab-programas" type="button" role="tab">Programas</button>
        </li>
    </c:if>
    <li class="nav-item" role="presentation">
        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#tab-red" type="button" role="tab">
            Seguidores <span class="badge text-bg-secondary">${seguidores.size()}</span>
        </button>
    </li>
</ul>

<div class="tab-content">

    <%-- Datos basicos --%>
    <div class="tab-pane show active" id="tab-datos" role="tabpanel">
        <dl class="row mb-0">
            <dt class="col-sm-4 col-lg-3">Nickname</dt><dd class="col-sm-8 col-lg-9"><c:out value="${perfil.nickname()}"/></dd>
            <dt class="col-sm-4 col-lg-3">Nombre</dt><dd class="col-sm-8 col-lg-9"><c:out value="${perfil.nombre()}"/></dd>
            <dt class="col-sm-4 col-lg-3">Apellido</dt><dd class="col-sm-8 col-lg-9"><c:out value="${perfil.apellido()}"/></dd>
            <dt class="col-sm-4 col-lg-3">Correo electrónico</dt><dd class="col-sm-8 col-lg-9"><c:out value="${perfil.mail()}"/></dd>
            <dt class="col-sm-4 col-lg-3">Fecha de nacimiento</dt><dd class="col-sm-8 col-lg-9"><e:fecha valor="${perfil.fechaNac()}"/></dd>
            <c:if test="${esDocente}">
                <dt class="col-sm-4 col-lg-3">Instituto</dt>
                <dd class="col-sm-8 col-lg-9"><c:forEach var="i" items="${institutos}" varStatus="st"><c:out value="${i}"/><c:if test="${not st.last}">, </c:if></c:forEach></dd>
            </c:if>
        </dl>
    </div>

    <%-- Docente: ediciones en las que participa / Estudiante: inscripciones a ediciones --%>
    <div class="tab-pane" id="tab-cursos" role="tabpanel">
        <c:choose>
            <c:when test="${esDocente}">
                <c:if test="${empty ediciones}"><p class="text-body-secondary">No participa en ninguna edición.</p></c:if>
                <c:if test="${not empty ediciones}">
                    <%-- TODO 4.1/4.2: links a curso y edicion. TODO 4.8: "Ver aceptados" en el perfil propio --%>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-light"><tr><th>Edición</th><th>Curso</th><th>Inicio</th><th>Fin</th></tr></thead>
                            <tbody>
                            <c:forEach var="ed" items="${ediciones}">
                                <tr>
                                    <td><c:out value="${ed.nombre()}"/></td>
                                    <td><c:out value="${ed.nombreCurso()}"/></td>
                                    <td><e:fecha valor="${ed.fechaInicio()}"/></td>
                                    <td><e:fecha valor="${ed.fechaFin()}"/></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </c:when>
            <c:otherwise>
                <c:if test="${empty inscripciones}"><p class="text-body-secondary">Sin inscripciones.</p></c:if>
                <c:if test="${not empty inscripciones}">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-light"><tr><th>Edición</th><th>Curso</th><th>Fecha</th><th>Estado</th></tr></thead>
                            <tbody>
                            <c:forEach var="i" items="${inscripciones}">
                                <tr>
                                    <td><c:out value="${i.edicion()}"/></td>
                                    <td><c:out value="${i.curso()}"/></td>
                                    <td><e:fecha valor="${i.fechaInscripcion()}"/></td>
                                    <td><e:estado valor="${i.estado()}"/></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- Estudiante: programas de formacion --%>
    <c:if test="${not esDocente}">
        <div class="tab-pane" id="tab-programas" role="tabpanel">
            <c:if test="${empty programasInscripto}"><p class="text-body-secondary">No está inscripto a ningún programa.</p></c:if>
            <ul class="list-group">
                <c:forEach var="p" items="${programasInscripto}">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <c:out value="${p.programa()}"/>
                        <span class="small text-body-secondary">desde <e:fecha valor="${p.fechaInscripcion()}"/></span>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <%-- Seguidores y seguidos --%>
    <div class="tab-pane" id="tab-red" role="tabpanel">
        <div class="row g-4">
            <section class="col-md-6">
                <h2 class="h6 text-uppercase text-body-secondary">Seguidores (${seguidores.size()})</h2>
                <c:if test="${empty seguidores}"><p class="text-body-secondary small">Nadie lo sigue todavía.</p></c:if>
                <div class="list-group">
                    <c:forEach var="s" items="${seguidores}">
                        <c:url var="uS" value="/usuarios"><c:param name="nick" value="${s}"/></c:url>
                        <a class="list-group-item list-group-item-action" href="${uS}"><c:out value="${s}"/></a>
                    </c:forEach>
                </div>
            </section>
            <section class="col-md-6">
                <h2 class="h6 text-uppercase text-body-secondary">Sigue a (${seguidos.size()})</h2>
                <c:if test="${empty seguidos}"><p class="text-body-secondary small">No sigue a nadie.</p></c:if>
                <div class="list-group">
                    <c:forEach var="s" items="${seguidos}">
                        <c:url var="uS" value="/usuarios"><c:param name="nick" value="${s}"/></c:url>
                        <a class="list-group-item list-group-item-action" href="${uS}"><c:out value="${s}"/></a>
                    </c:forEach>
                </div>
            </section>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/comun/fin-pagina.jspf" %>
