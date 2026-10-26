package ec.edu.monster.client.controller;

import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.SessionModel;
import ec.edu.monster.client.ws.IServicioConversion;
import ec.edu.monster.client.ws.SoapClientManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * [CONTROLADOR] Servlet para el procesamiento del cómputo de conversiones vía SOAP.
 */
@WebServlet(name = "ConversionServlet", urlPatterns = {"/convertir"})
public class ConversionServlet extends HttpServlet {

    private final SoapClientManager clientManager = new SoapClientManager();

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
            request.setAttribute("error", "Faltan parámetros obligatorios para la conversión.");
            request.getRequestDispatcher("conversor.jsp").forward(request, response);
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "El valor numérico ingresado no es válido.");
            request.getRequestDispatcher("conversor.jsp").forward(request, response);
            return;
        }

        ConversionRequest convReq = new ConversionRequest(magnitud, unidadOrigen, unidadDestino, valor);
        String token = sessionUser.getToken();

        try {
            IServicioConversion convService = clientManager.getConversionService();
            ConversionResponse convResp;

            switch (magnitud.toLowerCase()) {
                case "longitud":
                    convResp = convService.convertirLongitud(token, valor, unidadOrigen, unidadDestino);
                    break;
                case "masa":
                    convResp = convService.convertirMasa(token, valor, unidadOrigen, unidadDestino);
                    break;
                case "temperatura":
                    convResp = convService.convertirTemperatura(token, valor, unidadOrigen, unidadDestino);
                    break;
                default:
                    convResp = new ConversionResponse(false, 0.0, "Magnitud física desconocida: " + magnitud);
            }

            if (convResp != null && convResp.isExito()) {
                String params = String.format(java.util.Locale.US, "valor=%.4f&origen=%s&destino=%s&resultado=%.4f&magnitud=%s&usuario=%s&token=%s",
                        convReq.getValor(),
                        java.net.URLEncoder.encode(convReq.getUnidadOrigen(), java.nio.charset.StandardCharsets.UTF_8),
                        java.net.URLEncoder.encode(convReq.getUnidadDestino(), java.nio.charset.StandardCharsets.UTF_8),
                        convResp.getValorConvertido(),
                        java.net.URLEncoder.encode(convReq.getMagnitud(), java.nio.charset.StandardCharsets.UTF_8),
                        java.net.URLEncoder.encode(sessionUser.getUsuario(), java.nio.charset.StandardCharsets.UTF_8),
                        sessionUser.getTokenAbreviado());
                response.sendRedirect(request.getContextPath() + "/conversor.html?" + params);
            } else {
                String err = (convResp != null) ? convResp.getMensajeError() : "Error en el cálculo";
                String params = String.format("error=%s&magnitud=%s&usuario=%s&token=%s",
                        java.net.URLEncoder.encode(err, java.nio.charset.StandardCharsets.UTF_8),
                        java.net.URLEncoder.encode(convReq.getMagnitud(), java.nio.charset.StandardCharsets.UTF_8),
                        java.net.URLEncoder.encode(sessionUser.getUsuario(), java.nio.charset.StandardCharsets.UTF_8),
                        sessionUser.getTokenAbreviado());
                response.sendRedirect(request.getContextPath() + "/conversor.html?" + params);
            }

        } catch (Exception e) {
            String errorMsg = "Falla de comunicación con el servicio de conversión: " + e.getMessage();
            response.sendRedirect(request.getContextPath() + "/conversor.html?error=" + 
                    java.net.URLEncoder.encode(errorMsg, java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/conversor.html");
    }
}
