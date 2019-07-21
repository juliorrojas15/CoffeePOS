package com.example.pos_coffee;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Articulos_Adaptador  extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<Articulos_Entidad> originalArray,tempArray;
    CustomFilter cs;

    public Articulos_Adaptador(Context context, ArrayList<Articulos_Entidad> originalArray) {
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
        View Item=inflater.inflate(R.layout.articulos_lv,null);

        TextView otvNomArt_LV=(TextView)Item.findViewById(R.id.tvNomArt_LV);
        TextView otvStockArt_LV=(TextView)Item.findViewById(R.id.tvStockArt_LV);
        TextView otvCostoArt_LV=(TextView)Item.findViewById(R.id.tvCostoArt_LV);
        TextView otvPrecioArt_LV=(TextView)Item.findViewById(R.id.tvPrecioArt_LV);

        DecimalFormat decimalFormat=new DecimalFormat("#,##0");

        Integer iStock=originalArray.get(position).getiStockArt();
        Integer iCosto=originalArray.get(position).getiCostoArt();
        Integer iPrecio=originalArray.get(position).getiPrecioArt();

        String imCosto= decimalFormat.format(iCosto);
        String imPrecio= decimalFormat.format(iPrecio);

        otvNomArt_LV.setText(originalArray.get(position).getsNombreArt());
        otvStockArt_LV.setText(iStock.toString());
        otvCostoArt_LV.setText(imCosto);
        otvPrecioArt_LV.setText(imPrecio);

        return Item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Filter getFilter(){
        if(cs==null){
            cs=new CustomFilter();
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
                Articulos_Entidad articulos_entidad;
                for (int i = 0; i < tempArray.size(); i++) {
                    if (tempArray.get(i).getsNombreArt().toUpperCase().contains(constraint)) {
                        articulos_entidad = new Articulos_Entidad(tempArray.get(i).getsNombreArt(),
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
                    else{
                        if(tempArray.get(i).getsCategoriaArt().toUpperCase().contains(constraint) && constraint!="Todas"){
                            articulos_entidad = new Articulos_Entidad(tempArray.get(i).getsNombreArt(),
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
                        else{
                            if(((String) constraint).equalsIgnoreCase("Todas")){
                                articulos_entidad = new Articulos_Entidad(tempArray.get(i).getsNombreArt(),
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
