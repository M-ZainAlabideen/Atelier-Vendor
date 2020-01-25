package app.atelier.vendor.views.fragments;

import android.os.Bundle;
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

import com.google.android.material.snackbar.Snackbar;

import app.atelier.vendor.R;
import app.atelier.vendor.classes.FixControl;
import app.atelier.vendor.classes.GlobalFunctions;
import app.atelier.vendor.classes.Navigator;
import app.atelier.vendor.views.activities.MainActivity;
import app.atelier.vendor.webService.RetrofitConfig;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordFragment extends Fragment {
    public static FragmentActivity activity;
    public static ForgetPasswordFragment fragment;

    @BindView(R.id.fragment_forget_password_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_forget_password_et_email)
    EditText email;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static ForgetPasswordFragment newInstance(FragmentActivity activity) {
        fragment = new ForgetPasswordFragment();
        ForgetPasswordFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_forget_password, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.appbar.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        FixControl.setupUI(container,activity);
    }

    @OnClick(R.id.fragment_forget_password_btn_send)
    public void sendClick() {
        String emailStr = email.getText().toString();
        if(emailStr == null || emailStr.isEmpty()){
            Snackbar.make(loading,getString(R.string.enterEmail), Snackbar.LENGTH_SHORT).show();
        }
        else{
            if (GlobalFunctions.isNetworkConnected(activity)) {
                forgetPasswordApi(emailStr);
            } else {
                Snackbar.make(loading, getString(R.string.checkInternet), Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    private void forgetPasswordApi(String email) {
        loading.setVisibility(View.VISIBLE);
        RetrofitConfig.getServices(activity).FORGET_PASSWORD_RESPONSE_CALL(email)
                .enqueue(
                        new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {
                                    Snackbar.make(loading,getString(R.string.checkYourEmail), Snackbar.LENGTH_SHORT).show();
                                    Navigator.loadFragment(activity, LoginFragment.newInstance(activity), R.id.activity_main_fl_container, false);
                                }
                                loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        }
                );
    }
}


