package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.LoginResponse;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.rest.RestClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * [CONTROLADOR] Servlet para el procesamiento del inicio de sesión RESTful.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final RestClient restClient = new RestClient();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String usuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");

        if (usuario == null || usuario.trim().isEmpty() || clave == null || clave.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/index.html?error=" +
                    URLEncoder.encode("Por favor, complete todos los campos de acceso.", StandardCharsets.UTF_8));
            return;
        }

        try {
            LoginResponse loginResp = restClient.login(usuario.trim(), clave.trim());

            if (loginResp != null && loginResp.isExito()) {
                HttpSession session = request.getSession(true);
                SessionModel sessionModel = new SessionModel(usuario.trim(), loginResp.getToken());
                session.setAttribute("sessionUser", sessionModel);

                response.sendRedirect(request.getContextPath() + "/conversor.html?usuario=" +
                        URLEncoder.encode(usuario.trim(), StandardCharsets.UTF_8) +
                        "&token=" + sessionModel.getTokenAbreviado());
            } else {
                String errorMsg = (loginResp != null) ? loginResp.getMensaje() : "Credenciales inválidas.";
                response.sendRedirect(request.getContextPath() + "/index.html?error=" +
                        URLEncoder.encode(errorMsg, StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            String errorMsg = "Error de comunicación con el Servidor REST: " + e.getMessage();
            response.sendRedirect(request.getContextPath() + "/index.html?error=" +
                    URLEncoder.encode(errorMsg, StandardCharsets.UTF_8));
        }
    }
}
