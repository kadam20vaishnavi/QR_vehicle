package com.sbit.qr_vehicle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.print.PrintHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.DocumentException;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class QrCodeActivity extends AppCompatActivity implements PaymentResultListener {

    ImageView qrview;
    Button download,print;
    TextView  payable_amount,vehicle_type;
    String Reg_mob,vehicle_num,orderID,vehicletype;
    ProgressDialog pd;
    Button pay,buyCard;
    CardView payment_stud_linear;
    int amt,noofcard=1,cardrate=30,paytotal=30;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        qrview=findViewById(R.id.qrview);
        download=findViewById(R.id.qrcodedownload);
        print=findViewById(R.id.printqrcode);
        pay=findViewById(R.id.payanddownload);
        payment_stud_linear=findViewById(R.id.payment_stud_linear);
        payable_amount = findViewById(R.id.payable_price);
        vehicle_type = findViewById(R.id.vehicle_type);
        buyCard = findViewById(R.id.studbtn_pay);

        Checkout.preload(getApplicationContext());
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + "QR Code" + "</font>")));

        Intent intent = getIntent();    //print current registered user mobile to editext
        Reg_mob = intent.getStringExtra("device_mobile");
        vehicle_num = intent.getStringExtra("veh_num");
        Log.e("mobile:",""+Reg_mob);

        Api_list Ar = Api.getClient().create(Api_list.class);

        Call<DetailModal> call = Ar.getdetails(vehicle_num);

        pd = new ProgressDialog(QrCodeActivity.this);
        pd.setTitle("Checking Your Data!");
        pd.setMessage("Please Wait..");
        pd.show();

        call.enqueue(new Callback<DetailModal>() {
            @Override
            public void onResponse(Call<DetailModal> call, Response<DetailModal> response) {

                Log.e("response::",""+response.body().getResponse());
                DetailModal detailModal=response.body();
                Log.e("name",""+detailModal.getName());

                String base64String = "data:image/jpg;base64," + detailModal.getQrcode();
                final String pureBase64Encoded = base64String.split(",")[1];
                final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                qrview.setImageBitmap(decodedBitmap);
//              Glide.with(ViewQrCodeActivity.this).load(""+detailModal.getQrcode()).into(qrview);

                id= detailModal.getId();
                Reg_mob =detailModal.getMobile();
                vehicletype=detailModal.getVehicleType();

                Log.e("Mobile get::", Reg_mob);
                Log.e("id get::", id);
                checkifpaymentdone(detailModal);

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<DetailModal> call1, Throwable t) {
                System.out.println("error :" + t.getMessage());
                Toast.makeText(QrCodeActivity.this, "Something went wrong! try again later", Toast.LENGTH_LONG).show();
                pd.dismiss();

            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printPDF();
            }
        });
    }

    private void checkifpaymentdone(DetailModal detailModal) {

        try {
            Log.e("payment staus","check");
            if (detailModal.getPaymentstatus().equals("Yes") || detailModal.getPaymentstatus().equals("yes")) {

                Log.e("payment staus","yes");
//                cardview.setVisibility(View.VISIBLE);
                download.setVisibility(View.VISIBLE);
                print.setVisibility(View.VISIBLE);
                pay.setVisibility(View.GONE);

                payment_stud_linear.setVisibility(View.GONE);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            download_card();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else if (detailModal.getPaymentstatus().equals("No") || detailModal.getPaymentstatus().equals("no")) {

                Log.e("payment staus","No");
                Toast.makeText(QrCodeActivity.this, "Payment yet to be Made", Toast.LENGTH_SHORT).show();
                //Add payment option for student
                download.setVisibility(View.GONE);
                print.setVisibility(View.GONE);
                payment_stud_linear.setVisibility(View.GONE);

                pay.setVisibility(View.VISIBLE);

                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pd = new ProgressDialog(QrCodeActivity.this);
                        pd.setTitle("Processing to Pay!");
                        pd.setMessage("Please Wait..");
                        pd.show();

                        payment_stud_linear.setVisibility(View.VISIBLE);

                        Apiclienthttp apiClient;

                        apiClient = new Apiclienthttp();

                        String apiUrl = "https://qrvehicle.myidcard.org/subscription_api.php";
                        apiClient.makeApiCall(apiUrl, new Apiclienthttp.ApiResponseCallback() {
                            @Override
                            public void onSuccess(String response) {
                                // Handle the successful response here
                                if (response != null) {

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);

                                        Log.e("array size:", String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            Log.i("Response arry:",jsonArray.get(i).toString());
                                            String name = jsonArray.getJSONObject(i).getString("name");
                                            String price = jsonArray.getJSONObject(i).getString("price");

                                            Allvehicle_price_model vehicleResponse = new Allvehicle_price_model();
                                            vehicleResponse.setName(name);
                                            vehicleResponse.setPrice(price);

                                            if (name.equals(vehicletype)) {
                                                vehicle_type.setText(name);
                                                payable_amount.setText(price);
                                            }else{
                                                vehicle_type.setText(vehicletype);
                                                payable_amount.setText(price);
                                            }
                                            Log.e("name", name);
                                        }

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                // Handle the error here
                                Log.e("API Error", e.getMessage());
                            }
                        });

                        pay.setVisibility(View.GONE);
                        showPaymentLayoutValue();

                    }
                });

            } else {
                Toast.makeText(this, "Error While Searching Try Again!", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void showPaymentLayoutValue() {

        pd.dismiss();
        buyCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                pd.setMessage("Please wait...");
                pd.show();

                String text = payable_amount.getText().toString();

//                text = text.substring(1).trim();
//                Log.e("txtamount",""+text);
                int amount = Integer.parseInt(text);
                Log.e("intamount",""+amount);

                orderID = createOrderID();
                pd.dismiss();

                amt = convertRupeeToPaise(amount);
                Log.e("ruppetopaiseamount",""+amt);

                startPayment(amt);

            }
        });
    }

    public String createOrderID() {
        return "App-orderID" + UUID.randomUUID().toString();
    }

    public void startPayment(int amt) {

        System.out.println("Mobile no///" + Reg_mob);
        System.out.println("amount no///" + amt );
        final Activity activity = this;

        final Checkout co = new Checkout();

        co.setImage(R.drawable.fulllogo);
        Checkout.clearUserData(QrCodeActivity.this);
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Sb IT Service");
            options.put("description", "ID Card Charges");

            options.put("image", R.drawable.fulllogo);
            options.put("currency", "INR");
            options.put("amount", amt); //CONVERT AND write in paise

            JSONObject preFill = new JSONObject();
            //preFill.put("email", email);
            preFill.put("Mobile num", Reg_mob);

            options.put("prefill", preFill);
            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public int convertRupeeToPaise(int amount) {
        int amtInPaise = amount * 100;
        return amtInPaise;
    }

    @Override
    public void onPaymentSuccess(final String razorpayPaymentID) {
        /*
         * Add your logic here for a successful payment response
         */
        try {
            pd.setMessage("Updating Data...");
            pd.show();
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();

            String totalamount = "1500";
//            totalamount = totalamount.substring(1).trim();

            orderID = createOrderID();

            System.out.println("//mobile: " + Reg_mob);

            System.out.println("//orderId: " + orderID);

            System.out.println("//Amount: " + totalamount);

            System.out.println("//razorpay: " + razorpayPaymentID);

            //pin_linearlayout1.setVisibility(View.GONE);

            payment_stud_linear.setVisibility(View.GONE);

            //INSERT ALL DATA TO SERVER

            Api_list Ar = Api.getClient().create(Api_list.class);

            Call<Amount_Response> call = Ar.insertPaymentResponseStud(id,Reg_mob,orderID,vehicle_num,totalamount,razorpayPaymentID,"Yes");
            call.enqueue(new Callback<Amount_Response>() {
                @Override
                public void onResponse(Call<Amount_Response> call, Response<Amount_Response> response) {

                    pd.dismiss();
                    System.out.println("server response///////////////////" + response.code());
                    System.out.println("server response///////////////////" + response.isSuccessful());

                    if (response.code() == 500) {
                        System.out.println("Internal server Error");
                        Toast.makeText(QrCodeActivity.this, "Internal Server error, Please try After some time", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        System.out.println("404 Resource not found");
                        Toast.makeText(QrCodeActivity.this, "404 Resource not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Amount_Response po = response.body();
                        String res = po.getResponse();

                        if (res.equals("successful")) {

                            Toast.makeText(QrCodeActivity.this, "Payment success", Toast.LENGTH_SHORT).show();
                            payment_stud_linear.setVisibility(View.GONE);
                            download.setVisibility(View.VISIBLE);
                            print.setVisibility(View.VISIBLE);
                            pay.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Amount_Response> call, Throwable t) {
                    Toast.makeText(QrCodeActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            pd.dismiss();
            Log.e("PIN_Order_Details-", "Exception in onPaymentSuccess", e);
            System.out.println("Exception in onPaymentSuccess///- " + e);
            Toast.makeText(this, "Exception in Payment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_LONG).show();
            System.out.println("onPaymentError///- " + code);

            payment_stud_linear.setVisibility(View.VISIBLE);
            // pin_linearlayout1.setVisibility(View.GONE);

            pd.setMessage("Please Wait...");
            pd.show();

            String toatalamount = payable_amount.getText().toString();
            toatalamount = toatalamount.substring(1).trim();

            orderID = createOrderID();

//            System.out.println("//txt_no_of_cards: " + no_of_card.getText().toString());
            System.out.println("//Amount: " + toatalamount);
            System.out.println("//mobile: " + Reg_mob);

            System.out.println("PAYMENT ID: " + code + " Response: " + response);

            pd.dismiss();
            //INSERT ALL DATA TO SERVER

        } catch (Exception e) {
            Log.e("PIN_Order_Details", "Exception in onPaymentError", e);
            System.out.println("Exception in onPaymentError///- " + e);
            Toast.makeText(QrCodeActivity.this, "Exception in Payment", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }
    }

    private void printPDF() {
        PrintHelper photoPrinter = new PrintHelper(QrCodeActivity.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        //Bitmap bitmap = imageView.getDrawingCache(  );
        CardView content = findViewById(R.id.cardviewqrcode);
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();
        photoPrinter.printBitmap("test print",bitmap);
    }

    public void download_card() throws FileNotFoundException, DocumentException {

        pd = new ProgressDialog(QrCodeActivity.this);
        pd.setTitle("Downloading Card!");
        pd.setMessage("Please Wait..");
        pd.show();

        try {
            CardView content = findViewById(R.id.cardviewqrcode);
            content.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(content.getWidth(),content.getHeight(),Bitmap.Config.ARGB_8888);

            Canvas canvas=new Canvas(bitmap);

            content.draw(canvas);

            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "QRCode" ,Reg_mob );
            Toast.makeText(this,"Downloaded Successfully check Your Gallery!!",Toast.LENGTH_LONG).show();
            pd.dismiss();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}