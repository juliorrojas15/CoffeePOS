package com.example.pos_coffee;

import java.lang.ref.SoftReference;

public class Config_Entidad {
    private String sText_1;
    private String sText_2;
    private String sNum_1;

    public Config_Entidad(String sText_1, String sText_2, String sNum_1) {
        this.sText_1 = sText_1;
        this.sText_2 = sText_2;
        this.sNum_1 = sNum_1;
    }

    public String getText_1() {
        return sText_1;
    }

    public String getText_2() {
        return sText_2;
    }

    public String getNum_1() {
        return sNum_1;
    }
}
