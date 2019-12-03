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

import java.util.ArrayList;

import app.atelier.vendor.R;
import app.atelier.vendor.classes.GlobalFunctions;
import app.atelier.vendor.classes.Navigator;
import app.atelier.vendor.classes.SessionManager;
import app.atelier.vendor.views.fragments.OrderProductsFragment;
import app.atelier.vendor.webService.webModels.Order;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.viewHolder> {
    private Context context;
    private ArrayList<Order> ordersList;
    private SessionManager sessionManager;
    private onItemClickListener listener;
    public OrdersAdapter(Context context, ArrayList<Order> ordersList,onItemClickListener listener) {
        this.context = context;
        this.ordersList = ordersList;
        sessionManager = new SessionManager(context);
        this.listener = listener;
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_order_tv_date)
        TextView date;
        @BindView(R.id.item_order_tv_orderNumber)
        TextView orderNumber;
        @BindView(R.id.item_order_tv_price)
        TextView price;
        @BindView(R.id.item_order_tv_status)
        TextView status;
        @BindView(R.id.item_order_iv_arrow)
        ImageView arrow;
        @BindView(R.id.item_order_v_changeStatus)
        View changeStatus;
        @BindView(R.id.item_order_v_orderDetails)
        View orderDetails;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public OrdersAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_order, viewGroup, false);
        return new OrdersAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.viewHolder viewHolder, final int position) {
        viewHolder.date.setText(GlobalFunctions.formatDate(ordersList.get(position).createdDateUTC));
        viewHolder.price.setText(ordersList.get(position).orderTotal + " " + ordersList.get(position).customerCurrencyCode);
        viewHolder.orderNumber.setText(context.getString(R.string.orderName) + " " + ordersList.get(position).id);
        if(sessionManager.getLanguage().equals("en")){
            viewHolder.arrow.setRotation(180);
        }
        switch (ordersList.get(position).vendorStatusId) {

            case 20:
                viewHolder.status.setText(context.getString(R.string.Processing));
                viewHolder.status.setTextColor(Color.parseColor("#D8A228"));
                viewHolder.status.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_processing,0);
                break;
            case 30:
                viewHolder.status.setText(context.getString(R.string.ready));
                viewHolder.status.setTextColor(Color.parseColor("#4FC12D"));
                viewHolder.status.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_ready,0);
                break;
            case 40:
                viewHolder.status.setText(context.getString(R.string.shipped));
                viewHolder.status.setTextColor(Color.parseColor("#BB2CE6"));
                viewHolder.status.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_shipped,0);
                break;
            case 50:
                viewHolder.status.setText(context.getString(R.string.delivered));
                viewHolder.status.setTextColor(Color.parseColor("#007AFF"));
                viewHolder.status.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_delivered,0);
                break;
            default:
                viewHolder.status.setText(context.getString(R.string.pending));
                viewHolder.status.setTextColor(Color.parseColor("#E62C2C"));
                viewHolder.status.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_pending,0);
                break;
        }

        viewHolder.changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeStatusClick(position);
            }
        });

        viewHolder.orderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Navigator.loadFragment((FragmentActivity) context, OrderProductsFragment.newInstance((FragmentActivity) context,ordersList.get(position).id),R.id.activity_main_fl_container,true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public interface onItemClickListener{
        public void changeStatusClick(int position);
    }
}




