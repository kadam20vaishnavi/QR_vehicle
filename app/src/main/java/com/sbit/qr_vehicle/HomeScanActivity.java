package com.sbit.qr_vehicle;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class HomeScanActivity extends AppCompatActivity {

    Button getstarted;
    Button scan, login;
    String mobilenumb, devicemobile = "", devicemobile2 = "";
    TextView howappword,benefit;
    ArrayList<String> mobilenumber = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_scan);

        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + "QR Vehicle" + "</font>")));
        getstarted = findViewById(R.id.get_started);
        scan = findViewById(R.id.scan_code);
        login = findViewById(R.id.login);
        howappword = findViewById(R.id.howappwork);
        benefit = findViewById(R.id.benefit);
        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent1);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                int j = sharedPreferences.getInt("key", 0);

                Log.e("J", String.valueOf(j));
                if (j > 0) {

                    String Reg_mob = sharedPreferences.getString("mobile_number", "");
                    String vehicalnum = sharedPreferences.getString("vehicle_number", "");

                    Intent i = new Intent(HomeScanActivity.this, Dashboard_Activity.class);
                    i.putExtra("mobile", Reg_mob);
                    i.putExtra("veh_num", vehicalnum);
                    startActivity(i);

                } else {
                    Intent intent = new Intent(HomeScanActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

//        SpannableString content = new SpannableString("How It Works?");
//        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//
//        howappword.setText(content);

        howappword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(HomeScanActivity.this);
                dialog.setContentView(R.layout.app_functionality);
                // if button is clicked, close the custom dialog
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            }
        });

//        SpannableString content1 = new SpannableString("Benefits of App");
////        content1.setSpan(new UnderlineSpan(), 0, content.length(), 0);
////
////        benefit.setText(content1);
        benefit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(HomeScanActivity.this);
                dialog.setContentView(R.layout.app_benifits);
                // if button is clicked, close the custom dialog
                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    IntentIntegrator integrator = new IntentIntegrator(HomeScanActivity.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                    integrator.setPrompt("Scan QR");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(true);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();

                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(HomeScanActivity.this, "Error While Scan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getPhone() {

        if (ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            SubscriptionManager subscriptionManager = SubscriptionManager.from(getApplicationContext());
            List<SubscriptionInfo> subsInfoList = subscriptionManager.getActiveSubscriptionInfoList();

            Log.d("Test", "Current list = " + subsInfoList);
            if(subsInfoList.size()==2) {
                mobilenumber.clear();
                for (SubscriptionInfo subscriptionInfo : subsInfoList) {

                    String number = subscriptionInfo.getNumber();
                    mobilenumber.add(number);

                    Log.d("Test", " 2Number is  "+number);
                }
            }else{
                for (SubscriptionInfo subscriptionInfo : subsInfoList) {

                    devicemobile = subscriptionInfo.getNumber();

                    Log.d("Test", " 1Number is  " + devicemobile);
                }
            }
            Log.e("List size:", ""+mobilenumber.size());
            return;

        } else {
            // Ask for permission
            requestPermission();
        }
        Log.e("Mobile Number:",devicemobile);
    }

    private void requestPermission() {
        requestPermissions(new String[]{ READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if ( ActivityCompat.checkSelfPermission(this,
                        READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();
                devicemobile=mPhoneNumber;
                Log.e("Permission Mobile:",devicemobile);
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        getPhone();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPhone();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPhone();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String[] splitStr=null;
        String SID="null";
        System.out.println("Request Code : " + requestCode + " resultCode : " + " date : " + data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You Cancelled the scanning", Toast.LENGTH_SHORT).show();
            } else {
                String str = result.getContents();
                splitStr = str.split("\\s+");

                mobilenumb = splitStr[0];
                Log.e("Vehicle Number:",""+mobilenumb);
                Intent intent1 = new Intent(getApplicationContext(), ScanDataActivity.class);
                intent1.putExtra("device_num",devicemobile);
                intent1.putStringArrayListExtra("numberlist",mobilenumber);
                intent1.putExtra("veh_num",mobilenumb);
                startActivity(intent1);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(HomeScanActivity.this, "Error While Scan", Toast.LENGTH_SHORT).show();
        }
    }
}