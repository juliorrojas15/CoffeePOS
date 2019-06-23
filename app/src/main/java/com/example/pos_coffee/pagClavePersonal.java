package com.example.pos_coffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class pagClavePersonal extends AppCompatActivity {
    //Variables que se traen del login
    public static final String sPagOrg="NombrePag";

    public Button oVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_clave_personal);

        oVolver = (Button)findViewById(R.id.bVolver);

        final String sPagDes = getIntent().getStringExtra("NombrePag");

        oVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sPagDes){
                    case "MainActivity": fNavegar(MainActivity.class);break;
                }
            }
        });

    }
    public void fNavegar(Class Nombre){
        Intent Destino = new Intent(this,Nombre);
        startActivity(Destino);
    }
}
