package ec.edu.monster.client.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ec.edu.monster.client.R;
import ec.edu.monster.client.controller.LoginController;
import ec.edu.monster.client.model.SessionModel;

/**
 * [VISTA] Actividad de inicio de sesión obligatorio para el cliente Android RESTful.
 * Modalidad SINBDD: Credenciales fijas MONSTER / MONSTER9.
 * Servidor REST quemado: http://192.168.137.1:5000/api/
 *
 * @author Julio Enrique Viche Castillo - GR09 - NRC 36884
 */
public class LoginActivity extends AppCompatActivity {

    public static final String SERVER_URL = "http://192.168.137.1:5000/api/";

    private TextInputEditText edtUsuario;
    private TextInputEditText edtClave;
    private TextView txtMensajeError;
    private ProgressBar progressBarLogin;
    private MaterialButton btnIngresar;

    private LoginController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        controller = new LoginController();

        edtUsuario = findViewById(R.id.edtUsuario);
        edtClave = findViewById(R.id.edtClave);
        txtMensajeError = findViewById(R.id.txtMensajeError);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(v -> ejecutarLogin());
    }

    private void ejecutarLogin() {
        String usuario = (edtUsuario.getText() != null) ? edtUsuario.getText().toString().trim() : "";
        String clave = (edtClave.getText() != null) ? edtClave.getText().toString().trim() : "";

        if (usuario.isEmpty() || clave.isEmpty()) {
            mostrarError("Por favor, ingrese usuario y contraseña.");
            return;
        }

        txtMensajeError.setVisibility(View.GONE);
        progressBarLogin.setVisibility(View.VISIBLE);
        btnIngresar.setEnabled(false);

        controller.procesarLogin(SERVER_URL, usuario, clave, new LoginController.LoginCallback() {
            @Override
            public void onSuccess(SessionModel session, String resolvedBaseUrl) {
                progressBarLogin.setVisibility(View.GONE);
                btnIngresar.setEnabled(true);

                Intent intent = new Intent(LoginActivity.this, ConversionActivity.class);
                intent.putExtra("SESSION", session);
                intent.putExtra("SERVER_URL", (resolvedBaseUrl != null) ? resolvedBaseUrl : SERVER_URL);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String mensaje) {
                progressBarLogin.setVisibility(View.GONE);
                btnIngresar.setEnabled(true);
                mostrarError(mensaje);
            }
        });
    }

    private void mostrarError(String mensaje) {
        txtMensajeError.setText(mensaje);
        txtMensajeError.setVisibility(View.VISIBLE);
    }
}

