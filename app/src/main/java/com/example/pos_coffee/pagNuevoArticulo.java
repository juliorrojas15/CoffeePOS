package com.example.pos_coffee;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pos_coffee.pagLogin.gsTienda;
import static com.example.pos_coffee.pagLogin.gsUsuario;

public class pagNuevoArticulo extends AppCompatActivity {
    //#########################################################################################     //Variables que se traen de la pagina de Productos
    public static final String psTienda="tienda";
    public static final String pasDatosArticulos="DatosArticulo";
    public static final String psEditarEliminar="Editar/Eliminar";

    //#########################################################################################     //Keys de la base de datos
    public static final String NOMBRE_KEY = "Nombre";
    public static final String CATEGORIA_KEY = "Categoria";
    public static final String PRECIO_KEY = "Precio";
    public static final String COSTO_KEY = "Costo";
    public static final String STOCK_KEY = "Stock";
    public static final String REF_KEY = "Ref";
    public static final String CODIGO_BARRAS_KEY = "Codigo Barras";
    public static final String DESCUENTO_KEY = "Descuento";
    public static final String ALARMA_KEY = "Alarma";
    public static final String VISUALIZACION_KEY = "Visualización";
    public static final String COLOR_KEY = "Color";
    public static final String COLOR_INDEX_KEY = "Index Color";
    public static final String URL_IMAGEN_KEY = "Uri Imagen";

    //#########################################################################################     //Objetos del Layout
    private EditText oetNombre,oetPrecio,oetCosto,oetStock,oetRef,oetCodBarras,oetDescuento;
    private RadioGroup orgColores,orgVisualizacion;
    private RadioButton orbColor,orbVisual;
    private Spinner ospCategoria;
    private Button obVolver, obGuardar,obEditar,obEliminar;
    private final int PICK_PHOTO=1;
    private ImageView oimArticulo;

    //#########################################################################################     Variables Globales
    String sNombreArt,sCategoriaArt,sRefArt;
    int iCostoArt,iPrecioArt,iStockArt,iDescuentoArt,iCodigoBarrasArt;
    private Uri uriImagenPath;
    private FirebaseAuth mAuth;


    //#########################################################################################################################################
    //#########################################################################################     ON CREATE
    //#########################################################################################################################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_nuevo_articulo);

        //#####################################################################################     Relación de objetos con Layout
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

        orgColores=(RadioGroup)findViewById(R.id.rgColoresArt);
        orgVisualizacion=(RadioGroup)findViewById(R.id.rgVisualizacion);
        ospCategoria=(Spinner)findViewById(R.id.spNuevaCategoriaArt);
        oimArticulo=(ImageView)findViewById(R.id.imImagenArt);

        //###################################################################################       Variables que se traen de otros activities
        final String sTienda =gsTienda;
        final String sEmail=gsUsuario;
        final String [] sDatosArticulos=getIntent().getStringArrayExtra("DatosArticulo");
        final String psEditarEliminar=getIntent().getStringExtra("Editar/Eliminar");


        //###################################################################################       Cargas iniciales

        //###################################################################################       Visualización o no de botones de edición
        if (psEditarEliminar.equalsIgnoreCase("Agregar")){
            obGuardar.setVisibility(View.VISIBLE);
            obEditar.setVisibility(View.INVISIBLE);
            obEliminar.setVisibility(View.INVISIBLE);
            oetNombre.setEnabled(true);
            fNombreCategorias(sEmail,sTienda,"","");
        }
        if(psEditarEliminar.equalsIgnoreCase("Editar/Eliminar")){
            obGuardar.setVisibility(View.INVISIBLE);
            obEditar.setVisibility(View.VISIBLE);
            obEliminar.setVisibility(View.VISIBLE);
            oetNombre.setText(sDatosArticulos[0]);
            fNombreCategorias(sEmail,sTienda,"Editar",sDatosArticulos[1]);
            oetPrecio.setText(sDatosArticulos[2]);
            oetCosto.setText(sDatosArticulos[3]);
            oetStock.setText(sDatosArticulos[4]);
            oetRef.setText(sDatosArticulos[5]);
            oetCodBarras.setText(sDatosArticulos[6]);
            oetDescuento.setText(sDatosArticulos[7]);
            if (sDatosArticulos[8].equals("Color")){
                RadioButton orbVisualActual = (RadioButton) orgVisualizacion.getChildAt(0);
                orbVisualActual.setChecked(true);
                RadioButton orbColorActual = (RadioButton) orgColores.getChildAt(Integer.parseInt(sDatosArticulos[10]));
                orbColorActual.setChecked(true);
            }
            else{
                RadioButton orbVisualActual = (RadioButton) orgVisualizacion.getChildAt(1);
                orbVisualActual.setChecked(true);

                Glide
                        .with(this)
                        .load(sDatosArticulos[11])
                        .into(oimArticulo);
            }
            oetNombre.setEnabled(false);
        }


        //##################################################################################         Acciones de botones

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
                fCrearBBDD(sEmail,sTienda,sDatosArticulos[11]);
            }
        });
        obEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fScanner();
                fEliminarBBDD(sEmail,sTienda);
            }
        });
        oimArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fCargarImagen();
            }
        });



    }
    //#########################################################################################################################################
    //#########################################################################################
    //#########################################################################################################################################

    //#########################################################################################     NAVEGACIÓN SIMPLE
    void fNavegarSimple(Class clPagina){
        Intent Destino = new Intent(this,clPagina);
        startActivity(Destino);
    }

    //#########################################################################################     SCANNER
    void fScanner(){
        sNombreArt=oetNombre.getText().toString();
        sCategoriaArt=ospCategoria.getSelectedItem().toString().trim();
        sRefArt=oetRef.getText().toString();
        iCostoArt=Integer.parseInt(oetCosto.getText().toString());
        iPrecioArt=Integer.parseInt(oetPrecio.getText().toString());
        iStockArt=Integer.parseInt(oetStock.getText().toString());
        iDescuentoArt=Integer.parseInt(oetDescuento.getText().toString());
        iCodigoBarrasArt=Integer.parseInt(oetCodBarras.getText().toString());
    }

    //#########################################################################################     REVISIÓN DE NOMBRE
    void fRevisarNombre(final String sEmail,final String sTienda){

        final CollectionReference bd_Articulos= FirebaseFirestore.getInstance().
                collection("Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Artículos");
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
                    fCrearBBDD(sEmail,sTienda,"");

                } else {
                    Log.d("Alarma", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });

    }

    //#########################################################################################     CREAR ARTICULO EN BASE DE DATOS
    void fCrearBBDD(final String sEmail, final String sTienda,final String sUrlActual) {



        mAuth=FirebaseAuth.getInstance();
        //Creación de la carpeta de fotos
        //mStorageRef=bd_NuevaImagen
        final StorageReference bd_NuevaImagen= FirebaseStorage.getInstance().getReference();

        if (uriImagenPath!=null){
            //srImagenArt=fotoRef
            final StorageReference srImagenArt = bd_NuevaImagen.child("Imagenes").child(mAuth.getCurrentUser()
                    .getUid()).child(uriImagenPath.getLastPathSegment());
            srImagenArt.putFile(uriImagenPath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw new Exception();
                    }
                    return srImagenArt.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uriImagenBD=task.getResult();
                        fGuardarArticulo(sEmail,sTienda,uriImagenBD.toString());
                    }

                }
            });
        }
        else{
            fGuardarArticulo(sEmail,sTienda,sUrlActual);
        }
    }

    void fGuardarArticulo(String sEmail,String sTienda,String uriImagenBD){
        //obtener tipo de visualización
        final String sVisualizacion;
        int iVisualSeleccionada=orgVisualizacion.getCheckedRadioButtonId();
        orbVisual=(RadioButton)findViewById(iVisualSeleccionada);
        int iVisualizacion=orgVisualizacion.indexOfChild(orbVisual);
        switch (iVisualizacion){
            case 0: sVisualizacion="Color";break;
            case 1: sVisualizacion="Imagen";break;
            default: sVisualizacion="Color";
        }

        //obtener color seleccionado
        final String sColor;
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
        //Nuevo Articulo
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
        bd_GuardarArticulo.put(VISUALIZACION_KEY,sVisualizacion);
        bd_GuardarArticulo.put(COLOR_KEY,sColor);
        bd_GuardarArticulo.put(COLOR_INDEX_KEY,iColor);
        bd_GuardarArticulo.put(URL_IMAGEN_KEY,uriImagenBD.toString());

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Creando o Editando Artículo");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        bd_NuevoArticulo.set(bd_GuardarArticulo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(ALARMA_KEY,"El Artículo ha sido guardado");
                Toast.makeText(pagNuevoArticulo.this,"El Artículo ha sido guardado",Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                fNavegarSimple(pagProductos.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(ALARMA_KEY,"Documento NO fue guardado");
            }
        });
    }

    //#########################################################################################     ELIMINAR ARTICULO EN BASE DE DATOS
    void fEliminarBBDD(String sEmail, String sTienda){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Eliminando");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        DocumentReference bd_EliminarArticulo = FirebaseFirestore.getInstance()
                .document("Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Artículos/"+sNombreArt);
        bd_EliminarArticulo
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Alarma", "DocumentSnapshot successfully deleted!");
                        Toast.makeText(pagNuevoArticulo.this,"Artículo eliminado",Toast.LENGTH_LONG).show();
                        progressDialog.cancel();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(pagNuevoArticulo.this,"No se ha podido eliminar el Artículo",Toast.LENGTH_LONG).show();
                    }
                });
        //fNavegarSimple(pagProductos.class,sTienda);
    }

    //#########################################################################################     SPINNER DE CATEGORIAS
    void fNombreCategorias(String sEmail, String sTienda, final String sEditar_Crear, final String sDatoCategoria){
        Collection cCategorias;
        CollectionReference bd_Tiendas= FirebaseFirestore.getInstance().collection("Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Categorias");
        bd_Tiendas.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    Log.d("Alarma", list.toString());
                    ArrayAdapter Adapter = new ArrayAdapter(pagNuevoArticulo.this,android.R.layout.simple_spinner_dropdown_item,list);
                    ospCategoria.setAdapter(Adapter);
                    ospCategoria.setSelection(0);
                    if (sEditar_Crear.equals("Editar")){
                        for (int i=0;i<ospCategoria.getCount();i++){
                            if (ospCategoria.getItemAtPosition(i).toString().equalsIgnoreCase(sDatoCategoria)){
                                ospCategoria.setSelection(i);
                            }
                        }
                    }
                } else {
                    Log.d("Alarma", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });
    }

    //#########################################################################################     CARGAR IMAGEN
    void fCargarImagen(){

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),PICK_PHOTO);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_PHOTO && resultCode==RESULT_OK && data!= null && data.getData()!= null){
            uriImagenPath=data.getData();
            try {
                Bitmap bitmapImagen= MediaStore.Images.Media.getBitmap(getContentResolver(),uriImagenPath);
                oimArticulo.setImageBitmap(bitmapImagen);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
