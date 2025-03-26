package com.example.tiendaropachincha;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.tiendaropachincha.Entidades.Producto;
import com.example.tiendaropachincha.adaptadores.ProductoAdapter;

public class ListarProductosActivity extends AppCompatActivity {

    private final String URLWS = "http://192.168.0.107/wstienda/app/services/service-producto.php";

    private RequestQueue requestQueue;
    private ListView listView;
    private Button btnActualizar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_productos);

        // Inicializar vistas
        listView = findViewById(R.id.listViewProductos);
        btnActualizar = findViewById(R.id.btnActualizar);

        // Inicializar RequestQueue de Volley
        requestQueue = Volley.newRequestQueue(this);

        // Cargar datos al inicio
        obtenerProductos();

        // Configurar botón de actualización
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerProductos();
            }
        });
    }

    /**
     * Método para obtener la lista de productos desde el servidor
     */
    private void obtenerProductos() {
        // URL para listar todos los productos
        final String URL_LISTAR = URLWS + "?q=showAll";

        // Crear solicitud JsonArrayRequest
        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_LISTAR,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ProductosResponse", response.toString());
                        procesarDatosProductos(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ErrorWS", error.toString());
                        Toast.makeText(ListarProductosActivity.this,
                                "Error al obtener productos: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Agregar la solicitud a la cola
        requestQueue.add(jsonRequest);
    }

    /**
     * Procesa la respuesta JSON del servidor y la convierte en objetos Producto
     */
    private void procesarDatosProductos(JSONArray response) {
        try {
            if (response.length() == 0) {
                Toast.makeText(this, "No hay productos disponibles", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<Producto> listaProductos = new ArrayList<>();

            // Recorrer el JSONArray y convertir cada objeto en un Producto
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);

                Producto producto = new Producto();
                producto.setId(jsonObject.getInt("id"));
                producto.setTipo(jsonObject.getString("tipo"));
                producto.setGenero(jsonObject.getString("genero"));
                producto.setTalla(jsonObject.getString("talla"));
                producto.setPrecio(jsonObject.getDouble("precio"));

                listaProductos.add(producto);
            }

            // Crear y asignar el adaptador al ListView
            ProductoAdapter adaptador = new ProductoAdapter(this, listaProductos);
            listView.setAdapter(adaptador);

            // Mostrar mensaje de éxito
            Toast.makeText(this, "Se cargaron " + listaProductos.size() + " productos",
                    Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Log.e("ErrorProcesarDatos", e.toString());
            Toast.makeText(this, "Error al procesar datos: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}