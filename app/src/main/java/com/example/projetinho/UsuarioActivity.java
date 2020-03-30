package com.example.projetinho;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class UsuarioActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationManager locationManager;
    private LocationListener locationListener;
    EditText editTextLocalizacao;
    Button buttonEnviarLocalizacao;
    TextView textViewStatus;

    LatLng coordenadas;
    Double latitude, longitude;

    private String []permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Sua localização");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //startActivity(new Intent(MainActivity.this, RequisicoesActivity.class));

        editTextLocalizacao = findViewById(R.id.editTextLocalizacao);
        buttonEnviarLocalizacao = findViewById(R.id.buttonLocalizacao);
        textViewStatus = findViewById(R.id.textViewRequisicoesStatus);

        textViewStatus.setVisibility(View.GONE);

        PermissoesClasse.validarPermissoes(permissoes, this, 1);

        buttonEnviarLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaLocalizacaoUsuario();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        recuperaLocalizacaoUsuario();
    }

    private void recuperaLocalizacaoUsuario() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                coordenadas = new LatLng(latitude, longitude);
                String posicao = "Lat: " + String.valueOf(latitude) + " - Long: " + String.valueOf(longitude);
                editTextLocalizacao.setText(posicao);
                mMap.clear();
                mMap.addMarker(
                        new MarkerOptions()
                                .position(coordenadas)
                                .title("Minha localização")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.man_sick))
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 18));
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado: grantResults){
            if (permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidaPermissao();
            }
        }
    }


    public void alertaValidaPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissão negada!");
        builder.setMessage("Para utilizar esse APP aceite as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

    }


    private void salvaLocalizacaoUsuario(){

        try {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            LocalizacaoUsuarioClasse localizacaoDoUsuario = new LocalizacaoUsuarioClasse();
            localizacaoDoUsuario.setLatitude(String.valueOf(coordenadas.latitude));
            localizacaoDoUsuario.setLongitude(String.valueOf(coordenadas.longitude));
            localizacaoDoUsuario.setUsuario("Paulo");
            localizacaoDoUsuario.setStatus("Aguardando");
            localizacaoDoUsuario.setId(mAuth.getUid());
            localizacaoDoUsuario.salvarLocalizacao();

            buttonEnviarLocalizacao.setVisibility(View.GONE);
            editTextLocalizacao.setVisibility(View.GONE);
            textViewStatus.setSystemUiVisibility(View.VISIBLE);

        } catch (Exception ex) {
            ex.printStackTrace();
            String mens = "Aguarde o carregamento da localização GPS";
            Toast.makeText(this, mens, Toast.LENGTH_SHORT).show();
        }

    }


}

