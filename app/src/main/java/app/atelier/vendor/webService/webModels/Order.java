package app.atelier.vendor.webService.webModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Order {

    @SerializedName("vendor_status_id")
    public int vendorStatusId;

    @SerializedName("order_total")
    public String orderTotal;

    @SerializedName("customer_currency_code")
    public String customerCurrencyCode;

    @SerializedName("created_on_utc")
    public String createdDateUTC;

    @SerializedName("id")
    public int id;

    @SerializedName("order_items")
    public ArrayList<Product> orderProducts;

    @SerializedName("order_status")
    @Expose
    public String orderStatus;
    @SerializedName("payment_status")
    @Expose
    public String paymentStatus;
}
