package com.sbit.qr_vehicle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeActivity extends AppCompatActivity {

    ImageView qrview;
    Button download;
    String Reg_mob,vehicle_num;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        qrview=findViewById(R.id.qrview);
        download=findViewById(R.id.qrcodedownload);

        Intent intent = getIntent();    //print current registered user mobile to editext
        Reg_mob = intent.getStringExtra("mobile");
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
//                Glide.with(ViewQrCodeActivity.this).load(""+detailModal.getQrcode()).into(qrview);

                pd.dismiss();
            }

            @Override
            public void onFailure(Call<DetailModal> call1, Throwable t) {
                System.out.println("error :" + t.getMessage());
                Toast.makeText(QrCodeActivity.this, "Something went wrong! try again later", Toast.LENGTH_LONG).show();
                pd.dismiss();

            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    download_card();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void download_card() throws FileNotFoundException, DocumentException {

        pd = new ProgressDialog(QrCodeActivity.this);
        pd.setTitle("Downloading Card!");
        pd.setMessage("Please Wait..");
        pd.show();

        try {
            ImageView content = findViewById(R.id.qrview);
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