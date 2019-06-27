package com.example.pos_coffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class pagNuevoArticulo extends AppCompatActivity {

    public static final String psEditarEliminar="Editar/Eliminar";


    private Button obVolver, obGuardar,obEditar,obEliminar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_nuevo_articulo);

        obVolver=(Button)findViewById(R.id.bVolver);
        obGuardar=(Button)findViewById(R.id.bGuardar);
        obEditar=(Button)findViewById(R.id.bEditar);
        obEliminar=(Button)findViewById(R.id.bEliminar);

        obVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fNavegarSimple(pagProductos.class);
            }
        });
        /*obGuardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sNombreCategoria=oetNomCategoria.getText().toString();
                fRevisarNombre(sEmail,sTienda);
            }
        });
        obEditar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sNombreCategoria=oetNomCategoria.getText().toString();
                fCrearBBDD(sEmail,sTienda);
            }
        });
        obEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sNombreCategoria=oetNomCategoria.getText().toString();
                fEliminarBBDD(sEmail,sTienda);
            }
        });*/
    }

    void fNavegarSimple(Class clPagina){
        Intent Destino = new Intent(this,clPagina);
        startActivity(Destino);
    }
}
