package app.atelier.vendor.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import app.atelier.vendor.R;
import app.atelier.vendor.classes.FixControl;
import app.atelier.vendor.classes.GlobalFunctions;
import app.atelier.vendor.classes.LocaleHelper;
import app.atelier.vendor.classes.Navigator;
import app.atelier.vendor.classes.SessionManager;
import app.atelier.vendor.pushNotifications.AppController;
import app.atelier.vendor.views.activities.MainActivity;
import app.atelier.vendor.webService.RetrofitConfig;
import app.atelier.vendor.webService.response.LoginResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    public static FragmentActivity activity;
    public static LoginFragment fragment;
    private SessionManager sessionManager;
    private String regId = "";

    @BindView(R.id.fragment_login_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_login_et_password)
    EditText password;
    @BindView(R.id.fragment_login_et_userName)
    EditText userName;
    @BindView(R.id.loading)
    ProgressBar loading;


    public static LoginFragment newInstance(FragmentActivity activity) {
        fragment = new LoginFragment();
        LoginFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.appbar.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        sessionManager = new SessionManager(activity);
        FixControl.setupUI(container,activity);
    }

    @OnClick(R.id.fragment_login_btn_login)
    public void loginCLick() {
        String userNameOrEmailStr = userName.getText().toString();
        String passwordStr = password.getText().toString();
        if (userNameOrEmailStr == null || userNameOrEmailStr.isEmpty()) {
            Snackbar.make(loading, getString(R.string.enterUserNameOrEmail), Snackbar.LENGTH_SHORT).show();
        } else if (passwordStr == null || passwordStr.isEmpty()) {
            Snackbar.make(loading, getString(R.string.enterPassword), Snackbar.LENGTH_SHORT).show();
        } else {
            if (GlobalFunctions.isNetworkConnected(activity)) {
                loginApi(userNameOrEmailStr, passwordStr);
            } else {
                Snackbar.make(loading, getString(R.string.checkInternet), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.fragment_login_tv_forgetPass)
    public void forgetPassClick() {
        Navigator.loadFragment(activity, ForgetPasswordFragment.newInstance(activity), R.id.activity_main_fl_container, false);
    }

    @OnClick(R.id.fragment_login_tv_changeLang)
    public void changeLangClick() {
        changeLanguage();
    }


    private void changeLanguage() {

        if (sessionManager.getLanguage().equals("en")) {
            sessionManager.setLanguage("ar");
            MainActivity.isEnglish = false;
        } else {
            sessionManager.setLanguage("en");
            MainActivity.isEnglish = true;
        }
        LocaleHelper.setLocale(activity, sessionManager.getLanguage());

        activity.finish();
        activity.overridePendingTransition(0, 0);
        startActivity(new Intent(activity, MainActivity.class));
    }

    public void loginApi(String userNameOrEmail, String password) {
        loading.setVisibility(View.VISIBLE);
        RetrofitConfig.getServices(activity).LOGIN_RESPONSE_CALL(userNameOrEmail, password)
                .enqueue(
                        new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                if (response.code() == 200) {
                                    if (response.body().VendorUsers.get(0).vendorId > 0) {
                                        sessionManager.setId(response.body().VendorUsers.get(0).id);
                                        sessionManager.setVendorId(response.body().VendorUsers.get(0).vendorId);
                                        sessionManager.setEmail(response.body().VendorUsers.get(0).email);
                                        sessionManager.LoginSession();
                                        Navigator.loadFragment(activity, OrdersFragment.newInstance(activity), R.id.activity_main_fl_container, false);

                                        FirebaseInstanceId.getInstance().getInstanceId()
                                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                        if (!task.isSuccessful()) {
                                                            Log.w("login", "getInstanceId failed", task.getException());
                                                            return;
                                                        }

                                                        // Get new Instance ID token
                                                        regId = task.getResult().getToken();

                                                        Log.e("registrationId Login ", "regId -> "+regId+"------------"+sessionManager.getId());

                                                        registerInBackground();


                                                    }
                                                });
                                    }
                                } else {
                                    Snackbar.make(loading, getString(R.string.nameOrPasswordIncorrect), Snackbar.LENGTH_SHORT).show();
                                }
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {

                            }
                        }
                );
    }



    private void registerInBackground() {
        RetrofitConfig.getServices(activity).INSERT_TOKEN(regId,"2", AppController.getInstance().getIMEI(), sessionManager.getId().length()>0 ? sessionManager.getId() : "0")
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

