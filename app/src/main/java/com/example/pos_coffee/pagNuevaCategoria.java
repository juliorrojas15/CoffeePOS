package com.example.pos_coffee;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pagNuevaCategoria extends AppCompatActivity {
    //Variables que se traen del login
    public static final String sTienda="tienda";

    //Keys de la base de datos
    public static final String NOMBRE_KEY = "Nombre";
    public static final String CANT_DE_ARTICULOS_KEY = "Cant. de Articulos";
    public static final String COLOR_KEY = "Color";
    public static final String ALARMA_KEY = "Alarma";

    //Objetos del Layout
    private EditText oetNomCategoria;
    private RadioGroup orgColores;
    private RadioButton orbColor;
    private Button obVolver, obGuardar;


    int iNombreIgual=0;
    String sNombreCategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_nueva_categoria);

        oetNomCategoria=(EditText)findViewById(R.id.etNomCategoria);
        orgColores=(RadioGroup)findViewById(R.id.rgColores);
        obVolver=(Button)findViewById(R.id.bVolver);
        obGuardar=(Button)findViewById(R.id.bGuardar);

        //Variables que se traen de otros activities
        final String sTienda = "Cafe Babilonia";//getIntent().getStringExtra("tienda");
        final String sEmail="juliorrojas15@gmail.com";//FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();


        obVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fNavegarSimple(pagProductos.class);
            }
        });

        obGuardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sNombreCategoria=oetNomCategoria.getText().toString();
                fRevisarNombre(sEmail,sTienda);
            }
        });

    }

    void fNavegarSimple(Class clPagina){
        Intent Destino = new Intent(this,clPagina);
        startActivity(Destino);
    }


    void fRevisarNombre(final String sEmail,final String sTienda){

        final CollectionReference bd_Categorias= FirebaseFirestore.getInstance().collection("Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Categorias");
        bd_Categorias.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //List<String> list = new ArrayList<>();
                    String sNombreDocumento;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        sNombreDocumento=document.getId();
                        if (sNombreDocumento.equalsIgnoreCase(sNombreCategoria)){
                            Toast.makeText(pagNuevaCategoria.this,"EL nombre que colocaste ya está siendo utilizado...",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    fCrearBBDD(sEmail,sTienda);
                    fNavegarSimple(pagProductos.class);
                } else {
                    Log.d("Alarma", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });

    }

    void fCrearBBDD(String sEmail, String sTienda) {

        String sColor;
        //obtener color seleccionado
        int iColorSeleccionado=orgColores.getCheckedRadioButtonId();
        orbColor=(RadioButton)findViewById(iColorSeleccionado);
        int iColor=orgColores.indexOfChild(orbColor);
        switch (iColor){
            case 0: sColor="#F44336";break;
            case 1: sColor="#E91E63";break;
            case 2: sColor="#9C27B0";break;
            case 3: sColor="#2196F3";break;
            case 4: sColor="#00BCD4";break;
            case 5: sColor="#009688";break;
            case 6: sColor="#CDDC39";break;
            case 7: sColor="#FFEB3B";break;
            case 8: sColor="#FFC107";break;
            default: sColor = "#FFFFFF";
        }

        //oetNomCategoria.setBackgroundColor(Color.parseColor(sColor));

        //Nueva Categoria
        DocumentReference bd_NuevaCategoria = FirebaseFirestore.getInstance()
                .document("Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Categorias/"+sNombreCategoria);

        Map<String,Object> bd_GuardarCategoria=new HashMap<String, Object>();
        bd_GuardarCategoria.put(NOMBRE_KEY,sNombreCategoria);
        bd_GuardarCategoria.put(CANT_DE_ARTICULOS_KEY,"0");
        bd_GuardarCategoria.put(COLOR_KEY,sColor);

        bd_NuevaCategoria.set(bd_GuardarCategoria).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(ALARMA_KEY,"La Categoria ha sido guardada");
                Toast.makeText(pagNuevaCategoria.this,"La Categoria ha sido guardada",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(ALARMA_KEY,"Documento NO fue guardado");
            }
        });
   }

}
