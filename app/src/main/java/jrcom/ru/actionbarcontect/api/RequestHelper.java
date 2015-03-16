package jrcom.ru.actionbarcontect.api;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import jrcom.ru.actionbarcontect.R;
import jrcom.ru.actionbarcontect.StoreApplication;
import jrcom.ru.actionbarcontect.provider.DataManager;

/**
 * Created by eugeniy.rubilov on 14.03.2015.
 */
public class RequestHelper {

    public static void loadFile(final String url1, final ApiManager.ResponseCallback callback) {

        AsyncTask downloadTask = new AsyncTask<Object, Void, String>() {

            private PowerManager.WakeLock mWakeLock;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // take CPU lock to prevent CPU from going off if the user
                // presses the power button during download
                PowerManager pm = (PowerManager) StoreApplication.getInstance().getApplicationContext().getSystemService(Context.POWER_SERVICE);
                mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                        getClass().getName());
                mWakeLock.acquire();
            }

            @Override
            protected String doInBackground(Object... sUrl) {
                InputStream input = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL((String)sUrl[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    // expect HTTP 200 OK, so we don't mistakenly save error report
//                    instead of the file
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP " + connection.getResponseCode()
                                + " " + connection.getResponseMessage();
                    }

                    // this will be useful to display download percentage
                    // might be -1: server did not report the length
                    int fileLength = connection.getContentLength();
                    String savedFile = StoreApplication.getInstance().readGoodsList();
                    if (fileLength == -1
                            || fileLength != savedFile.getBytes().length){
                        // download the file
                        input = connection.getInputStream();
                        BufferedReader r = new BufferedReader(new InputStreamReader(input));
                        StringBuilder total = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            total.append(line);
                        }
                        Log.d("network", "file loaded:\n" + total.toString());
                        DataManager.getInstance().setGoods(total.toString());
                    } else {
                        return StoreApplication.getInstance().getApplicationContext().getString(R.string.loaded_from_local);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return e.toString();
                } finally {
                    try {
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                mWakeLock.release();
                if (result != null) {
                    Toast.makeText(StoreApplication.getInstance().getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    callback.onFailure(-1, result);
                } else {
                    Toast.makeText(StoreApplication.getInstance().getApplicationContext(),
                            StoreApplication.getInstance().getApplicationContext().getString(R.string.loaded_from_server),
                            Toast.LENGTH_SHORT).show();
                    callback.onSuccess(DataManager.getInstance().getGoods());
                }
            }
        };

        downloadTask.execute(url1);
    }
}
