package com.example.diriodeposioesensores;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private Sensor sensorLuminosidade;
    private LocationManager locationManager;
    private BancoHelper bancoHelper;

    private TextView txtSensor;
    private TextView txtGps;
    private Button btnSalvar;
    private IndicadorLuzView viewCustomizada;

    private float valorLuzAtual = 0f;
    private String stringGpsAtual = "Aguardando GPS...";

    private Button btnVerHistorico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ligar componentes da tela
        txtSensor = findViewById(R.id.txtSensor);
        txtGps = findViewById(R.id.txtGps);
        btnSalvar = findViewById(R.id.btnSalvar);
        viewCustomizada = findViewById(R.id.viewCustomizada);
        btnVerHistorico = findViewById(R.id.btnVerHistorico);

        //-- intenção para tela do histórico.
        btnVerHistorico.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoricoActivity.class);
            startActivity(intent);
        });

        // Inicializar o Banco de Dados SQLite
        bancoHelper = new BancoHelper(this);

        // 2. Configurar Sensor de Luz
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            sensorLuminosidade = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }

        // 3. Configurar GPS e Permissões
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            iniciarGPS();
        }

        // 4. Ação do Botão de Salvar no SQLite
        btnSalvar.setOnClickListener(v -> {
            boolean salvo = bancoHelper.inserirRegistro(valorLuzAtual, stringGpsAtual);
            if (salvo) {
                Toast.makeText(this, "Registro salvo no SQLite com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao salvar no banco de dados.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarGPS() {
        try {
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            iniciarGPS();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorLuminosidade != null) {
            sensorManager.registerListener(this, sensorLuminosidade, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    // Leitura do Sensor em tempo real
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            valorLuzAtual = event.values[0];

            // Mantemos apenas a linha correta que atualiza o texto:
            txtSensor.setText("Sensor (Luz): " + valorLuzAtual + " lx");

            // Atualiza a nossa View Customizada dinamicamente
            viewCustomizada.setIntensidadeLuz(valorLuzAtual);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    // Leitura do GPS
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        stringGpsAtual = "Lat: " + lat + " | Lon: " + lon;
        txtGps.setText("GPS: " + stringGpsAtual);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        txtGps.setText("GPS desativado.");
    }
}