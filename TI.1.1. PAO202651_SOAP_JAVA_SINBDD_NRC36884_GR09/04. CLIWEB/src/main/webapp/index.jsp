<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.edu.monster.client.model.SessionModel"%>
<%
    // Si ya existe sesión activa, redirigir directo al conversor
    SessionModel sessionUser = (SessionModel) session.getAttribute("sessionUser");
    if (sessionUser != null && sessionUser.isActivo() && sessionUser.getToken() != null) {
        response.sendRedirect(request.getContextPath() + "/conversor.jsp");
        return;
    }

    String error = (String) request.getAttribute("error");
    String urlParamError = request.getParameter("error");
    if (error == null && "SesionInvalida".equals(urlParamError)) {
        error = "Su sesión no es válida o ha expirado. Por favor, autentíquese nuevamente.";
    }

    String mensaje = request.getParameter("mensaje");
    if ("SesionCerrada".equals(mensaje)) {
        mensaje = "Ha cerrado su sesión exitosamente en el servidor SOAP.";
    }

    String usuarioPrevio = (String) request.getAttribute("usuarioPrevio");
    if (usuarioPrevio == null) {
        usuarioPrevio = "MONSTER";
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acceso al Sistema - Conversor de Unidades SOAP (GR09)</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>

    <!-- Encabezado Institucional -->
    <header class="header-bar">
        <div class="header-brand">
            <h1>UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE</h1>
            <p>Arquitectura de Software (TI.1.1) · Grupo GR09 | Julio Enrique Viche Castillo</p>
        </div>
        <div class="header-user">
            <span class="badge-session">
                <span class="status-dot">●</span> Servidor SOAP: 8080
            </span>
        </div>
    </header>

    <!-- Contenedor del Formulario de Login -->
    <main class="main-wrapper">
        <div class="card-box card-login">
            <h2 class="card-title">Iniciar Sesión</h2>
            <p class="card-subtitle">Autenticación requerida para acceder al Conversor SOAP (SINBDD)</p>

            <% if (error != null) { %>
                <div class="alert alert-error">
                    <%= error %>
                </div>
            <% } %>

            <% if (mensaje != null) { %>
                <div class="alert alert-info">
                    <%= mensaje %>
                </div>
            <% } %>

            <form action="<%=request.getContextPath()%>/login" method="POST">
                <div class="form-group">
                    <label class="form-label" for="txtUsuario">Usuario:</label>
                    <input type="text" id="txtUsuario" name="usuario" class="form-input" 
                           value="<%= usuarioPrevio %>" required autofocus>
                </div>

                <div class="form-group">
                    <label class="form-label" for="txtClave">Contraseña:</label>
                    <input type="password" id="txtClave" name="clave" class="form-input" 
                           value="MONSTER9" required>
                </div>

                <div class="alert alert-info" style="font-size: 0.8rem; padding: 8px 12px; margin-bottom: 20px;">
                    <strong>Credenciales del curso:</strong> Usuario: <code>MONSTER</code> | Clave: <code>MONSTER9</code>
                </div>

                <button type="submit" class="btn-primary">Ingresar al Conversor</button>
            </form>
        </div>
    </main>

    <!-- Barra de Estado Inferior -->
    <footer class="footer-bar">
        <span>Servidor Backend: <code>http://localhost:8080/ServidorSOAP</code> (Modalidad SINBDD)</span>
        <span>Patrón Arquitectónico: Modelo - Vista - Controlador (JSP / Servlet)</span>
    </footer>

</body>
</html>
