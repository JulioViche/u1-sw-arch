package ec.edu.monster.client.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ec.edu.monster.client.R;
import ec.edu.monster.client.controller.ConversionController;
import ec.edu.monster.client.model.SoapClient;
import ec.edu.monster.client.model.ConversionRequest;
import ec.edu.monster.client.model.ConversionResponse;
import ec.edu.monster.client.model.SessionModel;

/**
 * [VISTA] Actividad principal del Conversor de Unidades SOAP en Android (MVC).
 * Cumple estrictamente con:
 * - Exactamente 5 unidades por magnitud física.
 * - Formato visual estricto a 2 decimales (%.2f) con punto decimal.
 * - Restricción a máximo 16 caracteres y únicamente dígitos (0-9) y '.'.
 */
public class ConversionActivity extends AppCompatActivity {

    private static final Map<String, String[]> CATALOGO_UNIDADES = new HashMap<>();

    static {
        CATALOGO_UNIDADES.put("Longitud", new String[]{
                "metro", "centimetro", "pie", "yarda", "milla"
        });
        CATALOGO_UNIDADES.put("Masa", new String[]{
                "gramo", "kilogramo", "libra", "onza", "quintal"
        });
        CATALOGO_UNIDADES.put("Temperatura", new String[]{
                "celsius", "fahrenheit", "kelvin", "newton", "reaumur"
        });
    }

    private SessionModel session;
    private String serverUrl;
    private String magnitudSeleccionada = "Longitud";

    private TextView txtUsuarioActivo;
    private TextView txtTokenActivo;
    private MaterialButton btnCerrarSesion;
    private MaterialButtonToggleGroup toggleGroupMagnitud;
    private TextInputEditText edtValor;
    private Spinner spnOrigen;
    private Spinner spnDestino;
    private ImageButton btnSwap;
    private MaterialButton btnCalcular;
    private ProgressBar progressBarConversion;
    private MaterialCardView cardResultado;
    private TextView txtResultadoValor;
    private TextView txtResultadoDetalle;
    private MaterialButton btnCopiarResultado;

    private ConversionController controller;
    private String ultimoResultadoFormateado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

        session = (SessionModel) getIntent().getSerializableExtra("SESSION");
        serverUrl = getIntent().getStringExtra("SERVER_URL");
        if (serverUrl == null || serverUrl.trim().isEmpty()) {
            serverUrl = SoapClient.isEmulator() ? SoapClient.EMULATOR_URL_8733 : SoapClient.HOTSPOT_URL_8733;
        }

        controller = new ConversionController();

        inicializarVistas();
        configurarFiltroEntrada();
        actualizarSpinners("Longitud");

        toggleGroupMagnitud.check(R.id.btnTabLongitud);
        toggleGroupMagnitud.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnTabLongitud) {
                    magnitudSeleccionada = "Longitud";
                } else if (checkedId == R.id.btnTabMasa) {
                    magnitudSeleccionada = "Masa";
                } else if (checkedId == R.id.btnTabTemperatura) {
                    magnitudSeleccionada = "Temperatura";
                }
                actualizarSpinners(magnitudSeleccionada);
            }
        });

        btnSwap.setOnClickListener(v -> intercambiarUnidades());
        btnCalcular.setOnClickListener(v -> ejecutarConversion());
        btnCopiarResultado.setOnClickListener(v -> copiarAlPortapapeles());
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    private void inicializarVistas() {
        txtUsuarioActivo = findViewById(R.id.txtUsuarioActivo);
        txtTokenActivo = findViewById(R.id.txtTokenActivo);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        toggleGroupMagnitud = findViewById(R.id.toggleGroupMagnitud);
        edtValor = findViewById(R.id.edtValor);
        spnOrigen = findViewById(R.id.spnOrigen);
        spnDestino = findViewById(R.id.spnDestino);
        btnSwap = findViewById(R.id.btnSwap);
        btnCalcular = findViewById(R.id.btnCalcular);
        progressBarConversion = findViewById(R.id.progressBarConversion);
        cardResultado = findViewById(R.id.cardResultado);
        txtResultadoValor = findViewById(R.id.txtResultadoValor);
        txtResultadoDetalle = findViewById(R.id.txtResultadoDetalle);
        btnCopiarResultado = findViewById(R.id.btnCopiarResultado);

        if (session != null) {
            txtUsuarioActivo.setText("Usuario: " + session.getUsuario());
            txtTokenActivo.setText("Token: " + session.getTokenAbreviado());
        }
    }

    private void configurarFiltroEntrada() {
        InputFilter filtroCaracteres = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String resultado = dest.subSequence(0, dstart).toString()
                        + source.subSequence(start, end)
                        + dest.subSequence(dend, dest.length()).toString();

                if (resultado.length() > 16) {
                    return "";
                }
                if (!resultado.matches("^-?\\d*\\.?\\d*$")) {
                    return "";
                }
                return null;
            }
        };

        edtValor.setFilters(new InputFilter[]{filtroCaracteres, new InputFilter.LengthFilter(16)});
    }

    private void actualizarSpinners(String magnitud) {
        String[] unidades = CATALOGO_UNIDADES.getOrDefault(magnitud, new String[0]);
        String[] unidadesCapitalizadas = new String[unidades.length];
        for (int i = 0; i < unidades.length; i++) {
            unidadesCapitalizadas[i] = capitalizar(unidades[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                unidadesCapitalizadas
        );

        spnOrigen.setAdapter(adapter);
        spnDestino.setAdapter(adapter);

        if (unidades.length > 1) {
            spnDestino.setSelection(1);
        }
    }

    private void intercambiarUnidades() {
        int posOrigen = spnOrigen.getSelectedItemPosition();
        int posDestino = spnDestino.getSelectedItemPosition();
        spnOrigen.setSelection(posDestino);
        spnDestino.setSelection(posOrigen);
    }

    private void ejecutarConversion() {
        String valorStr = (edtValor.getText() != null) ? edtValor.getText().toString().trim() : "";

        if (valorStr.length() > 16) {
            mostrarError("El valor no puede superar los 16 caracteres.");
            return;
        }

        if (!valorStr.matches("^-?\\d+(\\.\\d+)?$")) {
            mostrarError("Ingrese un número válido. Solo se permiten dígitos, un punto decimal y '-' al inicio (ej: -15.50 o 100.00).");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr);
        } catch (NumberFormatException e) {
            mostrarError("Número fuera de rango.");
            return;
        }

        String unidadOrigen = spnOrigen.getSelectedItem().toString().toLowerCase();
        String unidadDestino = spnDestino.getSelectedItem().toString().toLowerCase();
        String token = (session != null) ? session.getToken() : "";

        ConversionRequest request = new ConversionRequest(magnitudSeleccionada, unidadOrigen, unidadDestino, valor);

        progressBarConversion.setVisibility(View.VISIBLE);
        btnCalcular.setEnabled(false);

        controller.procesarConversion(serverUrl, token, request, new ConversionController.ConversionCallback() {
            @Override
            public void onSuccess(ConversionResponse response) {
                progressBarConversion.setVisibility(View.GONE);
                btnCalcular.setEnabled(true);

                ultimoResultadoFormateado = String.format(Locale.US, "%.2f %s = %.2f %s",
                        request.getValor(),
                        capitalizar(request.getUnidadOrigen()),
                        response.getValorConvertido(),
                        capitalizar(request.getUnidadDestino()));

                txtResultadoValor.setText(ultimoResultadoFormateado);
                txtResultadoValor.setTextColor(Color.parseColor("#0D9488"));
                txtResultadoDetalle.setText("Conversión SOAP procesada exitosamente en memoria (SINBDD).");
                cardResultado.setCardBackgroundColor(Color.parseColor("#F0FDF4"));
                cardResultado.setStrokeColor(Color.parseColor("#99F6E4"));
                btnCopiarResultado.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String mensaje) {
                progressBarConversion.setVisibility(View.GONE);
                btnCalcular.setEnabled(true);
                mostrarError(mensaje);
            }
        });
    }

    private void mostrarError(String mensaje) {
        txtResultadoValor.setText("Error en Conversión");
        txtResultadoValor.setTextColor(Color.parseColor("#DC2626"));
        txtResultadoDetalle.setText(mensaje);
        cardResultado.setCardBackgroundColor(Color.parseColor("#FEF2F2"));
        cardResultado.setStrokeColor(Color.parseColor("#FECACA"));
        btnCopiarResultado.setVisibility(View.GONE);
    }

    private void copiarAlPortapapeles() {
        if (!ultimoResultadoFormateado.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Resultado de Conversión", ultimoResultadoFormateado);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "¡Resultado copiado al portapapeles!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cerrarSesion() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return "";
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }
}

