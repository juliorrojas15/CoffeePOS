package com.example.pos_coffee;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
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
        fActualizar_SP_Categorias(sUsuario,sTienda);
    }



    //#########################################################################################     //Variables que se traen de la pagina de Productos
    public static final String spUsuario="usuario";
    public static final String spTienda="tienda";

    //#########################################################################################     //Keys de la base de datos

    //#########################################################################################     //Objetos del Layout
    Button obGuardar;
    Button obNuevoTicket;
    TextView oTienda,oUsuario;
    GridView ogvListaArticulos;
    TextView oetFiltroArticulos;
    Spinner ospFiltroArticulos;
    ListView olvVentaArticulos;
    TextView otvTotalVenta;
    ListView olvTickets;

    //#########################################################################################     Variables Globales
    ArrayList<Articulos_Entidad> myList_Art;
    Art_Disp_Adaptador myAdapter_Art;

    Art_Venta_Adaptador myAdapter_Venta_Art;
    Articulos_Entidad listItems;
    Articulos_Entidad oItemSeleccionado;
    Object oArregloSeleccionado;
    ArrayList<ArrayList<Integer>> listCantidades=new ArrayList<ArrayList<Integer>>();
    ArrayList<ArrayList<Articulos_Entidad>> list_Venta_Art=new ArrayList<ArrayList<Articulos_Entidad>>();

    int iRepeticiones=2;

    ArrayList<Ticket_Abiertos_Entidad> myList_Tickets=new ArrayList<>();
    Ticket_Abiertos_Adaptador myAdapter_Tickets;
    Ticket_Abiertos_Entidad listTickets;
    ArrayList<Integer> alValores=new ArrayList<>();
    int iTicketSeleccionado=0;
    Art_Venta_Adaptador adapterVacio;


    String sUsuario,sTienda;
    ArrayList<Integer> alTotalVenta=new ArrayList<Integer>();
    DecimalFormat decimalFormat=new DecimalFormat("#,##0");



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
        oTienda.setText(sTienda);
        oUsuario.setText(sUsuario);
        //Button obBorrarTicket=(Button)headerView.findViewById(R.id.bBorrarTicket);


        //#####################################################################################     Relación de objetos con Layout
        obGuardar = (Button) findViewById(R.id.bGuardar);
        obNuevoTicket=(Button)findViewById(R.id.bNuevoTicket);
        ogvListaArticulos=(GridView)findViewById(R.id.gvListaArticulos);
        oetFiltroArticulos=(TextView)findViewById(R.id.etFiltroArticulos);
        ospFiltroArticulos=(Spinner)findViewById(R.id.spFiltro_Art_Disp);
        olvVentaArticulos=(ListView)findViewById(R.id.lvVentaArticulos);
        otvTotalVenta=(TextView)findViewById(R.id.tvTotal);
        olvTickets=(ListView)findViewById((R.id.lvTicketsAbiertos));

        //##################################################################################         Acciones de botones
        obGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Destino=new Intent(MainActivity.this,pagGuardar.class);

                Destino.putExtra("EstaVenta",alTotalVenta.get(iTicketSeleccionado));
                Destino.putExtra("Cantidades",listCantidades.get(iTicketSeleccionado));
                Destino.putExtra("ListadoArt",list_Venta_Art.get(iTicketSeleccionado));
                startActivity(Destino);
            }
        });

        obNuevoTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                olvVentaArticulos.setAdapter(adapterVacio);
                fTickets_LV("NUEVO",0,olvTickets.getCount()+1);
                otvTotalVenta.setText("$ 0");
            }
        });

        //###################################################################################       Filtros de los ListView
        oetFiltroArticulos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    MainActivity.this.myAdapter_Art.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ospFiltroArticulos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sCategoriaFiltrada=ospFiltroArticulos.getSelectedItem().toString();
                if (!sCategoriaFiltrada.equals("")){
                    MainActivity.this.myAdapter_Art.getFilter().filter(sCategoriaFiltrada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //##################################################################################         Acciones de las List view

        //Listado de todos los artículos disponibles
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


                Log.d("Campos: ", listItems.toString());

                //Identificar si el ultimo articulo en el LV de ventas es igual al seleccionado

                int iUltimoItem=0;
                if (olvVentaArticulos.getCount()>0) {
                    iUltimoItem=olvVentaArticulos.getLastVisiblePosition();
                    Object oUltimoItemVenta = olvVentaArticulos.getItemAtPosition(iUltimoItem);
                    Articulos_Entidad oUltimoItem = (Articulos_Entidad) oUltimoItemVenta;
                    String sNombreUltimoArt = oUltimoItem.getsNombreArt();


                    if (sNombreUltimoArt == null) {
                        listCantidades.get(iTicketSeleccionado).add(1);
                        iRepeticiones=2;
                    }
                    else {
                        if (sNombreUltimoArt.equals(oItemSeleccionado.getsNombreArt())) {
                            int iSizeListado= listCantidades.get(iTicketSeleccionado).size()-1;
                            listCantidades.get(iTicketSeleccionado).set(iSizeListado,iRepeticiones);
                            iRepeticiones++;
                        } else {
                            listCantidades.get(iTicketSeleccionado).add(1);
                            iRepeticiones=2;
                            list_Venta_Art.get(iTicketSeleccionado).add(listItems);
                        }

                    }
                }
                else{
                    iRepeticiones=2;
                    list_Venta_Art.add(new ArrayList<Articulos_Entidad>());
                    list_Venta_Art.get(iTicketSeleccionado).add(listItems);

                    listCantidades.add(new ArrayList<Integer>());
                    listCantidades.get(iTicketSeleccionado).add(1);

                    alTotalVenta.add(0);

                    if (olvTickets==null || olvTickets.getCount()==0){
                        fTickets_LV("NUEVO",0,0);
                        //iTicketSeleccionado=0;
                    }
                }

                myAdapter_Venta_Art = new Art_Venta_Adaptador(MainActivity.this,
                        list_Venta_Art.get(iTicketSeleccionado),listCantidades.get(iTicketSeleccionado));
                olvVentaArticulos.setAdapter(myAdapter_Venta_Art);

                alTotalVenta.set(iTicketSeleccionado,alTotalVenta.get(iTicketSeleccionado)+listItems.getiPrecioArt());
                String sTotalVenta= "$ "+decimalFormat.format(alTotalVenta.get(iTicketSeleccionado));
                otvTotalVenta.setText(sTotalVenta);
                fTickets_LV("EDICION",alTotalVenta.get(iTicketSeleccionado),iTicketSeleccionado);
            }
        });

        //Listado de los artículos seleccionados para la venta
        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(olvVentaArticulos),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter recyclerView, int position) {
                                Object oArregloSeleccionado = olvVentaArticulos.getItemAtPosition(position);
                                Articulos_Entidad oItemSeleccionado=(Articulos_Entidad)oArregloSeleccionado;
                                int iPrecio=oItemSeleccionado.getiPrecioArt();
                                alTotalVenta.set(iTicketSeleccionado,alTotalVenta.get(iTicketSeleccionado)-iPrecio*listCantidades.get(iTicketSeleccionado).get(position));
                                String sTotalVenta= "$ "+decimalFormat.format(alTotalVenta.get(iTicketSeleccionado));
                                otvTotalVenta.setText(sTotalVenta);
                                listCantidades.get(iTicketSeleccionado).remove(position);
                                myAdapter_Venta_Art.remove(position);
                                fTickets_LV("EDICION",alTotalVenta.get(iTicketSeleccionado),iTicketSeleccionado);
                            }
                        });

        olvVentaArticulos.setOnTouchListener(touchListener);
        olvVentaArticulos.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        olvVentaArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Position " + position, LENGTH_SHORT).show();
                }
            }
        });

        //Listado de Tickets
        //ogvListaArticulos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        olvTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iTicketSeleccionado=position;
                if (alTotalVenta.size()>iTicketSeleccionado){
                    fTickets_LV("EDICION",alTotalVenta.get(iTicketSeleccionado),iTicketSeleccionado);
                    myAdapter_Venta_Art = new Art_Venta_Adaptador(MainActivity.this,
                            list_Venta_Art.get(iTicketSeleccionado),listCantidades.get(iTicketSeleccionado));
                    olvVentaArticulos.setAdapter(myAdapter_Venta_Art);
                    String sTotalVenta= "$ "+decimalFormat.format(alTotalVenta.get(iTicketSeleccionado));
                    otvTotalVenta.setText(sTotalVenta);
                }
                else{
                    fTickets_LV("EDICION",0,iTicketSeleccionado);
                    olvVentaArticulos.setAdapter(adapterVacio);
                    otvTotalVenta.setText("$ 0");
                }
            }
        });

    }
    //#########################################################################################################################################
    //#########################################################################################
    //#########################################################################################################################################

    //#########################################################################################     NAVEGACIÓN SIMPLE
    public void fNavegar (){
        //Navagación
        //Intent Destino=new Intent(MainActivity.this,pagGuardar.class);
        //startActivity(Destino);
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
        if (id == R.id.bBorrarTicket) {

            myAdapter_Tickets.remove(iTicketSeleccionado);
            if (olvVentaArticulos.getCount()>0){
                listCantidades.remove(iTicketSeleccionado);
                list_Venta_Art.remove(iTicketSeleccionado);
                alValores.remove(iTicketSeleccionado);
                alTotalVenta.remove(iTicketSeleccionado);
            }

            if (alTotalVenta.size()>0){
                myAdapter_Venta_Art = new Art_Venta_Adaptador(MainActivity.this,
                        list_Venta_Art.get(0),listCantidades.get(0));
                olvVentaArticulos.setAdapter(myAdapter_Venta_Art);

                iTicketSeleccionado=0;
                String sTotalVenta= "$ "+decimalFormat.format(alTotalVenta.get(0));
                otvTotalVenta.setText(sTotalVenta);
                fTickets_LV("EDICION",alTotalVenta.get(0),0);
            }
            else{
                olvVentaArticulos.setAdapter(adapterVacio);
                otvTotalVenta.setText("$ 0");
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //#########################################################################################     CONSTRUCCIÓN DE LIST VIEW DE ARTÍCULOS
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

    //#########################################################################################     CONSTRUCCIÓN DE LIST VIEW DE TICKETS

    void fTickets_LV(String Nuevo_Edicion, int iValor,int iIndex){

        Calendar Hora=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String sHora=sdf.format(Hora.getTime());

        listTickets=new Ticket_Abiertos_Entidad(
                "T-"+sHora,
                iValor);
        if (Nuevo_Edicion.equals("NUEVO")){
            myList_Tickets.add(listTickets);
            alValores.add(iValor);
            if (olvTickets.getCount()>0){
                iTicketSeleccionado=olvTickets.getCount();
            }
        }
        else{
            alValores.set(iIndex,iValor);
        }
        //iTicketSeleccionado=olvTickets.getCount()-1;
        myAdapter_Tickets = new Ticket_Abiertos_Adaptador(MainActivity.this,myList_Tickets,alValores,iTicketSeleccionado);
        olvTickets.setAdapter(myAdapter_Tickets);
    }

    //#########################################################################################     CONSTRUCCIÓN DE SPINNER DE CATEGORIAS
    void fActualizar_SP_Categorias(final String sEmail, final String sTienda) {

        String sPath = "Usuarios/" + sEmail + "/Tiendas/" + sTienda + "/Categorias";
        CollectionReference bd_Datos = FirebaseFirestore.getInstance().collection(sPath);
        final List<String> lNombreCategorias = new ArrayList<>();
        bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //myList = new ArrayList<>();
                    Categ_Entidad listItems;
                    lNombreCategorias.add("Todas");
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Collection collection = document.getData().values();
                        List listPrevia = new ArrayList<>(collection);
                        int iPrimero = 0, iSegundo = 2, iTercero = 1;
                        listItems = new Categ_Entidad(listPrevia.get(iPrimero).toString(),
                                listPrevia.get(iSegundo).toString(),
                                listPrevia.get(iTercero).toString());
                        lNombreCategorias.add(listPrevia.get(iPrimero).toString());
                        Log.d("Campos: ", listItems.toString());
                    }

                    ArrayAdapter<String> aNombreCategorias;
                    aNombreCategorias=new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,lNombreCategorias);
                    ospFiltroArticulos.setAdapter(aNombreCategorias);

                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });
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
