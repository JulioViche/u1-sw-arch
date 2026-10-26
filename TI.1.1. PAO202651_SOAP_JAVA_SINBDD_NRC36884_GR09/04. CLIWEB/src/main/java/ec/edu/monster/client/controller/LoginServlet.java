package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.LoginResponse;
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
 * [CONTROLADOR] Servlet para el procesamiento del inicio de sesión SOAP.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final SoapClientManager clientManager = new SoapClientManager();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String usuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");

        if (usuario == null || usuario.trim().isEmpty() || clave == null || clave.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/index.html?error=" + 
                    java.net.URLEncoder.encode("Por favor, complete todos los campos de acceso.", java.nio.charset.StandardCharsets.UTF_8));
            return;
        }

        try {
            IServicioAutenticacion authService = clientManager.getAuthService();
            LoginResponse loginResp = authService.iniciarSesion(usuario.trim(), clave.trim());

            if (loginResp != null && loginResp.isExito()) {
                HttpSession session = request.getSession(true);
                SessionModel sessionModel = new SessionModel(usuario.trim(), loginResp.getToken());
                session.setAttribute("sessionUser", sessionModel);

                response.sendRedirect(request.getContextPath() + "/conversor.html?usuario=" + 
                        java.net.URLEncoder.encode(usuario.trim(), java.nio.charset.StandardCharsets.UTF_8) + 
                        "&token=" + sessionModel.getTokenAbreviado());
            } else {
                String errorMsg = (loginResp != null) ? loginResp.getMensaje() : "Credenciales inválidas.";
                response.sendRedirect(request.getContextPath() + "/index.html?error=" + 
                        java.net.URLEncoder.encode(errorMsg, java.nio.charset.StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            String errorMsg = "Error de comunicación con el Servidor SOAP: " + e.getMessage();
            response.sendRedirect(request.getContextPath() + "/index.html?error=" + 
                    java.net.URLEncoder.encode(errorMsg, java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/index.html");
    }
}
