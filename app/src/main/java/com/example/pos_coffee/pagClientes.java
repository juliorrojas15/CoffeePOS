package com.example.pos_coffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class pagClientes extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_clientes);
    }

    void fNavegar(View view){
        Intent Destino = new Intent(this, MainActivity.class);
        startActivity(Destino);
    }
}
