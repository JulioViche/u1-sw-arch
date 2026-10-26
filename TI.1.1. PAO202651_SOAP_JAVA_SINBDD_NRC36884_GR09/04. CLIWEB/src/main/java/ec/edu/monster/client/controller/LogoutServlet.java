package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.ws.IServicioAutenticacion;
import ec.edu.monster.client.ws.SoapClientManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * [CONTROLADOR] Servlet para el cierre de sesión y revocación del token en el servidor SOAP.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private final SoapClientManager clientManager = new SoapClientManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarLogout(request, response);
    }

    private void procesarLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            SessionModel sessionUser = (SessionModel) session.getAttribute("sessionUser");
            if (sessionUser != null && sessionUser.getToken() != null) {
                try {
                    IServicioAutenticacion authService = clientManager.getAuthService();
                    authService.cerrarSesion(sessionUser.getToken());
                } catch (Exception ignored) {
                }
            }
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/index.html?mensaje=SesionCerrada");
    }
}
