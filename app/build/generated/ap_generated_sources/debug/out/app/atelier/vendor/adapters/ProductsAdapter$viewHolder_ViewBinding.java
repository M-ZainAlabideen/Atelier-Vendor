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

public class ProductsAdapter$viewHolder_ViewBinding implements Unbinder {
  private ProductsAdapter.viewHolder target;

  @UiThread
  public ProductsAdapter$viewHolder_ViewBinding(ProductsAdapter.viewHolder target, View source) {
    this.target = target;

    target.productImg = Utils.findRequiredViewAsType(source, R.id.item_product_iv_productImg, "field 'productImg'", ImageView.class);
    target.date = Utils.findRequiredViewAsType(source, R.id.item_product_tv_date, "field 'date'", TextView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.item_product_tv_title, "field 'title'", TextView.class);
    target.price = Utils.findRequiredViewAsType(source, R.id.item_product_tv_price, "field 'price'", TextView.class);
    target.quantity = Utils.findRequiredViewAsType(source, R.id.item_product_tv_quantity, "field 'quantity'", TextView.class);
    target.arrow = Utils.findRequiredView(source, R.id.item_product_iv_arrow, "field 'arrow'");
    target.details = Utils.findRequiredView(source, R.id.item_product_v_details, "field 'details'");
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductsAdapter.viewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.productImg = null;
    target.date = null;
    target.title = null;
    target.price = null;
    target.quantity = null;
    target.arrow = null;
    target.details = null;
  }
}
