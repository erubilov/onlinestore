package jrcom.ru.actionbarcontect;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Pattern;

/**
 * Created by eugeniy.rubilov on 14.03.2015.
 */
public class Utils {
    public final static String TAG = "store-tag";

    public static void writeFile(String data) {

        try {
            File secondFile = getFile();
            secondFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(secondFile);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getFileOrderName() {
        return StoreApplication.getInstance().getCacheDir() + File.separator + "order.txt";

    }

    private static File getFile() {
        return new File(Environment.getExternalStorageDirectory(), "order.txt");
    }

    public static Intent getEmailIntent() {
        TelephonyManager tm = (TelephonyManager)StoreApplication.getInstance().getSystemService(Activity.TELEPHONY_SERVICE);
        String number = tm.getLine1Number();

        String gmail = null;

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(StoreApplication.getInstance()).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                gmail = account.name;
                break;
            }
        }

        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"e.rubilov@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Заказ");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, number + "\n" + gmail);
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(getFile()));
        return emailIntent;
    }

    public static void checkNetworkAvailable(final Activity context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage(R.string.flash_no_network_prompt)
                    .setCancelable(false)
                    .setPositiveButton(R.string.alert_settings, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.alert_close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            context.finish();
                        }
                    });
            tryShowAlert(builder);
        }
    }

    private static void tryShowAlert(AlertDialog.Builder builder) {
        try {
            builder.show();
        } catch (WindowManager.BadTokenException ex) {
            Log.e(TAG, "Unable to show alert: " + ex);
        }
    }

}
