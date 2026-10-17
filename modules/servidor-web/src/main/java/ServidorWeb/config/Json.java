package ServidorWeb.config;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Respuestas JSON para los pedidos Ajax (fetch). Solo hace falta un objeto plano con textos, numeros,
 * booleanos o null, asi que se arma a mano y no se agrega una libreria.
 * Formato de la casa: {"ok": true, ...} o {"ok": false, "error": "mensaje para el usuario"}.
 */
public final class Json {

    private Json() {}

    public static void escribir(HttpServletResponse resp, int status, Map<String, ?> datos) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Cache-Control", "no-store");
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, ?> e : datos.entrySet()) {
            if (sb.length() > 1) {
                sb.append(',');
            }
            sb.append(texto(e.getKey())).append(':').append(valor(e.getValue()));
        }
        resp.getWriter().write(sb.append('}').toString());
    }

    /** {"ok": false, "error": mensaje} con el status indicado. */
    public static void error(HttpServletResponse resp, int status, String mensaje) throws IOException {
        escribir(resp, status, Map.of("ok", false, "error", mensaje));
    }

    private static String valor(Object v) {
        if (v == null) {
            return "null";
        }
        if (v instanceof Number || v instanceof Boolean) {
            return v.toString();
        }
        return texto(v.toString());
    }

    private static String texto(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.append('"').toString();
    }
}
