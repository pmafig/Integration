package nsop.neds.mycascais.integration;

import androidx.appcompat.app.AppCompatActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private static String PACKAGE_NAME;
    private static String MYCASCAIS = "nsop.neds.mycascais";
    static final String packagename = "PACKAGE_NAME";
    static final String appid = "APP_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        Button callButton = findViewById(R.id.externalAppCall);

        Intent intent = getIntent();

        String json = "";
        Bundle bundle = intent.getExtras();

        if(bundle != null && bundle.containsKey(PACKAGE_NAME + ".vault")) {
            json = bundle.getString(PACKAGE_NAME + ".vault");

            System.out.println(json);

            //********************************************************
            //********* Third party application code here ************
            //********************************************************
        }

        if(json != null && !json.isEmpty()) {
            TextView info = findViewById(R.id.result);
            info.setText(json);
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText externalAppId = findViewById(R.id.externalAppId);
                final String appId = externalAppId.getText().toString();

                if(!appId.trim().isEmpty()) {
                    call(appId);
                }else{
                    Toast.makeText(MainActivity.this, "Por favor insira um appId.", Toast.LENGTH_SHORT).show();
                }
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
        Toast.makeText(this, "APP Cascais 360 não está instalada.", Toast.LENGTH_SHORT).show();
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
