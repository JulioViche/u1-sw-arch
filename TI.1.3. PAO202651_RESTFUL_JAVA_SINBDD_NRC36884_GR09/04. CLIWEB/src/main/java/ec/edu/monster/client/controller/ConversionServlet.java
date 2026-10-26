package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
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
import java.util.Locale;

/**
 * [CONTROLADOR] Servlet para el procesamiento de conversiones RESTful JSON.
 * Formateo numérico estricto a 2 decimales con punto decimal (Locale.US)
 * y límite de 16 caracteres.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
@WebServlet(name = "ConversionServlet", urlPatterns = {"/convertir"})
public class ConversionServlet extends HttpServlet {

    private final RestClient restClient = new RestClient();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        SessionModel sessionUser = (session != null) ? (SessionModel) session.getAttribute("sessionUser") : null;

        // Validación estricta de sesión activa (SINBDD)
        if (sessionUser == null || !sessionUser.isActivo() || sessionUser.getToken() == null) {
            response.sendRedirect(request.getContextPath() + "/index.html?error=SesionInvalida");
            return;
        }

        String magnitud = request.getParameter("magnitud");
        String unidadOrigen = request.getParameter("unidadOrigen");
        String unidadDestino = request.getParameter("unidadDestino");
        String valorStr = request.getParameter("valor");

        if (magnitud == null || unidadOrigen == null || unidadDestino == null || valorStr == null) {
            response.sendRedirect(request.getContextPath() + "/conversor.html?error=" +
                    URLEncoder.encode("Faltan parámetros obligatorios para la conversión.", StandardCharsets.UTF_8));
            return;
        }

        double valor;
        try {
            String valorLimpio = valorStr.trim();
            if (valorLimpio.length() > 16) {
                response.sendRedirect(request.getContextPath() + "/conversor.html?error=" +
                        URLEncoder.encode("El valor numérico no puede superar los 16 caracteres.", StandardCharsets.UTF_8));
                return;
            }
            if (!valorLimpio.matches("^\\d+(\\.\\d+)?$")) {
                response.sendRedirect(request.getContextPath() + "/conversor.html?error=" +
                        URLEncoder.encode("Solo se permiten números y el punto decimal '.' (ejemplo: 25.5 o 100).", StandardCharsets.UTF_8));
                return;
            }
            valor = Double.parseDouble(valorLimpio);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/conversor.html?error=" +
                    URLEncoder.encode("Formato numérico no válido.", StandardCharsets.UTF_8));
            return;
        }

        try {
            ConversionResponse resp = restClient.convertir(
                    magnitud,
                    unidadOrigen,
                    unidadDestino,
                    valor,
                    sessionUser.getToken()
            );

            if (resp != null && resp.isExito()) {
                String valorOrigenFmt = String.format(Locale.US, "%.2f", valor);
                String valorDestinoFmt = String.format(Locale.US, "%.2f", resp.getValorConvertido());

                String resultadoTexto = valorDestinoFmt + " " + capitalizar(unidadDestino);
                String detalleTexto = valorOrigenFmt + " " + capitalizar(unidadOrigen) + " equivale a " + resultadoTexto;

                response.sendRedirect(request.getContextPath() + "/conversor.html?resultado=" +
                        URLEncoder.encode(resultadoTexto, StandardCharsets.UTF_8) +
                        "&detalle=" + URLEncoder.encode(detalleTexto, StandardCharsets.UTF_8) +
                        "&usuario=" + URLEncoder.encode(sessionUser.getUsuario(), StandardCharsets.UTF_8) +
                        "&token=" + sessionUser.getTokenAbreviado());
            } else {
                String errorMsg = (resp != null) ? resp.getMensajeError() : "Sin respuesta del servidor REST";
                response.sendRedirect(request.getContextPath() + "/conversor.html?error=" +
                        URLEncoder.encode(errorMsg, StandardCharsets.UTF_8) +
                        "&usuario=" + URLEncoder.encode(sessionUser.getUsuario(), StandardCharsets.UTF_8) +
                        "&token=" + sessionUser.getTokenAbreviado());
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/conversor.html?error=" +
                    URLEncoder.encode("Error de comunicación: " + e.getMessage(), StandardCharsets.UTF_8) +
                    "&usuario=" + URLEncoder.encode(sessionUser.getUsuario(), StandardCharsets.UTF_8) +
                    "&token=" + sessionUser.getTokenAbreviado());
        }
    }

    private String capitalizar(String str) {
        if (str == null || str.isEmpty()) return "";
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
