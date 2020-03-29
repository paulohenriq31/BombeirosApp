package com.example.projetinho;

import android.Manifest;
import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Sua localização");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        editTextLocalizacao = findViewById(R.id.editTextLocalizacao);
        buttonEnviarLocalizacao = findViewById(R.id.buttonLocalizacao);
        textViewStatus = findViewById(R.id.textViewRequisicoesStatus);

        textViewStatus.setVisibility(View.GONE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        recuperarLocalizacaoUsuario();
    }

    public void salvarLocalizacaoUsuario(View view) {
        try {
            LocalizacaoUsuarioClasse localizacaoUsuario = new LocalizacaoUsuarioClasse();
            localizacaoUsuario.setLatitude(String.valueOf(coordenadas.latitude));
            localizacaoUsuario.setLongitude(String.valueOf(coordenadas.longitude));
            localizacaoUsuario.setUsuario("Paulo");
            localizacaoUsuario.setStatus(StatusRequisicoesClasse.STATUS_AGUARDANDO);
            localizacaoUsuario.salvarLocalizacao();

            buttonEnviarLocalizacao.setVisibility(View.GONE);
            editTextLocalizacao.setVisibility(View.GONE);
            textViewStatus.setSystemUiVisibility(View.VISIBLE);

        } catch (Exception ex) {
            ex.printStackTrace();
            String mens = "Aguarde o carregamento da localização GPS";
            Toast.makeText(this, mens, Toast.LENGTH_SHORT).show();
        }

    }

    private void recuperarLocalizacaoUsuario() {

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);
        }

    }

}
