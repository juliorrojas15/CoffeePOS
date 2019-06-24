package com.example.pos_coffee;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
            convertView =  ((Activity)getContext()).getLayoutInflater().inflate(R.layout.config_lv,parent,false);
        }
        Config_Entidad Item=(Config_Entidad) getItem(position);

        TextView oText_1=convertView.findViewById(R.id.ConfigText_1);
        TextView oText_2=convertView.findViewById(R.id.ConfigText_2);
        TextView oNum_1=convertView.findViewById(R.id.ConfigNum_1);

        oText_1.setText(Item.getText_1());
        oText_2.setText(Item.getText_2());
        oNum_1.setText(Item.getNum_1());


        return convertView;
    }
}
