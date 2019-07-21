package com.example.pos_coffee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import okio.Timeout;

public class pagLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText oUsuario, oClave;
    private Button oLogin, oCancelar,oIngresar,oRegistrar,oOlvideClave;
    private Spinner oTienda;
    private ProgressDialog oProgressDialog;

    String sEmail,sClave,sTienda;
    public static final String TAG = "Resultado";

    public static String gsUsuario="Sin Usuario";
    public static String gsTienda;

    @Override
    protected void onResume(){
        super.onResume();

        //###################################################################################       Codigo para leer variables dentro del mismo dispositivo
        SharedPreferences sharedUsuario=getPreferences(context.MODE_PRIVATE);
        gsUsuario=sharedUsuario.getString("Usuario","No hay Usuario");
        SharedPreferences sharedTienda=getPreferences(context.MODE_PRIVATE);
        gsTienda=sharedTienda.getString("Tienda","No hay Tienda");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Toast.makeText(this, "Usuario: " + gsUsuario + ", Tienda: " + gsTienda, Toast.LENGTH_LONG).show();
            if (!gsUsuario.equals(null) && !gsUsuario.equals("Sin Usuario")) {
                Intent Destino = new Intent(this, MainActivity.class);
                startActivity(Destino);
            }
        }
    }
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_login);

        //Comunicación con usuarios Firebase
        mAuth=FirebaseAuth.getInstance();

        //Variables a memorizar incluso cerrada la aplicación
        context =this;
        SharedPreferences sharedPref=getSharedPreferences("DatosPOS_Coffee",context.MODE_PRIVATE);


        //Declaración de objetos del Layout
        oUsuario =(EditText)findViewById(R.id.etUsuario);
        oClave=(EditText)findViewById(R.id.etClave);
        oLogin=(Button)findViewById(R.id.bLogin);
        oCancelar=(Button)findViewById(R.id.bCancelar);
        oTienda=(Spinner)findViewById(R.id.spTienda);
        oIngresar=(Button)findViewById(R.id.bIngresar);
        oRegistrar=(Button)findViewById(R.id.bRegistrarse);
        oOlvideClave=(Button)findViewById(R.id.bOlvideClave);
        oProgressDialog=new ProgressDialog(this);

        //Habilitación o no de botones
        oCancelar.setVisibility(View.INVISIBLE);
        oTienda.setVisibility(View.INVISIBLE);
        oIngresar.setVisibility(View.INVISIBLE);



        oLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               fLogin();
           }
        });
        oCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fCancelar();
            }
        });
        oIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fIngresar();
            }
        });
        oRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fNavegar(pagNewLogin.class);
            }
        });
        oOlvideClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fNavegar(pagRecuperarClave.class);
            }
        });

    }

    public void fLogin(){
        sEmail=oUsuario.getText().toString().trim();
        sClave=oClave.getText().toString().trim();

        //Verificación de espacios vacios
        if (TextUtils.isEmpty(sEmail)){
            Toast.makeText(this,"Se debe ingresar un email",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(sClave)){
            Toast.makeText(this,"Se debe ingresar una clave",Toast.LENGTH_LONG).show();
            return;
        }

        oProgressDialog.setMessage("Ingresando a la sesión del usuario...");
        oProgressDialog.setCanceledOnTouchOutside(false);
        oProgressDialog.show();

        //Ingreso de Usuario
        mAuth.signInWithEmailAndPassword(sEmail,sClave)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    //Temporización de n Segundos pen caso de no realizar el registro
                    CountDownTimer Timer=new CountDownTimer(10000, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Toast.makeText(pagLogin.this,"Algo anda mal con la conexión",Toast.LENGTH_LONG).show();
                            oProgressDialog.cancel();
                            return;
                        }
                    }.start();

                    //Autenticación
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(pagLogin.this,"Bienvenido...Ahora selecciona tu tienda",Toast.LENGTH_LONG).show();
                            oProgressDialog.cancel();
                            Timer.cancel();
                            oUsuario.setEnabled(false);
                            oClave.setEnabled(false);
                            oLogin.setEnabled(false);
                            oRegistrar.setEnabled(false);
                            oOlvideClave.setEnabled(false);
                            oTienda.setVisibility(View.VISIBLE);
                            oCancelar.setVisibility(View.VISIBLE);
                            fCargarTiendas();
                            oProgressDialog.dismiss();
                        }
                        else{
                            Toast.makeText(pagLogin.this,"No se pudo Ingresar",Toast.LENGTH_LONG).show();
                            oProgressDialog.cancel();
                            Timer.cancel();
                            return;
                        }
                    }
                });
    }

    public void fIngresar(){

        sTienda=oTienda.getSelectedItem().toString().trim();
        if (TextUtils.isEmpty(sTienda)){
            Toast.makeText(pagLogin.this,"Debes seleccionar una Tienda",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            gsUsuario=sEmail;
            gsTienda=sTienda;

            SharedPreferences sharedUsuario =getPreferences(context.MODE_PRIVATE);
            SharedPreferences.Editor editorUsuario=sharedUsuario.edit();
            editorUsuario.putString("Usuario",gsUsuario);
            editorUsuario.commit();
            SharedPreferences sharedTienda =getPreferences(context.MODE_PRIVATE);
            SharedPreferences.Editor editorTienda=sharedTienda.edit();
            editorTienda.putString("Tienda",gsTienda);
            editorTienda.commit();



            Intent Destino=new Intent(getApplication(),MainActivity.class);
            startActivity(Destino);
        }


    }
    public void fCancelar(){
        oUsuario.setEnabled(true);
        oClave.setEnabled(true);
        oLogin.setEnabled(true);
        oRegistrar.setEnabled(true);
        oOlvideClave.setEnabled(true);

        oCancelar.setVisibility(View.INVISIBLE);
        oTienda.setVisibility(View.INVISIBLE);
        oIngresar.setVisibility(View.INVISIBLE);
    }
    public void fNavegar(Class Nombre){
        Intent Destino = new Intent(this,Nombre);
        startActivity(Destino);
    }

    public void fCargarTiendas(){
        Collection Prueba;
        CollectionReference bd_Tiendas= FirebaseFirestore.getInstance().collection("Usuarios/"+sEmail+"/Tiendas");
        bd_Tiendas.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    Log.d(TAG, list.toString());
                    ArrayAdapter Adapter = new ArrayAdapter(pagLogin.this,android.R.layout.simple_spinner_dropdown_item,list);
                    oTienda.setAdapter(Adapter);
                    if (task.isComplete()){
                        oIngresar.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d(TAG, "Error adquiriendo documentos: ", task.getException());
                }
            }
        });

    }

}
