package com.example.pos_coffee;

public class Clientes_Entidad {
    String sNombre;
    long iTelefono;
    int iDeuda;
    int iDescuento;

    public Clientes_Entidad(String sNombre, long iTelefono, int iDeuda,int iDescuento) {
        this.sNombre = sNombre;
        this.iTelefono = iTelefono;
        this.iDeuda = iDeuda;
        this.iDescuento=iDescuento;
    }

    public String getsNombre() {
        return sNombre;
    }

    public void setsNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    public long getiTelefono() {
        return iTelefono;
    }

    public void setiTelefono(int iTelefono) {
        this.iTelefono = iTelefono;
    }

    public int getiDeuda() {
        return iDeuda;
    }

    public void setiDeuda(int iDeuda) {
        this.iDeuda = iDeuda;
    }

    public int getiDescuento() {
        return iDescuento;
    }

    public void setiDescuento(int iDescuento) {
        this.iDescuento = iDescuento;
    }
}
