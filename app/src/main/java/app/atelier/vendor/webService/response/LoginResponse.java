package app.atelier.vendor.webService.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.atelier.vendor.webService.webModels.Customer;

public class LoginResponse {
    @SerializedName("customers")
    public ArrayList<Customer> VendorUsers;
}
