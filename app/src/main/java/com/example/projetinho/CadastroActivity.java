package com.example.projetinho;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class CadastroActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference reference;

    EditText editTextNome, editTextEmail, editTextTelefone, editTextSenha;
    Button buttonCadastrar;
    Switch switchTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextEmail = findViewById(R.id.editTextEmailC);
        editTextNome = findViewById(R.id.editTextNomeC);
        editTextTelefone = findViewById(R.id.editTextTelefoneC);
        editTextSenha = findViewById(R.id.editTextSenhaC);
        switchTipo = findViewById(R.id.switchTipo);

    }

    public String tipoUsuario(){
        return switchTipo.isChecked() ? "Usuario" : "Socorrista";
    }

    public boolean ValidarCampoC() {
        if (editTextNome.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha o campo nome", Toast.LENGTH_SHORT).show();
            editTextNome.requestFocus();
            return false;

        } else if (editTextTelefone.getText().toString().isEmpty() || editTextTelefone.getText().toString().length() < 8) {
            Toast.makeText(this, "Preencha o campo telefone corretamente", Toast.LENGTH_SHORT).show();
            editTextTelefone.requestFocus();
            return false;

        } else if (editTextEmail.getText().toString().isEmpty() || editTextEmail.getText().toString().equals("@")) {
            Toast.makeText(this, "Preencha o campo e-mail corretamente", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return false;

        }else if(editTextSenha.getText().toString().isEmpty() || editTextSenha.getText().toString().length()<6){
            Toast.makeText(this, "Preencha o campo senha corretamente", Toast.LENGTH_SHORT).show();
            editTextSenha.requestFocus();
            return false;

        }else{
            return true;

        }

    }

}
