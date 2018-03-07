package br.com.mdm.android.example;

import br.com.hands.mdm.libs.android.bundle.MDMBundle;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Start do Bundle com todos os módulos
        MDMBundle.start(getApplicationContext());

        //
        // OU
        //

        // Start de cada módulo separadamente
        // MDMGeoBehavior.start(getApplicationContext());
        // MDMAppBehavior.start(getApplicationContext());
        // MDMAd.start(getApplicationContext());
        // MDMNotification.start(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}