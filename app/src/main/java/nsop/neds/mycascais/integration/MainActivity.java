package nsop.neds.mycascais.integration;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button callButton = findViewById(R.id.externalAppCall);

        Intent intent = getIntent();

        String json = "";
        Bundle bundle = intent.getExtras();

        if(bundle != null && bundle.containsKey("nsop.neds.mycascais.integration.vault")) {
            json = bundle.getString("nsop.neds.mycascais.integration.vault");
            System.out.println(json);
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
            final Account availableAccounts[] = accountManager.getAccountsByType("com.MyCascais.authenticator");

           open360App(Integer.valueOf(appId));

        } else {
            Toast.makeText(this, "Cascais 360 não está instalada.", Toast.LENGTH_SHORT).show();;
            openMarketApp();
        }
    }

    private boolean isMyCascaisInstalled(PackageManager packageManager) {
        try {
            packageManager.getPackageInfo("nsop.neds.mycascais", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void openMarketApp(){
        final String appPackageName = "com.citypoints";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
        startActivity(intent);
    }

    static final int PICK_CONTACT_REQUEST = 1;

    private void open360App(int appId){
        try {
            final String appPackageName = "nsop.neds.mycascais";
            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.putExtra("packageName", "nsop.neds.mycascais.integration");
            intent.putExtra("externalAppId", appId);

            startActivityForResult(intent, PICK_CONTACT_REQUEST);
        }catch (Exception ex){
            TextView info = findViewById(R.id.result);
            info.setText(ex.getMessage());
        }
    }
}
