package com.example.pos_coffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class pagGuardar extends AppCompatActivity {

    Button obCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_guardar);
        obCancelar=(Button)findViewById(R.id.bCancelar);

    }

    void fNavegar(View view){
        Intent Destino = new Intent(this, MainActivity.class);
        startActivity(Destino);
    }

}
