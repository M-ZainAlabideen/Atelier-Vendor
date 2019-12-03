package app.atelier.vendor.webService.webModels;

import com.google.gson.annotations.SerializedName;

public class Customer {
    @SerializedName("vendor_id")
    public int vendorId;

    @SerializedName("id")
    public int id;

    @SerializedName("language_id")
    public String languageId;

    @SerializedName("email")
    public String email;
}
