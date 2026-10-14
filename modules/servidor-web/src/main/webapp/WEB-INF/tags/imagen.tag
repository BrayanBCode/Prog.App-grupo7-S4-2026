<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="ruta" required="false" %>
<%@ attribute name="alt" required="true" %>
<%@ attribute name="clase" required="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%-- Imagen de usuario/curso/edicion/programa. "ruta" es la ruta RELATIVA guardada en la BD
     (ej. cursos/Flor_del_Ceibo.jpg). Sin imagen se dibuja un recuadro con la inicial. --%>
<c:choose>
    <c:when test="${not empty ruta}">
        <img class="img ${clase}" src="${pageContext.request.contextPath}/imagenes/${ruta}" alt="<c:out value='${alt}'/>" loading="lazy">
    </c:when>
    <c:otherwise>
        <span class="img img-vacia ${clase}" role="img" aria-label="<c:out value='${alt}'/>"><c:out value="${fn:substring(alt, 0, 1)}"/></span>
    </c:otherwise>
</c:choose>
