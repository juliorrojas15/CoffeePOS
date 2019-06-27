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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class pagProductos extends AppCompatActivity{
    public static final String sTienda="tienda";


    Button obVolver,obAgregarCategoria,obAgregarArticulo;
    ListView olvCategorias,olvArticulos;
    EditText oetFiltroCategorias,oetFiltroArticulos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_productos);
        obVolver=(Button)findViewById(R.id.bVolver);
        obAgregarCategoria=(Button)findViewById(R.id.bAgregarCategorias);
        obAgregarArticulo=(Button)findViewById(R.id.bAgregarArticulos);

        olvCategorias=(ListView)findViewById(R.id.lvCategorias);
        olvArticulos=(ListView)findViewById(R.id.lvArticulos);
        oetFiltroCategorias=(EditText)findViewById(R.id.etFiltroCategorias);
        //oetFiltroCategorias.addTextChangedListener(this);
        oetFiltroArticulos=(EditText)findViewById(R.id.etFiltroArticulos);

        //########### Filtros de los ListView
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
                String psEditarEliminar="Agregar";

                Intent Destino=new Intent(getApplication(),pagNuevaCategoria.class);
                Destino.putExtra(pagNuevaCategoria.psEditarEliminar,psEditarEliminar);
                startActivity(Destino);
            }
        });
        obAgregarArticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psEditarEliminar="Agregar";

                Intent Destino=new Intent(getApplication(),pagNuevoArticulo.class);
                Destino.putExtra(pagNuevoArticulo.psEditarEliminar,psEditarEliminar);
                startActivity(Destino);
            }
        });

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
                startActivity(Destino);
            }
        });



        //------------- Actualización de listView
        fActualizar_LV_Categorias(sEmail,sTienda);
        fActualizar_LV_Articulos(sEmail,sTienda);
    }

    void fNavegar (View view){
        Intent Destino=new Intent(this,MainActivity.class);
        startActivity(Destino);
    }

    //############################################ Construcción de ListView de categorias
    ArrayList<Categ_Entidad> myList;
    Categ_Adaptador myAdapter;
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
                        Log.d("Campos: ", listItems.toString());
                    }
                    //olvCategorias=(ListView)findViewById(R.id.lvCategorias);
                    myAdapter = new Categ_Adaptador(pagProductos.this, myList);
                    olvCategorias.setAdapter(myAdapter);
                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });
    }

    //############################################ Construcción de ListView de Artículos
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
                        int [] PosicionList=new int[20];
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
                                case 8: PosicionList[i]=fBuscarList("Nom_Var_1",listSet);break;
                                case 9: PosicionList[i]=fBuscarList("Nom_Var_2",listSet);break;
                                case 10: PosicionList[i]=fBuscarList("Nom_Var_3",listSet);break;
                                case 11: PosicionList[i]=fBuscarList("Nom_Var_4",listSet);break;
                                case 12: PosicionList[i]=fBuscarList("Nom_Var_5",listSet);break;
                                case 13: PosicionList[i]=fBuscarList("Nom_Var_6",listSet);break;
                                case 14: PosicionList[i]=fBuscarList("Precio_Var_1",listSet);break;
                                case 15: PosicionList[i]=fBuscarList("Precio_Var_2",listSet);break;
                                case 16: PosicionList[i]=fBuscarList("Precio_Var_3",listSet);break;
                                case 17: PosicionList[i]=fBuscarList("Precio_Var_4",listSet);break;
                                case 18: PosicionList[i]=fBuscarList("Precio_Var_5",listSet);break;
                                case 19: PosicionList[i]=fBuscarList("Precio_Var_6",listSet);break;
                            }
                        }
                        int iPrimero=0,iSegundo=2,iTercero=1;
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
                                listPrevia.get(PosicionList[10]).toString(),
                                listPrevia.get(PosicionList[11]).toString(),
                                listPrevia.get(PosicionList[12]).toString(),
                                listPrevia.get(PosicionList[13]).toString(),
                                Integer.parseInt(listPrevia.get(PosicionList[14]).toString()),
                                Integer.parseInt(listPrevia.get(PosicionList[15]).toString()),
                                Integer.parseInt(listPrevia.get(PosicionList[16]).toString()),
                                Integer.parseInt(listPrevia.get(PosicionList[17]).toString()),
                                Integer.parseInt(listPrevia.get(PosicionList[18]).toString()),
                                Integer.parseInt(listPrevia.get(PosicionList[19]).toString()));
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
