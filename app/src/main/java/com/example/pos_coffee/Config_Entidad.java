package com.example.pos_coffee;

import java.lang.ref.SoftReference;

public class Config_Entidad {
    private String sNombre;
    private String sDireccion;
    private String sTelefono;

    public Config_Entidad(String sNombre, String sDireccion, String sTelefono) {
        this.sNombre = sNombre;
        this.sDireccion = sDireccion;
        this.sTelefono = sTelefono;
    }

    public String getNombre() {
        return sNombre;
    }

    public String getDireccion() {
        return sDireccion;
    }

    public String getTelefono() {
        return sTelefono;
    }
}
