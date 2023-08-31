package com.sbit.qr_vehicle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api_list {

    @FormUrlEncoded
    @POST("user_register_api.php")
    Call<Registration_pojo> registrationData(
            @Field("name") String name,
            @Field("address") String address,
            @Field("mobile") String mobile,
            @Field("email") String email,
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
    @POST("user_payment_api.php")
    Call<Amount_Response> insertPaymentResponseStud(
            @Field("id") String sid,
            @Field("reg_mob") String reg_mob,
            @Field("order_id") String order_id,
            @Field("vehicle_number") String customer_type,
            @Field("amount") String amount,
            @Field("gateway_payment_id") String gateway_payment_id,
            @Field("payment_status") String payment_status
    );

    @GET("subscription_api.php")
    Call<Allvehicle_price_model> getAllPrices();

    @FormUrlEncoded
    @POST("user_edit_api.php")
    Call<DetailModal> updatedetails(
            @Field("mobile") String mobile,
            @Field("name") String name,
            @Field("email") String email,
            @Field("address") String add,
            @Field("vehicle_number") String vehi_num,
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
