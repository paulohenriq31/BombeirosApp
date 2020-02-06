package com.example.projetinho;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ConexaoRealtimeDatabase {

    private static DatabaseReference reference;

    public static DatabaseReference check(){
        if (reference == null){
            reference = FirebaseDatabase.getInstance().getReference();
        }
        return reference;
    }


}
