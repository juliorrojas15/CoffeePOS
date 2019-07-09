package com.example.pos_coffee;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.example.pos_coffee.pagLogin.gsTienda;
import static com.example.pos_coffee.pagLogin.gsUsuario;

public class pagProductos extends AppCompatActivity{

    @Override
    protected void onResume(){
        super.onResume();
        sUsuario= gsUsuario;
        sTienda=gsTienda;
        Toast.makeText(this,"Listas Actualizadas",Toast.LENGTH_SHORT).show();
        fActualizar_LV_Articulos(sUsuario,sTienda);
        fActualizar_LV_Categorias(sUsuario,sTienda);
    }


    //#########################################################################################     //Variables que se traen de la pagina de Productos
    //public static final String spEmail="email";
    public static final String spTienda="tienda";


    //#########################################################################################     //Keys de la base de datos

    //#########################################################################################     //Objetos del Layout
    Button obVolver,obAgregarCategoria,obAgregarArticulo;
    ListView olvCategorias,olvArticulos;
    EditText oetFiltroCategorias,oetFiltroArticulos;
    Spinner ospFiltroArticulos;

    //#########################################################################################     Variables Globales
    String sUsuario;
    String sTienda;

    //#########################################################################################################################################
    //#########################################################################################     ON CREATE
    //#########################################################################################################################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_productos);

        //#####################################################################################     Relación de objetos con Layout
        obVolver=(Button)findViewById(R.id.bVolver);
        obAgregarCategoria=(Button)findViewById(R.id.bAgregarCategorias);
        obAgregarArticulo=(Button)findViewById(R.id.bAgregarArticulos);

        olvCategorias=(ListView)findViewById(R.id.lvCategorias);
        olvArticulos=(ListView)findViewById(R.id.lvArticulos);
        oetFiltroCategorias=(EditText)findViewById(R.id.etFiltroCategorias);
        oetFiltroArticulos=(EditText)findViewById(R.id.etFiltroArticulos);
        ospFiltroArticulos=(Spinner)findViewById(R.id.spFiltroArticulos);

        //###################################################################################       Variables que se traen de otros activities

        //###################################################################################       Filtros de los ListView
        oetFiltroCategorias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pagProductos.this.myAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        oetFiltroArticulos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pagProductos.this.myAdapter_Art.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
/*
        ospFiltroArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pagProductos.this.myAdapter_Art.getFilter().filter(Long.toString(myAdapter_Art.getItemId(position)));
            }
        });
*/
        //##################################################################################         Acciones de botones
        obVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Intent Destino=new Intent(pagProductos.this,MainActivity.class);
                //startActivity(Destino);
            }
        });
        obAgregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psEditarEliminar="Agregar";

                Intent Destino=new Intent(getApplication(),pagNuevaCategoria.class);
                Destino.putExtra(pagNuevaCategoria.psEditarEliminar,psEditarEliminar);
                Destino.putExtra(pagNuevaCategoria.psTienda,sTienda);
                startActivity(Destino);
            }
        });
        obAgregarArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psEditarEliminar="Agregar";

                Intent Destino=new Intent(getApplication(),pagNuevoArticulo.class);
                Destino.putExtra(pagNuevoArticulo.psEditarEliminar,psEditarEliminar);
                Destino.putExtra(pagNuevoArticulo.psTienda,sTienda);
                startActivity(Destino);
            }
        });

        //##################################################################################         Acciones de las List view
        olvCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object oArregloSeleccionado = olvCategorias.getItemAtPosition(position);
                Categ_Entidad oItemSeleccionado=(Categ_Entidad)oArregloSeleccionado;
                String psNomCategoria=oItemSeleccionado.getsNomCategoria();
                String psColCategoria=oItemSeleccionado.getsColorCategoria();
                String psEditarEliminar="Editar/Eliminar";

                Intent Destino=new Intent(getApplication(),pagNuevaCategoria.class);
                Destino.putExtra(pagNuevaCategoria.psNomCategoria,psNomCategoria);
                Destino.putExtra(pagNuevaCategoria.psColorCategoria,psColCategoria);
                Destino.putExtra(pagNuevaCategoria.psEditarEliminar,psEditarEliminar);
                Destino.putExtra(pagNuevaCategoria.psTienda,sTienda);
                startActivityForResult(Destino,1);
            }
        });
        olvArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String asDatosArticulos[]=new String[12];
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object oArregloSeleccionado = olvArticulos.getItemAtPosition(position);
                Articulos_Entidad oItemSeleccionado=(Articulos_Entidad)oArregloSeleccionado;
                asDatosArticulos[0]=oItemSeleccionado.getsNombreArt();
                asDatosArticulos[1]=oItemSeleccionado.getsCategoriaArt();
                asDatosArticulos[2]=String.valueOf(oItemSeleccionado.getiPrecioArt());
                asDatosArticulos[3]=String.valueOf(oItemSeleccionado.getiCostoArt());
                asDatosArticulos[4]=String.valueOf(oItemSeleccionado.getiStockArt());
                asDatosArticulos[5]=oItemSeleccionado.getsRefArt();
                asDatosArticulos[6]=String.valueOf(oItemSeleccionado.getiCodigoBarrasArt());
                asDatosArticulos[7]=String.valueOf(oItemSeleccionado.getiDescuentoArt());
                asDatosArticulos[8]=String.valueOf(oItemSeleccionado.getsTipoVisualizacion());
                asDatosArticulos[9]=String.valueOf(oItemSeleccionado.getsColor());
                asDatosArticulos[10]=String.valueOf(oItemSeleccionado.getiIndexColor());
                asDatosArticulos[11]=String.valueOf(oItemSeleccionado.getsUriImagen());

                String psEditarEliminar="Editar/Eliminar";

                Intent Destino=new Intent(getApplication(),pagNuevoArticulo.class);
                Destino.putExtra(pagNuevoArticulo.pasDatosArticulos,asDatosArticulos);
                Destino.putExtra(pagNuevoArticulo.psEditarEliminar,psEditarEliminar);
                Destino.putExtra(pagNuevoArticulo.psTienda,sTienda);
                startActivity(Destino);
            }
        });

        //------------- Actualización de listView
        //fActualizar_LV_Categorias(sEmail,sTienda);
        //fActualizar_LV_Articulos(sEmail,sTienda);
    }
    //#########################################################################################################################################
    //#########################################################################################
    //#########################################################################################################################################

    //#########################################################################################     NAVEGACIÓN SIMPLE
    void fNavegar (View view){
        Intent Destino=new Intent(this,MainActivity.class);
        startActivity(Destino);
    }

    //#########################################################################################     CONSTRUCCIÓN DE LIST VIEW DE CATEGORIAS
    ArrayList<Categ_Entidad> myList;
    Categ_Adaptador myAdapter;
    List<String> lNombreCategorias = new ArrayList<>();
    void fActualizar_LV_Categorias(final String sEmail, final String sTienda) {

        String sPath = "Usuarios/" + sEmail + "/Tiendas/" + sTienda + "/Categorias";
        CollectionReference bd_Datos = FirebaseFirestore.getInstance().collection(sPath);

        bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    myList = new ArrayList<>();
                    Categ_Entidad listItems;
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Collection collection = document.getData().values();
                        List listPrevia = new ArrayList<>(collection);
                        int iPrimero = 0, iSegundo = 2, iTercero = 1;
                        listItems = new Categ_Entidad(listPrevia.get(iPrimero).toString(),
                                listPrevia.get(iSegundo).toString(),
                                listPrevia.get(iTercero).toString());
                        myList.add(listItems);
                        lNombreCategorias.add(listPrevia.get(iPrimero).toString()); //Para filtro de articulos por categoría
                        Log.d("Campos: ", listItems.toString());
                    }
                    myAdapter = new Categ_Adaptador(pagProductos.this, myList);
                    olvCategorias.setAdapter(myAdapter);

                    //Visualización de categorias en filtro de articulos por categorias
                    ArrayAdapter<String> aNombreCategorias=new ArrayAdapter<String>(pagProductos.this,
                            android.R.layout.simple_spinner_dropdown_item,lNombreCategorias);
                    ospFiltroArticulos.setAdapter(aNombreCategorias);

                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });
    }

    //#########################################################################################     CONSTRUCCIÓN DE LIST VIEW DE ARTÍCULOS
    ArrayList<Articulos_Entidad> myList_Art;
    Articulos_Adaptador myAdapter_Art;
    void fActualizar_LV_Articulos(final String sEmail, final String sTienda){

        String sPath = "Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Artículos";
        CollectionReference bd_Datos= FirebaseFirestore.getInstance().collection(sPath);

        bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    myList_Art=new ArrayList<>();
                    Articulos_Entidad listItems;
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Collection collection=document.getData().values();
                        Collection collection2=document.getData().keySet();

                        List listPrevia=new ArrayList<>(collection);
                        List listSet=new ArrayList<>(collection2);
                        int [] PosicionList=new int[12];
                        for (int i=0;i<listSet.size();i++){
                            switch (i){
                                case 0: PosicionList[i]=fBuscarList("Nombre",listSet);break;
                                case 1: PosicionList[i]=fBuscarList("Categoria",listSet);break;
                                case 2: PosicionList[i]=fBuscarList("Ref",listSet);break;
                                case 3: PosicionList[i]=fBuscarList("Costo",listSet);break;
                                case 4: PosicionList[i]=fBuscarList("Precio",listSet);break;
                                case 5: PosicionList[i]=fBuscarList("Stock",listSet);break;
                                case 6: PosicionList[i]=fBuscarList("Descuento",listSet);break;
                                case 7: PosicionList[i]=fBuscarList("Codigo Barras",listSet);break;
                                case 8: PosicionList[i]=fBuscarList("Visualización",listSet);break;
                                case 9: PosicionList[i]=fBuscarList("Color",listSet);break;
                                case 10: PosicionList[i]=fBuscarList("Index Color",listSet);break;
                                case 11: PosicionList[i]=fBuscarList("Uri Imagen",listSet);break;
                            }
                        }
                        listItems=new Articulos_Entidad(
                                listPrevia.get(PosicionList[0]).toString(),
                                listPrevia.get(PosicionList[1]).toString(),
                                listPrevia.get(PosicionList[2]).toString(),
                                Integer.parseInt(listPrevia.get(PosicionList[3]).toString()),
                                Integer.parseInt(listPrevia.get(PosicionList[4]).toString()),
                                Integer.parseInt(listPrevia.get(PosicionList[5]).toString()),
                                Integer.parseInt(listPrevia.get(PosicionList[6]).toString()),
                                Integer.parseInt(listPrevia.get(PosicionList[7]).toString()),
                                listPrevia.get(PosicionList[8]).toString(),
                                listPrevia.get(PosicionList[9]).toString(),
                                Integer.parseInt(listPrevia.get(PosicionList[10]).toString()),
                                listPrevia.get(PosicionList[11]).toString());

                        myList_Art.add(listItems);
                        Log.d("Campos: ", listItems.toString());
                    }
                    myAdapter_Art = new Articulos_Adaptador(pagProductos.this,myList_Art);
                    olvArticulos.setAdapter(myAdapter_Art);
                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });



    }
    int fBuscarList(String Clave, List Listado){
        for(int i=0;i<Listado.size();i++){
            if (Listado.get(i).toString().equals(Clave)){
                return i;
            }
        }
        return 0;
    }

}
