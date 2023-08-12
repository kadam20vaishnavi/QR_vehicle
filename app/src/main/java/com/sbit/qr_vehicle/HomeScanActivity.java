package com.sbit.qr_vehicle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeScanActivity extends AppCompatActivity {

    Button getstarted,scan;
    String mobilenumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_scan);
        getstarted=findViewById(R.id.get_started);
        scan=findViewById(R.id.scan_code);

        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(HomeScanActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan QR");
                integrator.setOrientationLocked(true);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
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
                Log.e("mobile:",""+mobilenumb);
                Intent intent1 = new Intent(getApplicationContext(), ScanDataActivity.class);
                intent1.putExtra("veh_num",mobilenumb);
                startActivity(intent1);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}