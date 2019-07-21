package com.example.pos_coffee;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Art_Venta_Adaptador extends BaseAdapter{
    Context context;
    ArrayList<Articulos_Entidad> originalArray,tempArray;
    ArrayList<Integer> alCantidades;

    public Art_Venta_Adaptador(Context context, ArrayList<Articulos_Entidad> originalArray,ArrayList alCantidades) {
        this.context = context;
        this.originalArray = originalArray;
        this.alCantidades=alCantidades;
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
        View Item=inflater.inflate(R.layout.art_venta_lv,null);

        TextView otvNomVentaArt_LV=(TextView)Item.findViewById(R.id.tvNomVentaArticulo_LV);
        TextView otvPrecioUndVentaArt_LV=(TextView)Item.findViewById(R.id.tvPrecioUndVentaArticulo_LV);
        TextView otvCantVentaArticulo_LV=(TextView)Item.findViewById(R.id.tvCantVentaArticulo_LV);
        TextView otvSubTotalVentaArticulo_LV=(TextView)Item.findViewById(R.id.tvSubTotalVentaArticulo_LV);

        DecimalFormat decimalFormat=new DecimalFormat("#,##0");

        Integer iPrecio=originalArray.get(position).getiPrecioArt();
        String imPrecio= decimalFormat.format(iPrecio);

        String sTotal=decimalFormat.format(iPrecio*alCantidades.get(position));
        otvCantVentaArticulo_LV.setText(String.valueOf(alCantidades.get(position)));
        otvSubTotalVentaArticulo_LV.setText("$ "+sTotal);
        otvNomVentaArt_LV.setText(originalArray.get(position).getsNombreArt());
        otvPrecioUndVentaArt_LV.setText("$ "+ imPrecio);

        return Item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}

