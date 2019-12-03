package app.atelier.vendor.webService.webModels;

import com.google.gson.annotations.SerializedName;

public class OrdersTotal {
    @SerializedName("order_total")
    public String orderTotal;

    @SerializedName("formatted_ordertotal")
    public String formattedOrderTotal;

    @SerializedName("id")
    public int id;
}
