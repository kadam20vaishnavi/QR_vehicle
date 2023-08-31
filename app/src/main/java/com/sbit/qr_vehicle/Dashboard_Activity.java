package com.sbit.qr_vehicle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Dashboard_Activity extends AppCompatActivity {

    CardView viewdetail,viewqrcode;
    AlertDialog.Builder builder;
    String Reg_mob,vehicalnum;

    TextView ownername,logout;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + "Dashboard" + "</font>")));
        viewdetail = findViewById(R.id.viewdetails);
//        ownername = findViewById(R.id.ownername);
        logout = findViewById(R.id.logout);
        viewqrcode = findViewById(R.id.qrcode);

        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedpreferences.getInt("key", 0);
        Log.e("J", String.valueOf(j));

        if(j > 0){

            Reg_mob=sharedpreferences.getString("mobile_number","");
            vehicalnum=sharedpreferences.getString("vehicle_number","");

        }else{
            Intent intent = getIntent();    //print current registered user mobile to editext
            Reg_mob = intent.getStringExtra("mobile");
            vehicalnum = intent.getStringExtra("veh_num");
        }

        viewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard_Activity.this, Details_Activity.class);
                i.putExtra("mobile",Reg_mob);
                i.putExtra("veh_num",vehicalnum);
                startActivity(i);
            }
        });

        viewqrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard_Activity.this, QrCodeActivity.class);
                i.putExtra("mobile",Reg_mob);
                i.putExtra("veh_num",vehicalnum);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
    }

    void Logout(){
        builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

        builder.setMessage("Do you want Log Out ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("key", 0);
                        editor.apply();

                        Intent intent=new Intent(getApplicationContext(),HomeScanActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Exit");
        alert.show();
    }
    @Override
    public void onBackPressed() {
        builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

        builder.setMessage("Do you want to Exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent=new Intent(getApplicationContext(),HomeScanActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Exit");
        alert.show();
    }


}