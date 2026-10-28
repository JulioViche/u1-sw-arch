package ec.edu.monster.controlador;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para la raíz del contexto web RESTful ("/").
 * Muestra una página de bienvenida / documentación visual interactiva en el navegador
 * o el estado en JSON según el header Accept.
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
@Path("/")
public class RootController {

    private static final String HTML_PAGE = """
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Servidor REST · Conversor de Unidades</title>
            <style>
                * { box-sizing: border-box; margin: 0; padding: 0; }
                body {
                    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
                    background: #0f172a;
                    color: #e2e8f0;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    min-height: 100vh;
                    padding: 24px;
                }
                .card {
                    background: #1e293b;
                    border: 1px solid #334155;
                    border-radius: 16px;
                    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
                    max-width: 760px;
                    width: 100%;
                    padding: 36px;
                }
                .header {
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    margin-bottom: 20px;
                    border-bottom: 1px solid #334155;
                    padding-bottom: 16px;
                }
                .badge {
                    background: #065f46;
                    color: #34d399;
                    font-size: 0.75rem;
                    font-weight: 700;
                    letter-spacing: 0.05em;
                    padding: 6px 14px;
                    border-radius: 9999px;
                    text-transform: uppercase;
                }
                h1 {
                    color: #38bdf8;
                    font-size: 1.8rem;
                    margin-bottom: 6px;
                }
                .subtitle {
                    color: #94a3b8;
                    font-size: 0.95rem;
                    margin-bottom: 20px;
                    line-height: 1.5;
                }
                .meta-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                    gap: 12px;
                    background: #0f172a;
                    padding: 16px;
                    border-radius: 10px;
                    border: 1px solid #1e293b;
                    margin-bottom: 24px;
                }
                .meta-item strong {
                    color: #38bdf8;
                    display: block;
                    font-size: 0.8rem;
                    text-transform: uppercase;
                    margin-bottom: 2px;
                }
                .meta-item span {
                    color: #f1f5f9;
                    font-size: 0.95rem;
                }
                h2 {
                    color: #f1f5f9;
                    font-size: 1.2rem;
                    margin-bottom: 12px;
                }
                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-bottom: 24px;
                }
                th, td {
                    text-align: left;
                    padding: 12px;
                    border-bottom: 1px solid #334155;
                    font-size: 0.9rem;
                }
                th {
                    color: #94a3b8;
                    background: #0f172a;
                    text-transform: uppercase;
                    font-size: 0.75rem;
                    letter-spacing: 0.05em;
                }
                .method {
                    display: inline-block;
                    padding: 2px 8px;
                    border-radius: 4px;
                    font-weight: 700;
                    font-size: 0.75rem;
                    font-family: monospace;
                }
                .method-post { background: #0369a1; color: #bae6fd; }
                .method-get { background: #047857; color: #a7f3d0; }
                code {
                    font-family: 'Consolas', 'Courier New', monospace;
                    color: #f43f5e;
                    background: #0f172a;
                    padding: 2px 6px;
                    border-radius: 4px;
                }
                .actions {
                    display: flex;
                    gap: 12px;
                    margin-top: 20px;
                    flex-wrap: wrap;
                }
                .btn {
                    display: inline-block;
                    padding: 10px 20px;
                    border-radius: 8px;
                    font-weight: 600;
                    text-decoration: none;
                    font-size: 0.9rem;
                    transition: all 0.2s ease;
                }
                .btn-primary {
                    background: #0284c7;
                    color: white;
                }
                .btn-primary:hover {
                    background: #0369a1;
                }
            </style>
        </head>
        <body>
            <div class="card">
                <div class="header">
                    <div>
                        <h1>Servidor RESTful · ESPE</h1>
                        <p style="color: #94a3b8; font-size: 0.9rem;">Conversor de Unidades (SINBDD)</p>
                    </div>
                    <span class="badge">Activo · Payara Server</span>
                </div>

                <div class="meta-grid">
                    <div class="meta-item">
                        <strong>Tarea Oficial</strong>
                        <span>TI.1.3 · RESTful Java</span>
                    </div>
                    <div class="meta-item">
                        <strong>Autor & Grupo</strong>
                        <span>Julio Viche · GR09 · NRC 36884</span>
                    </div>
                    <div class="meta-item">
                        <strong>Arquitectura</strong>
                        <span>Jakarta REST / GoF Strategy</span>
                    </div>
                    <div class="meta-item">
                        <strong>Credenciales Fijas</strong>
                        <span>MONSTER / MONSTER9</span>
                    </div>
                </div>

                <h2>Endpoints del Servicio</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Método</th>
                            <th>Ruta</th>
                            <th>Descripción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><span class="method method-post">POST</span></td>
                            <td><code>/auth/login</code></td>
                            <td>Autenticación y generación de token de sesión</td>
                        </tr>
                        <tr>
                            <td><span class="method method-post">POST</span></td>
                            <td><code>/conversor/longitud</code></td>
                            <td>Conversión de longitud (5 unidades estrictas)</td>
                        </tr>
                        <tr>
                            <td><span class="method method-post">POST</span></td>
                            <td><code>/conversor/masa</code></td>
                            <td>Conversión de masa (5 unidades estrictas)</td>
                        </tr>
                        <tr>
                            <td><span class="method method-post">POST</span></td>
                            <td><code>/conversor/temperatura</code></td>
                            <td>Conversión de temperatura (5 unidades estrictas)</td>
                        </tr>
                        <tr>
                            <td><span class="method method-get">GET</span></td>
                            <td><code>/health</code></td>
                            <td>Comprobación de salud y estado del servicio</td>
                        </tr>
                    </tbody>
                </table>

                <div class="actions">
                    <a class="btn btn-primary" href="health" target="_blank">Consultar /health (JSON)</a>
                </div>
            </div>
        </body>
        </html>
        """;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getRootHtml() {
        return Response.ok(HTML_PAGE).build();
    }

    @GET
    @Path("index.html")
    @Produces(MediaType.TEXT_HTML)
    public Response getIndexHtml() {
        return Response.ok(HTML_PAGE).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRootJson() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "ServidorREST");
        status.put("protocol", "RESTful JSON");
        status.put("platform", "Java 17+ / Jakarta REST");
        status.put("author", "Julio Enrique Viche Castillo");
        status.put("nrc", "36884");
        status.put("grupo", "GR09");
        return Response.ok(status).build();
    }
}
