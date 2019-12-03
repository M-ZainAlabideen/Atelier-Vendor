// Generated code from Butter Knife. Do not modify!
package app.atelier.vendor.views.fragments;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import app.atelier.vendor.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginFragment_ViewBinding implements Unbinder {
  private LoginFragment target;

  private View view7f090092;

  private View view7f090097;

  private View view7f090096;

  @UiThread
  public LoginFragment_ViewBinding(final LoginFragment target, View source) {
    this.target = target;

    View view;
    target.container = Utils.findRequiredViewAsType(source, R.id.fragment_login_cl_container, "field 'container'", ConstraintLayout.class);
    target.password = Utils.findRequiredViewAsType(source, R.id.fragment_login_et_password, "field 'password'", EditText.class);
    target.userName = Utils.findRequiredViewAsType(source, R.id.fragment_login_et_userName, "field 'userName'", EditText.class);
    target.loading = Utils.findRequiredViewAsType(source, R.id.loading, "field 'loading'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.fragment_login_btn_login, "method 'loginCLick'");
    view7f090092 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.loginCLick();
      }
    });
    view = Utils.findRequiredView(source, R.id.fragment_login_tv_forgetPass, "method 'forgetPassClick'");
    view7f090097 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.forgetPassClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.fragment_login_tv_changeLang, "method 'changeLangClick'");
    view7f090096 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.changeLangClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.container = null;
    target.password = null;
    target.userName = null;
    target.loading = null;

    view7f090092.setOnClickListener(null);
    view7f090092 = null;
    view7f090097.setOnClickListener(null);
    view7f090097 = null;
    view7f090096.setOnClickListener(null);
    view7f090096 = null;
  }
}
