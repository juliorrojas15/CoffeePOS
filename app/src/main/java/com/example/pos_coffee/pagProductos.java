package com.example.pos_coffee;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class pagProductos extends AppCompatActivity {
    public static final String sTienda="tienda";
    Button obVolver,obAgregarCategoria;
    ListView olvCategorias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_productos);
        obVolver=(Button)findViewById(R.id.bVolver);
        obAgregarCategoria=(Button)findViewById(R.id.bAgregarCategorias);
        olvCategorias=(ListView)findViewById(R.id.lvCategorias);

        //Variables que se traen de otros activities
        final String sTienda = "Cafe Babilonia";//getIntent().getStringExtra("tienda");
        final String sEmail="juliorrojas15@gmail.com";//FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        obVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Destino=new Intent(pagProductos.this,MainActivity.class);
                startActivity(Destino);
            }
        });
        obAgregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Destino=new Intent(pagProductos.this,pagNuevaCategoria.class);
                startActivity(Destino);
            }
        });

        fActualizar_LV_Categorias(sEmail,sTienda);

    }

    void fNavegar (View view){
        Intent Destino=new Intent(this,MainActivity.class);
        startActivity(Destino);
    }

    void fActualizar_LV_Categorias(final String sEmail, final String sTienda){

        String sPath = "Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Categorias";
        CollectionReference bd_Datos= FirebaseFirestore.getInstance().collection(sPath);

        bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final List<String> listDocuments = new ArrayList<>();
                    final List<Categ_Entidad> listItems=new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Collection collection=document.getData().values();
                        List listPrevia=new ArrayList<>(collection);
                        int iPrimero=0,iSegundo=2,iTercero=1;

                        listItems.add(new Categ_Entidad(listPrevia.get(iPrimero).toString(),
                                listPrevia.get(iSegundo).toString(),
                                listPrevia.get(iTercero).toString()));
                        Log.d("Campos: ", listItems.toString());
                    }
                    olvCategorias=(ListView)findViewById(R.id.lvCategorias);
                    Categ_Adaptador Adapter = new Categ_Adaptador(pagProductos.this,listItems);
                    olvCategorias.setAdapter(Adapter);
                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });





    }

}
