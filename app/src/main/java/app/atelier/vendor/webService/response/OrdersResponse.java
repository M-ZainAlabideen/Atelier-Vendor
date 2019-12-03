package app.atelier.vendor.webService.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.atelier.vendor.webService.webModels.Order;
import app.atelier.vendor.webService.webModels.OrdersTotal;

public class OrdersResponse {
    @SerializedName("orders")
    public ArrayList<Order> orders;

    @SerializedName("ordersTotals")
    public OrdersTotal total;
}
