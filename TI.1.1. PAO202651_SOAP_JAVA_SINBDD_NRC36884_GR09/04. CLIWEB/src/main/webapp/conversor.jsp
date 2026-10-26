<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.edu.monster.client.model.SessionModel"%>
<%@page import="ec.edu.monster.client.model.ConversionRequest"%>
<%@page import="ec.edu.monster.client.model.ConversionResponse"%>
<%
    // Validación obligatoria de sesión activa (SINBDD)
    SessionModel sessionUser = (SessionModel) session.getAttribute("sessionUser");
    if (sessionUser == null || !sessionUser.isActivo() || sessionUser.getToken() == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp?error=SesionInvalida");
        return;
    }

    ConversionRequest convReq = (ConversionRequest) request.getAttribute("conversionReq");
    ConversionResponse convResp = (ConversionResponse) request.getAttribute("conversionResp");
    String error = (String) request.getAttribute("error");

    String magnitudActual = (convReq != null) ? convReq.getMagnitud() : "Longitud";
    String unidadOrigenActual = (convReq != null) ? convReq.getUnidadOrigen() : "";
    String unidadDestinoActual = (convReq != null) ? convReq.getUnidadDestino() : "";
    double valorActual = (convReq != null) ? convReq.getValor() : 100.0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Conversor de Unidades SOAP - Panel Web (GR09)</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>

    <!-- Encabezado con sesión activa -->
    <header class="header-bar">
        <div class="header-brand">
            <h1>CONVERSOR DE UNIDADES (SOAP JAVA · SINBDD)</h1>
            <p>ESPE · Arquitectura de Software (TI.1.1) | Autor: Julio Enrique Viche Castillo</p>
        </div>
        <div class="header-user">
            <span class="badge-session">
                <span class="status-dot">●</span> 
                <strong><%= sessionUser.getUsuario() %></strong>
                <span style="opacity: 0.8; font-size: 0.75rem;">(Token: <%= sessionUser.getTokenAbreviado() %>)</span>
            </span>
            <a href="<%=request.getContextPath()%>/logout" class="btn-logout">Cerrar Sesión</a>
        </div>
    </header>

    <!-- Panel Central del Conversor -->
    <main class="main-wrapper">
        <div class="card-box">
            <h2 class="card-title">Conversor de Unidades Multimagnitud</h2>
            <p class="card-subtitle">Cálculos procesados mediante servicios web SOAP protegidos por token</p>

            <% if (error != null) { %>
                <div class="alert alert-error">
                    <%= error %>
                </div>
            <% } %>

            <!-- 1. Pestañas de Magnitud -->
            <label class="form-label">1. Seleccione la Magnitud Física:</label>
            <div class="segmented-group">
                <button type="button" class="segment-btn <%= "Longitud".equalsIgnoreCase(magnitudActual) ? "active" : "" %>"
                        onclick="cambiarMagnitud('Longitud')">
                    Longitud
                </button>
                <button type="button" class="segment-btn <%= "Masa".equalsIgnoreCase(magnitudActual) ? "active" : "" %>"
                        onclick="cambiarMagnitud('Masa')">
                    Masa
                </button>
                <button type="button" class="segment-btn <%= "Temperatura".equalsIgnoreCase(magnitudActual) ? "active" : "" %>"
                        onclick="cambiarMagnitud('Temperatura')">
                    Temperatura
                </button>
            </div>

            <!-- Formulario de Conversión -->
            <form action="<%=request.getContextPath()%>/convertir" method="POST" id="formConversion">
                <input type="hidden" id="inputMagnitud" name="magnitud" value="<%= magnitudActual %>">

                <!-- 2. Valor Numérico -->
                <div class="form-group">
                    <label class="form-label" for="txtValor">2. Ingrese el Valor a Convertir:</label>
                    <input type="number" step="any" id="txtValor" name="valor" class="form-input" 
                           value="<%= valorActual %>" required style="font-size: 1.15rem; font-weight: bold;">
                </div>

                <!-- 3. Unidades Origen -> Destino con Intercambio -->
                <div class="conversion-units-row">
                    <div>
                        <label class="form-label" for="cmbOrigen">De (Unidad de Origen):</label>
                        <select id="cmbOrigen" name="unidadOrigen" class="form-select" required></select>
                    </div>

                    <div>
                        <button type="button" class="btn-swap" onclick="intercambiarUnidades()" title="Intercambiar unidades (Origen ⇄ Destino)">
                            &#8644;
                        </button>
                    </div>

                    <div>
                        <label class="form-label" for="cmbDestino">A (Unidad de Destino):</label>
                        <select id="cmbDestino" name="unidadDestino" class="form-select" required></select>
                    </div>
                </div>

                <button type="submit" class="btn-primary">Calcular Conversión SOAP</button>
            </form>

            <!-- 4. Tarjeta de Resultado -->
            <% if (convResp != null) { %>
                <% if (convResp.isExito()) { %>
                    <div class="result-card">
                        <div class="result-value" id="resultadoTexto">
                            <%= String.format("%.4f", convReq.getValor()) %> <%= convReq.getUnidadOrigen() %> = <%= String.format("%.4f", convResp.getValorConvertido()) %> <%= convReq.getUnidadDestino() %>
                        </div>
                        <div class="result-subtext">
                            Cálculo procesado exitosamente por el Servidor SOAP en memoria (SINBDD).
                        </div>
                        <button type="button" class="btn-copy" onclick="copiarResultado()">Copiar Resultado</button>
                    </div>
                <% } else { %>
                    <div class="result-card error">
                        <div class="result-value">
                            Error en Conversión
                        </div>
                        <div class="result-subtext">
                            <%= convResp.getMensajeError() %>
                        </div>
                    </div>
                <% } %>
            <% } %>

        </div>
    </main>

    <!-- Barra de Estado Inferior -->
    <footer class="footer-bar">
        <span>Servicio SOAP: <code>http://localhost:8080/ServidorSOAP/ServicioConversion?wsdl</code></span>
        <span>Arquitectura: Modelo - Vista - Controlador (JSP / Servlet / JAX-WS)</span>
    </footer>

    <!-- Catálogo de JavaScript para unidades dinámicas -->
    <script>
        const CATALOGO = {
            'Longitud': ['Metro', 'Kilometro', 'Centimetro', 'Milimetro', 'Milla', 'Yarda', 'Pie', 'Pulgada'],
            'Masa': ['Kilogramo', 'Gramo', 'Miligramo', 'Libra', 'Onza', 'Tonelada'],
            'Temperatura': ['Celsius', 'Fahrenheit', 'Kelvin']
        };

        const unidadOrigenPrevia = '<%= unidadOrigenActual %>';
        const unidadDestinoPrevia = '<%= unidadDestinoActual %>';

        function cambiarMagnitud(magnitud) {
            document.getElementById('inputMagnitud').value = magnitud;

            // Actualizar estilo visual de los botones de pestaña
            document.querySelectorAll('.segment-btn').forEach(btn => {
                btn.classList.remove('active');
                if (btn.innerText.trim().toLowerCase() === magnitud.toLowerCase()) {
                    btn.classList.add('active');
                }
            });

            // Actualizar selectores de unidades
            const cmbOrigen = document.getElementById('cmbOrigen');
            const cmbDestino = document.getElementById('cmbDestino');
            cmbOrigen.innerHTML = '';
            cmbDestino.innerHTML = '';

            const unidades = CATALOGO[magnitud] || [];
            unidades.forEach((u, idx) => {
                const opt1 = new Option(u, u);
                const opt2 = new Option(u, u);
                cmbOrigen.add(opt1);
                cmbDestino.add(opt2);
            });

            if (unidades.length > 1) {
                cmbDestino.selectedIndex = 1;
            }

            // Restaurar selección previa si coincide con la magnitud
            if (unidadOrigenPrevia && unidades.includes(unidadOrigenPrevia)) {
                cmbOrigen.value = unidadOrigenPrevia;
            }
            if (unidadDestinoPrevia && unidades.includes(unidadDestinoPrevia)) {
                cmbDestino.value = unidadDestinoPrevia;
            }
        }

        function intercambiarUnidades() {
            const cmbOrigen = document.getElementById('cmbOrigen');
            const cmbDestino = document.getElementById('cmbDestino');
            const temp = cmbOrigen.value;
            cmbOrigen.value = cmbDestino.value;
            cmbDestino.value = temp;
        }

        function copiarResultado() {
            const texto = document.getElementById('resultadoTexto').innerText.trim();
            navigator.clipboard.writeText(texto).then(() => {
                const btn = event.target;
                const orig = btn.innerText;
                btn.innerText = '¡Copiado!';
                setTimeout(() => { btn.innerText = orig; }, 1500);
            });
        }

        // Inicializar al cargar la página
        window.onload = function() {
            cambiarMagnitud('<%= magnitudActual %>');
        };
    </script>

</body>
</html>
