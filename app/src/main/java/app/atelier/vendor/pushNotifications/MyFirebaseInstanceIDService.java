package app.atelier.vendor.pushNotifications;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import app.atelier.vendor.classes.Constants;
import app.atelier.vendor.classes.SessionManager;
import app.atelier.vendor.webService.RetrofitConfig;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    SessionManager sessionManager;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("Refreshed", "Refreshed token: " + s);

        sessionManager = new SessionManager(this);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(s,this);

    }

    private void sendRegistrationToServer(final String regId, final Context context) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                msg = "Device registered, registration ID=" + regId ;
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.e("registrationId service", "regId -> "+regId +"------------"+ sessionManager.getId());

                RetrofitConfig.getServices(context).INSERT_TOKEN(regId,"2", AppController.getInstance().getIMEI(), sessionManager.getId().length()>0 ? sessionManager.getId() : "0")
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
                                    sessionManager.setRegId(regId);

                                } catch (Exception ex) {

                                    ex.printStackTrace();


                                }

                        /*Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        intent.putExtra(DrwazaConstans.FIRST_TIME_LAUNCH, "0");
                        intent.putExtra("regid", regid);

                        startActivity(intent);
                        finish();*/
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

            }
        }.execute(null, null, null);
    }


}
