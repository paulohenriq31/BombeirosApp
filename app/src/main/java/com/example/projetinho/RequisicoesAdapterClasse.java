package com.example.projetinho;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class RequisicoesAdapterClasse extends RecyclerView.Adapter<RequisicoesAdapterClasse.MyViewHolder>{

    private List<LocalizacaoUsuarioClasse> requisicoes;
    private Context context;
    private LocalizacaoUsuarioClasse usuario;

    public RequisicoesAdapterClasse(List<LocalizacaoUsuarioClasse> requisicoes, Context context, LocalizacaoUsuarioClasse usuario)
    {
        this.requisicoes = requisicoes;
        this.context = context;
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adaptar_requisicoes, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        LocalizacaoUsuarioClasse localizacaoDoUsuario = requisicoes.get(position);

        try
        {
            Double latitude, longitude;
            latitude = Double.parseDouble(localizacaoDoUsuario.getLatitude());
            longitude = Double.parseDouble(localizacaoDoUsuario.getLongitude());

            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = addresses.get(0);

            holder.pkUsuario.setText("Identificação: " + localizacaoDoUsuario.getUsuario());
            holder.nome.setText("Usuário: " + localizacaoDoUsuario.getUsuario());
            holder.endereco.setText("Endereço: " + address.getAddressLine(0).toString());
            holder.status.setText("Status do chamado: " + localizacaoDoUsuario.getStatus());

        }
        catch (Exception ex)
        {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount()
    {
        return requisicoes.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nome, status, latitude, longitude, pkUsuario, endereco;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            pkUsuario = itemView.findViewById(R.id.textViewRequisicoesPkUsuario);
            nome = itemView.findViewById(R.id.textViewRequisicoesUsuario);
            endereco = itemView.findViewById(R.id.textViewRequisicoesEndereco);
            status = itemView.findViewById(R.id.textViewRequisicoesStatusAdap);
        }
    }
}
