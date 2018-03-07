package br.com.mdm.android.example;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.smartadserver.android.library.SASBannerView;
import com.smartadserver.android.library.SASInterstitialView;

import br.com.hands.mdm.libs.android.ad.MDMAd;
import br.com.hands.mdm.libs.android.appbehavior.MDMAppBehavior;
import br.com.hands.mdm.libs.android.geobehavior.MDMGeoBehavior;
import br.com.hands.mdm.libs.android.notification.MDMNotification;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private SASBannerView banner;
    private SASInterstitialView interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pedir permissão de Geolocalização ao usuário e inicia o módulo de GeoBehavior
        requestGeoTrackingPermissions();

        // Inicia o módulo de AppBehavior
        if (!MDMAppBehavior.hasPermissionToRun(getApplicationContext())) {
            MDMAppBehavior.askPermission(this);
        }

        // Apresenta os banners dos IDs de formato e página do arquivo de configuração MDMAdServerConfig.xml
        banner = (SASBannerView) findViewById(R.id.banner);
        interstitial = new SASInterstitialView(this);

        MDMAd.loadAd(this, banner, true, "ARROBA", "HOME", null);
        MDMAd.loadAd(this, interstitial, true, "INTERSTITIAL", "HOME", null);
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
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Requisitar permissão de geolocalização
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (200 == requestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permissão concedida, inicializar módulo GeoBehavior
                MDMGeoBehavior.start(getApplicationContext());
            } else {
                // Permissão não concedida
            }
        }
    }


    // Métodos necessários para o módulo de Notification
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        MDMNotification.processNotification(getIntent().getExtras(), this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        MDMNotification.processNotification(getIntent().getExtras(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            setIntent(intent);
        }
        super.onNewIntent(intent);
    }

}
