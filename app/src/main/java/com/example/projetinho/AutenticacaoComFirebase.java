package com.example.projetinho;

import com.google.firebase.auth.FirebaseAuth;

public class AutenticacaoComFirebase {

    private static FirebaseAuth auth;

    public static FirebaseAuth check(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
