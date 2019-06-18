package com.example.pos_coffee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class pagProductos extends AppCompatActivity {

    Button obVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_productos);
        obVolver=(Button)findViewById(R.id.bVolver);

        obVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Destino=new Intent(pagProductos.this,MainActivity.class);
                startActivity(Destino);
            }
        });
    }

    void fNavegar (View view){
        Intent Destino=new Intent(this,MainActivity.class);
        startActivity(Destino);
    }


}
