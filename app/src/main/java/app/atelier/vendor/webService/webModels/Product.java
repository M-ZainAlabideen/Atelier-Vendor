package app.atelier.vendor.webService.webModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Product {

    @SerializedName("product")
    public ProductDetails productDetails;

    @SerializedName("quantity")
    public String quantity;

    @SerializedName("order_total")
    @Expose
    public Double orderTotal;

    @SerializedName("created_on_utc")
    @Expose
    public String createdOnUtc;

    @SerializedName("order_status")
    @Expose
    public String orderStatus;

    @SerializedName("attribute_description")
    public String attributesDescription;

    public class ProductDetails {

        @SerializedName("images")
        public ArrayList<Image> images;


        @SerializedName("localized_names")
        public ArrayList<LocalizedName> localizedNames;

        @SerializedName("localized_full_descriptions")
        public ArrayList<Description> Descriptions;

        @SerializedName("formatted_price")
        public String formattedPrice;

        @SerializedName("created_on_utc")
        public String createdDateUTC;

    }

    public class Image {
        @SerializedName("src")
        public String src;
    }

    public class Description {
        @SerializedName("localized_full_description")
        public String description;
    }

    public class LocalizedName {
        @SerializedName("localized_name")
        public String localizedName;

    }

}
