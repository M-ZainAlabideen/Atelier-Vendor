// Generated code from Butter Knife. Do not modify!
package app.atelier.vendor.views.fragments;

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

public class ProductDetailsFragment_ViewBinding implements Unbinder {
  private ProductDetailsFragment target;

  @UiThread
  public ProductDetailsFragment_ViewBinding(ProductDetailsFragment target, View source) {
    this.target = target;

    target.productImg = Utils.findRequiredViewAsType(source, R.id.fragment_product_details_iv_productImg, "field 'productImg'", ImageView.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.fragment_product_details_tv_name, "field 'name'", TextView.class);
    target.description = Utils.findRequiredViewAsType(source, R.id.fragment_product_details_tv_description, "field 'description'", TextView.class);
    target.quantity = Utils.findRequiredViewAsType(source, R.id.fragment_product_details_tv_quantity, "field 'quantity'", TextView.class);
    target.price = Utils.findRequiredViewAsType(source, R.id.fragment_product_details_tv_price, "field 'price'", TextView.class);
    target.details = Utils.findRequiredViewAsType(source, R.id.fragment_product_details_tv_details, "field 'details'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProductDetailsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.productImg = null;
    target.name = null;
    target.description = null;
    target.quantity = null;
    target.price = null;
    target.details = null;
  }
}
