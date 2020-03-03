package nsop.neds.mycascais.integration;

import androidx.appcompat.app.AppCompatActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppConfiguration.PACKAGE_NAME = getApplicationContext().getPackageName();

        Button callButton = findViewById(R.id.externalAppCall);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(AppConfiguration.APP_ID);
            }
        });

        new Helper(this).readMessage(getIntent());
    }

    private void call(String appId){
        if (isMyCascaisInstalled()) {
           open360App(Integer.valueOf(appId));
        } else {
            openAuthWeb();
        }
    }

    private boolean isMyCascaisInstalled() {
        try {
            PackageManager pm = getPackageManager();

            pm.getPackageInfo(AppConfiguration.MYCASCAIS, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void openAuthWeb(){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(AppConfiguration.webauth));
        startActivity(i);
    }

    private void open360App(int id){
        try {
            final String appPackageName = AppConfiguration.MYCASCAIS;
            Intent intent = getPackageManager().getLaunchIntentForPackage(appPackageName);

            intent.putExtra(AppConfiguration.packagename, AppConfiguration.PACKAGE_NAME);
            intent.putExtra(AppConfiguration.appid, id);

            startActivity(intent);
        }catch (Exception ex){
            Toast.makeText(this, "error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
