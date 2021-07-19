package br.com.mdm.android.example;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import br.com.hands.mdm.libs.android.appbehavior.MDMAppBehavior;
import br.com.hands.mdm.libs.android.core.MDMCore;
import br.com.hands.mdm.libs.android.geobehavior.MDMGeoBehavior;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity activity = this;
        MDMCore.askPermissionFullDialog(this, new MDMCore.PermissionListener() {
            @Override
            public void onPermissionResponse(boolean response) {
                if (response && !MDMAppBehavior.hasAskedPermission(getApplicationContext())) {
                    MDMAppBehavior.setPermission(getApplicationContext(), true);
                    MDMAppBehavior.start(activity.getApplicationContext());
                } else {
                    if (!MDMAppBehavior.hasPermissionToRun(getApplicationContext()) &&
                            !MDMAppBehavior.hasAskedPermission(getApplicationContext())) {
                        MDMAppBehavior.askPermission(activity);
                    }
                }
                // Pedir permissão de Geolocalização ao usuário e inicia o módulo de GeoBehavior
                requestGeoTrackingPermissions();
            }
        });
    }

    // Método exemplo para pedir permissão de geolocalização
    protected void requestGeoTrackingPermissions() {
        // Verifica Google Play Services
        final GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        final int result = googleAPI.isGooglePlayServicesAvailable(getApplicationContext());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 9000).show();
            }
            // Usuário precisa atualizar o Google Play Services
            return;
        }

        // Verifica permissão de geolocalização
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                /*android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getApplicationContext(),*/
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            // Requisitar permissão de geolocalização
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    //android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    android.Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MDMGeoBehavior.start(getApplicationContext());
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
            } else {
                // Permissão não concedida
            }
        }
    }
}
