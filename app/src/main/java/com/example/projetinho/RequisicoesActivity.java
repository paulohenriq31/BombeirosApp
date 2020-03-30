package com.example.projetinho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RequisicoesActivity extends AppCompatActivity {

    DatabaseReference reference;
    TextView textViewStatus;
    RecyclerView recyclerView;
    List<LocalizacaoUsuarioClasse> listaRequisicoes = new ArrayList<>();
    RequisicoesAdapterClasse adapter;
    LocalizacaoUsuarioClasse usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisicoes);

        textViewStatus = findViewById(R.id.textViewSemRequisicoes);
        recyclerView = findViewById(R.id.recyclerViewRequisicoes);

        usuario = new LocalizacaoUsuarioClasse();

        adapter = new RequisicoesAdapterClasse(listaRequisicoes, getApplicationContext(), usuario);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        //adiciona evento de clique no recycler
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListenerClasse(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListenerClasse.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                LocalizacaoUsuarioClasse localizacaoDoUsuario = listaRequisicoes.get(position);

                                Intent i = new Intent(RequisicoesActivity.this, DetalhesPedidoSocorroActivity.class);

                                Geocoder geocoder = new Geocoder(RequisicoesActivity.this, Locale.getDefault());
                                try{

                                    List<android.location.Address> addresses = geocoder.getFromLocation(Double.parseDouble(localizacaoDoUsuario.getLatitude()), Double.parseDouble(localizacaoDoUsuario.getLongitude()), 1);
                                    if (addresses.size() > 0){
                                        Address address = addresses.get(0);
                                        i.putExtra("id", localizacaoDoUsuario.getId());
                                        i.putExtra("usuario", localizacaoDoUsuario.getUsuario());
                                        i.putExtra("endereco", address.getAddressLine(0).toString());
                                        i.putExtra("status", localizacaoDoUsuario.getStatus());
                                        startActivity(i);
                                    }

                                }catch (Exception ex){
                                    Toast.makeText(RequisicoesActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        recuperaRequisicoes();

    }
    public void recuperaRequisicoes(){

        reference = ConexaoRealtimeDatabase.check();
        DatabaseReference dados = reference.getDatabase().getReference("localizacao");

        Query query =  dados.orderByChild("status").equalTo("Aguardando");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0){
                    textViewStatus.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    textViewStatus.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    LocalizacaoUsuarioClasse localizacaoDoUsuario = ds.getValue(LocalizacaoUsuarioClasse.class);
                    listaRequisicoes.add(localizacaoDoUsuario);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
