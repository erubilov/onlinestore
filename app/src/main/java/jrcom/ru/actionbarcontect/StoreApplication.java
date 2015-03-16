package jrcom.ru.actionbarcontect;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

import jrcom.ru.actionbarcontect.provider.DataManager;

/**
 * Created by eugeniy.rubilov on 14.03.2015.
 */
public class StoreApplication extends Application{

    private SharedPreferences pref;
    private DataManager mDataManager;
    private static StoreApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        hackMenu();
    }

    public static StoreApplication getInstance(){
        return instance;
    }

    public void initDataManager(){
        mDataManager = new DataManager();
    }

    public DataManager getDataManager(){
        return mDataManager;
    }

    public void saveGoodsList(String jsonGoodsList){
        pref.edit().putString("list", jsonGoodsList).apply();
    }
    public String readGoodsList(){
        return pref.getString("list", "");
    }

    private void hackMenu(){
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
    }
}
