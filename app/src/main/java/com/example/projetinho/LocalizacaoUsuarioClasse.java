package com.example.projetinho;

import com.google.firebase.database.DatabaseReference;

public class LocalizacaoUsuarioClasse {
    String latitude, longitude, usuario, status;

    public LocalizacaoUsuarioClasse(){
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void salvarLocalizacao(){

        ConexaoRealtimeDatabase reference = new ConexaoRealtimeDatabase();
        DatabaseReference conn = reference.check();

        conn.child("localização").push().setValue(this);
    }
}
