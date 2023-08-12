package com.sbit.qr_vehicle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api_list {
    @FormUrlEncoded
    @POST("user_register_api.php")
    Call<Registration_pojo> registrationData(
            @Field("name") String name,
            @Field("address") String address,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("otp") String otp,
            @Field("vehicle_type") String vt,
            @Field("vehicle_number") String vnum,
            @Field("vehicle_copy") String vehimg,
            @Field("driving_licence_exp_date") String dl_edate,
            @Field("driving_licence_copy") String dlcopy,
            @Field("puc_exp_date") String puc_edate,
            @Field("puc_copy") String puccopy,
            @Field("insurance_exp_date") String ins_edate,
            @Field("insurance_copy") String inscopy,
            @Field("vehical_reg_date") String reg_edate,
            @Field("vehical_reg_copy") String regcopy,
            @Field("qrcode") String Qrcode
    );

    @FormUrlEncoded
    @POST("user_login_api.php")
    Call<Registration_pojo> login(
            @Field("mobile") String mobile,
            @Field("vehicle_number") String vehnum
    );

    @FormUrlEncoded
    @POST("user_details_api.php")
    Call<DetailModal> getdetails(
            @Field("vehiclenumber") String vehicle_number
    );

    @FormUrlEncoded
    @POST("user_edit_api.php")
    Call<DetailModal> updatedetails(
            @Field("mobile") String mobile,
            @Field("name") String name,
            @Field("email") String email,
            @Field("address") String add,
            @Field("driving_licence_exp_date") String dl_edate,
            @Field("puc_exp_date") String puc_edate,
            @Field("insurance_exp_date") String ins_edate,
            @Field("vehical_reg_date") String reg_edate,
            @Field("vehicle_copy") String vehimg,
            @Field("driving_licence_copy") String dlcopy,
            @Field("puc_copy") String puccopy,
            @Field("insurance_copy") String inscopy,
            @Field("vehical_reg_copy") String regcopy
    );
}
