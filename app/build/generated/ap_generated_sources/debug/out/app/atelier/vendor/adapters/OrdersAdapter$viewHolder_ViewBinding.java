// Generated code from Butter Knife. Do not modify!
package app.atelier.vendor.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import app.atelier.vendor.R;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrdersAdapter$viewHolder_ViewBinding implements Unbinder {
  private OrdersAdapter.viewHolder target;

  @UiThread
  public OrdersAdapter$viewHolder_ViewBinding(OrdersAdapter.viewHolder target, View source) {
    this.target = target;

    target.date = Utils.findRequiredViewAsType(source, R.id.item_order_tv_date, "field 'date'", TextView.class);
    target.orderNumber = Utils.findRequiredViewAsType(source, R.id.item_order_tv_orderNumber, "field 'orderNumber'", TextView.class);
    target.price = Utils.findRequiredViewAsType(source, R.id.item_order_tv_price, "field 'price'", TextView.class);
    target.status = Utils.findRequiredViewAsType(source, R.id.item_order_tv_status, "field 'status'", TextView.class);
    target.arrow = Utils.findRequiredViewAsType(source, R.id.item_order_iv_arrow, "field 'arrow'", ImageView.class);
    target.changeStatus = Utils.findRequiredView(source, R.id.item_order_v_changeStatus, "field 'changeStatus'");
    target.orderDetails = Utils.findRequiredView(source, R.id.item_order_v_orderDetails, "field 'orderDetails'");
  }

  @Override
  @CallSuper
  public void unbind() {
    OrdersAdapter.viewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.date = null;
    target.orderNumber = null;
    target.price = null;
    target.status = null;
    target.arrow = null;
    target.changeStatus = null;
    target.orderDetails = null;
  }
}
