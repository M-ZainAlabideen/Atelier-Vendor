package app.atelier.vendor.webService.webModels;

import com.google.gson.annotations.SerializedName;

import app.atelier.vendor.views.activities.MainActivity;

public class Status {
    @SerializedName("StatusAr")
    private String StatusAr;

    @SerializedName("StatusEn")
    private String StatusEn;

    @SerializedName("id")
    public int statusId;

    public String getStatus() {
        if (MainActivity.isEnglish) {
            return StatusEn;
        } else {
            return StatusAr;
        }
    }
}
