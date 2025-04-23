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
import br.com.hands.mdm.libs.android.databehavior.MDMDataBehavior;
import br.com.hands.mdm.libs.android.geobehavior.MDMGeoBehavior;
import br.com.hands.mdm.libs.android.notification.MDMNotification;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private boolean notificationPermission = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MDMGeoBehavior.start(getApplicationContext());
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 2);
            } else {
                // Permissão não concedida
                if (notificationPermission) {
                    requestNotificationPermissions();
                }
            }
        } else if (requestCode == 2) {
            if (notificationPermission) {
                requestNotificationPermissions();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!MDMCore.hasAskedPermission(getApplicationContext())) {
            final Activity activity = this;
            MDMCore.askPermissionFullDialog(this, new MDMCore.PermissionListener() {
                @Override
                public void onPermissionResponse(boolean responseDevice, boolean responseGeo,
                                                 boolean responseApp, boolean responseNotification, boolean responseData) {
                    if (responseApp && !MDMAppBehavior.hasAskedPermission(getApplicationContext())) {
                        MDMAppBehavior.setPermission(getApplicationContext(), true);
                        MDMAppBehavior.start(activity.getApplicationContext());
                    } else {
                        MDMAppBehavior.setPermission(getApplicationContext(), false);
                        /*if (!MDMAppBehavior.hasPermissionToRun(getApplicationContext()) &&
                                !MDMAppBehavior.hasAskedPermission(getApplicationContext())) {
                            MDMAppBehavior.askPermission(activity);
                        }*/
                    }

                    if (responseNotification) {
                        MDMNotification.setOptOut(getApplicationContext(), false);
                        notificationPermission = true;
                    } else {
                        MDMNotification.setOptOut(getApplicationContext(), true);
                    }

                    if (responseGeo) {
                        MDMGeoBehavior.setOptOut(getApplicationContext(), false);
                        requestGeoTrackingPermissions();
                    } else {
                        MDMGeoBehavior.setOptOut(getApplicationContext(), true);
                    }

                    if (responseData && !MDMDataBehavior.hasAskedPermission(getApplicationContext())) {
                        MDMDataBehavior.setPermission(getApplicationContext(), true);
                        MDMDataBehavior.start(activity.getApplicationContext());
                    } else {
                        MDMDataBehavior.setPermission(getApplicationContext(), false);
                        /*if (!MDMDataBehavior.hasPermissionToRun(getApplicationContext()) &&
                                !MDMDataBehavior.hasAskedPermission(getApplicationContext())) {
                            MDMDataBehavior.askPermission(activity);
                        }*/
                    }
                }
            });
        }
    }

    protected void requestNotificationPermissions() {
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

        // Verifica permissão de notificação
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Requisitar permissão de notificação
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.POST_NOTIFICATIONS
            }, 3);
        }
    }

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
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Requisitar permissão de geolocalização
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    //android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            }, 1);
        }
    }
}
