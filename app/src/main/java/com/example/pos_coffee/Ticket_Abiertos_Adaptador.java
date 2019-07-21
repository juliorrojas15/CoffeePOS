package com.example.pos_coffee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.graphics.Color.GREEN;

public class Ticket_Abiertos_Adaptador extends BaseAdapter {
    Context context;
    ArrayList<Ticket_Abiertos_Entidad> originalArray;
    ArrayList<Integer> alValores;
    int iTicketSeleccionado;

    public Ticket_Abiertos_Adaptador(Context context, ArrayList<Ticket_Abiertos_Entidad> originalArray,ArrayList alValores, int iTicketSeleccionado) {
        this.context = context;
        this.originalArray = originalArray;
        this.alValores=alValores;
        this.iTicketSeleccionado=iTicketSeleccionado;
    }


    public void remove(int position){
        originalArray.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return originalArray.get(position);
    }
    @Override
    public int getCount() {
        return originalArray.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //ViewHolder holder;
        //holder =new ViewHolder();
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Item=inflater.inflate(R.layout.tickets_abiertos_lv,null);

        TextView otvTicketN_LV=(TextView)Item.findViewById(R.id.tvTicketN_LV);
        TextView otvTicketValor_LV=(TextView)Item.findViewById(R.id.tvTicketValor_LV);

        DecimalFormat decimalFormat=new DecimalFormat("#,##0");

        Integer iValor=alValores.get(position);
        String imValor= decimalFormat.format(iValor);

        otvTicketN_LV.setText(originalArray.get(position).getsTicketNumero());
        otvTicketValor_LV.setText("$ "+imValor);

        if (position==iTicketSeleccionado){
            otvTicketN_LV.setBackgroundColor(GREEN);
            otvTicketValor_LV.setBackgroundColor(GREEN);
        }

        return Item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
