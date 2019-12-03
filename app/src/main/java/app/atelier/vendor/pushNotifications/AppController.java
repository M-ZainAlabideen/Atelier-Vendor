package app.atelier.vendor.pushNotifications;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import java.util.Locale;

import android.provider.Settings;

import androidx.multidex.MultiDex;

import app.atelier.vendor.classes.LocaleHelper;
import app.atelier.vendor.classes.SessionManager;


public class AppController extends Application {

    SessionManager sessionManager;
    private static AppController mInstance;
    private Locale locale = null;
    private String language;

    public static Context getContext() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sessionManager = new SessionManager(this);
        mInstance = this;
        Configuration config = getBaseContext().getResources().getConfiguration();
        language = sessionManager.getLanguage();
        if (!"".equals(language) && !config.locale.getLanguage().equals(language)) {
            locale = new Locale(language);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    public static synchronized AppController getInstance() {
        if (mInstance == null) {
            try {
                mInstance = AppController.class.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return mInstance;
    }

    public String getIMEI() {

        return Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }

}
