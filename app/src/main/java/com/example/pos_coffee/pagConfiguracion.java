package com.example.pos_coffee;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SnapshotMetadata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pagConfiguracion extends AppCompatActivity {
    //Variables que se traen del login
    public static final String sTienda="tienda";


    public static final String NOMBRE_KEY = "Nombre";
    public static final String DIRECCIÓN_KEY = "Dirección";
    public static final String TELEFONO_KEY = "Telefono";
    public static final String ALARMA = "DocTienda";
    public static final String CLAVE_KEY = "Clave";
    public static final String PERFIL = "Perfil";
    public static final String TAG = "Alarma";


    private FirebaseAuth mAuth;

    private Button oVolver,oCerrarSesion,oNuevaTienda,oNuevoPersonal;
    private TextView oUsuario;
    private ListView oTiendas_LV,oPersonal_LV;
    private EditText oNomTiendaNueva,oDirTiendaNueva,oTelTiendaNueva;
    private EditText oNomPersonalNuevo,oClavePersonalNuevo;
    private Spinner oPerfilPersonalNuevo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_configuracion);

        //Relación de variables con objetos del Layout
        oVolver=(Button)findViewById(R.id.bVolver);
        oCerrarSesion=(Button)findViewById(R.id.bCerrarSesion);
        oNuevaTienda=(Button)findViewById(R.id.bNuevaTienda);
        oNuevoPersonal=(Button)findViewById(R.id.bNuevoPersonal);
        oUsuario=(TextView)findViewById(R.id.tvUsuario);
        oNomTiendaNueva=(EditText)findViewById(R.id.etNomTiendaNueva);
        oDirTiendaNueva=(EditText)findViewById(R.id.etDirTiendaNueva);
        oTelTiendaNueva=(EditText)findViewById(R.id.etTelTiendaNueva);
        oNomPersonalNuevo=(EditText)findViewById(R.id.etNomPersonalNuevo);
        oClavePersonalNuevo=(EditText)findViewById(R.id.etClavePersonalNuevo);
        oPerfilPersonalNuevo=(Spinner)findViewById(R.id.spPerfilPersonalNuevo);

        //Variables que se traen de otros activities
        final String sTienda = "Apto 303";//getIntent().getStringExtra("tienda");
        final String sEmail="juliorrojas15@gmail.com";//FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        oCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(pagConfiguracion.this,"Gracias... ¡Vuelve pronto!",Toast.LENGTH_SHORT).show();
                fNavegar(pagLogin.class);
            }
        });
        oVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fNavegar(MainActivity.class);
            }
        });

        oNuevaTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fCrearBBDD_Tienda(sEmail);
            }
        });
        oNuevoPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fCrearBBDD_Personal(sEmail,sTienda);
            }
        });

        //--------- Actualizar ListView
        Actualizar_LV(sEmail);

    }

    void fNavegar(Class clPagina){
        Intent Destino = new Intent(this,clPagina);
        startActivity(Destino);
    }


    void fCrearBBDD_Tienda(String sEmail) {

        //--------- Condiciones
        String sNombreTienda = oNomTiendaNueva.getText().toString().trim();
        String sDirTienda = oDirTiendaNueva.getText().toString().trim();
        String sTelTienda = oTelTiendaNueva.getText().toString().trim();

        if (TextUtils.isEmpty(sNombreTienda)) {
            Toast.makeText(this, "Debes ingresar un Nombre de tienda", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(sDirTienda)) {
            Toast.makeText(this, "Debes ingresar una dirección válida", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(sTelTienda)) {
            Toast.makeText(this, "Debes ingresar un telefono válido", Toast.LENGTH_LONG).show();
            return;
        }

        //--------- Nueva tienda
        DocumentReference bd_NuevaTienda = FirebaseFirestore.getInstance()
                .document("Usuarios/" + sEmail + "/Tiendas/" + sNombreTienda);

        Map<String, Object> bd_GuardarTienda = new HashMap<String, Object>();
        bd_GuardarTienda.put(NOMBRE_KEY, sNombreTienda);
        bd_GuardarTienda.put(DIRECCIÓN_KEY, sDirTienda);
        bd_GuardarTienda.put(TELEFONO_KEY, sTelTienda);

        bd_NuevaTienda.set(bd_GuardarTienda).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(ALARMA, "Documento ha sido guardado");
                Toast.makeText(pagConfiguracion.this,"Tienda creada satisfactoriamente",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(ALARMA, "Documento NO fue guardado");
                Toast.makeText(pagConfiguracion.this,"Se presentaron fallas en el proceso",Toast.LENGTH_LONG).show();
            }
        });

        //--------- Actualizar ListView
        Actualizar_LV(sEmail);

        //---------  Borrar Campos
        oNomTiendaNueva.setText("");
        oDirTiendaNueva.setText("");
        oTelTiendaNueva.setText("");

    }
    void fCrearBBDD_Personal(String sEmail,String sNombreTienda) {

        //---------  Condiciones
        String sNombrePersonal = oNomPersonalNuevo.getText().toString().trim();
        String sClavePersonal = oClavePersonalNuevo.getText().toString().trim();
       String sPerfil = oPerfilPersonalNuevo.getSelectedItem().toString().trim();

        if (TextUtils.isEmpty(sNombrePersonal)) {
            Toast.makeText(this, "Debes ingresar un Nombre", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sClavePersonal)) {
            Toast.makeText(this, "Debes ingresar una clave válida", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(sPerfil)) {
            Toast.makeText(this, "Debes seleccionar un perfil válido", Toast.LENGTH_SHORT).show();
            return;
        }
        //---------  Nuevo personal
        DocumentReference bd_NuevoPersonal = FirebaseFirestore.getInstance()
                .document("Usuarios/"+sEmail+"/Tiendas/"+sNombreTienda+"/Personal/"+sNombrePersonal);

        Map<String,Object> bd_GuardarPersonal=new HashMap<String, Object>();
        bd_GuardarPersonal.put(NOMBRE_KEY,sNombrePersonal);
        bd_GuardarPersonal.put(CLAVE_KEY,sClavePersonal);
        bd_GuardarPersonal.put(PERFIL,sPerfil);


        bd_NuevoPersonal.set(bd_GuardarPersonal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(ALARMA, "Documento ha sido guardado");
                Toast.makeText(pagConfiguracion.this,"Personal creado satisfactoriamente",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(ALARMA, "Documento NO fue guardado");
                Toast.makeText(pagConfiguracion.this,"Se presentaron fallas en el proceso",Toast.LENGTH_SHORT).show();

            }
        });

        //---------  Borrar Campos
        oNomPersonalNuevo.setText("");
        oClavePersonalNuevo.setText("");
        oPerfilPersonalNuevo.setSelection(0);


    }

    void Actualizar_LV(final String sEmail){

        Collection cTiedas;
        CollectionReference bd_Tiendas= FirebaseFirestore.getInstance().collection("Usuarios/"+sEmail+"/Tiendas");
        bd_Tiendas.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    final List<String> listDocuments = new ArrayList<>();
                    final List<Config_Entidad> listItems=new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Collection collection=document.getData().values();
                        List listPrevia=new ArrayList<>(collection);
                        listItems.add(new Config_Entidad(listPrevia.get(1).toString(),
                                                            listPrevia.get(2).toString(),
                                                            listPrevia.get(0).toString()));
                        Log.d("Campos: ", listItems.toString());
                    }
                    oTiendas_LV=(ListView)findViewById(R.id.lvTiendas);
                    Config_Adaptador Adapter = new Config_Adaptador(pagConfiguracion.this,listItems);
                    oTiendas_LV.setAdapter(Adapter);

                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });





    }


}
