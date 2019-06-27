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

public class pagProductos extends AppCompatActivity implements TextWatcher{
    public static final String sTienda="tienda";


    Button obVolver,obAgregarCategoria,obAgregarArticulo;
    ListView olvCategorias;
    EditText oetFiltroCategorias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_productos);
        obVolver=(Button)findViewById(R.id.bVolver);
        obAgregarCategoria=(Button)findViewById(R.id.bAgregarCategorias);
        obAgregarArticulo=(Button)findViewById(R.id.bAgregarArticulos);

        olvCategorias=(ListView)findViewById(R.id.lvCategorias);
        oetFiltroCategorias=(EditText)findViewById(R.id.etFiltroCategorias);
        oetFiltroCategorias.addTextChangedListener(this);

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
        //fActualizar_LV_Articulos(sEmail,sTienda);
    }

    //Filtrar las Categorias
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.myAdapter.getFilter().filter(s);
    }
    @Override
    public void afterTextChanged(Editable s) {

    }



    void fNavegar (View view){
        Intent Destino=new Intent(this,MainActivity.class);
        startActivity(Destino);
    }

    ArrayList<Categ_Entidad> myList;
    Categ_Adaptador myAdapter;

    void fActualizar_LV_Categorias(final String sEmail, final String sTienda){

        String sPath = "Usuarios/"+sEmail+"/Tiendas/"+sTienda+"/Categorias";
        CollectionReference bd_Datos= FirebaseFirestore.getInstance().collection(sPath);

        bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    myList=new ArrayList<>();
                    Categ_Entidad listItems;
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Collection collection=document.getData().values();
                        List listPrevia=new ArrayList<>(collection);
                        int iPrimero=0,iSegundo=2,iTercero=1;
                        listItems=new Categ_Entidad(listPrevia.get(iPrimero).toString(),
                                listPrevia.get(iSegundo).toString(),
                                listPrevia.get(iTercero).toString());
                        myList.add(listItems);
                        Log.d("Campos: ", listItems.toString());
                    }
                    //olvCategorias=(ListView)findViewById(R.id.lvCategorias);
                    myAdapter = new Categ_Adaptador(pagProductos.this,myList);
                    olvCategorias.setAdapter(myAdapter);
                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });





    }

}
