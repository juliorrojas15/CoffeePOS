package com.example.pos_coffee;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Config_Adaptador extends ArrayAdapter<Config_Entidad> {
    private Context context;
    //private ArrayList<Config_Entidad> list;

    public Config_Adaptador(Context context, List<Config_Entidad> object){
        super(context,0, object);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView =  ((Activity)getContext()).getLayoutInflater().inflate(R.layout.config_lv_tiendas,parent,false);
        }
        Config_Entidad Item=(Config_Entidad) getItem(position);

        TextView oNombre=convertView.findViewById(R.id.ConfigNombre);
        TextView oDireccion=convertView.findViewById(R.id.ConfigDireccion);
        TextView oTelefono=convertView.findViewById(R.id.ConfigTelefono);

        oNombre.setText(Item.getNombre());
        oDireccion.setText(Item.getDireccion());
        oTelefono.setText(Item.getTelefono());

        return convertView;
    }
}
