package app.atelier.vendor.views.fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import app.atelier.vendor.R;
import app.atelier.vendor.adapters.ProductsAdapter;
import app.atelier.vendor.classes.FixControl;
import app.atelier.vendor.classes.GlobalFunctions;
import app.atelier.vendor.views.activities.MainActivity;
import app.atelier.vendor.webService.webModels.Product;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailsFragment extends Fragment {
    public static FragmentActivity activity;
    public static ProductDetailsFragment fragment;

    Product product;
    int id;

    @BindView(R.id.fragment_product_details_iv_productImg)
    ImageView productImg;
    @BindView(R.id.fragment_product_details_tv_name)
    TextView name;
    @BindView(R.id.fragment_product_details_tv_description)
    TextView description;
    @BindView(R.id.fragment_product_details_tv_quantity)
    TextView quantity;
    @BindView(R.id.fragment_product_details_tv_price)
    TextView price;
    @BindView(R.id.fragment_product_details_tv_details)
    TextView details;

    public static ProductDetailsFragment newInstance(FragmentActivity activity, int id, Product product) {
        fragment = new ProductDetailsFragment();
        ProductDetailsFragment.activity = activity;
        fragment.product = product;
        fragment.id = id;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.appbar.setVisibility(View.VISIBLE);
        MainActivity.filter.setVisibility(View.GONE);
        MainActivity.search.setVisibility(View.GONE);
        MainActivity.back.setVisibility(View.VISIBLE);
        MainActivity.all.setVisibility(View.GONE);
        MainActivity.title.setText(getString(R.string.orderName) + " " + id);
        setData();
    }


    private void setData() {

        int Width = FixControl.getImageWidth(activity, R.mipmap.placeholder_pro_details);
        int Height = FixControl.getImageHeight(activity, R.mipmap.placeholder_pro_details);
        productImg.getLayoutParams().height = Height;
        productImg.getLayoutParams().width = Width;
        if (product.productDetails.images.get(0).src != null
                && !product.productDetails.images.get(0).src.matches("")
                && !product.productDetails.images.get(0).src.isEmpty()) {
            Glide.with(activity).load(product.productDetails.images.get(0).src)
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.placeholder_pro_details))
                    .into(productImg);

            price.setText(product.productDetails.formattedPrice);
            quantity.setText(product.quantity + getString(R.string.pieces));
            name.setText(product.productDetails.localizedNames.get(0).localizedName);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                description.setText(Html.fromHtml(product.productDetails.Descriptions.get(0).description,Html.FROM_HTML_MODE_LEGACY));
            } else {
                description.setText(Html.fromHtml(product.productDetails.Descriptions.get(0).description));
            }
            details.setText(product.attributesDescription.replace("<br />","\n"));
        }
    }
}


