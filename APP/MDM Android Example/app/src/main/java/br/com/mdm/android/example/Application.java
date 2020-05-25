package br.com.mdm.android.example;

import br.com.hands.mdm.libs.android.bundle.MDMBundle;
import br.com.hands.mdm.libs.android.core.MDMCore;
import br.com.hands.mdm.libs.android.core.OnStartListener;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Debug Mode
        MDMCore.setDebugMode(true);

        String appId = "SEU_APP_ID";

        // Start do Bundle com todos os módulos
        MDMCore.start(getApplicationContext(), appId, new OnStartListener() {
            @Override
            public void onStart() {
                MDMBundle.start(getApplicationContext());
            }
        });

        //
        // OU
        //

        // Start de cada módulo separadamente
        /*MDMCore.start(getApplicationContext(), appId, new OnStartListener() {
            @Override
            public void onStart() {
                MDMGeoBehavior.start(getApplicationContext());
                MDMAppBehavior.start(getApplicationContext());
                MDMNotification.start(getApplicationContext());
            }
        });*/
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}