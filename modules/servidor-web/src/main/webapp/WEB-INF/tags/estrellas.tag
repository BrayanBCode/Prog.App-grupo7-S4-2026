<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ attribute name="valor" required="true" type="java.lang.Number" %>
<%--
  Puntaje de 0 a 5 en 5 estrellas (requerimiento 7.6, Figura 6). Admite decimales: las estrellas doradas se
  superponen a las grises y se recortan al porcentaje del valor. Uso: <e:estrellas valor="${3.5}"/>
--%>
<span class="estrellas" role="img" aria-label="${valor} de 5 estrellas"><span
        class="estrellas-relleno" style="width: ${valor * 20}%" aria-hidden="true">★★★★★</span><span
        aria-hidden="true">★★★★★</span></span>
