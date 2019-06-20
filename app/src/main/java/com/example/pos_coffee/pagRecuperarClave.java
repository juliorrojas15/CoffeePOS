package com.example.pos_coffee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class pagRecuperarClave extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText oUsuario;
    private Button oReestablecer,oVolver;
    private ProgressDialog pdAvance;

    String sEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_recuperar_clave);

        //Comunicación con usuarios Firebase
        mAuth= FirebaseAuth.getInstance();
        oUsuario =(EditText)findViewById(R.id.etUsuario);
        oReestablecer=(Button)findViewById(R.id.bReestablecer);
        oVolver=(Button)findViewById(R.id.bVolver);
        pdAvance=new ProgressDialog(this);

        oReestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fReestablecer();
            }
        });

        oVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fNavegar();
            }
        });

    }
    void fReestablecer(){
        sEmail=oUsuario.getText().toString().trim();
        if (sEmail.isEmpty()){
            Toast.makeText(this,"Debe ingresar un email",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            pdAvance.setMessage("Espere un momento...");
            pdAvance.setCanceledOnTouchOutside(false);
            pdAvance.show();

            CountDownTimer Timer=new CountDownTimer(10000, 100) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Toast.makeText(pagRecuperarClave.this,"Algo anda mal con la conexión o el email que ingresaste",Toast.LENGTH_LONG).show();
                    pdAvance.cancel();
                    return;
                }
            }.start();

            mAuth.setLanguageCode("es");
            mAuth.sendPasswordResetEmail(sEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(pagRecuperarClave.this,"Se ha enviado un correo para reestablecer tu clave...",Toast.LENGTH_LONG);
                        fNavegar();
                    }
                    else{
                        Toast.makeText(pagRecuperarClave.this,"No se pudo enviar el correo para reestablecer tu clave...",Toast.LENGTH_LONG);
                        return;
                    }
                    pdAvance.dismiss();
                }
            });
        }
    }
    void fNavegar(){
        Intent Destino=new Intent(this,pagLogin.class);
        startActivity(Destino);
    }
}
