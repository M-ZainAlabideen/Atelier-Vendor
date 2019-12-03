// Generated code from Butter Knife. Do not modify!
package app.atelier.vendor.views.fragments;

import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import app.atelier.vendor.R;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrderProductsFragment_ViewBinding implements Unbinder {
  private OrderProductsFragment target;

  private View view7f090098;

  @UiThread
  public OrderProductsFragment_ViewBinding(final OrderProductsFragment target, View source) {
    this.target = target;

    View view;
    target.products = Utils.findRequiredViewAsType(source, R.id.fragment_order_products_rv_products, "field 'products'", RecyclerView.class);
    target.loading = Utils.findRequiredViewAsType(source, R.id.loading, "field 'loading'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.fragment_order_products_btn_pdfInvoice, "method 'pdfInvoice'");
    view7f090098 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pdfInvoice();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    OrderProductsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.products = null;
    target.loading = null;

    view7f090098.setOnClickListener(null);
    view7f090098 = null;
  }
}
