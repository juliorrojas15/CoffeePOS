package com.example.pos_coffee;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.pos_coffee.pagLogin.gsTienda;
import static com.example.pos_coffee.pagLogin.gsUsuario;


public class pagGuardar extends AppCompatActivity {

    //#########################################################################################     //Variables que se traen de la pagina de Productos

    //#########################################################################################     //Keys de la base de datos

    //#########################################################################################     //Objetos del Layout
    Button obGuardar,obImprimirTodo,obImprimirUltimo,obEnviarTodo,obEnviarUltimo,obCancelar;
    RadioGroup orgTipoCliente;
    RadioButton orbClienteNuevo,orbClienteExistente;
    Spinner ospDescuento;
    EditText oetBuscarCliente,oetNombreNuevo,oetCelularNuevo;
    ListView olvClientes;
    TextView otvNombre,otvCelular,otvDeudaAnterior,otvEstaCuenta_1,otvEstaCuenta_2,otvDeudaNueva,
            otvDescuento;


    //#########################################################################################     Variables Globales
    ArrayList<Clientes_Entidad> alClientes=new ArrayList<>();
    ArrayList<Integer> alCantidades;
    ArrayList<Articulos_Entidad> alListadoArt;

    Clientes_Adaptador myAdapter;
    Integer InEstaVenta,InVentaFinal,InDescuento,InPorcentaje;

    //#########################################################################################################################################
    //#########################################################################################     ON CREATE
    //#########################################################################################################################################

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_guardar);

        //###################################################################################       Variables que se traen de otros activities
        InEstaVenta=(Integer)getIntent().getSerializableExtra("EstaVenta");
        alCantidades=(ArrayList<Integer>)getIntent().getSerializableExtra("Cantidades");
        alListadoArt=(ArrayList<Articulos_Entidad>)getIntent().getSerializableExtra("ListadoArt");

        //###################################################################################       Traer los TextView de otros layouts

        //#####################################################################################     Relación de objetos con Layout
        obGuardar = (Button)findViewById(R.id.bSoloGuardar);
        obImprimirTodo = (Button)findViewById(R.id.bImprimirTodo);
        obImprimirUltimo = (Button)findViewById(R.id.bImprimirUltimo);
        obEnviarTodo = (Button)findViewById(R.id.bEnviarTodo);
        obEnviarUltimo = (Button)findViewById(R.id.bEnviarUltimo);
        obCancelar = (Button)findViewById(R.id.bCancelar);
        orgTipoCliente = (RadioGroup)findViewById(R.id.rgTipoCliente);
        orbClienteNuevo=(RadioButton)findViewById(R.id.rbClienteNuevo);
        orbClienteExistente=(RadioButton)findViewById(R.id.rbClienteExistente);
        ospDescuento=(Spinner)findViewById(R.id.spDescuento);
        oetBuscarCliente=(EditText)findViewById(R.id.etBuscarCliente);
        oetNombreNuevo=(EditText)findViewById(R.id.etNombreClienteNuevo);
        oetCelularNuevo=(EditText)findViewById(R.id.etCelularClienteNuevo);
        olvClientes=(ListView)findViewById(R.id.lvClientes);
        otvNombre=(TextView)findViewById(R.id.tvNombreClienteExistente);
        otvCelular=(TextView)findViewById(R.id.tvCelClienteExistente);
        otvDeudaAnterior=(TextView)findViewById(R.id.tvDeudaAnterior);
        otvEstaCuenta_1=(TextView)findViewById(R.id.tvEstaCuenta_1);
        otvEstaCuenta_2=(TextView)findViewById(R.id.tvEstaCuenta_2);
        otvDeudaNueva=(TextView)findViewById(R.id.tvDeudaNueva);
        otvDescuento=(TextView)findViewById(R.id.tvDescuento);

        //##################################################################################         Acciones de inicio de pagina
        //Bloqueo de layout dependiendo del tipo de cliente
        orgTipoCliente.check(orbClienteExistente.getId());
        fVisibilidadClienteExistente(0);
        fVisibilidadNuevoCliente(4);
        //Traer la base de datos de clientes
        fBD_Clientes();

        //Ver el valor de la venta actual
        DecimalFormat decimalFormat=new DecimalFormat("#,##0");
        String imPrecio= decimalFormat.format(InEstaVenta);
        otvEstaCuenta_1.setText("Esta venta: $ " + imPrecio);
        otvEstaCuenta_2.setText("Esta venta: $ " + imPrecio);


        //##################################################################################         Acciones de botones
        obCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        obGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orbClienteNuevo.isChecked()){
                    if (oetNombreNuevo.getText().length()==0){
                        Toast.makeText(pagGuardar.this,"Ingrese un nombre",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        fVerificarNombreNuevo();
                    }
                }
            }
        });

        orgTipoCliente.setOnCheckedChangeListener(new  RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (orbClienteExistente.isChecked()){
                    fVisibilidadNuevoCliente(4);
                    fVisibilidadClienteExistente(0);
                }
                if (orbClienteNuevo.isChecked()){
                    fVisibilidadNuevoCliente(0);
                    fVisibilidadClienteExistente(4);
                }
            }
        });

        ospDescuento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fBajarVenta();
                DecimalFormat decimalFormat=new DecimalFormat("#,##0");
                String imPrecio= decimalFormat.format(InVentaFinal);
                otvEstaCuenta_2.setText("Esta venta: $ " + imPrecio);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //###################################################################################       Filtros de los ListView
        oetBuscarCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    pagGuardar.this.myAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //##################################################################################         Acciones de las List view



    }
    //#########################################################################################################################################
    //#########################################################################################
    //#########################################################################################################################################

    void fVisibilidadNuevoCliente(int Accion){
        oetNombreNuevo.setVisibility(Accion);
        oetCelularNuevo.setVisibility(Accion);
        otvEstaCuenta_2.setVisibility(Accion);
        ospDescuento.setVisibility(Accion);
        otvDescuento.setVisibility(Accion);
    }
    void fVisibilidadClienteExistente(int Accion){
        oetBuscarCliente.setVisibility(Accion);
        olvClientes.setVisibility(Accion);
        otvNombre.setVisibility(Accion);
        otvCelular.setVisibility(Accion);
        otvDeudaAnterior.setVisibility(Accion);
        otvEstaCuenta_1.setVisibility(Accion);
        otvDeudaNueva.setVisibility(Accion);
    }

    void fVerificarNombreNuevo(){

        final String sNuevoNombre=oetNombreNuevo.getText().toString();

        String sPath = "Usuarios/"+gsUsuario+"/Tiendas/"+gsTienda+"/Clientes";
        final CollectionReference bd_Datos= FirebaseFirestore.getInstance().collection(sPath);


        bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Collection collection = document.getData().values();
                        if (document.getId().toString().equals(sNuevoNombre)) {
                            Toast.makeText(pagGuardar.this, "Este Cliente ya existe", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    fCrearCliente(sNuevoNombre);

                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });

    }

    void fCrearCliente(final String sNuevoNombre){
        final long llTelefono;
        InPorcentaje=Integer.parseInt(ospDescuento.getSelectedItem().toString());
        if (oetCelularNuevo.getText().length()==0){
            llTelefono=0;
        }
        else{
            llTelefono= Long.parseLong(oetCelularNuevo.getText().toString());
        }

        DocumentReference bd_NuevoCliente = FirebaseFirestore.getInstance()
                .document("Usuarios/"+gsUsuario+"/Tiendas/"+gsTienda+"/Clientes/"+sNuevoNombre);

        Map<String,Object> bd_GuardarCliente=new HashMap<String, Object>();
        bd_GuardarCliente.put("Nombre",sNuevoNombre);
        bd_GuardarCliente.put("Telefono",llTelefono);
        bd_GuardarCliente.put("Deuda",InVentaFinal);
        bd_GuardarCliente.put("Descuento",InPorcentaje);

        bd_NuevoCliente.set(bd_GuardarCliente).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fCrearTicket(sNuevoNombre,llTelefono);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }

    void fCrearTicket(String sCliente,long lTelefono){

        //Calendario
        ArrayList alDatosCalendario=fCalendario();
        //Listado de productos
        ArrayList<String> alNombreArt=new ArrayList();
        ArrayList<Integer> alPrecioUndArt=new ArrayList();
        ArrayList<Integer> alPrecioTotalArt=new ArrayList();

        for(int i=0;i<alListadoArt.size();i++){
            alNombreArt.add( alListadoArt.get(i).getsNombreArt());
            alPrecioUndArt.add( alListadoArt.get(i).getiPrecioArt());
            alPrecioTotalArt.add( alListadoArt.get(i).getiPrecioArt()*alCantidades.get(i));
        }


        String sNumTicket= alDatosCalendario.get(7).toString();

        //Base de datos
        DocumentReference bd_NuevoTicket = FirebaseFirestore.getInstance()
                .document("Usuarios/"+gsUsuario+"/Tiendas/"+gsTienda+"/Tickets/"+sNumTicket);

        Map<String,Object> bd_GuardarTicket=new HashMap<String, Object>();
        bd_GuardarTicket.put("NumTicket",sNumTicket);
        bd_GuardarTicket.put("Año",alDatosCalendario.get(0).toString());
        bd_GuardarTicket.put("Mes",alDatosCalendario.get(1).toString());
        bd_GuardarTicket.put("Día",alDatosCalendario.get(2).toString());
        bd_GuardarTicket.put("Hora",alDatosCalendario.get(5).toString());
        bd_GuardarTicket.put("Estado","Debe");
        bd_GuardarTicket.put("Cliente",sCliente);
        bd_GuardarTicket.put("SubTotal",InEstaVenta);
        bd_GuardarTicket.put("Descuento",InDescuento);
        bd_GuardarTicket.put("Porcentaje",InPorcentaje);
        bd_GuardarTicket.put("Total",InVentaFinal);
        bd_GuardarTicket.put("Productos",alNombreArt);
        bd_GuardarTicket.put("Precio Und",alPrecioUndArt);
        bd_GuardarTicket.put("Precio Total x Art",alPrecioTotalArt);

        bd_GuardarTicket.put("Cantidades",alCantidades);


        final ProgressDialog progressDialog=new ProgressDialog(pagGuardar.this);
        progressDialog.setMessage("Creando Cliente y guardando ticket");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        bd_NuevoTicket.set(bd_GuardarTicket).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(pagGuardar.this,"Cliente y Ticket Guardados",Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    ArrayList fCalendario(){

        ArrayList alDatosCalendario=new ArrayList();

        Calendar Date=Calendar.getInstance();
        int iAño= Date.get(Calendar.YEAR);
        int iMes=Date.get(Calendar.MONTH)+1;
        int iFecha=Date.get(Calendar.DAY_OF_MONTH);
        int iDia=Date.get(Calendar.DAY_OF_WEEK);
        String sDia="",sMes="";
        switch (iDia){
            case 1:sDia="Dom";break;
            case 2:sDia="Lun";break;
            case 3:sDia="Mar";break;
            case 4:sDia="Mie";break;
            case 5:sDia="Jue";break;
            case 6:sDia="Vie";break;
            case 7:sDia="Sab";break;
        }
        switch (iMes){
            case 1:sMes="Ene";break;
            case 2:sMes="Feb";break;
            case 3:sMes="Mar";break;
            case 4:sMes="Abr";break;
            case 5:sMes="May";break;
            case 6:sMes="Jun";break;
            case 7:sMes="Jul";break;
            case 8:sMes="Ago";break;
            case 9:sMes="Sep";break;
            case 10:sMes="Oct";break;
            case 11:sMes="Nov";break;
            case 12:sMes="Dic";break;
        }
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf2=new SimpleDateFormat("HHmmddssMMyy");
        String sHora=sdf.format(Date.getTime());
        String sTodo=sdf2.format(Date.getTime());
        String sFecha=sDia +" de "+ sMes+ " de " + iAño;

        alDatosCalendario.add(iAño);
        alDatosCalendario.add(iMes);
        alDatosCalendario.add(iDia);
        alDatosCalendario.add(sDia);
        alDatosCalendario.add(sMes);
        alDatosCalendario.add(sHora);
        alDatosCalendario.add(sFecha);
        alDatosCalendario.add(sTodo);
        return(alDatosCalendario);
    }

    //#########################################################################################     CONSTRUCCIÓN DE LIST VIEW DE CLIENTES

    void fBD_Clientes(){
        String sPath = "Usuarios/"+gsUsuario+"/Tiendas/"+gsTienda+"/Clientes";
        CollectionReference bd_Datos= FirebaseFirestore.getInstance().collection(sPath);

        bd_Datos.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Clientes_Entidad listItems;
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Collection collection = document.getData().values();
                        List listPrevia = new ArrayList<>(collection);
                        int iPrimero = 2, iSegundo = 0, iTercero = 3,iCuarto=1;
                        listItems = new Clientes_Entidad(listPrevia.get(iPrimero).toString(),
                                Long.parseLong(listPrevia.get(iSegundo).toString()),
                                Integer.parseInt(listPrevia.get(iTercero).toString()),
                                Integer.parseInt(listPrevia.get(iCuarto).toString()));
                        alClientes.add(listItems);
                        Log.d("Campos: ", listItems.toString());
                    }
                    myAdapter = new Clientes_Adaptador(pagGuardar.this, alClientes);
                    olvClientes.setAdapter(myAdapter);

                } else {
                    Log.d("Actividad", "Error adquiriendo documentos: ", task.getException());
                }
            }
        });

    }
    //#########################################################################################     BAJAR PRECIO DE VENTA POR DESCUENTO
    void fBajarVenta(){
        InPorcentaje= Integer.parseInt(ospDescuento.getSelectedItem().toString());

        InDescuento = Math.round((InEstaVenta * InPorcentaje / 100) / 100) * 100;
        InVentaFinal=InEstaVenta-InDescuento;
    }



}
