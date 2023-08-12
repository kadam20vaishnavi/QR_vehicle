package com.sbit.qr_vehicle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText mobile,vehicleno;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobile =findViewById(R.id.lmobile);
        submit=findViewById(R.id.btnlogin);
        vehicleno=findViewById(R.id.lvehical);

        Intent intent = getIntent();    //print current registered user mobile to editext
        String Reg_mob = intent.getStringExtra("reg_mob");
        String vehicle_no = intent.getStringExtra("veh_num");

    if (Reg_mob != null&&vehicle_no!=null) {
        mobile.setText(Reg_mob);
        vehicleno.setText(vehicle_no);
    }else{
        Toast.makeText(this, "Mobile Not Found!!!", Toast.LENGTH_SHORT).show();
    }

    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            isEmptyField();
            if (isEmptyField() == true) {
                //login asyncTask
                UserLoginTask rt = new UserLoginTask(LoginActivity.this, submit);
                rt.execute(mobile.getText().toString(),vehicleno.getText().toString());

                submit.setEnabled(false);

            }
        }
    });
}

//9184572767
    public boolean isEmptyField() {

        if (mobile.getText().toString().isEmpty() ) {
            Toast.makeText(this, "Fields Are Empty..", Toast.LENGTH_LONG).show();
            return false;

        }
        return true;
    }

}

class UserLoginTask extends AsyncTask<String, Void, String> {
    ProgressDialog pd;
    Context mctx;
    String mobile, veh_num, res,passResponse;
    Button btnlogin;

    private static int LOGIN_TIME_OUT = 1500;

    public UserLoginTask(Context mctx, Button btnlogin) {
        this.mctx = mctx;
        this.btnlogin = btnlogin;
    }

    @Override
    protected String doInBackground(String... data) {
        mobile = data[0];
        veh_num = data[1];

        Api_list Ar = Api.getClient().create(Api_list.class);
        Call<Registration_pojo> call = Ar.login(mobile,veh_num);
        call.enqueue(new Callback<Registration_pojo>() {
            @Override
            public void onResponse(Call<Registration_pojo> call, Response<Registration_pojo> response) {

                Registration_pojo po = response.body();
                System.out.println("///////////////////" + po.getResponse());
                Toast.makeText(mctx, "" + po.getResponse(), Toast.LENGTH_SHORT).show();

                res = po.getResponse();
                passResponse=res;

                if (res.equals("User Login Successfully")) {

                    Intent i = new Intent(mctx, Home_Page_Activity.class);
                    i.putExtra("mobile",mobile);
                    i.putExtra("veh_num",veh_num);
                    mctx.startActivity(i);

                }else{
                    Toast.makeText(mctx, "Error While Login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Registration_pojo> call, Throwable t) {
                System.out.println("Error :" + t.getMessage());
                Toast.makeText(mctx, "Server error", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
        return null;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(mctx);
        pd.setMessage("Please Wait..");
        pd.show();
    }

    @Override
    protected void onPostExecute(String result) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnlogin.setEnabled(true);
                pd.dismiss();
                try {

                    if (passResponse.equals("User Login Successfully")) {
                        System.out.println("Result from doInBackground Method : " + passResponse);
                        ((LoginActivity) mctx).finish();
                    } else {
                        Toast.makeText(mctx, "Error While Login", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, LOGIN_TIME_OUT);

        super.onPostExecute(result);
    }
}