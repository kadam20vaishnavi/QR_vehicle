package com.sbit.qr_vehicle;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    TextView licencedate,pucdate,insurancedate,regexpdate,login;
    ImageButton licencecopy,puccopy,inscopy,vehregcopy,vehicleimg;
    ImageView licereg,pucreg,insreg,vehregreg,vehireg,qrcode;
    EditText name,location,email,mobile,vehiclenumber;
    int newWidth = 413,newHeight = 531,num = 0;
    Matrix matrix;
    float scaleHeight,scaleWidth;
    ProgressDialog pd1;
    ByteArrayOutputStream outputStream;
    Bitmap bitmap, bitmap1, bitmap2,bitmap3,bitmap4,resizedBitmap;
    final private int REQUEST_CODE_ASK_PERMISSIONS_EXTERNAL_STORAGE = 200,REQUEST_CODE_ASK_PERMISSIONS_CAMERA = 100;
    Button submit;
    Spinner vehicletype;
    SharedPreferences sharedPreferences;
    String QRString,Imagelicence="",Imageinsuranc="",Imageregist="" ,Imagepuc="" ,Imagevehicle ="" ,type;

    Boolean cameraintent,gallaryintent;
    Date l_date,i_date,r_date,puc_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + "Register" + "</font>")));

        vehicletype=findViewById(R.id.vehicletypespinner);
        licencedate=findViewById(R.id.licencedate);
        pucdate=findViewById(R.id.pucdate);
        insurancedate=findViewById(R.id.insurancedate);
        regexpdate=findViewById(R.id.regexpdate);

        licereg=findViewById(R.id.reglicenceimage);
        pucreg=findViewById(R.id.regpucimage);
        insreg=findViewById(R.id.reginsuimage);
        vehregreg=findViewById(R.id.regregisimage);
        vehireg=findViewById(R.id.reglvehiimage);

        licencecopy=findViewById(R.id.sel_dl);
        puccopy=findViewById(R.id.sel_puc);
        inscopy=findViewById(R.id.sel_ins);
        vehregcopy=findViewById(R.id.sel_reg);
        vehicleimg=findViewById(R.id.sel_vehicleimg);
        vehiclenumber=findViewById(R.id.vehiclenumber);
        qrcode=findViewById(R.id.qrcode);

        name=findViewById(R.id.name);
        location=findViewById(R.id.address);
        email=findViewById(R.id.email);
        mobile=findViewById(R.id.mobile);

        submit=findViewById(R.id.idBtnRegister);
        login=findViewById(R.id.btnlogin);
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                GetNumber(view);
            }
        });

        SpannableString content = new SpannableString("LogIn");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        login.setText(content);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                int j = sharedPreferences.getInt("key", 0);

                Log.e("J", String.valueOf(j));
                if(j > 0){

                    String Reg_mob = sharedPreferences.getString("mobile_number","");
                    String vehicalnum = sharedPreferences.getString("vehicle_number","");

                    Intent i = new Intent(RegistrationActivity.this, Dashboard_Activity.class);
                    i.putExtra("mobile",Reg_mob);
                    i.putExtra("veh_num",vehicalnum);
                    startActivity(i);

                }else {
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        licencecopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage("1");
                num = 1;
                //loadImagefromGallery();
            }
        });

        puccopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage("2");
                num = 2;
                //loadImagefromGallery();
            }
        });

        inscopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage("3");
                num = 3;
                //loadImagefromGallery();
            }
        });

        vehregcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage("4");
                num = 4;
                //loadImagefromGallery();
            }
        });
        vehicleimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage("5");
                num = 5;
            }
        });

        licencedate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getdate(licencedate);
            }
        });

        pucdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               getdate(pucdate);
            }
        });

        insurancedate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getdate(insurancedate);
            }
        });

        regexpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               getdate(regexpdate);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmptyField()) {

                    String QRdata = vehiclenumber.getText().toString();
                    type = vehicletype.getSelectedItem().toString();

                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(QRdata, BarcodeFormat.QR_CODE, 1000, 1000);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmapQR = barcodeEncoder.createBitmap(bitMatrix);
                        Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.logowhite3);
                        QRString = bitmapToString(mergeBitmaps(overlay, bitmapQR));

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    pd1 = new ProgressDialog(RegistrationActivity.this);
                    pd1.setMessage("Please Wait.......");
                    pd1.show();

                    if(licereg.getDrawable()!=null){
                        Imagelicence = imageToString(licereg);
                    }if (insreg.getDrawable()!=null){
                        Imageinsuranc = imageToString(insreg);
                    }if (vehregreg.getDrawable()!=null){
                        Imageregist = imageToString(vehregreg);
                    }if (pucreg.getDrawable()!=null){
                        Imagepuc = imageToString(pucreg);
                    }if (vehireg.getDrawable()!=null){
                        Imagevehicle = imageToString(vehireg);
                    }else{
                        Toast.makeText(RegistrationActivity.this, "Some Images Are Empty", Toast.LENGTH_SHORT).show();
                    }

                    String licen_date=licencedate.getText().toString();
                    String insu_date=insurancedate.getText().toString();
                    String reg_date=regexpdate.getText().toString();
                    String puc_date1=pucdate.getText().toString();

                    System.out.println("Date1:" + licen_date);

                    try {

                        l_date = new SimpleDateFormat("yyyy/MM/dd").parse(licen_date);
                        i_date = new SimpleDateFormat("yyyy/MM/dd").parse(insu_date);
                        r_date = new SimpleDateFormat("yyyy/MM/dd").parse(reg_date);
                        puc_date = new SimpleDateFormat("yyyy/MM/dd").parse(puc_date1);

                        licen_date=new SimpleDateFormat("yyyy/MM/dd").format(l_date);
                        insu_date=new SimpleDateFormat("yyyy/MM/dd").format(i_date);
                        reg_date=new SimpleDateFormat("yyyy/MM/dd").format(r_date);
                        puc_date1=new SimpleDateFormat("yyyy/MM/dd").format(puc_date);

                        Log.e("Date1", String.valueOf(licen_date));
                        Log.e("Date2", String.valueOf(insu_date));
                        Log.e("Date3", String.valueOf(reg_date));
                        Log.e("Date4", String.valueOf(puc_date1));

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    System.out.println("Image.path:" + Imagelicence);
                    System.out.println("Date:" + licen_date);

                    Api_list Ar = Api.getClient().create(Api_list.class);
                    Call<Registration_pojo> call = Ar.registrationData(name.getText().toString(),location.getText().toString(),mobile.getText().toString(),email.getText().toString(),vehicletype.getSelectedItem().toString(),vehiclenumber.getText().toString(),Imagevehicle,licen_date,Imagelicence,puc_date1,Imagepuc,insu_date,Imageinsuranc,reg_date,Imageregist,QRString);
                    call.enqueue(new Callback<Registration_pojo>() {

                        @Override
                        public void onResponse(Call<Registration_pojo> call, Response<Registration_pojo> response) {
                            if (response.body() != null) {
//                                System.out.println("Response:"+response.body());
                                Registration_pojo po = response.body();
                                String res = po.getResponse();

                                if (res.equals("user registered successfully")) {
                                    System.out.println("Response:" + res);
                                    pd1.dismiss();

                                    Intent intent1 = new Intent(RegistrationActivity.this, LoginActivity.class);
                                    intent1.putExtra("reg_mob", mobile.getText().toString());
                                    intent1.putExtra("veh_num", vehiclenumber.getText().toString());
                                    startActivity(intent1);
                                } else {
                                    System.out.println("Error Response:" + response.body());
                                    pd1.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Registration_pojo> call, Throwable t) {
                            System.out.println("Response:Error");
                        }
                    });
                    }else{
                        Toast.makeText(RegistrationActivity.this, "Fields Are Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        System.out.println("Width : "+overlay.getWidth() + " Height : "+overlay.getHeight());

        int centreX = (canvasWidth - overlay.getWidth()) / 2;
        int centreY = (canvasHeight - overlay.getHeight()) / 2;

        canvas.drawBitmap(overlay, centreX, centreY, null);
        return combined;

    }
        public void getdate(TextView textView){
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            long now = System.currentTimeMillis() - 1000;

            DatePickerDialog datePickerDialog = new DatePickerDialog(

                    RegistrationActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            if(monthOfYear==1 ||monthOfYear==2 ||monthOfYear==3||monthOfYear==4||monthOfYear==5||monthOfYear==6||monthOfYear==7||monthOfYear==8){
                                textView.setText(year + "/0" + (monthOfYear + 1) + "/" + dayOfMonth);
                            } else if(monthOfYear==9||monthOfYear == 10 || monthOfYear == 11) {
                                textView.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }
                        }
                    },
                    year, month, day);

            datePickerDialog.getDatePicker().setMinDate(now);
            datePickerDialog.show();
        }

    private String imageToString(ImageView picture) {

        BitmapDrawable drawable = (BitmapDrawable) picture.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);
        }

        byte[] imgByte = btyeArrayOutputStream.toByteArray();
        System.out.println("base64" + Base64.encodeToString(imgByte, Base64.DEFAULT));
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private String bitmapToString(Bitmap bitmapqr) {

        Bitmap bitmap = bitmapqr;

        ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);
        }
        byte[] imgByte = btyeArrayOutputStream.toByteArray();
        System.out.println("Inside imageToString base64 : " + Base64.encodeToString(imgByte, Base64.DEFAULT));

        System.out.println("Inside imageToString Reg Mobile: " + mobile.getText().toString() + " QR String " + Base64.encodeToString(imgByte, Base64.DEFAULT));

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void selectImage(String number) {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    if (ContextCompat.checkSelfPermission(RegistrationActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_CAMERA);
                    } else {
                        cameraintent=true;
                        gallaryintent=false;
                        if (number.equals("1")) {
                            cameraIntent(1);
                        } else if (number.equals("2")) {
                            cameraIntent(2);
                        } else if (number.equals("3")) {
                            cameraIntent(3);
                        } else if (number.equals("4")) {
                            cameraIntent(4);
                        } else {
                            Log.e("in else ","doc3galla");
                            cameraIntent(5);
                        }
                    }
                } else if (items[item].equals("Choose from Gallery")) {

                    if (ContextCompat.checkSelfPermission(RegistrationActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_EXTERNAL_STORAGE);
                    } else {

                        gallaryintent=true;
                        cameraintent=false;

                        System.out.print("number:" + number);

                        if (number.equals("1")) {
                            loadImagefromGallery(1);
                        } else if (number.equals("2")) {
                            loadImagefromGallery(2);
                        } else if (number.equals("3")) {
                            loadImagefromGallery(3);
                        } else if (number.equals("4")) {
                            loadImagefromGallery(4);
                        } else {
                            Log.e("in else ","doc3galla");
                            loadImagefromGallery(5);
                        }
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public boolean isEmptyField() {

        if (email.getText().toString().isEmpty() || mobile.getText().toString().isEmpty() || vehiclenumber.getText().toString().isEmpty()||vehicletype.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(this, "Email , Mobile , Vehicalnumber Are Empty..", Toast.LENGTH_LONG).show();
            return false;

        } else if (!isValideEmail(email.getText().toString())) {
            email.setError("Please Enter Valid Email");
            return false;

        } else if (!isValideMobile(mobile.getText().toString())) {
            mobile.setError("Please Enter Valid mobile no");
            return false;
        }
        return true;
    }

    private boolean isValideEmail(String email) {
        //Email Validation..................................................................................................
        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
        Matcher matcher = Pattern.compile(validemail).matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            return false;

        }
    }

    private boolean isValideMobile(String mobile) {

        //mobile number validation.....................................................................
        String validmobile = "^[7-9][0-9]{9}$";
        Matcher matcher1 = Pattern.compile(validmobile).matcher(mobile);

        if (matcher1.matches()) {
            return true;
        } else {
            return false;

        }
    }
    private void cameraIntent(int number) {
        gallaryintent=false;
        cameraintent=true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            if(number==1){
                startActivityForResult(intent, 0);
            }else if(number==2){
                startActivityForResult(intent, 1);
            }else if(number==3){
                startActivityForResult(intent, 2);
            }else if(number==4){
                startActivityForResult(intent, 3);
            }else if(number==5){
                startActivityForResult(intent, 4);
            }
        }
    }

    public void loadImagefromGallery(int number) {
        gallaryintent=true;
        cameraintent=false;

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra("key", "1");
        if(number==1){
            startActivityForResult(galleryIntent, 0);
        }else if(number==2){
            startActivityForResult(galleryIntent, 1);
        }else if(number==3){
            startActivityForResult(galleryIntent, 2);
        }else if(number==4){
            startActivityForResult(galleryIntent, 3);
        }else if(number==5){
            startActivityForResult(galleryIntent, 4);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("request code :" + requestCode + " resultCode:" + resultCode + " data:" + data);

        try {
            // When an Image is picked

            if(cameraintent){

                Log.e("gallary:", String.valueOf(gallaryintent));
                Log.e("camera:", String.valueOf(cameraintent));

                if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    File destination = new   File(Environment.getExternalStorageDirectory() + "/" +
                            getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    licereg.setImageBitmap(bitmap);

                } else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                    bitmap1 = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    File destination = new   File(Environment.getExternalStorageDirectory() + "/" +
                            getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    pucreg.setImageBitmap(bitmap1);

                } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {

                    bitmap2 = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    File destination = new   File(Environment.getExternalStorageDirectory() + "/" +
                            getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    insreg.setImageBitmap(bitmap2);

                } else if (requestCode == 3 && resultCode == RESULT_OK && null != data) {

                    bitmap3 = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap3.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    File destination = new   File(Environment.getExternalStorageDirectory() + "/" +
                            getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    vehregreg.setImageBitmap(bitmap3);

                } else if (requestCode == 4 && resultCode == RESULT_OK && null != data) {

                    bitmap4 = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap4.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    File destination = new   File(Environment.getExternalStorageDirectory() + "/" +
                            getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    vehireg.setImageBitmap(bitmap4);
                } else {

                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

                }

            } else if (gallaryintent) {

                if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    System.out.println("URI of image" + selectedImage);
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    // imgView.setImageBitmap(bitmap);
                    imagequality(bitmap, licereg);

                } else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    System.out.println("URI of image1" + selectedImage);
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    // imgView.setImageBitmap(bitmap);
                    imagequality(bitmap1, pucreg);

                } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    System.out.println("URI of image2" + selectedImage);
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    // imgView.setImageBitmap(bitmap);
                    imagequality(bitmap2, insreg);

                } else if (requestCode == 3 && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    System.out.println("URI of image3" + selectedImage);
                    bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    // imgView.setImageBitmap(bitmap);
                    imagequality(bitmap3, vehregreg);

                } else if (requestCode == 4 && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    System.out.println("URI of image4" + selectedImage);
                    bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    // imgView.setImageBitmap(bitmap);
                    imagequality(bitmap4, vehireg);

                } else if (requestCode == 5 && resultCode == RESULT_OK && null != data) {


                } else {
                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS_CAMERA) {
            System.out.println("Inside REQUEST_CODE_ASK_PERMISSIONS_CAMERA");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (num==1) {
                    cameraIntent(1);
                } else if (num==2) {
                    cameraIntent(2);
                } else if (num==3) {
                    cameraIntent(3);
                } else if (num==4) {
                    cameraIntent(4);
                } else if (num==4){
                    cameraIntent(5);
                }else{
                    Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Permission Denied
                Toast.makeText(RegistrationActivity.this, "Required permission is disable.", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == 100) {
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if ( ActivityCompat.checkSelfPermission(this,
                    READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            String phoneNumber = telephonyManager.getLine1Number();
            mobile.setText(phoneNumber);
        }
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS_EXTERNAL_STORAGE) {
            System.out.println("Inside REQUEST_CODE_ASK_PERMISSIONS_EXTERNAL_STORAGE");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (num == 1) {
                    loadImagefromGallery(1);
                } else if (num == 2) {
                    loadImagefromGallery(2);
                } else if (num == 3) {
                    loadImagefromGallery(3);
                } else if(num == 4){
                    loadImagefromGallery(4);
                } else if(num == 5){
                    loadImagefromGallery(5);
                }
            } else {
                // Permission Denied
                Toast.makeText(RegistrationActivity.this, "Required permission is disable.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void imagequality(Bitmap pic, ImageView imageview) {

        int imgWidth = pic.getWidth();
        int imgHeight = pic.getHeight();

        System.out.println("image width:" + imgWidth + " image Height :" + imgHeight);
        matrix = new Matrix();
        scaleWidth = ((float) newWidth) / imgWidth;
        scaleHeight = ((float) newHeight) / imgHeight;
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(0);

        resizedBitmap = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, matrix, true);
        outputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        imageview.setImageBitmap(resizedBitmap);

    }
}