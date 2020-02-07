package com.example.projetinho;

import com.google.firebase.auth.FirebaseAuth;

public class AutenticacaoComFirebase {
    //variavel estatica permanece na memoria
    private static FirebaseAuth auth;

    //cria método estático para verificar a autenticacao
    public static FirebaseAuth check(){

        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}



