
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author briha
 */
@WebServlet("/logout")
public class logoutServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Acá llamarías a tu Controller de servidor-central
        // y guardarías el resultado para que el JSP lo muestre:
        req.setAttribute("titulo", "Inicio");

        req.getRequestDispatcher("/WEB-INF/views/sesion/logout.jsp").forward(req, resp);
    }
    
    
    
}
