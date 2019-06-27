package com.example.pos_coffee;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class pagNuevoArticulo extends AppCompatActivity {
    //Variables que se traen de la pagina de Productos
    public static final String sTienda="tienda";
    public static final String psNomArticulo="NomArticulo";
    public static final String psEditarEliminar="Editar/Eliminar";



    //Keys de la base de datos
    public static final String NOMBRE_KEY = "Nombre";
    public static final String CATEGORIA_KEY = "Categoria";
    public static final String PRECIO_KEY = "Precio";
    public static final String COSTO_KEY = "Costo";
    public static final String STOCK_KEY = "Stock";
    public static final String REF_KEY = "Ref";
    public static final String CODIGO_BARRAS_KEY = "Codigo Barras";
    public static final String DESCUENTO_KEY = "Descuento";
    public static final String N_VAR_1_KEY = "Nom_Var_1";
    public static final String N_VAR_2_KEY = "Nom_Var_2";
    public static final String N_VAR_3_KEY = "Nom_Var_3";
    public static final String N_VAR_4_KEY = "Nom_Var_4";
    public static final String N_VAR_5_KEY = "Nom_Var_5";
    public static final String N_VAR_6_KEY = "Nom_Var_6";
    public static final String P_VAR_1_KEY = "Precio_Var_1";
    public static final String P_VAR_2_KEY = "Precio_Var_2";
    public static final String P_VAR_3_KEY = "Precio_Var_3";
    public static final String P_VAR_4_KEY = "Precio_Var_4";
    public static final String P_VAR_5_KEY = "Precio_Var_5";
    public static final String P_VAR_6_KEY = "Precio_Var_6";

    public static final String ALARMA_KEY = "Alarma";

    //Objetos del Layout
    private EditText oetNombre,oetPrecio,oetCosto,oetStock,oetRef,oetCodBarras,oetDescuento;
    private EditText oetNomVar_1,oetNomVar_2,oetNomVar_3,oetNomVar_4,oetNomVar_5,oetNomVar_6;
    private EditText oetPrecioVar_1,oetPrecioVar_2,oetPrecioVar_3,oetPrecioVar_4,oetPrecioVar_5,oetPrecioVar_6;
    private RadioGroup orgColores,orgVisualizacion;
    private RadioButton orbColor;
    private Spinner ospCategoria;
    private Button obVolver, obGuardar,obEditar,obEliminar;


    String sNombreArt,sCategoriaArt,sRefArt;
    int iCostoArt,iPrecioArt,iStockArt,iDescuentoArt,iCodigoBarrasArt;
    String sNomVar_1,sNomVar_2,sNomVar_3,sNomVar_4,sNomVar_5,sNomVar_6;
    int iPrecioVar_1,iPrecioVar_2,iPrecioVar_3,iPrecioVar_4,iPrecioVar_5,iPrecioVar_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_nuevo_articulo);

        obVolver=(Button)findViewById(R.id.bVolver);
        obGuardar=(Button)findViewById(R.id.bGuardar);
        obEditar=(Button)findViewById(R.id.bEditar);
        obEliminar=(Button)findViewById(R.id.bEliminar);

        oetNombre=(EditText)findViewById(R.id.etNuevoNombreArt);
        oetPrecio=(EditText)findViewById(R.id.etNuevoPrecioArt);
        oetCosto=(EditText)findViewById(R.id.etNuevoCostoArt);
        oetStock=(EditText)findViewById(R.id.etNuevoStockArt);
        oetRef=(EditText)findViewById(R.id.etNuevoRefArt);
        oetCodBarras=(EditText)findViewById(R.id.etNuevoCodigoArt);
        oetDescuento=(EditText)findViewById(R.id.etNuevoDescuentoArt);

        oetNomVar_1=(EditText)findViewById(R.id.etNombreArtVar_1);
        oetNomVar_2=(EditText)findViewById(R.id.etNombreArtVar_2);
        oetNomVar_3=(EditText)findViewById(R.id.etNombreArtVar_3);
        oetNomVar_4=(EditText)findViewById(R.id.etNombreArtVar_4);
        oetNomVar_5=(EditText)findViewById(R.id.etNombreArtVar_5);
        oetNomVar_6=(EditText)findViewById(R.id.etNombreArtVar_6);

        oetPrecioVar_1=(EditText)findViewById(R.id.etPrecioArtVar_1);
        oetPrecioVar_2=(EditText)findViewById(R.id.etPrecioArtVar_2);
        oetPrecioVar_3=(EditText)findViewById(R.id.etPrecioArtVar_3);
        oetPrecioVar_4=(EditText)findViewById(R.id.etPrecioArtVar_4);
        oetPrecioVar_5=(EditText)findViewById(R.id.etPrecioArtVar_5);
        oetPrecioVar_6=(EditText)findViewById(R.id.etPrecioArtVar_6);

        orgColores=(RadioGroup)findViewById(R.id.rgColoresArt);
        orgVisualizacion=(RadioGroup)findViewById(R.id.rgVisualizacion);
        ospCategoria=(Spinner)findViewById(R.id.spNuevaCategoriaArt);

        //Variables que se traen de otros activities
        final String sTienda = "Cafe Babilonia";//getIntent().getStringExtra("tienda");
        final String sEmail="juliorrojas15@gmail.com";//FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        final String psNomArticulo=getIntent().getStringExtra("NomArticulo");
        final String psEditarEliminar=getIntent().getStringExtra("Editar/Eliminar");

        //Visualización o no de botones de edición
        if (psEditarEliminar.equalsIgnoreCase("Agregar")){
            obGuardar.setVisibility(View.VISIBLE);
            obEditar.setVisibility(View.INVISIBLE);
            obEliminar.setVisibility(View.INVISIBLE);
            oetNombre.setEnabled(true);
        }
        if(psEditarEliminar.equalsIgnoreCase("Editar/Eliminar")){
            obGuardar.setVisibility(View.INVISIBLE);
            obEditar.setVisibility(View.VISIBLE);
            obEliminar.setVisibility(View.VISIBLE);
            oetNombre.setText(psNomArticulo);
            oetNombre.setEnabled(false);
        }




        obVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fNavegarSimple(pagProductos.class);
            }
        });
        obGuardar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fScanner();
                fRevisarNombre(sEmail,sTienda);
            }
        });
        obEditar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fScanner();
                fCrearBBDD(sEmail,sTienda);
            }
        });
        obEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fScanner();
                fEliminarBBDD(sEmail,sTienda);
            }
        });
    }

    void fNavegarSimple(Class clPagina){
        Intent Destino = new Intent(this,clPagina);
        startActivity(Destino);
    }

    void fScanner(){
        sNombreArt=oetNombre.getText().toString();
        sCategoriaArt="Bebidas";//ospCategoria.getSelectedItem().toString();
        sRefArt=oetRef.getText().toString();
        iCostoArt=Integer.parseInt(oetCosto.getText().toString());
        iPrecioArt=Integer.parseInt(oetPrecio.getText().toString());
        iStockArt=Integer.parseInt(oetStock.getText().toString());
        iDescuentoArt=Integer.parseInt(oetDescuento.getText().toString());
        iCodigoBarrasArt=Integer.parseInt(oetCodBarras.getText().toString());
        sNomVar_1=oetNomVar_1.getText().toString();
        sNomVar_2=oetNomVar_2.getText().toString();
        sNomVar_3=oetNomVar_3.getText().toString();
        sNomVar_4=oetNomVar_4.getText().toString();
        sNomVar_5=oetNomVar_5.getText().toString();
        sNomVar_6=oetNomVar_6.getText().toString();
        iPrecioVar_1=Integer.parseInt(oetPrecioVar_1.getText().toString());
        iPrecioVar_2=Integer.parseInt(oetPrecioVar_2.getText().toString());
        iPrecioVar_3=Integer.parseInt(oetPrecioVar_3.getText().toString());
        iPrecioVar_4=Integer.parseInt(oetPrecioVar_4.getText().toString());
        iPrecioVar_5=Integer.parseInt(oetPrecioVar_5.getText().toString());
        iPrecioVar_6=Integer.parseInt(oetPrecioVar_6.getText().toString());
    }

    void fRevisarNombre(final String sEmail,final String sTienda){

        final CollectionReference bd_Articulos= FirebaseFirestore.getInstance().collection("Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Artículos");
        bd_Articulos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String sNombreDocumento;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        sNombreDocumento=document.getId();
                        if (sNombreDocumento.equalsIgnoreCase(sNombreArt)){
                            Toast.makeText(pagNuevoArticulo.this,"EL nombre que colocaste ya está siendo utilizado...",Toast.LENGTH_LONG).show();
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

        /*String sColor;
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
        }*/

        //oetNomCategoria.setBackgroundColor(Color.parseColor(sColor));

        //Nueva Categoria
        DocumentReference bd_NuevoArticulo = FirebaseFirestore.getInstance()
                .document("Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Artículos/"+sNombreArt);

        Map<String,Object> bd_GuardarArticulo=new HashMap<String, Object>();

        bd_GuardarArticulo.put( NOMBRE_KEY,sNombreArt);
        bd_GuardarArticulo.put( CATEGORIA_KEY,sCategoriaArt);
        bd_GuardarArticulo.put( PRECIO_KEY,iPrecioArt);
        bd_GuardarArticulo.put( COSTO_KEY,iCostoArt);
        bd_GuardarArticulo.put( STOCK_KEY,iStockArt);
        bd_GuardarArticulo.put( REF_KEY,sRefArt);
        bd_GuardarArticulo.put( CODIGO_BARRAS_KEY,iCodigoBarrasArt);
        bd_GuardarArticulo.put( DESCUENTO_KEY,iDescuentoArt);
        bd_GuardarArticulo.put( N_VAR_1_KEY,sNomVar_1);
        bd_GuardarArticulo.put( N_VAR_2_KEY,sNomVar_2);
        bd_GuardarArticulo.put( N_VAR_3_KEY,sNomVar_3);
        bd_GuardarArticulo.put( N_VAR_4_KEY,sNomVar_4);
        bd_GuardarArticulo.put( N_VAR_5_KEY,sNomVar_5);
        bd_GuardarArticulo.put( N_VAR_6_KEY,sNomVar_6);
        bd_GuardarArticulo.put( P_VAR_1_KEY,iPrecioVar_1);
        bd_GuardarArticulo.put( P_VAR_2_KEY,iPrecioVar_2);
        bd_GuardarArticulo.put( P_VAR_3_KEY,iPrecioVar_3);
        bd_GuardarArticulo.put( P_VAR_4_KEY,iPrecioVar_4);
        bd_GuardarArticulo.put( P_VAR_5_KEY,iPrecioVar_5);
        bd_GuardarArticulo.put( P_VAR_6_KEY,iPrecioVar_6);

        bd_NuevoArticulo.set(bd_GuardarArticulo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(ALARMA_KEY,"El Artículo ha sido guardado");
                Toast.makeText(pagNuevoArticulo.this,"El Artículo ha sido guardado",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(ALARMA_KEY,"Documento NO fue guardado");
            }
        });
        fNavegarSimple(pagProductos.class);
    }

    void fEliminarBBDD(String sEmail, String sTienda){
        DocumentReference bd_EliminarArticulo = FirebaseFirestore.getInstance()
                .document("Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Artículos/"+sNombreArt);
        bd_EliminarArticulo
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Alarma", "DocumentSnapshot successfully deleted!");
                        Toast.makeText(pagNuevoArticulo.this,"Artículo eliminado",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(pagNuevoArticulo.this,"No se ha podido eliminar el Artículo",Toast.LENGTH_LONG).show();
                    }
                });
        fNavegarSimple(pagProductos.class);
    }
}
