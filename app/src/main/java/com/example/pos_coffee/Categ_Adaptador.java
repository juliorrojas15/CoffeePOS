package com.example.pos_coffee;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Categ_Adaptador extends ArrayAdapter<Categ_Entidad> {
    private Context context;

    public Categ_Adaptador(Context context, List<Categ_Entidad> object){
        super(context,0, object);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView =  ((Activity)getContext()).getLayoutInflater().inflate(R.layout.categorias_lv,parent,false);
        }
        Categ_Entidad Item=(Categ_Entidad) getItem(position);

        TextView oNomCategoria=convertView.findViewById(R.id.tvNomCategoria);
        TextView oCantArticulos=convertView.findViewById(R.id.tvCantArticulos);
        TextView oColorCategoria=convertView.findViewById(R.id.tvColorCategoria);

        oNomCategoria.setText(Item.getsNomCategoria());
        oCantArticulos.setText(Item.getsCantArticulos());
        oColorCategoria.setBackgroundColor(Color.parseColor(Item.getsColorCategoria()));


        return convertView;
    }
}
