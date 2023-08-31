package com.sbit.qr_vehicle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanDataActivity extends AppCompatActivity {

    String Reg_mob="",vehicalno;
    TextView name,email,address;
    TextView ldate,rdate,idate,pucdate,scanvehinum;
    TextView mobile,vehicaltype,warningmesg;
    ImageView licenceimage,vehicleimg,registrationimg,pucimg,insuranceimg;
    ProgressDialog pd;
    String Num1="",num2="",num3="";
    ArrayList<String> numberdata;
    LinearLayout imagelayout,datelayout,addresslayout,emaillayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_data);

        name = findViewById(R.id.suser_name);
        mobile = findViewById(R.id.suser_mobile);
        email = findViewById(R.id.suser_email);
        address = findViewById(R.id.suser_addres);
        vehicaltype = findViewById(R.id.svehicaltypeinst);
        scanvehinum = findViewById(R.id.scanvehnumber);

        ldate = findViewById(R.id.slicenceexpdate);
        rdate = findViewById(R.id.sregisterexpdate);
        idate = findViewById(R.id.sinsuranceexpdate);
        pucdate = findViewById(R.id.spucexpdate);

        warningmesg = findViewById(R.id.warningmesg);

        imagelayout = findViewById(R.id.imagelayout);
        datelayout = findViewById(R.id.datelayout);
        addresslayout = findViewById(R.id.addresslayout);
        emaillayout = findViewById(R.id.emaillayout);

        licenceimage = findViewById(R.id.slicenceimage);
        vehicleimg = findViewById(R.id.svehicalcopy);
        registrationimg = findViewById(R.id.sregistercopy);
        pucimg = findViewById(R.id.spuccopy);
        insuranceimg = findViewById(R.id.sinsurancecopy);

        Intent intent = getIntent();    // print current registered user mobile to editext
        Reg_mob = intent.getStringExtra("device_num");
        numberdata = intent.getStringArrayListExtra("numberlist");
        vehicalno = intent.getStringExtra("veh_num");

        try {
            if (numberdata.size() != 0) {

                Log.e("List Element1:", numberdata.get(0));
                if(numberdata.get(0)!=null && !Objects.equals(numberdata.get(0), "")) {
                    num2 = numberdata.get(0);
                    String Reg_mob = num2.replaceAll(" ", "");
                    num2 = Reg_mob.substring(Reg_mob.length() - 10);//change number 10 according to the preferred number of digits of Phone Number Excluding country code
                    System.out.println("Number Without Country Code : " + num2);
                }else {
                    Toast.makeText(ScanDataActivity.this, "First number not detected", Toast.LENGTH_SHORT).show();
                }

                Log.e("List Element2:", numberdata.get(1));
                if(numberdata.get(1)!=null && !Objects.equals(numberdata.get(1), "")) {
                    num3 = numberdata.get(1);
                    String Reg_mob1 = num3.replaceAll(" ", "");
                    num3 = Reg_mob1.substring(Reg_mob1.length() - 10);//change number 10 according to the preferred number of digits of Phone Number Excluding country code
                    System.out.println("Number Without Country Code : " + num3);
                }else{
                    Toast.makeText(ScanDataActivity.this, "Second Phone Not Found", Toast.LENGTH_SHORT).show();
                }

            } else if (Reg_mob != null) {

                Reg_mob = Reg_mob.replaceAll(" ", "");

                Num1 = Reg_mob.substring(Reg_mob.length() - 10);//change number 10 according to the preferred number of digits of Phone Number Excluding country code
                System.out.println("Number Without Country Code : " + Num1);

                Log.e("mobile//:", "" + Reg_mob);
            }else{
                Toast.makeText(ScanDataActivity.this, "mobile number not found", Toast.LENGTH_SHORT).show();

            }
        }catch(Exception e){
            Toast.makeText(ScanDataActivity.this, "Error while cheking mobile number", Toast.LENGTH_SHORT).show();
        }
            try{

            if (vehicalno != null) {

                Api_list Ar = Api.getClient().create(Api_list.class);

                Call<DetailModal> call = Ar.getdetails(vehicalno);

                pd = new ProgressDialog(ScanDataActivity.this);
                pd.setTitle("Checking Your Data!");
                pd.setMessage("Please Wait..");
                pd.show();

                call.enqueue(new Callback<DetailModal>() {

                    @Override
                    public void onResponse(Call<DetailModal> call, Response<DetailModal> response) {

                        if (response.body() != null) {

                            DetailModal detailModal = response.body();
                            System.out.println("Response:" + detailModal.getResponse());
                            if (Num1 != null || (num2!=null && num3!=null)) {
                                Log.e("Num1=", Num1);
                                Log.e("Num2=", num2);
                                Log.e("Num3=", num3);
                                Log.e("Number2=", detailModal.getMobile());

                                if (Num1.equals(detailModal.getMobile())||num2.equals(detailModal.getMobile())||num3.equals(detailModal.getMobile())) {

                                    name.setText(detailModal.getName());
                                    mobile.setText(detailModal.getMobile());
                                    email.setText(detailModal.getEmail());
                                    address.setText(detailModal.getAddress());

                                    ldate.setText(detailModal.getDrivingLicenceExpDate());
                                    rdate.setText(detailModal.getVehicalRegDate());
                                    idate.setText(detailModal.getInsuranceExpDate());
                                    pucdate.setText(detailModal.getPucExpDate());

                                    vehicaltype.setText(detailModal.getVehicleType());
                                    scanvehinum.setText(detailModal.getVehicleNumber());

                                    try {
                                        String base64String = "data:image/jpg;base64," + detailModal.getDrivingLicenceCopy();
                                        final String pureBase64Encoded = base64String.split(",")[1];
                                        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                                        licenceimage.setImageBitmap(decodedBitmap);

                                        String base64String1 = "data:image/jpg;base64," + detailModal.getVehicleregcopy();
                                        final String pureBase64Encoded1 = base64String1.split(",")[1];
                                        final byte[] decodedBytes1 = Base64.decode(pureBase64Encoded1, Base64.DEFAULT);
                                        Bitmap decodedBitmap1 = BitmapFactory.decodeByteArray(decodedBytes1, 0, decodedBytes1.length);
                                        registrationimg.setImageBitmap(decodedBitmap1);

                                        String base64String2 = "data:image/jpg;base64," + detailModal.getVehicalCopy();
                                        final String pureBase64Encoded2 = base64String2.split(",")[1];
                                        final byte[] decodedBytes2 = Base64.decode(pureBase64Encoded2, Base64.DEFAULT);
                                        Bitmap decodedBitmap2 = BitmapFactory.decodeByteArray(decodedBytes2, 0, decodedBytes2.length);
                                        vehicleimg.setImageBitmap(decodedBitmap2);

                                        String base64String3 = "data:image/jpg;base64," + detailModal.getPuccopy();
                                        final String pureBase64Encoded3 = base64String3.split(",")[1];
                                        final byte[] decodedBytes3 = Base64.decode(pureBase64Encoded3, Base64.DEFAULT);
                                        Bitmap decodedBitmap3 = BitmapFactory.decodeByteArray(decodedBytes3, 0, decodedBytes3.length);
                                        pucimg.setImageBitmap(decodedBitmap3);

                                        String base64String4 = "data:image/jpg;base64," + detailModal.getInsurancecopy();
                                        final String pureBase64Encoded4 = base64String4.split(",")[1];
                                        final byte[] decodedBytes4 = Base64.decode(pureBase64Encoded4, Base64.DEFAULT);
                                        Bitmap decodedBitmap4 = BitmapFactory.decodeByteArray(decodedBytes4, 0, decodedBytes4.length);
                                        insuranceimg.setImageBitmap(decodedBitmap4);

                                    } catch (Exception e) {
                                        Toast.makeText(ScanDataActivity.this, "Image Not Found", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(ScanDataActivity.this, "Mobile Not Matched To Data", Toast.LENGTH_SHORT).show();
                                    name.setText(detailModal.getName());
                                    mobile.setText(detailModal.getMobile());
                                    vehicaltype.setText(detailModal.getVehicleType());
                                    scanvehinum.setText(detailModal.getVehicleNumber());

                                    emaillayout.setVisibility(View.GONE);
                                    imagelayout.setVisibility(View.GONE);
                                    datelayout.setVisibility(View.GONE);
                                    addresslayout.setVisibility(View.GONE);
                                    warningmesg.setVisibility(View.VISIBLE);
                                }
                            } else {

                                name.setText(detailModal.getName());
                                mobile.setText(detailModal.getMobile());
                                email.setText(detailModal.getEmail());

                                mobile.setText(detailModal.getMobile());
                                vehicaltype.setText(detailModal.getVehicleType());
                                scanvehinum.setText(detailModal.getVehicleNumber());

                                imagelayout.setVisibility(View.GONE);
                                datelayout.setVisibility(View.GONE);
                                addresslayout.setVisibility(View.GONE);
                                warningmesg.setVisibility(View.VISIBLE);

                                Toast.makeText(ScanDataActivity.this, "Mobile Not Found", Toast.LENGTH_SHORT).show();
                            }

                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailModal> call1, Throwable t) {
                        System.out.println("error :" + t.getMessage());
                        Toast.makeText(ScanDataActivity.this, "Something went wrong! try again later", Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                });
            } else {
                Toast.makeText(ScanDataActivity.this, "Vehicle Number Not Found", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(ScanDataActivity.this, "There is been Error While get Result", Toast.LENGTH_SHORT).show();
        }
    }
}