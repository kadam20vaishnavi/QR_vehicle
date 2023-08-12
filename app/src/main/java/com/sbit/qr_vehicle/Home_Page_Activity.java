package com.sbit.qr_vehicle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home_Page_Activity extends AppCompatActivity {

    CardView viewdetail,viewqrcode;
    AlertDialog.Builder builder;
    String Reg_mob,vehicalnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        viewdetail = findViewById(R.id.viewdetails);

        viewqrcode = findViewById(R.id.qrcode);

        Intent intent = getIntent();    //print current registered user mobile to editext
        Reg_mob = intent.getStringExtra("mobile");
        vehicalnum = intent.getStringExtra("veh_num");

        viewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_Page_Activity.this, Home_Activity.class);
                i.putExtra("mobile",Reg_mob);
                i.putExtra("veh_num",vehicalnum);
                startActivity(i);
            }
        });

        viewqrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_Page_Activity.this, QrCodeActivity.class);
                i.putExtra("mobile",Reg_mob);
                i.putExtra("veh_num",vehicalnum);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

        builder.setMessage("Do you want Log Out ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
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