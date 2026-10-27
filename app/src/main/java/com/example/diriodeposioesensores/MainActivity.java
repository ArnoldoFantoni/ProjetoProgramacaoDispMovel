package com.example.diriodeposioesensores;

import android.Manifest;
import android.content.Context;
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

    private TextView txtSensor;
    private TextView txtGps;
    private Button btnSalvar;

    private String ultimaLocalizacao = "Aguardando GPS...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ligar componentes visuais
        txtSensor = findViewById(R.id.txtSensor);
        txtGps = findViewById(R.id.txtGps);
        btnSalvar = findViewById(R.id.btnSalvar);

        // 2. Configurar Sensor de Luz
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            sensorLuminosidade = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }

        // 3. Configurar Geolocalização (GPS)
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Verificar e solicitar permissão de GPS se o usuário ainda não permitiu
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            iniciarGPS();
        }

        // 4. Ação do Botão Salvar
        btnSalvar.setOnClickListener(v -> {
            Toast.makeText(this, "Dados prontos para salvar no SQLite!", Toast.LENGTH_SHORT).show();
        });
    }

    private void iniciarGPS() {
        try {
            if (locationManager != null) {
                // Pede atualizações de localização via GPS
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
                Toast.makeText(this, "Buscando sinal de GPS...", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    // Tratamento da resposta da permissão do usuário
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            iniciarGPS();
        } else {
            Toast.makeText(this, "Permissão de GPS negada!", Toast.LENGTH_SHORT).show();
        }
    }

    // Métodos do Sensor
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float valorLuz = event.values[0];
            txtSensor.setText("Sensor (Luz): " + valorLuz + " lx");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    // Métodos do GPS (LocationListener)
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        ultimaLocalizacao = "Lat: " + lat + " | Lon: " + lon;
        txtGps.setText("GPS: " + ultimaLocalizacao);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {} // No Android mais antigo pode ser String provider

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        txtGps.setText("GPS desativado no aparelho.");
    }
}