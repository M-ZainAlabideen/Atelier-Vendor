// Generated code from Butter Knife. Do not modify!
package app.atelier.vendor.views.fragments;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import app.atelier.vendor.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrdersFragment_ViewBinding implements Unbinder {
  private OrdersFragment target;

  private View view7f09009d;

  private View view7f09009e;

  private View view7f09009b;

  @UiThread
  public OrdersFragment_ViewBinding(final OrdersFragment target, View source) {
    this.target = target;

    View view;
    target.container = Utils.findRequiredViewAsType(source, R.id.fragment_orders_cl_container, "field 'container'", ConstraintLayout.class);
    target.total = Utils.findRequiredViewAsType(source, R.id.fragment_orders_tv_total, "field 'total'", TextView.class);
    view = Utils.findRequiredView(source, R.id.fragment_orders_tv_fromDate, "field 'fromDate' and method 'pickStartDate'");
    target.fromDate = Utils.castView(view, R.id.fragment_orders_tv_fromDate, "field 'fromDate'", TextView.class);
    view7f09009d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pickStartDate();
      }
    });
    view = Utils.findRequiredView(source, R.id.fragment_orders_tv_toDate, "field 'toDate' and method 'pickEndDate'");
    target.toDate = Utils.castView(view, R.id.fragment_orders_tv_toDate, "field 'toDate'", TextView.class);
    view7f09009e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pickEndDate();
      }
    });
    target.orders = Utils.findRequiredViewAsType(source, R.id.fragment_orders_rv_orders, "field 'orders'", RecyclerView.class);
    target.loading = Utils.findRequiredViewAsType(source, R.id.loading, "field 'loading'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.fragment_orders_iv_dateFilter, "method 'dateFilterClick'");
    view7f09009b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.dateFilterClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    OrdersFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.container = null;
    target.total = null;
    target.fromDate = null;
    target.toDate = null;
    target.orders = null;
    target.loading = null;

    view7f09009d.setOnClickListener(null);
    view7f09009d = null;
    view7f09009e.setOnClickListener(null);
    view7f09009e = null;
    view7f09009b.setOnClickListener(null);
    view7f09009b = null;
  }
}
