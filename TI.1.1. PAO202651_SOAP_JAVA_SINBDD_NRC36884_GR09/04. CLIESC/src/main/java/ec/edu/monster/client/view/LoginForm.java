package ec.edu.monster.client.view;

import ec.edu.monster.client.controller.DesktopAppController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * [VISTA] Ventana gráfica de Autenticación (Login) para el Cliente de Escritorio.
 */
public class LoginForm extends JFrame {

    private final DesktopAppController controller;

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JLabel lblEstado;
    private JButton btnIngresar;
    private JButton btnSalir;

    public LoginForm(DesktopAppController controller) {
        this.controller = controller;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Acceso al Sistema - Conversor de Unidades SOAP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Panel Superior: Encabezado Institucional
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(15, 60, 110)); // Azul oscuro institucional
        pnlHeader.setPreferredSize(new Dimension(460, 115));
        pnlHeader.setLayout(new GridBagLayout());

        GridBagConstraints gbcH = new GridBagConstraints();
        gbcH.gridx = 0;
        gbcH.insets = new Insets(4, 10, 4, 10);

        JLabel lblUniv = new JLabel("UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE");
        lblUniv.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUniv.setForeground(Color.WHITE);
        lblUniv.setHorizontalAlignment(SwingConstants.CENTER);
        gbcH.gridy = 0;
        pnlHeader.add(lblUniv, gbcH);

        JLabel lblSub = new JLabel("Arquitectura de Software - Tarea TI.1.1 (SOAP)");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(200, 220, 245));
        gbcH.gridy = 1;
        pnlHeader.add(lblSub, gbcH);

        JLabel lblAutor = new JLabel("Grupo GR09 | Julio Enrique Viche Castillo");
        lblAutor.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblAutor.setForeground(new Color(180, 210, 235));
        gbcH.gridy = 2;
        pnlHeader.add(lblAutor, gbcH);

        add(pnlHeader, BorderLayout.NORTH);

        // Panel Central: Formulario de credenciales
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(new Color(245, 247, 250));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.gridx = 0;

        JLabel lblTitulo = new JLabel("Iniciar Sesión (Obligatorio)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitulo.setForeground(new Color(30, 41, 59));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        pnlForm.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Credenciales del sistema: MONSTER / MONSTER9");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtitulo.setForeground(new Color(100, 116, 139));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 4, 16, 4);
        pnlForm.add(lblSubtitulo, gbc);

        gbc.insets = new Insets(4, 4, 2, 4);

        // Campo Usuario
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(51, 65, 85));
        gbc.gridy = 2;
        pnlForm.add(lblUser, gbc);

        txtUsuario = new JTextField("MONSTER");
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setPreferredSize(new Dimension(300, 36));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridy = 3;
        pnlForm.add(txtUsuario, gbc);

        // Campo Contraseña
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(51, 65, 85));
        gbc.gridy = 4;
        gbc.insets = new Insets(8, 4, 2, 4);
        pnlForm.add(lblPass, gbc);

        txtClave = new JPasswordField("MONSTER9");
        txtClave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtClave.setPreferredSize(new Dimension(300, 36));
        txtClave.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridy = 5;
        gbc.insets = new Insets(4, 4, 10, 4);
        pnlForm.add(txtClave, gbc);

        // Etiqueta de Estado / Errores
        lblEstado = new JLabel(" ");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 6;
        gbc.insets = new Insets(2, 4, 10, 4);
        pnlForm.add(lblEstado, gbc);

        // Botón Ingresar
        btnIngresar = new JButton("Iniciar Sesión");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setBackground(new Color(15, 60, 110));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setPreferredSize(new Dimension(300, 40));
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setFocusPainted(false);
        btnIngresar.addActionListener(e -> ejecutarLogin());
        gbc.gridy = 7;
        gbc.insets = new Insets(4, 4, 8, 4);
        pnlForm.add(btnIngresar, gbc);

        // Botón Salir
        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSalir.setBackground(new Color(226, 232, 240));
        btnSalir.setForeground(new Color(51, 65, 85));
        btnSalir.setPreferredSize(new Dimension(300, 34));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(e -> System.exit(0));
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 4, 4, 4);
        pnlForm.add(btnSalir, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // Permitir inicio de sesión presionando Enter en cualquier campo
        getRootPane().setDefaultButton(btnIngresar);
    }

    private void ejecutarLogin() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();

        if (usuario.isEmpty() || clave.isEmpty()) {
            mostrarMensaje("Por favor, complete ambos campos.", false);
            return;
        }

        mostrarMensaje("Autenticando con el servidor SOAP...", true);
        btnIngresar.setEnabled(false);

        // Ejecutar en hilo de fondo para no congelar la UI de Swing
        new Thread(() -> {
            try {
                controller.procesarLogin(usuario, clave);
            } finally {
                btnIngresar.setEnabled(true);
            }
        }).start();
    }

    public void mostrarMensaje(String mensaje, boolean informativo) {
        lblEstado.setText(mensaje);
        lblEstado.setForeground(informativo ? new Color(15, 118, 110) : new Color(220, 38, 38));
    }
}
