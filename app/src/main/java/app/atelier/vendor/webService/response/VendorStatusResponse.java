package app.atelier.vendor.webService.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.atelier.vendor.webService.webModels.Status;

public class VendorStatusResponse {
    @SerializedName("vendorstatus")
    public ArrayList<Status> vendorStatus;
}
