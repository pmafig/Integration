package nsop.neds.mycascais.integration;

import androidx.appcompat.app.AppCompatActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nsop.neds.mycascais.messageencryption.MessageEncryption;

public class MainActivity extends AppCompatActivity {


    private static String PACKAGE_NAME = "nsop.neds.mycascais.integration";
    private static String MYCASCAIS = "nsop.neds.mycascais";
    static final String packagename = "PACKAGE_NAME";

    static final String appid = "APP_ID";
    static final String webauth = "https://myqua.cascais.pt/Account/Login?appid=21&rt=9PHlCIAREWMuKD8Hr5xD1HUI6WF4+H+j8GgdymdAXPX6ie1Vuk60HyaqxJGkkNza";

    static final String APP_KEY = "fc4e5f84847b4712b88f11db42fd804a";
    static final String APP_ID = "21";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        Button callButton = findViewById(R.id.externalAppCall);

        Intent intent = getIntent();

        String jsonMessage = "";
        String json = "";
        Bundle bundle = intent.getExtras();

        Uri appLinkData = getIntent().getData();

        if(appLinkData != null){
            json = appLinkData.getQueryParameter("tid");
        }

        if(bundle != null && bundle.containsKey(PACKAGE_NAME + ".vault")) {
            json = bundle.getString(PACKAGE_NAME + ".vault");

            MessageEncryption enc = new MessageEncryption();

            try {
                jsonMessage = enc.Decrypt(APP_KEY, json);
            }catch (Exception ex){
                jsonMessage = "error: " + ex.getMessage();
            }

            System.out.println(jsonMessage);
        }

        if(json != null && !json.isEmpty()) {
            TextView info = findViewById(R.id.result);

            MessageEncryption enc = new MessageEncryption();

            try {
                jsonMessage = enc.Decrypt(APP_KEY, json);
            }catch (Exception ex){
                jsonMessage = "error: " + ex.getMessage();
            }

            info.setText(jsonMessage);
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    call(APP_ID);
            }
        });
    }

    private void call(String appId){
        AccountManager accountManager = AccountManager.get(this);
        PackageManager pm = getPackageManager();

        if (isMyCascaisInstalled(pm)) {
           open360App(Integer.valueOf(appId));
        } else {
            openMarketApp();
        }
    }

    private boolean isMyCascaisInstalled(PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(MYCASCAIS, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void openMarketApp(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(webauth));
        startActivity(i);
    }

    private void open360App(int id){
        try {
            final String appPackageName = MYCASCAIS;
            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);

            intent.putExtra(packagename, PACKAGE_NAME);
            intent.putExtra(appid, id);

            startActivity(intent);
        }catch (Exception ex){
            TextView info = findViewById(R.id.result);
            info.setText(ex.getMessage());
        }
    }
}
