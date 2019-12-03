package app.atelier.vendor.classes;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SessionManager {
    Context context;
    public  SharedPreferences sharedPref;
    public  SharedPreferences.Editor editor;
    public  final String USER_PREF = "user-pref";
    private  final String IS_LOGGED = "isLogged";
    private  final String ID = "id";
    private  final String VENDOR_ID = "vendor_id";
    private  final String LANGUAGE_CODE = "language_code";
    private  final String CURRENCY_CODE = "currency_name";
    private final String EMAIL = "email";
    private static String IsNotificationOn = "IsNotificationOn";
    public static final String KEY_RegID = "regId";


    public SessionManager(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(USER_PREF, MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public  void LoginSession() {
        editor.putBoolean(IS_LOGGED, true);
        editor.commit();
    }

    public  boolean isLoggedIn() {
        return sharedPref.getBoolean(IS_LOGGED, false);
    }

    public  void logout() {
        editor.putBoolean(IS_LOGGED, false);
        setEmail("");
        setVendorId(0);
        setId(0);
        editor.commit();
    }

    public  void setId(int id) {
        editor.putString(ID, String.valueOf(id));
        editor.apply();
    }

    public  String getId() {
        return sharedPref.getString(ID, "");
    }


    public  void setVendorId(int vendorId) {
        editor.putString(VENDOR_ID, String.valueOf(vendorId));
        editor.apply();
    }

    public  String getVendorId() {
        return sharedPref.getString(VENDOR_ID, "");
    }

    public  void setLanguage(String languageCode) {
        editor.putString(LANGUAGE_CODE, languageCode);
        editor.apply();

    }

    public  String getLanguage() {
        return sharedPref.getString(LANGUAGE_CODE, "");
    }

    public  void setCurrencyCode(String currencyCode) {
        editor.putString(CURRENCY_CODE, currencyCode);
        editor.apply();
    }

    public  String getCurrencyCode() {
        return sharedPref.getString(CURRENCY_CODE, "");
    }


    public  void setEmail(String email) {
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public  String getEmail() {
        return sharedPref.getString(EMAIL, "");
    }


    public void changeNotification(boolean status){
        editor.putBoolean(IsNotificationOn,status);
        editor.commit();
    }

    public boolean isNotificationOn(){
        return  sharedPref.getBoolean(IsNotificationOn,true);
    }

    public String getRegId() {
        return sharedPref.getString(KEY_RegID, "");
    }

    public void setRegId(String id) {
        editor.putString(KEY_RegID, id);
        editor.commit();
    }
}
