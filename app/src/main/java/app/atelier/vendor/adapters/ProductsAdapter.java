package app.atelier.vendor.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;

import app.atelier.vendor.R;
import app.atelier.vendor.classes.FixControl;
import app.atelier.vendor.classes.GlobalFunctions;
import app.atelier.vendor.classes.Navigator;
import app.atelier.vendor.classes.SessionManager;
import app.atelier.vendor.views.fragments.ProductDetailsFragment;
import app.atelier.vendor.webService.webModels.Product;
import app.atelier.vendor.webService.webModels.Product;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.viewHolder> {
    private Context context;
    private ArrayList<Product> productsList;
    private SessionManager sessionManager;
    private int orderId;

    public ProductsAdapter(Context context, ArrayList<Product> productsList,int orderId) {
        this.context = context;
        this.productsList = productsList;
        sessionManager = new SessionManager(context);
        this.orderId = orderId;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_product_iv_productImg)
        ImageView productImg;
        @BindView(R.id.item_product_tv_date)
        TextView date;
        @BindView(R.id.item_product_tv_title)
        TextView title;
        @BindView(R.id.item_product_tv_price)
        TextView price;
        @BindView(R.id.item_product_tv_quantity)
        TextView quantity;
        @BindView(R.id.item_product_iv_arrow)
        View arrow;
        @BindView(R.id.item_product_v_details)
        View details;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ProductsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_product, viewGroup, false);
        return new ProductsAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.viewHolder viewHolder, final int position) {


        int Width = FixControl.getImageWidth(context, R.mipmap.placeholder_product);
        int Height = FixControl.getImageHeight(context, R.mipmap.placeholder_product);
        viewHolder.productImg.getLayoutParams().height = Height;
        viewHolder.productImg.getLayoutParams().width = Width;
        if (productsList.get(position).productDetails.images.get(0).src != null
                && !productsList.get(position).productDetails.images.get(0).src.matches("")
                && !productsList.get(position).productDetails.images.get(0).src.isEmpty()) {
            Glide.with(context.getApplicationContext()).load(productsList.get(position).productDetails.images.get(0).src)
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.placeholder_product))
                    .into(viewHolder.productImg);

            viewHolder.date.setText(GlobalFunctions.formatDate(productsList.get(position).productDetails.createdDateUTC));
            viewHolder.price.setText(productsList.get(position).productDetails.formattedPrice);
            viewHolder.quantity.setText(productsList.get(position).quantity + context.getString(R.string.pieces));
            String name = productsList.get(position).productDetails.localizedNames.get(0).localizedName;
            if (name.length() > 25)
                viewHolder.title.setText(name.substring(0, 24) + "...");
            else
                viewHolder.title.setText(name);

            if (sessionManager.getLanguage().equals("en")) {
                viewHolder.arrow.setRotation(180);
            }

            viewHolder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigator.loadFragment((FragmentActivity) context, ProductDetailsFragment.newInstance((FragmentActivity) context,orderId,productsList.get(position)),R.id.activity_main_fl_container,true);
                }
            });

        }
    }
    @Override
    public int getItemCount() {
        return productsList.size();
    }
}





