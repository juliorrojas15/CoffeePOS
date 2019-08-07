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

public class Clientes_Adaptador extends BaseAdapter implements Filterable {
    Context context;
    ArrayList<Clientes_Entidad> originalArray,tempArray;
    Clientes_Adaptador.CustomFilter cs;

    public Clientes_Adaptador(Context context, ArrayList<Clientes_Entidad> originalArray) {
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



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Item=inflater.inflate(R.layout.clientes_lv,null);

        TextView oNombre=(TextView)Item.findViewById(R.id.tvNombreCliente_LV);
        TextView oTelefono=(TextView)Item.findViewById(R.id.tvTelefonoCliente_LV);
        TextView oDeuda=(TextView)Item.findViewById(R.id.tvDeudaCliente_LV);
        TextView oDescuento=(TextView)Item.findViewById(R.id.tvDescuento_LV);

        DecimalFormat decimalFormat=new DecimalFormat("#,##0");
        String imPrecio= decimalFormat.format(originalArray.get(position).getiDeuda());

        oNombre.setText(originalArray.get(position).getsNombre());
        oTelefono.setText(String.valueOf(originalArray.get(position).getiTelefono()));
        oDeuda.setText("$ " +imPrecio);
        oDescuento.setText(originalArray.get(position).getiDescuento()+"%");
        return Item;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Filter getFilter(){
        if(cs==null){
            cs=new Clientes_Adaptador.CustomFilter();
        }
        return cs;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<Clientes_Entidad> filters = new ArrayList<>();
                for (int i = 0; i < tempArray.size(); i++) {
                    if (tempArray.get(i).getsNombre().toUpperCase().contains(constraint)) {
                        Clientes_Entidad clientes_entidad = new Clientes_Entidad(tempArray.get(i).getsNombre(),
                                tempArray.get(i).getiTelefono(), tempArray.get(i).getiDeuda(),tempArray.get(i).getiDescuento());
                        filters.add(clientes_entidad);
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
            originalArray=(ArrayList<Clientes_Entidad>)results.values;
            notifyDataSetChanged();

        }
    }
}
