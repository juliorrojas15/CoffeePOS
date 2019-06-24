package com.example.pos_coffee;

import android.widget.ImageView;

public class Categ_Entidad {
    private String sNomCategoria;
    private String sCantArticulos;
    private String sColorCategoria;

    public Categ_Entidad(String sNomCategoria, String sCantArticulos, String sColorCategoria) {
        this.sNomCategoria = sNomCategoria;
        this.sCantArticulos = sCantArticulos;
        this.sColorCategoria = sColorCategoria;
    }

    public String getsNomCategoria() {
        return sNomCategoria;
    }

    public String getsCantArticulos() {
        return sCantArticulos;
    }

    public String getsColorCategoria() {
        return sColorCategoria;
    }
}
