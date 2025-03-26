package com.example.tiendaropachincha;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class BuscarProducto extends AppCompatActivity {

    // Cambiar por la URL correcta de tu servicio web
    private final String URLWS = "http://192.168.0.107/wstienda/app/services/service-producto.php";

    private RequestQueue requestQueue;
    private EditText edtID, edtTipo, edtGenero, edtTalla, edtPrecio;
    private Button btnBuscarProducto, btnActualizarProducto, btnEliminarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_producto);

        loadUI();

        btnBuscarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarProducto();
            }
        });

        btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarEliminacion();
            }
        });

        adBOtones(false);
    }

    private void adBOtones(boolean sw) {
        btnEliminarProducto.setEnabled(sw);
        btnActualizarProducto.setEnabled(sw);
    }

    private void buscarProducto() {
        String productoId = edtID.getText().toString().trim();
        if (TextUtils.isEmpty(productoId)) {
            showToast("Ingrese un ID de producto");
            edtID.requestFocus();
            return;
        }

        requestQueue = Volley.newRequestQueue(this);

        String URLparams = Uri.parse(URLWS)
                .buildUpon()
                .appendQueryParameter("q", "findById")
                .appendQueryParameter("id", productoId)
                .build()
                .toString();

        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET,
                URLparams,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() == 0) {
                            resetUI();
                            showToast("No existe el producto");
                            adBOtones(false);
                            edtID.requestFocus();
                        } else {
                            try {
                                JSONObject jsonObject = response.getJSONObject(0);

                                edtTipo.setText(jsonObject.optString("tipo", ""));
                                edtGenero.setText(jsonObject.optString("genero", ""));
                                edtTalla.setText(jsonObject.optString("talla", ""));
                                edtPrecio.setText(jsonObject.optString("precio", ""));

                                adBOtones(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                                showToast("Error al procesar datos");
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ErrorWS", error.toString());
                        showToast("Error de comunicación");
                    }
                }
        );

        requestQueue.add(jsonRequest);
    }

    private void eliminarProducto() {
        String productoId = edtID.getText().toString().trim();
        if (TextUtils.isEmpty(productoId)) {
            showToast("No hay un producto seleccionado");
            return;
        }

        requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                URLWS + "/" + productoId,  // Añadir ID a la URL para DELETE
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean status = jsonResponse.getBoolean("status");

                            if (status) {
                                showToast("Producto eliminado correctamente");
                                resetUI();
                                adBOtones(false);
                            } else {
                                showToast("No se pudo eliminar el producto");
                            }
                        } catch (Exception e) {
                            Log.e("DELETE_RESPONSE_ERROR", e.toString());
                            showToast("Error al procesar la respuesta");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DELETE_ERROR", error.toString());
                        showToast("Error de conexión al eliminar");
                    }
                }
        );

        requestQueue.add(stringRequest);
    }

    private void confirmarEliminacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);

        dialogo.setTitle("Tienda");
        dialogo.setMessage("¿Seguro de eliminar el producto?");
        dialogo.setCancelable(false);

        dialogo.setNegativeButton("No", null);
        dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarProducto();
            }
        });

        dialogo.create().show();
    }

    private void resetUI() {
        edtID.setText("");
        edtTipo.setText("");
        edtGenero.setText("");
        edtTalla.setText("");
        edtPrecio.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(BuscarProducto.this, message, Toast.LENGTH_SHORT).show();
    }

    private void loadUI() {
        edtID = findViewById(R.id.edtID);
        edtTipo = findViewById(R.id.edtTipo);
        edtGenero = findViewById(R.id.edtGenero);
        edtTalla = findViewById(R.id.edtTalla);
        edtPrecio = findViewById(R.id.edtPrecio);

        btnBuscarProducto = findViewById(R.id.btnBuscarProducto);
        btnActualizarProducto = findViewById(R.id.btnActualizarProducto);
        btnEliminarProducto = findViewById(R.id.btnEliminarProducto);
    }
}