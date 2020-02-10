package nsop.neds.mycascais.integration;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccountManager accountManager = AccountManager.get(this);
        PackageManager pm = getPackageManager();

        if (isMyCascaisInstalled(pm)) {
            final Account availableAccounts[] = accountManager.getAccountsByType("com.MyCascais.authenticator");

            if (availableAccounts.length != 0) {

                String name[] = new String[availableAccounts.length];

                for (int i = 0; i < availableAccounts.length; i++) {
                    Intent intent = getIntent();

                    String json = "";
                    Bundle bundle = intent.getExtras();

                    if(bundle != null && bundle.containsKey("pt.app.sampleapp.vault")) {
                        json = bundle.getString("pt.app.sampleapp.vault");
                    }

                    if(json != null && !json.isEmpty()) {
                        System.out.println(availableAccounts[i].name);
                        System.out.println(availableAccounts[i].type);
                        System.out.println(availableAccounts[i]);
                    }else{
                        open360App();
                    }
                }
            }else{
                open360App();
            }

        } else {
            System.out.println("MyCascais not installed");
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

    private void open360App(){
        final String appPackageName = "nsop.neds.mycascais";
        Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);


        intent.putExtra("packageName", "nsop.neds.mycascais.integration");
        intent.putExtra("externalAppId", 15);

        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }
}
