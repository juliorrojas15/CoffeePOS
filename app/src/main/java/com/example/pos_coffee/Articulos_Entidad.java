package com.example.pos_coffee;

public class Articulos_Entidad {
    private String sNombreArt,sCategoriaArt,sRefArt;
    private int iCostoArt,iPrecioArt,iStockArt,iDescuentoArt,iCodigoBarrasArt,iIndexColor;
    private String sTipoVisualizacion,sColor, sUriImagen;

    public Articulos_Entidad(String sNombreArt, String sCategoriaArt, String sRefArt, int iCostoArt,
                             int iPrecioArt, int iStockArt, int iDescuentoArt, int iCodigoBarrasArt,
                             String sTipoVisualizacion, String sColor,int iIndexColor, String sUriImagen) {
        this.sNombreArt = sNombreArt;
        this.sCategoriaArt = sCategoriaArt;
        this.sRefArt = sRefArt;
        this.iCostoArt = iCostoArt;
        this.iPrecioArt = iPrecioArt;
        this.iStockArt = iStockArt;
        this.iDescuentoArt = iDescuentoArt;
        this.iCodigoBarrasArt = iCodigoBarrasArt;
        this.sTipoVisualizacion=sTipoVisualizacion;
        this.sColor=sColor;
        this.iIndexColor=iIndexColor;
        this.sUriImagen=sUriImagen;
    }

    public int getiIndexColor() {
        return iIndexColor;
    }

    public void setiIndexColor(int iIndexColor) {
        this.iIndexColor = iIndexColor;
    }

    public String getsTipoVisualizacion() {
        return sTipoVisualizacion;
    }

    public void setsTipoVisualizacion(String sTipoVisualizacion) {
        this.sTipoVisualizacion = sTipoVisualizacion;
    }

    public String getsColor() {
        return sColor;
    }

    public void setsColor(String sColor) {
        this.sColor = sColor;
    }

    public String getsUriImagen() {
        return sUriImagen;
    }

    public void setsUriImagen(String sUriImagen) {
        this.sUriImagen = sUriImagen;
    }

    public String getsNombreArt() {
        return sNombreArt;
    }

    public void setsNombreArt(String sNombreArt) {
        this.sNombreArt = sNombreArt;
    }

    public String getsCategoriaArt() {
        return sCategoriaArt;
    }

    public void setsCategoriaArt(String sCategoriaArt) {
        this.sCategoriaArt = sCategoriaArt;
    }

    public String getsRefArt() {
        return sRefArt;
    }

    public void setsRefArt(String sRefArt) {
        this.sRefArt = sRefArt;
    }

    public int getiCostoArt() {
        return iCostoArt;
    }

    public void setiCostoArt(int iCostoArt) {
        this.iCostoArt = iCostoArt;
    }

    public int getiPrecioArt() {
        return iPrecioArt;
    }

    public void setiPrecioArt(int iPrecioArt) {
        this.iPrecioArt = iPrecioArt;
    }

    public int getiStockArt() {
        return iStockArt;
    }

    public void setiStockArt(int iStockArt) {
        this.iStockArt = iStockArt;
    }

    public int getiDescuentoArt() {
        return iDescuentoArt;
    }

    public void setiDescuentoArt(int iDescuentoArt) {
        this.iDescuentoArt = iDescuentoArt;
    }

    public int getiCodigoBarrasArt() {
        return iCodigoBarrasArt;
    }

    public void setiCodigoBarrasArt(int iCodigoBarrasArt) {
        this.iCodigoBarrasArt = iCodigoBarrasArt;
    }


}
