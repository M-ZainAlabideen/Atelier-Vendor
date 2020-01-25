package app.atelier.vendor.webService.webModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.atelier.vendor.views.activities.MainActivity;

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

    @SerializedName("vendor_statusEn")
    @Expose
    private String vendorStatusEn;

    @SerializedName("vendor_statusAr")
    @Expose
    private String vendorStatusAr;


    @SerializedName("payment_status")
    @Expose
    public String paymentStatus;

    public void setVendorStatus(String vendorStatus) {
        if (MainActivity.isEnglish)
            vendorStatusEn = vendorStatus;
        else
            vendorStatusAr = vendorStatus;
    }

    public String getVendorStatus() {
        if (MainActivity.isEnglish)
            return vendorStatusEn;
        else
            return vendorStatusAr;
    }

}
