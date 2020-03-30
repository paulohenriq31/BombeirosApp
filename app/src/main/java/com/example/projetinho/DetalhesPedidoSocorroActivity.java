package com.example.projetinho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetalhesPedidoSocorroActivity extends AppCompatActivity {

    TextView textViewUsuario, textViewEndereco, textViewStatus, textViewId;
    Button buttonAceitarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pedido_socorro);

        final String []dadosDoPedido = new String[4];

        Bundle extras = getIntent().getExtras();

        dadosDoPedido[0] = extras.getString("usuario");
        dadosDoPedido[1] = extras.getString("endereco");
        dadosDoPedido[2] = extras.getString("status");
        dadosDoPedido[3] = extras.getString("id");

        textViewId = findViewById(R.id.textViewID);
        textViewUsuario = findViewById(R.id.textViewUsuario);
        textViewEndereco = findViewById(R.id.textViewEndereco);
        textViewStatus = findViewById(R.id.textViewStatus);
        buttonAceitarPedido = findViewById(R.id.buttonAceitarPedido);

        textViewUsuario.setText("Usuário: " + dadosDoPedido[0]);
        textViewEndereco.setText("Endereço: " + dadosDoPedido[1]);
        textViewStatus.setText("Status do pedido: " + dadosDoPedido[2]);
        textViewId.setText("ID do usuário: " + dadosDoPedido[3]);

        buttonAceitarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("localizacao");
                Query query = reference.child(dadosDoPedido[3]);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try{
                            LocalizacaoUsuarioClasse p = dataSnapshot.getValue(LocalizacaoUsuarioClasse.class);
                            p.alteraStatus(p, "Andamento", dadosDoPedido[3]);
                            textViewStatus.setText("ID do usuário: " + p.getStatus());
                            buttonAceitarPedido.setEnabled(false);
                        }catch (Exception ex){
                            Toast.makeText(DetalhesPedidoSocorroActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
