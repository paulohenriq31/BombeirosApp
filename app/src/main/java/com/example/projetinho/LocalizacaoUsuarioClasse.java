package com.example.projetinho;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocalizacaoUsuarioClasse {
    String latitude, longitude, usuario, status, id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

        conn.child("localizacao").push().setValue(this);
    }

    public void alteraStatus(LocalizacaoUsuarioClasse localizacao, String status, String id){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("localizacao");
        this.setStatus(status);
        reference.child(id).setValue(this);

    }
}
