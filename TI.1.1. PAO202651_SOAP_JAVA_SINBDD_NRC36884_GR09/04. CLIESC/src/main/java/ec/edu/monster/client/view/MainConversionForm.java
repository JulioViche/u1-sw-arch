package ec.edu.monster.client.view;

import ec.edu.monster.client.controller.DesktopAppController;
import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.SessionModel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.SwingConstants;

/**
 * [VISTA] Ventana gráfica principal del Conversor de Unidades SOAP (Swing).
 * Diseño ejecutivo, responsivo y adaptativo bajo patrón MVC.
 */
public class MainConversionForm extends JFrame {

    private final DesktopAppController controller;
    private final SessionModel sessionModel;

    // Catálogo de magnitudes y sus unidades
    private static final String[] MAGNITUDES = {"Longitud", "Masa", "Temperatura"};
    private static final Map<String, String[]> CATALOGO_UNIDADES = new HashMap<>();

    static {
        CATALOGO_UNIDADES.put("Longitud", new String[]{
            "Metro", "Centimetro", "Pie", "Yarda", "Milla"
        });
        CATALOGO_UNIDADES.put("Masa", new String[]{
            "Gramo", "Kilogramo", "Libra", "Onza", "Quintal"
        });
        CATALOGO_UNIDADES.put("Temperatura", new String[]{
            "Celsius", "Fahrenheit", "Kelvin", "Newton", "Reaumur"
        });
    }

    private String magnitudSeleccionada = "Longitud";

    // Componentes de interfaz
    private JButton btnTabLongitud;
    private JButton btnTabMasa;
    private JButton btnTabTemperatura;

    private JComboBox<String> cmbOrigen;
    private JComboBox<String> cmbDestino;
    private JTextField txtValor;
    private JButton btnIntercambiar;
    private JButton btnConvertir;
    private JButton btnCerrarSesion;
    private JButton btnCopiar;

    private JPanel pnlCardResultado;
    private JLabel lblResultado;
    private JLabel lblDetalleResultado;
    private String ultimoResultadoFormateado = "";

    public MainConversionForm(DesktopAppController controller, SessionModel sessionModel) {
        this.controller = controller;
        this.sessionModel = sessionModel;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Conversor de Unidades Multimagnitud (SOAP) - ESPE GR09");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(840, 680);
        setMinimumSize(new Dimension(800, 640));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. BARRA SUPERIOR (HEADER)
        add(crearBarraSuperior(), BorderLayout.NORTH);

        // 2. CONTENIDO PRINCIPAL (TARJETA CENTRAL)
        JPanel pnlFondo = new JPanel(new GridBagLayout());
        pnlFondo.setBackground(new Color(241, 245, 249)); // Slate claro #F1F5F9
        pnlFondo.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel pnlTarjeta = crearTarjetaConversor();
        GridBagConstraints gbcT = new GridBagConstraints();
        gbcT.gridx = 0;
        gbcT.gridy = 0;
        gbcT.weightx = 1.0;
        gbcT.fill = GridBagConstraints.HORIZONTAL;
        pnlFondo.add(pnlTarjeta, gbcT);

        add(pnlFondo, BorderLayout.CENTER);

        // 3. BARRA INFERIOR DE ESTADO (STATUS BAR)
        add(crearBarraEstado(), BorderLayout.SOUTH);

        // Estado inicial
        seleccionarMagnitud("Longitud");
        getRootPane().setDefaultButton(btnConvertir);
    }

    private JPanel crearBarraSuperior() {
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(15, 44, 89)); // Azul marino ejecutivo
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        // Título y Cátedra (Izquierda)
        JPanel pnlTitulos = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlTitulos.setOpaque(false);

        JLabel lblTitulo = new JLabel("CONVERSOR DE UNIDADES (SOAP JAVA · SINBDD)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("ESPE · Arquitectura de Software (TI.1.1) | Autor: Julio Enrique Viche Castillo");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(203, 228, 255));

        pnlTitulos.add(lblTitulo);
        pnlTitulos.add(lblSub);
        pnlHeader.add(pnlTitulos, BorderLayout.WEST);

        // Información de Sesión y Botón Logout (Derecha)
        JPanel pnlDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        pnlDerecha.setOpaque(false);

        // Badge de usuario conectado
        JPanel pnlBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        pnlBadge.setBackground(new Color(26, 62, 115));
        pnlBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 105, 170), 1),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));

        JLabel lblDot = new JLabel("●");
        lblDot.setForeground(new Color(52, 211, 153)); // Verde activo
        lblDot.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel lblUser = new JLabel("Usuario: " + sessionModel.getUsuario());
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(Color.WHITE);

        JLabel lblToken = new JLabel(" | Token: " + sessionModel.getTokenAbreviado());
        lblToken.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblToken.setForeground(new Color(186, 218, 255));

        pnlBadge.add(lblDot);
        pnlBadge.add(lblUser);
        pnlBadge.add(lblToken);
        pnlDerecha.add(pnlBadge);

        // Botón Cerrar Sesión
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnCerrarSesion.setBackground(new Color(185, 28, 28)); // Rojo moderno
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(153, 27, 27)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        btnCerrarSesion.addActionListener(e -> controller.procesarLogout());
        pnlDerecha.add(btnCerrarSesion);

        pnlHeader.add(pnlDerecha, BorderLayout.EAST);
        return pnlHeader;
    }

    private JPanel crearTarjetaConversor() {
        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(24, 30, 24, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // 1. Selector de Magnitud (Botones Segmentados)
        JLabel lblSec1 = new JLabel("1. Seleccione la Magnitud Física:");
        lblSec1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSec1.setForeground(new Color(30, 41, 59));
        gbc.gridy = 0;
        tarjeta.add(lblSec1, gbc);

        JPanel pnlSegmentos = new JPanel(new GridLayout(1, 3, 10, 0));
        pnlSegmentos.setOpaque(false);

        btnTabLongitud = crearBotonSegmento("Longitud");
        btnTabMasa = crearBotonSegmento("Masa");
        btnTabTemperatura = crearBotonSegmento("Temperatura");

        btnTabLongitud.addActionListener(e -> seleccionarMagnitud("Longitud"));
        btnTabMasa.addActionListener(e -> seleccionarMagnitud("Masa"));
        btnTabTemperatura.addActionListener(e -> seleccionarMagnitud("Temperatura"));

        pnlSegmentos.add(btnTabLongitud);
        pnlSegmentos.add(btnTabMasa);
        pnlSegmentos.add(btnTabTemperatura);

        gbc.gridy = 1;
        gbc.insets = new Insets(4, 6, 16, 6);
        tarjeta.add(pnlSegmentos, gbc);

        // 2. Campo de Valor Numérico
        JLabel lblSec2 = new JLabel("2. Ingrese el Valor Numérico a Convertir:");
        lblSec2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSec2.setForeground(new Color(30, 41, 59));
        gbc.gridy = 2;
        gbc.insets = new Insets(4, 6, 4, 6);
        tarjeta.add(lblSec2, gbc);

        txtValor = new JTextField("100.00");
        txtValor.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtValor.setPreferredSize(new Dimension(300, 40));
        txtValor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        gbc.gridy = 3;
        gbc.insets = new Insets(2, 6, 16, 6);
        configurarFiltroNumerico(txtValor);
        tarjeta.add(txtValor, gbc);

        // 3. Selectores de Unidades (Origen <-> Destino) con botón de intercambio interactivo
        JLabel lblSec3 = new JLabel("3. Configuración de Unidades (Origen y Destino):");
        lblSec3.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSec3.setForeground(new Color(30, 41, 59));
        gbc.gridy = 4;
        gbc.insets = new Insets(4, 6, 4, 6);
        tarjeta.add(lblSec3, gbc);

        JPanel pnlUnidadesFila = new JPanel(new GridBagLayout());
        pnlUnidadesFila.setOpaque(false);
        GridBagConstraints gbcU = new GridBagConstraints();
        gbcU.fill = GridBagConstraints.HORIZONTAL;
        gbcU.insets = new Insets(0, 4, 0, 4);

        // Columna Origen
        JPanel pnlColOrigen = new JPanel(new BorderLayout(0, 4));
        pnlColOrigen.setOpaque(false);
        JLabel lblOrigen = new JLabel("De (Unidad de Origen):");
        lblOrigen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblOrigen.setForeground(new Color(71, 85, 105));
        cmbOrigen = new JComboBox<>();
        cmbOrigen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbOrigen.setPreferredSize(new Dimension(280, 38));
        pnlColOrigen.add(lblOrigen, BorderLayout.NORTH);
        pnlColOrigen.add(cmbOrigen, BorderLayout.CENTER);

        gbcU.gridx = 0;
        gbcU.weightx = 0.45;
        pnlUnidadesFila.add(pnlColOrigen, gbcU);

        // Columna Botón Intercambiar (Vector Icon)
        JPanel pnlColBoton = new JPanel(new BorderLayout(0, 4));
        pnlColBoton.setOpaque(false);
        JLabel lblVacio = new JLabel(" ");
        lblVacio.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Botón de intercambio con icono dibujado por vectores (nunca falla en renderizado)
        btnIntercambiar = new JButton(new VectorSwapArrowsIcon(24, 18, new Color(15, 44, 89)));
        btnIntercambiar.setToolTipText("Intercambiar unidades (Origen ⇄ Destino)");
        btnIntercambiar.setPreferredSize(new Dimension(54, 38));
        btnIntercambiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIntercambiar.setFocusPainted(false);
        btnIntercambiar.setBackground(new Color(241, 245, 249));
        btnIntercambiar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        btnIntercambiar.addActionListener(e -> intercambiarUnidades());

        pnlColBoton.add(lblVacio, BorderLayout.NORTH);
        pnlColBoton.add(btnIntercambiar, BorderLayout.CENTER);

        gbcU.gridx = 1;
        gbcU.weightx = 0.10;
        gbcU.fill = GridBagConstraints.NONE;
        pnlUnidadesFila.add(pnlColBoton, gbcU);

        // Columna Destino
        JPanel pnlColDestino = new JPanel(new BorderLayout(0, 4));
        pnlColDestino.setOpaque(false);
        JLabel lblDestino = new JLabel("A (Unidad de Destino):");
        lblDestino.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDestino.setForeground(new Color(71, 85, 105));
        cmbDestino = new JComboBox<>();
        cmbDestino.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbDestino.setPreferredSize(new Dimension(280, 38));
        pnlColDestino.add(lblDestino, BorderLayout.NORTH);
        pnlColDestino.add(cmbDestino, BorderLayout.CENTER);

        gbcU.gridx = 2;
        gbcU.weightx = 0.45;
        gbcU.fill = GridBagConstraints.HORIZONTAL;
        pnlUnidadesFila.add(pnlColDestino, gbcU);

        gbc.gridy = 5;
        gbc.insets = new Insets(2, 6, 20, 6);
        tarjeta.add(pnlUnidadesFila, gbc);

        // 4. Botón Acción Principal "Calcular Conversión"
        btnConvertir = new JButton("Calcular Conversión SOAP");
        btnConvertir.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnConvertir.setBackground(new Color(15, 60, 110)); // Azul institucional
        btnConvertir.setForeground(Color.WHITE);
        btnConvertir.setPreferredSize(new Dimension(300, 44));
        btnConvertir.setFocusPainted(false);
        btnConvertir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConvertir.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnConvertir.addActionListener(e -> ejecutarConversion());

        gbc.gridy = 6;
        gbc.insets = new Insets(4, 6, 20, 6);
        tarjeta.add(btnConvertir, gbc);

        // 5. Tarjeta de Resultado (Dashboard Result Card)
        pnlCardResultado = new JPanel(new BorderLayout(10, 8));
        pnlCardResultado.setBackground(new Color(240, 253, 250)); // Soft Teal #F0FDF4
        pnlCardResultado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(153, 246, 228), 1),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));

        lblResultado = new JLabel("Presione 'Calcular Conversión SOAP' para iniciar", SwingConstants.CENTER);
        lblResultado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblResultado.setForeground(new Color(15, 118, 110));

        JPanel pnlSubResultado = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pnlSubResultado.setOpaque(false);

        lblDetalleResultado = new JLabel("Listo para despachar peticiones al puerto 8080");
        lblDetalleResultado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDetalleResultado.setForeground(new Color(71, 85, 105));
        pnlSubResultado.add(lblDetalleResultado);

        btnCopiar = new JButton("Copiar");
        btnCopiar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnCopiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCopiar.setFocusPainted(false);
        btnCopiar.setBackground(Color.WHITE);
        btnCopiar.setVisible(false);
        btnCopiar.addActionListener(e -> copiarResultadoPortapapeles());
        pnlSubResultado.add(btnCopiar);

        pnlCardResultado.add(lblResultado, BorderLayout.CENTER);
        pnlCardResultado.add(pnlSubResultado, BorderLayout.SOUTH);

        gbc.gridy = 7;
        gbc.insets = new Insets(4, 6, 6, 6);
        tarjeta.add(pnlCardResultado, gbc);

        return tarjeta;
    }

    private JPanel crearBarraEstado() {
        JPanel pnlStatus = new JPanel(new BorderLayout());
        pnlStatus.setBackground(new Color(248, 250, 252));
        pnlStatus.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(6, 20, 6, 20)
        ));

        JLabel lblServer = new JLabel("Servidor activo: http://localhost:8080/ServidorSOAP | Modo: SINBDD");
        lblServer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblServer.setForeground(new Color(100, 116, 139));
        pnlStatus.add(lblServer, BorderLayout.WEST);

        JLabel lblPatron = new JLabel("Arquitectura: Modelo - Vista - Controlador (MVC)");
        lblPatron.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblPatron.setForeground(new Color(100, 116, 139));
        pnlStatus.add(lblPatron, BorderLayout.EAST);

        return pnlStatus;
    }

    private JButton crearBotonSegmento(String titulo) {
        JButton btn = new JButton(titulo);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 36));
        return btn;
    }

    private void seleccionarMagnitud(String magnitud) {
        this.magnitudSeleccionada = magnitud;

        // Estilos de botones segmentados
        actualizarEstiloBotonSegmento(btnTabLongitud, magnitud.equals("Longitud"));
        actualizarEstiloBotonSegmento(btnTabMasa, magnitud.equals("Masa"));
        actualizarEstiloBotonSegmento(btnTabTemperatura, magnitud.equals("Temperatura"));

        // Cargar unidades correspondientes
        String[] unidades = CATALOGO_UNIDADES.getOrDefault(magnitud, new String[0]);
        cmbOrigen.setModel(new DefaultComboBoxModel<>(unidades));
        cmbDestino.setModel(new DefaultComboBoxModel<>(unidades));

        if (unidades.length > 1) {
            cmbDestino.setSelectedIndex(1);
        }
    }

    private void actualizarEstiloBotonSegmento(JButton btn, boolean activo) {
        if (activo) {
            btn.setBackground(new Color(15, 60, 110)); // Seleccionado
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(new Color(10, 45, 85), 1));
        } else {
            btn.setBackground(new Color(241, 245, 249)); // Inactivo
            btn.setForeground(new Color(51, 65, 85));
            btn.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
        }
    }

    private void intercambiarUnidades() {
        int idxOrig = cmbOrigen.getSelectedIndex();
        int idxDest = cmbDestino.getSelectedIndex();
        cmbOrigen.setSelectedIndex(idxDest);
        cmbDestino.setSelectedIndex(idxOrig);
    }

    private void ejecutarConversion() {
        String origen = (String) cmbOrigen.getSelectedItem();
        String destino = (String) cmbDestino.getSelectedItem();
        String valorStr = txtValor.getText().trim();
        if (valorStr.length() > 16) {
            mostrarError("El valor no puede superar los 16 caracteres.");
            return;
        }
        if (!valorStr.matches("^\\d+(\\.\\d+)?$")) {
            mostrarError("Por favor, ingrese un número válido (solo números y el punto decimal '.').");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            mostrarError("Por favor, ingrese un número válido (ejemplo: 100 o 25.5).");
            return;
        }

        btnConvertir.setEnabled(false);
        lblResultado.setText("Consultando al Servidor SOAP...");
        lblResultado.setForeground(new Color(15, 60, 110));
        pnlCardResultado.setBackground(new Color(240, 249, 255));
        pnlCardResultado.setBorder(BorderFactory.createLineBorder(new Color(186, 230, 253), 1));

        new Thread(() -> {
            try {
                ConversionRequest request = new ConversionRequest(magnitudSeleccionada, origen, destino, valor);
                controller.procesarConversion(request);
            } finally {
                btnConvertir.setEnabled(true);
            }
        }).start();
    }

    public void mostrarResultado(ConversionRequest req, ConversionResponse resp) {
        if (resp != null && resp.isExito()) {
            ultimoResultadoFormateado = String.format(java.util.Locale.US, "%.2f %s = %.2f %s",
                    req.getValor(), req.getUnidadOrigen(), resp.getValorConvertido(), req.getUnidadDestino());

            lblResultado.setText(ultimoResultadoFormateado);
            lblResultado.setForeground(new Color(15, 118, 110)); // Verde azulado elegante
            lblDetalleResultado.setText("Conversión SOAP procesada exitosamente en memoria (SINBDD).");
            pnlCardResultado.setBackground(new Color(240, 253, 250));
            pnlCardResultado.setBorder(BorderFactory.createLineBorder(new Color(153, 246, 228), 1));
            btnCopiar.setVisible(true);
        } else {
            String err = (resp != null) ? resp.getMensajeError() : "Sin respuesta del servidor";
            mostrarError(err);
        }
    }

    public void mostrarError(String mensaje) {
        lblResultado.setText(mensaje);
        lblResultado.setForeground(new Color(220, 38, 38));
        lblDetalleResultado.setText("Ocurrió un error en la solicitud.");
        pnlCardResultado.setBackground(new Color(254, 242, 242));
        pnlCardResultado.setBorder(BorderFactory.createLineBorder(new Color(254, 202, 202), 1));
        btnCopiar.setVisible(false);
    }

    private void copiarResultadoPortapapeles() {
        if (!ultimoResultadoFormateado.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ultimoResultadoFormateado), null);
            btnCopiar.setText("¡Copiado!");
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    btnCopiar.setText("Copiar");
                } catch (InterruptedException ignored) {
                }
            }).start();
        }
    }

    /**
     * [ICONO VECTORIAL] Dibuja flechas dobles de intercambio con anti-aliasing.
     * Es 100% independiente de las fuentes tipográficas del sistema operativo.
     */
    private static class VectorSwapArrowsIcon implements Icon {
        private final int width;
        private final int height;
        private final Color color;

        public VectorSwapArrowsIcon(int width, int height, Color color) {
            this.width = width;
            this.height = height;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int topY = y + (height / 3);
            int botY = y + (2 * height / 3);
            int startX = x + 3;
            int endX = x + width - 3;

            // Flecha superior hacia la derecha: ---->
            g2.drawLine(startX, topY, endX, topY);
            g2.drawLine(endX - 5, topY - 4, endX, topY);
            g2.drawLine(endX - 5, topY + 4, endX, topY);

            // Flecha inferior hacia la izquierda: <----
            g2.drawLine(endX, botY, startX, botY);
            g2.drawLine(startX + 5, botY - 4, startX, botY);
            g2.drawLine(startX + 5, botY + 4, startX, botY);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }

    private void configurarFiltroNumerico(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String nuevo = actual.substring(0, offset) + string + actual.substring(offset);
                if (nuevo.length() <= 16 && esNumeroValido(nuevo)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) text = "";
                String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
                String nuevo = actual.substring(0, offset) + text + actual.substring(offset + length);
                if (nuevo.length() <= 16 && (nuevo.isEmpty() || esNumeroValido(nuevo))) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean esNumeroValido(String text) {
                return text.matches("^\\d*\\.?\\d*$");
            }
        });
    }
}
