package com.example.projetinho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

        auth = AutenticacaoComFirebase.check();

        editTextEmail = findViewById(R.id.editTextEmailC);
        editTextNome = findViewById(R.id.editTextNomeC);
        editTextTelefone = findViewById(R.id.editTextTelefoneC);
        editTextSenha = findViewById(R.id.editTextSenhaC);
        switchTipo = findViewById(R.id.switchTipo);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidarCampoC()){
                    cadastrarUsuario();
                }
            }
        });

    }

    public String tipoUsuario(){
        return switchTipo.isChecked() ? "Socorrista" : "Usuario";
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

        } else if (editTextEmail.getText().toString().isEmpty() || !editTextEmail.getText().toString().contains("@")) {
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

    public void cadastrarUsuario(){
        //primeiro vamos salvar os dados de login
        auth.createUserWithEmailAndPassword(editTextEmail.getText().toString(),editTextSenha.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //instancia a classe usuarioa
                            UsuarioClasse usuario = new UsuarioClasse();
                            //pega o id que foi salvo no processo de craição do login e senha
                            usuario.setId(task.getResult().getUser().getUid());
                            usuario.setEmail(editTextEmail.getText().toString());
                            usuario.setSenha(editTextSenha.getText().toString());
                            usuario.setNome(editTextNome.getText().toString());
                            usuario.setTelefone(editTextTelefone.getText().toString());
                            usuario.setTipo(tipoUsuario());

                            if(tipoUsuario() == "Usuario"){
                                startActivity(new Intent(CadastroActivity.this, UsuarioActivity.class));
                            }else{
                                startActivity(new Intent(CadastroActivity.this, SocorristaActivity.class));
                            }

                            usuario.cadastrarUsuario(task.getResult().getUser().getUid());

                        }else{
                            try{
                                throw task.getException();
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                });

    }
}
