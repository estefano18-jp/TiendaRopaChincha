package com.example.tiendaropachincha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnBuscarProducto, btnRegistrar, btnListar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a los botones
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnListar = findViewById(R.id.btnListar);
        btnBuscarProducto = findViewById(R.id.btnBuscarProducto);  // Aquí está el botón faltante

        // Acción del botón para registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de registrar productos
                Intent intent = new Intent(MainActivity.this, RegistrarProductoActivity.class);
                startActivity(intent);
            }
        });

        // Acción del botón para listar
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de listar productos
                Intent intent = new Intent(MainActivity.this, ListarProductosActivity.class);
                startActivity(intent);
            }
        });

        // Acción del botón para buscar productos
        btnBuscarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad de buscar productos
                Intent intent = new Intent(MainActivity.this, BuscarProducto.class);
                startActivity(intent);
            }
        });
    }
}