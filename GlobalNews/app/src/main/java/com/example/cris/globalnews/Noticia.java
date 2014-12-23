package com.example.cris.globalnews;

public class Noticia {

    private String titulo;
    private String usuario;
    private String fecha;
    public double latitud;
    public double longitud;
    private int id;
    private String url;

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String headline) {
        this.titulo = headline;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String reporterName) {
        this.usuario = reporterName;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String date) {
        this.fecha = date;
    }

    @Override
    public String toString() {
        return "[ Tittulo=" + titulo + ", Usuario=" + usuario + " , Fecha=" + fecha + "]";
    }

}