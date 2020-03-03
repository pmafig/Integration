package nsop.neds.mycascais.integration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import nsop.neds.mycascais.messageencryption.MessageEncryption;

public class Helper {

    private Context context;

    public Helper(Context context){
        this.context = context;
    }

    public void readMessage(Intent intent){
        Bundle bundle = intent.getExtras();

        Uri appLinkData = intent.getData();

        if(appLinkData != null){
            String qs = appLinkData.getQueryParameter("tid");

            if(qs != null && !qs.isEmpty()) {
                printResult(qs);
            }
        }

        if(bundle != null && bundle.containsKey(AppConfiguration.PACKAGE_NAME + ".vault")) {
            printResult(bundle.getString(AppConfiguration.PACKAGE_NAME + ".vault"));
        }
    }

    private void printResult(String result){
        String message;

        MessageEncryption enc = new MessageEncryption();

        try {
            message = enc.Decrypt(AppConfiguration.APP_KEY, result);
        }catch (Exception ex){
            message = "error: " + ex.getMessage();
        }
        new AlertDialog.Builder(context).setMessage(message).show();

        System.out.println(message);
    }
}
