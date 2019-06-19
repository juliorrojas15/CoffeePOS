package com.example.pos_coffee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okio.Timeout;

public class pagLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText oUsuario;
    private EditText oClave;
    private Button oLogin;
    private Button oCancelar;
    private Spinner oTienda;
    private Button oIngresar;
    private Button oRegistrar;
    private ProgressDialog oProgressDialog;

    String sEmail,sClave,sTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_login);

        //Comunicación con usuarios Firebase
        mAuth=FirebaseAuth.getInstance();

        //Declaración de objetos del Layout
        oUsuario =(EditText)findViewById(R.id.etUsuario);
        oClave=(EditText)findViewById(R.id.etClave);
        oLogin=(Button)findViewById(R.id.bLogin);
        oCancelar=(Button)findViewById(R.id.bCancelar);
        oTienda=(Spinner)findViewById(R.id.spTienda);
        oIngresar=(Button)findViewById(R.id.bIngresar);
        oRegistrar=(Button)findViewById(R.id.bRegistrarse);

        oProgressDialog=new ProgressDialog(this);

        //Habilitación o no de botones
        oTienda.setEnabled(false);
        oIngresar.setEnabled(false);
        oCancelar.setEnabled(false);


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
                fNavegar();
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
                            oTienda.setEnabled(true);
                            oCancelar.setEnabled(true);
                            oIngresar.setEnabled(true);
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

        //sTienda=oTienda.getSelectedItem().toString().trim();
        sTienda="Babilonia";
        if (TextUtils.isEmpty(sTienda)){
            Toast.makeText(pagLogin.this,"Debes seleccionar una Tienda",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Intent Destino=new Intent(getApplication(),MainActivity.class);
            Destino.putExtra(MainActivity.sUsuario,sEmail);
            Destino.putExtra(MainActivity.sTienda,sTienda);
            startActivity(Destino);
        }


    }
    public void fCancelar(){
        oUsuario.setEnabled(true);
        oClave.setEnabled(true);
        oLogin.setEnabled(true);
        oRegistrar.setEnabled(true);

        oCancelar.setEnabled(false);
        oTienda.setEnabled(false);
        oIngresar.setEnabled(false);
    }
    public void fNavegar(){
        Intent Destino = new Intent(this,pagNewLogin.class);
        startActivity(Destino);
    }

}
