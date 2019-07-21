package com.example.pos_coffee;

public class Ticket_Abiertos_Entidad {
    private String sTicketNumero;
    private int iTicketValor;

    public Ticket_Abiertos_Entidad(String sTicketNumero, int iTicketValor) {
        this.sTicketNumero = sTicketNumero;
        this.iTicketValor = iTicketValor;
    }

    public String getsTicketNumero() {
        return sTicketNumero;
    }

    public void setsTicketNumero(String sTicketNumero) {
        this.sTicketNumero = sTicketNumero;
    }


    public int getiTicketValor() {
        return iTicketValor;
    }

    public void setiTicketValor(int iTicketValor) {
        this.iTicketValor = iTicketValor;
    }
}
