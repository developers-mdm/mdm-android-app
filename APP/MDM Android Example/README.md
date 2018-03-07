# Hands SDK APP Exemplo

Esse app tem como objetivo demonstrar a integração do Smart SDK, Hands MDM e Hands MBH.


# Documentação Hands MDM Integrado com Smart SDK

Esse documento tem como objetivo orientar acerca da implementação da biblioteca MDM Hands integrado com o Smart SDK.

# Sobre a biblioteca

Essa biblioteca funciona a partir do Android 4.1+ e está preparada para o sistema de permissionamento Android 6+. Segue requerimentos:

* Android SDK 16 ou superior
* Gradle 2.1 ou superior
* buildToolsVersion 21.1.2 ou superior
* Android Studio 2.1.2 ou superior
* Dispositivo habilitado para desenvolvimento ou emulador (Consulte informações aqui: https://developer.android.com/studio/run/device.html)

# Como iniciar o projeto

A seguir instruções sobre como integrar o **Hands MDM** ao seu projeto utilizando a linguagem Java.


## Passo 1: Integrando o SDK em seu aplicativo

Importe os arquivos hands.properties, MDMAdServerConfig.xml (assets), mdm.aar e SmartAdServer-Android-SDK.jar (libs) em seu projeto. A forma mais comum de fazer isso é copiando os arquivos hands.properties e MDMAdServerConfig.xml para a pasta de assets (src/main/assets), mdm.aar e SmartAdServer-Android-SDK.jar para a pasta libs (libs) do seu projeto. 

No seu build.gradle adicione as diretivas:

    repositories {
        flatDir {
            dirs 'libs'
        }
    }
    
    dependencies {
        compile(name: 'mdm', ext: 'aar')
    }

O Conteúdo do arquivo MDMAdServerConfig.xml deve ser no formato:

    <?xml version="1.0" encoding="UTF-8"?>
    <config>
        <server value="http://mobile.smartadserver.com" />
        <publisher value="98861" />
    
        <formats>
            <format id="INTERSTITIAL" value="20529" />
        </formats>
    
        <screens>
            <screen id="MAIN" value="684802" />
        </screens>
    </config>

Todas tags são obrigatórias. Esse arquivo geralmente será enviado pela Hands devidamente configurado. 

Rode o comando ``gradle build`` para aplicar as alterações de projeto.


## Passo 2: Inicializando a biblioteca MDM

A biblioteca é inicializada assincronamente. É recomendado inicializar o framework no evento onCreate da Activity. É preciso passar a referência do banner e da activity: 

```java
handsMdm = HandsMdmAds.getInstance().setContext(getApplicationContext()).setActivity(this).setBannerView(banner);
if (!handsMdm.isInitialized()) {
    handsMdm.init(new OnMdmAdsBootFinished() {
        @Override
        public void onInitFinished(HandsMdm handsMdm) {
            // MDM inicializado
        }
    });
}
```


## Passo 3: Utilizando o MDM

Para setar um valor não criptografado:

```java
if (handsMdm.isInitialized()) {
    handsMdm.setData("key", "value", false);
}
```


Para setar um valor criptografado:
```java
if (handsMdm.isInitialized()) {
    handsMdm.setData("key", "value", true);
}
```


## Passo 4: Obtendo audiências

Para obter a audiência basta utilizar o método track:


```java
if (handsMdm.isInitialized()) {
    handsMdm.track(new TransportResult() {
        @Override
        public void onFinished(final List<MdmAudience> audiences) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // utiliza a lista de audiencias aqui
                }
            });
        }
    });
}
```
  
  
# Documentação Hands MBH

Esse documento tem como objetivo orientar acerca da implementação do agente de localização **Hands MBH**.

# Sobre a biblioteca

Essa biblioteca funciona a partir do Android 4.1+ e está preparada para o sistema de permissionamento Android 6+. Segue requerimentos:

* Android SDK 16 ou superior
* Gradle 2.1 ou superior
* buildToolsVersion 21.1.2 ou superior
* Android Studio 2.1.2 ou superior
* Dispositivo habilitado para desenvolvimento ou emulador (Consulte informações aqui: https://developer.android.com/studio/run/device.html)

# Como iniciar o projeto

A seguir instruções sobre como integrar o **Hands MBH** ao seu projeto utilizando a linguagem Java.


## Passo 1: Integrando o SDK em seu aplicativo

Importe os arquivos mbh.properties (assets), hands-mbh.aar (libs) em seu projeto. A forma mais comum de fazer isso é copiando os arquivos mbh.properties para a pasta de assets (src/main/assets) e o hands-mbh.aar para a pasta libs (libs) do seu projeto. 

No seu build.gradle adicione as diretivas:

    repositories {
        flatDir {
            dirs 'libs'
        }
    }
    
    dependencies {
        compile(name: 'mbh', ext: 'aar')
    }

Rode o comando ``gradle build`` para aplicar as alterações de projeto.


## Passo 2: Ativar serviços de localização e atualização em segundo plano no seu app

A biblioteca se encarregará de solicitar as devidas permissões ao usuário a cada inicialização do app (foreground). São elas: android.permission.ACCESS_COARSE_LOCATION e android.permission.ACCESS_FINE_LOCATION. Não é necessário declarar nenhuma permissão no AndroidManifest.xml do app principal.

Caso o serviço de localização esteja desativado a biblioteca não funcionará em background até o usuário ativá-lo novamente. No entanto, independentemente o app perguntará ao usuário pela permissões sempre que for necessário.

Se o app for aberto novamente e o app ainda não possuir as permissões necessárias a biblioteca solicitará novamente.


## Passo 3: Inicializando o serviço de monitoramento

Você precisa ativar a monitoração do SDK manualmente em seu método onCreate na sua Activity: inclua a inicialização do MBH.

```java
final Activity activity = this;
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        if (!MbhManager.getInstance().isRunning()) {
            try {
                MbhManager.getInstance().setContext(getApplicationContext()).setActivity(activity).run();
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }
    }
});
```


## Passo 4: Mantendo o serviço ativo

O serviço se manterá ativo sem nenhuma intervenção do usuário. A SDK uma vez inicializada a única maneira de desativá-la é desinstalando o app principal.

  
# Criando um link dados do usuário com outros sistemas: 

É possível associar os dados de visitas coletados pelo SDK com outros sistemas, como por exemplo um CRM. Esse link é feito com um "Identificador externo" anônimo gerado por você que é relacionado ao seu usuário e será armazenado pelo SDK com os dados de visita coletados. Isso permite que mais tarde você acesse os dados daquele usuário facilmente independente do dispositivo e sem que o SDK tenha acesso a nenhum dado do sistema integrado, garantindo privacidade das informações. 

Você pode definir esse identificador a qualquer momento depois na inicialização do SDK utilizando o método:

```java
MbhManager.getInstance().setExternalId("Seu Identificador externo");
```

A partir deste momento os dados coletados serão associados ao ID que você informar. Esse método só precisa ser invocado uma vez, ou se o identificador do usuário mudar no seu sistema.    