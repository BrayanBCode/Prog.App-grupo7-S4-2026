import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/inicio")
public class InicioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Acá llamarías a tu Controller de servidor-central
        // y guardarías el resultado para que el JSP lo muestre:
        req.setAttribute("titulo", "Inicio");

        req.getRequestDispatcher("/WEB-INF/views/sesion/login.jsp").forward(req, resp);
    }
}