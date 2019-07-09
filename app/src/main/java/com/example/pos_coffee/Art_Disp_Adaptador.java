package com.example.pos_coffee;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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

public class Art_Disp_Adaptador extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<Articulos_Entidad> originalArray,tempArray;
    Art_Disp_Adaptador.CustomFilter cs;

    public Art_Disp_Adaptador(Context context, ArrayList<Articulos_Entidad> originalArray) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Item=inflater.inflate(R.layout.art_disp_lv,null);

        TextView otvNomArtDisp_LV=(TextView)Item.findViewById(R.id.tvNomArtDisp_LV);
        TextView otvPrecioArtDisp_LV=(TextView)Item.findViewById(R.id.tvPrecioArtDisp_LV);
        ImageView oimImagenArtDisp_LV=(ImageView)Item.findViewById(R.id.imImagenArtDisp_LV);

        DecimalFormat decimalFormat=new DecimalFormat("#,##0");

        Integer iPrecio=originalArray.get(position).getiPrecioArt();
        String imPrecio= decimalFormat.format(iPrecio);


        otvNomArtDisp_LV.setText(originalArray.get(position).getsNombreArt());
        otvPrecioArtDisp_LV.setText("$ "+ imPrecio);

        String sTipoVisual = originalArray.get(position).getsTipoVisualizacion();
        String sColor=originalArray.get(position).getsColor();

        if (sTipoVisual.equals("Color")){
            oimImagenArtDisp_LV.setBackgroundColor(Color.parseColor(sColor));
        }
        else{
            Glide
                    .with(context)
                    .load(originalArray.get(position).getsUriImagen())
                    .into(oimImagenArtDisp_LV);
        }


        return Item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Filter getFilter(){
        if(cs==null){
            cs=new Art_Disp_Adaptador.CustomFilter();
        }
        return cs;
    }

    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<Articulos_Entidad> filters = new ArrayList<>();
                for (int i = 0; i < tempArray.size(); i++) {
                    if (tempArray.get(i).getsNombreArt().toUpperCase().contains(constraint)) {
                        Articulos_Entidad articulos_entidad = new Articulos_Entidad(tempArray.get(i).getsNombreArt(),
                                tempArray.get(i).getsCategoriaArt(),
                                tempArray.get(i).getsRefArt(),
                                tempArray.get(i).getiCostoArt(),
                                tempArray.get(i).getiPrecioArt(),
                                tempArray.get(i).getiStockArt(),
                                tempArray.get(i).getiDescuentoArt(),
                                tempArray.get(i).getiCodigoBarrasArt(),
                                tempArray.get(i).getsTipoVisualizacion(),
                                tempArray.get(i).getsColor(),
                                tempArray.get(i).getiIndexColor(),
                                tempArray.get(i).getsUriImagen());
                        filters.add(articulos_entidad);
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
            originalArray=(ArrayList<Articulos_Entidad>)results.values;
            notifyDataSetChanged();

        }
    }

}
