package com.example.projetinho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
    }

    public void goToLogin(View view){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();

        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    String id = task.getResult().getUser().getUid();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("usuario");
                    Query query = reference.child(id);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Usuario usuario = dataSnapshot.getValue(Usuario.class);

                            if(usuario.getTipo().equals("Usuario")){
                                startActivity(new Intent(MainActivity.this, MapsUsuarioActivity.class));
                            }else if(usuario.getTipo().equals("Socorrista")){
                                startActivity(new Intent(MainActivity.this, SocorristaActivity.class));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(MainActivity.this, id, Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this, "Falha no Login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToCadastro (View view){
        Intent cadastro = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(cadastro);
    }
}
