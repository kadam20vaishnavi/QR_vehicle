package com.sbit.qr_vehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class Allvehicle_price_model {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("response")
    @Expose
    private String response;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public static Allvehicle_price_model fromJson(JSONObject jsonObject) throws JSONException {
        Allvehicle_price_model data = new Allvehicle_price_model();

        // Parse the JSON object and populate the fields
        data.setName(jsonObject.getString("name"));
        data.setPrice(jsonObject.getString("price"));
        data.setResponse(jsonObject.getString("response"));
        // Set other fields similarly

        return data;
    }
}
