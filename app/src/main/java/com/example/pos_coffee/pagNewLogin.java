package com.example.pos_coffee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class pagNewLogin extends AppCompatActivity {

    public static final String NOMBRE_KEY = "Nombre";
    public static final String DIRECCIÓN_KEY = "Dirección";
    public static final String TELEFONO_KEY = "Telefono";
    public static final String ALARMA = "DocTienda";
    public static final String CLAVE_KEY = "Clave";

    private FirebaseAuth mAuth;
    private EditText oUsuario,oClave,oConfirmarClave;
    private EditText oNombreTienda,oDirTienda,oTelTienda;
    private Button oRegistrar,oVolver;
    private ProgressDialog oProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_new_login);

        mAuth=FirebaseAuth.getInstance();

        oUsuario =(EditText)findViewById(R.id.etUsuario);
        oClave=(EditText)findViewById(R.id.etClave);
        oConfirmarClave=(EditText)findViewById(R.id.etConfirmarClave);
        oNombreTienda=(EditText)findViewById(R.id.etNombreTienda1);
        oDirTienda=(EditText)findViewById(R.id.etDirTienda1);
        oTelTienda=(EditText)findViewById(R.id.etTelTienda1);
        oRegistrar=(Button)findViewById(R.id.bRegistrarse);
        oVolver=(Button)findViewById(R.id.bVolver);

        oProgressDialog=new ProgressDialog(this);

        oRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fRegistrarUsuario();
            }
        });
        oVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fNavegar();
            }
        });


    }

    void fNavegar(){
        Intent Destino=new Intent(this,pagLogin.class);
        startActivity(Destino);
    }

    String sEmail,sNombreTienda;

    public void fRegistrarUsuario(){
        sEmail=oUsuario.getText().toString().trim();
        String sClave=oClave.getText().toString().trim();
        String sConfirmarClave=oConfirmarClave.getText().toString().trim();
        sNombreTienda=oNombreTienda.getText().toString().trim();


        if (TextUtils.isEmpty(sEmail)){
            Toast.makeText(this,"Se debe ingresar un email",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(sClave)){
            Toast.makeText(this,"Se debe ingresar una clave",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(sConfirmarClave)){
            Toast.makeText(this,"Se debe ingresar la confirmación de la clave",Toast.LENGTH_LONG).show();
            return;
        }
        if (!sClave.equals(sConfirmarClave)){
            Toast.makeText(this,"Las contraseñas no coiciden",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(sNombreTienda)){
            Toast.makeText(this,"Se debe ingresar el nombre de la primera Tienda",Toast.LENGTH_LONG).show();
            return;
        }


        oProgressDialog.setMessage("Realizando registro en Linea...");
        oProgressDialog.show();


        mAuth.createUserWithEmailAndPassword(sEmail,sClave)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    //Temporización de n Segundos pen caso de no realizar el registro
                    CountDownTimer Timer=new CountDownTimer(10000, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            Toast.makeText(pagNewLogin.this,"Algo anda mal con la conexión",Toast.LENGTH_LONG).show();
                            oProgressDialog.cancel();
                            return;
                        }
                    }.start();

                    //Autenticación
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(pagNewLogin.this,"Se ha registrado el usuario "+sEmail,Toast.LENGTH_LONG).show();
                            oProgressDialog.cancel();
                            Intent Destino=new Intent(pagNewLogin.this,MainActivity.class);
                            Timer.cancel();
                            fCrearBBDD();
                            startActivity(Destino);
                        }
                        else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(pagNewLogin.this,"Este usuario ya existe...",Toast.LENGTH_SHORT).show();
                                oProgressDialog.cancel();
                                Timer.cancel();
                                return;
                            }
                            else{
                                Toast.makeText(pagNewLogin.this,"No se pudo Registrar el usuario",Toast.LENGTH_LONG).show();
                                oProgressDialog.cancel();
                                Timer.cancel();
                                return;
                            }
                        }
                    }
                });
    }

    void fCrearBBDD(){
        //Nueva tienda
        DocumentReference bd_NuevaTienda = FirebaseFirestore.getInstance()
                .document("Usuarios/"+sEmail+"/Tiendas/"+sNombreTienda);

        Map<String,Object> bd_GuardarTienda=new HashMap<String, Object>();
        bd_GuardarTienda.put(NOMBRE_KEY,sNombreTienda);
        bd_GuardarTienda.put(DIRECCIÓN_KEY,oDirTienda.getText().toString().trim());
        bd_GuardarTienda.put(TELEFONO_KEY,oTelTienda.getText().toString().trim());

        bd_NuevaTienda.set(bd_GuardarTienda).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(ALARMA,"Documento ha sido guardado");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(ALARMA,"Documento NO fue guardado");
            }
        });

        //Nuevo personal
        DocumentReference bd_NuevoPersonal = FirebaseFirestore.getInstance()
                .document("Usuarios/"+sEmail+"/Tiendas/"+sNombreTienda+"/Personal/Admon_1");

        Map<String,Object> bd_GuardarPersonal=new HashMap<String, Object>();
        bd_GuardarPersonal.put(NOMBRE_KEY,"Administrador 1");
        bd_GuardarPersonal.put(CLAVE_KEY,1111);

        bd_NuevoPersonal.set(bd_GuardarPersonal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(ALARMA,"Documento ha sido guardado");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(ALARMA,"Documento NO fue guardado");
            }
        });




    }


}
