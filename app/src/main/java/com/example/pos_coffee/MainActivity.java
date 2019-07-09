package com.example.pos_coffee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.util.List;

import static com.example.pos_coffee.pagLogin.gsTienda;
import static com.example.pos_coffee.pagLogin.gsUsuario;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onResume(){
        super.onResume();
        sUsuario= gsUsuario;
        sTienda=gsTienda;
        Toast.makeText(this,"Listas Actualizadas",Toast.LENGTH_SHORT).show();
        oTienda.setText(sTienda);
        oUsuario.setText(sUsuario);
        fActualizar_LV_Articulos(sUsuario,sTienda);
    }



    //#########################################################################################     //Variables que se traen de la pagina de Productos
    public static final String spUsuario="usuario";
    public static final String spTienda="tienda";

    //#########################################################################################     //Keys de la base de datos

    //#########################################################################################     //Objetos del Layout
    Button obGuardar;
    TextView oTienda,oUsuario;
    GridView ogvListaArticulos;
    TextView oetFiltroArticulos;
    ListView olvVentaArticulos;


    //#########################################################################################     Variables Globales
    ArrayList<Articulos_Entidad> myList_Venta_Art=new ArrayList<>();
    Art_Venta_Adaptador myAdapter_Venta_Art;
    Articulos_Entidad listItems;
    Object oArregloSeleccionado;
    Articulos_Entidad oItemSeleccionado;
    String sUsuario,sTienda;
    //#########################################################################################################################################
    //#########################################################################################     ON CREATE
    //#########################################################################################################################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //###################################################################################       Configuración de los menús y barras de desplazamiento
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //###################################################################################       Variables que se traen de otros activities

        //###################################################################################       Traer los TextView de otros layouts
        View headerView=navigationView.getHeaderView(0);
        oUsuario=(TextView)headerView.findViewById(R.id.tvCorreoUsuario);
        oTienda=(TextView)headerView.findViewById(R.id.tvNombreTienda);
        //oTienda.setText(sTienda);
        //oUsuario.setText(sUsuario);


        //#####################################################################################     Relación de objetos con Layout
        obGuardar = (Button) findViewById(R.id.bGuardar);
        ogvListaArticulos=(GridView)findViewById(R.id.gvListaArticulos);
        oetFiltroArticulos=(TextView)findViewById(R.id.etFiltroArticulos);
        olvVentaArticulos=(ListView)findViewById(R.id.lvVentaArticulos);

        //##################################################################################         Acciones de botones
        obGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fNavegar();
            }
        });

        //###################################################################################       Filtros de los ListView
        oetFiltroArticulos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.this.myAdapter_Art.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //##################################################################################         Acciones de las List view

        ogvListaArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                oArregloSeleccionado = ogvListaArticulos.getItemAtPosition(position);
                oItemSeleccionado=(Articulos_Entidad)oArregloSeleccionado;

                listItems=new Articulos_Entidad(
                oItemSeleccionado.getsNombreArt(),
                oItemSeleccionado.getsCategoriaArt(),
                String.valueOf(oItemSeleccionado.getsRefArt()),
                oItemSeleccionado.getiCostoArt(),
                oItemSeleccionado.getiPrecioArt(),
                oItemSeleccionado.getiStockArt(),
                oItemSeleccionado.getiDescuentoArt(),
                oItemSeleccionado.getiCodigoBarrasArt(),
                String.valueOf(oItemSeleccionado.getsTipoVisualizacion()),
                String.valueOf(oItemSeleccionado.getsColor()),
                oItemSeleccionado.getiIndexColor(),
                String.valueOf(oItemSeleccionado.getsUriImagen()));

                myList_Venta_Art.add(listItems);
                Log.d("Campos: ", listItems.toString());

                myAdapter_Venta_Art = new Art_Venta_Adaptador(MainActivity.this,myList_Venta_Art,1);
                olvVentaArticulos.setAdapter(myAdapter_Venta_Art);

            }
        });

        //------------- Actualización de listView
        //fActualizar_LV_Articulos(sUsuario,sTienda);

    }
    //#########################################################################################################################################
    //#########################################################################################
    //#########################################################################################################################################

    /*
    @Override
    protected void onStop(){
        super.onStop();
        String sPagina=MainActivity.class.getSimpleName();
        Intent Destino=new Intent(getApplication(),pagClavePersonal.class);
        Destino.putExtra(pagClavePersonal.sPagOrg,sPagina);
        startActivity(Destino);
    }
    */

    //#########################################################################################     NAVEGACIÓN SIMPLE
    public void fNavegar (){
        //Navagación
        Intent Destino=new Intent(MainActivity.this,pagGuardar.class);
        startActivity(Destino);
    }

    //#########################################################################################     ACCIONES DEL MENÚ

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //#########################################################################################     CONSTRUCCIÓN DE LIST VIEW DE ARTÍCULOS
    ArrayList<Articulos_Entidad> myList_Art;
    Art_Disp_Adaptador myAdapter_Art;
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
                    myAdapter_Art = new Art_Disp_Adaptador(MainActivity.this,myList_Art);
                    ogvListaArticulos.setAdapter(myAdapter_Art);
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



    //#########################################################################################     NAVEGACIÓN DEL MENÚ

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_Turno) {

        } else if (id == R.id.nav_Clientes) {
            fNavegarMenu(pagClientes.class);
        } else if (id == R.id.nav_Productos) {
            fNavegarMenu(pagProductos.class);
            Intent Destino=new Intent(getApplication(),pagProductos.class);
            Destino.putExtra(pagProductos.spTienda,sTienda);
            startActivityForResult(Destino,1);
        }
        else if (id == R.id.nav_Usuarios) {

        } else if (id == R.id.nav_Configuracion) {
            Intent Destino=new Intent(getApplication(),pagConfiguracion.class);
            Destino.putExtra(pagConfiguracion.spTienda,sTienda);
            startActivity(Destino);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void fNavegarMenu(Class clPagina){
        Intent Destino = new Intent(this,clPagina);
        startActivity(Destino);
    }



}
