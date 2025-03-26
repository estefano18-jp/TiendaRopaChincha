package com.example.tiendaropachincha.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.tiendaropachincha.Entidades.Producto;
import com.example.tiendaropachincha.R;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<Producto> {

    private Context context;
    private List<Producto> productos;

    public ProductoAdapter(Context context, List<Producto> productos) {
        super(context, R.layout.item_producto, productos);
        this.context = context;
        this.productos = productos;
    }

    // Método para mostrar diálogo modal con detalles del producto
    private void showModal(String message) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this.context);
        dialogo.setTitle("Tienda de Ropa");
        dialogo.setMessage(message);
        dialogo.setCancelable(false);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogo.create().show();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Crear la vista si es necesario
        View item = convertView;
        if (item == null) {
            item = inflater.inflate(R.layout.item_producto, parent, false);
        }

        // Obtener el producto actual
        Producto producto = productos.get(position);

        // Configurar los elementos visibles de la vista (solo tipo y género)
        TextView tvTipo = item.findViewById(R.id.tvTipo);
        TextView tvGenero = item.findViewById(R.id.tvGenero);
        Button btnVerDetalles = item.findViewById(R.id.btnVerDetalles);

        // Asignar los valores
        tvTipo.setText(producto.getTipo());
        tvGenero.setText(producto.getGenero());

        // Configurar acción del botón Ver Detalles
        btnVerDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = "";
                mensaje += "ID: " + producto.getId() + "\n";
                mensaje += "Tipo: " + producto.getTipo() + "\n";
                mensaje += "Género: " + producto.getGenero() + "\n";
                mensaje += "Talla: " + producto.getTalla() + "\n";
                mensaje += "Precio: S/. " + producto.getPrecio();
                showModal(mensaje);
            }
        });

        return item;
    }
}