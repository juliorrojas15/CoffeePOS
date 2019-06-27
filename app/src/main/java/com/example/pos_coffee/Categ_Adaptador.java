package com.example.pos_coffee;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Categ_Adaptador extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<Categ_Entidad> originalArray,tempArray;
    CustomFilter cs;

    public Categ_Adaptador(Context context, ArrayList<Categ_Entidad> originalArray) {
        //super(context,0, object);
        this.context = context;
        this.originalArray = originalArray;
        this.tempArray = originalArray;
    }

    @Override
    public Object getItem(int position) {
        return originalArray.get(position);
    }
    @Override
    public int getCount() {
        return originalArray.size();
    }

    TextView oNomCategoria,oCantArticulos,oColorCategoria;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Item=inflater.inflate(R.layout.categorias_lv,null);

        oNomCategoria=(TextView)Item.findViewById(R.id.tvNomCategoria_LV);
        oCantArticulos=(TextView)Item.findViewById(R.id.tvCantArticulos_LV);
        oColorCategoria=(TextView)Item.findViewById(R.id.tvColorCategoria_LV);

        oNomCategoria.setText(originalArray.get(position).getsNomCategoria());
        oCantArticulos.setText(originalArray.get(position).getsCantArticulos());
        oColorCategoria.setBackgroundColor(Color.parseColor(originalArray.get(position).getsColorCategoria()));


        return Item;
    }
    @Override
    public long getItemId(int position) {
        //return originalArray.indexOf(getItemId(position));
        return position;
    }
    @Override
    public Filter getFilter(){
        if(cs==null){
            cs=new CustomFilter();
        }
        return cs;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<Categ_Entidad> filters = new ArrayList<>();
                for (int i = 0; i < tempArray.size(); i++) {
                    if (tempArray.get(i).getsNomCategoria().toUpperCase().contains(constraint)) {
                        Categ_Entidad categ_entidad = new Categ_Entidad(tempArray.get(i).getsNomCategoria(),
                                tempArray.get(i).getsCantArticulos(), tempArray.get(i).getsColorCategoria());
                        filters.add(categ_entidad);
                    }
                }
                results.count=filters.size();
                results.values=filters;
            }
            else{
                results.count=tempArray.size();
                results.values=tempArray;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            originalArray=(ArrayList<Categ_Entidad>)results.values;
            notifyDataSetChanged();

        }
    }
}
