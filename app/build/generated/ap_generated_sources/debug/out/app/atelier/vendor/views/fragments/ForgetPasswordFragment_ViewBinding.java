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

public class ForgetPasswordFragment_ViewBinding implements Unbinder {
  private ForgetPasswordFragment target;

  private View view7f09008f;

  @UiThread
  public ForgetPasswordFragment_ViewBinding(final ForgetPasswordFragment target, View source) {
    this.target = target;

    View view;
    target.container = Utils.findRequiredViewAsType(source, R.id.fragment_forget_password_cl_container, "field 'container'", ConstraintLayout.class);
    target.email = Utils.findRequiredViewAsType(source, R.id.fragment_forget_password_et_email, "field 'email'", EditText.class);
    target.loading = Utils.findRequiredViewAsType(source, R.id.loading, "field 'loading'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.fragment_forget_password_btn_send, "method 'sendClick'");
    view7f09008f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.sendClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ForgetPasswordFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.container = null;
    target.email = null;
    target.loading = null;

    view7f09008f.setOnClickListener(null);
    view7f09008f = null;
  }
}
