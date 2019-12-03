package app.atelier.vendor.classes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import app.atelier.vendor.R;
import app.atelier.vendor.views.activities.MainActivity;

public class GlobalFunctions {
    public static SessionManager sessionManager;
    private static int PERMISSION_CODE = 23;

    public static void DisableLayout(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                DisableLayout((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

    public static void EnableLayout(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                EnableLayout((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }

    public static String formatDate(String date){
        String dateResult = "";
        Locale locale = new Locale("en");


        SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",locale);
        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MM/dd/yyyy",locale);

        //SimpleDateFormat dateFormatter2 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa", locale);

        int index = date.lastIndexOf('/');

        try {

            dateResult = dateFormatter2.format(dateFormatter1.parse(date.substring(index + 1)));

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return dateResult;
    }

    public static void generalErrorMessage(ProgressBar loading,Context context){
        loading.setVisibility(View.GONE);
        Snackbar.make(loading,context.getString(R.string.generalError), Snackbar.LENGTH_SHORT).show();
    }

    public static void setDefaultLanguage(Context context){
        SessionManager sessionManager = new SessionManager(context);
        String language = sessionManager.getLanguage();
        if (language.equals("en")) {
            LocaleHelper.setLocale(context,"en");
            MainActivity.isEnglish = true;
        } else {
            LocaleHelper.setLocale(context,"ar");
            MainActivity.isEnglish = false;
        }
    }

    public static void clearAllStack(FragmentActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
    public static void clearLastStack(FragmentActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.popBackStack();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager myConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = myConnectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected() && netInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isWriteExternalStorageAllowed(FragmentActivity activity) {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    public static void requestWriteExternalStoragePermission(FragmentActivity activity) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission

            //checkMyPermission();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
    }
}
