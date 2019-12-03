package app.atelier.vendor.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import app.atelier.vendor.R;
import app.atelier.vendor.classes.LocaleHelper;
import app.atelier.vendor.classes.SessionManager;
import app.atelier.vendor.pushNotifications.AppController;
import app.atelier.vendor.webService.RetrofitConfig;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
private static int SPLASH_DISPLAY_LENGTH = 3000;
private String regId="";
private SessionManager sessionManager;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make the screen without statusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(this);
        //after splash screen >> the main activity will be opened and the main fragment will be displayed(MagalesTypesFragment)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, SPLASH_DISPLAY_LENGTH);

        FirebaseApp.initializeApp(this);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("splash", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        regId = task.getResult().getToken();
                        Log.d("regisId",regId);
                        registerInBackground();


                    }
                });
    }

    private void registerInBackground() {
        RetrofitConfig.getServices().INSERT_TOKEN(regId,"2", AppController.getInstance().getIMEI(), sessionManager.getId().length()>0 ? sessionManager.getId() : "0")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {

                            BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));

                            StringBuilder out = new StringBuilder();

                            String newLine = System.getProperty("line.separator");

                            String line;

                            while ((line = reader.readLine()) != null) {
                                out.append(line);
                                out.append(newLine);
                            }

                            String outResponse = out.toString();
                            Log.d("outResponse", ""+outResponse);

                            sessionManager.setRegId(regId);



                        } catch (Exception ex) {

                            ex.printStackTrace();


                        }

                        /*Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.putExtra(DrwazaConstans.FIRST_TIME_LAUNCH, "0");
                        intent.putExtra("regId", regId);

                        startActivity(intent);
                        finish();*/
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
        });

    }
}
