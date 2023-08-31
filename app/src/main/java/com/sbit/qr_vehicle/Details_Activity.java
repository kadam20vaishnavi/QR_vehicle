package com.sbit.qr_vehicle;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Html;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Details_Activity extends AppCompatActivity {

    String Reg_mob,vehicle_num;
    EditText name,email,address;
    TextView ldate,rdate,idate,pucdate;
    TextView mobile,vehicaltype,vehnumber;
    ImageButton sellicen,selreg,selins,selpuc,selvehi;
    ImageView licenceimage,vehicleimg,registrationimg,pucimg,insuranceimg;
    ProgressDialog pd;
    Button update;
    int num = 0;
    int newWidth = 413;
    int newHeight = 531;
    Matrix matrix;
    Bitmap resizedBitmap;
    float scaleWidth;
    float scaleHeight;
    ProgressDialog pd1;
    ByteArrayOutputStream outputStream;
    Bitmap bitmap, bitmap1, bitmap2,bitmap3,bitmap4;
    final private int REQUEST_CODE_ASK_PERMISSIONS_EXTERNAL_STORAGE = 200;
    final private int REQUEST_CODE_ASK_PERMISSIONS_CAMERA = 100;
    String Imagelicence="" ;
    String Imageinsuranc="" ;
    String Imageregist="" ;
    String Imagepuc="" ;
    String Imagevehicle ="" ;

    Spinner vehicletype;

    boolean cameraintent=false,gallaryintent=true;

    boolean isImageFitToScreen;

    Date l_date,i_date,r_date,puc_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

//        vehicletype=findViewById(R.id.vehispinnere);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + "Details" + "</font>")));

        name = findViewById(R.id.user_name);
        mobile = findViewById(R.id.user_mobile);
        email = findViewById(R.id.user_email);
        address = findViewById(R.id.user_addres);

        vehicaltype = findViewById(R.id.vehicaltypeinst);
        ldate = findViewById(R.id.licenceexpdate);
        rdate = findViewById(R.id.registerexpdate);
        idate = findViewById(R.id.insuranceexpdate);
        pucdate = findViewById(R.id.pucexpdate);

        selins = findViewById(R.id.selinsu);
        selreg = findViewById(R.id.selregi);
        sellicen = findViewById(R.id.sellicen);
        selpuc = findViewById(R.id.selpuc);
        selvehi = findViewById(R.id.selvehi);
        vehnumber = findViewById(R.id.vehicalnumberdet);

        licenceimage = findViewById(R.id.licenceimage);
        vehicleimg = findViewById(R.id.vehicalcopy);
        registrationimg = findViewById(R.id.registercopy);
        pucimg = findViewById(R.id.puccopy);
        insuranceimg = findViewById(R.id.insurancecopy);

        update = findViewById(R.id.update);

        sellicen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage("1");
                num = 1;
            }
        });

        selreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage("2");
                num = 2;
            }
        });

        selins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage("3");
                num = 3;
            }
        });

        selpuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage("4");
                num = 4;
            }
        });

        selvehi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage("5");
                num = 5;
            }
        });

        ldate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getdate(ldate);
            }
        });

        pucdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getdate(pucdate);
            }
        });

        idate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getdate(idate);
            }
        });

        rdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getdate(rdate);
            }
        });

        Intent intent = getIntent();    // print current registered user mobile to editext
        Reg_mob = intent.getStringExtra("mobile");
        vehicle_num = intent.getStringExtra("veh_num");
        Log.e("mobile//:", "" + Reg_mob);

        Api_list Ar = Api.getClient().create(Api_list.class);

        Call<DetailModal> call = Ar.getdetails(vehicle_num);

        pd = new ProgressDialog(Details_Activity.this);
        pd.setTitle("Checking Your Data!");
        pd.setMessage("Please Wait..");
        pd.show();

        call.enqueue(new Callback<DetailModal>() {

            @Override
            public void onResponse(Call<DetailModal> call, Response<DetailModal> response) {

                if (response.body() != null) {

                    DetailModal detailModal = response.body();
                    System.out.println("Response:" + detailModal.getResponse());

                    name.setText(detailModal.getName());
                    mobile.setText(detailModal.getMobile());
                    email.setText(detailModal.getEmail());
                    address.setText(detailModal.getAddress());

                    ldate.setText(detailModal.getDrivingLicenceExpDate());
                    rdate.setText(detailModal.getVehicalRegDate());
                    idate.setText(detailModal.getInsuranceExpDate());
                    pucdate.setText(detailModal.getPucExpDate());

                    vehicaltype.setText(detailModal.getVehicleType());
                    vehnumber.setText(detailModal.getVehicleNumber());

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

                    }catch (Exception e){
                        Toast.makeText(Details_Activity.this, "Image Not Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DetailModal> call1, Throwable t) {
                System.out.println("error :" + t.getMessage());
                Toast.makeText(Details_Activity.this, "Something went wrong! try again later", Toast.LENGTH_LONG).show();
                pd.dismiss();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View view) {

                Api_list Ar = Api.getClient().create(Api_list.class);

                if(licenceimage.getDrawable()!=null){
                    Imagelicence = imageToString(licenceimage);
                }if (insuranceimg.getDrawable()!=null){
                    Imageinsuranc = imageToString(insuranceimg);
                }if (registrationimg.getDrawable()!=null){
                    Imageregist = imageToString(registrationimg);
                }if (pucimg.getDrawable()!=null){
                    Imagepuc = imageToString(pucimg);
                }if (vehicleimg.getDrawable()!=null){
                    Imagevehicle = imageToString(vehicleimg);
                }else{
                    Toast.makeText(Details_Activity.this, "Some Images Are Empty", Toast.LENGTH_SHORT).show();
                }
                String lDate1=ldate.getText().toString();
                String iDate1=idate.getText().toString();
                String rDate1=rdate.getText().toString();
                String pucDate1=pucdate.getText().toString();

                String licen_date=lDate1;
                String insu_date=iDate1;
                String reg_date=rDate1;
                String puc_date1=pucDate1;

                try {

                        l_date = new SimpleDateFormat("yyyy/MM/dd").parse(lDate1);
                        i_date = new SimpleDateFormat("yyyy/MM/dd").parse(iDate1);
                        r_date = new SimpleDateFormat("yyyy/MM/dd").parse(rDate1);
                        puc_date = new SimpleDateFormat("yyyy/MM/dd").parse(pucDate1);

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

                Log.e("Mobiletext",""+mobile.getText().toString());
                Call<DetailModal> call = Ar.updatedetails(mobile.getText().toString(),
                        name.getText().toString(),
                        email.getText().toString(),
                        address.getText().toString(),
                        vehnumber.getText().toString(),
                        licen_date,
                        puc_date1,
                        insu_date,
                        reg_date,
                        Imagevehicle,
                        Imagelicence,
                        Imagepuc,
                        Imageinsuranc,
                        Imageregist);

                pd = new ProgressDialog(Details_Activity.this);
                pd.setTitle("Updating Data!");
                pd.setMessage("Please Wait..");
                pd.show();

                call.enqueue(new Callback<DetailModal>() {

                    @Override
                    public void onResponse(Call<DetailModal> call, Response<DetailModal> response) {
                        if (response.body() != null) {
                            DetailModal detailModal = response.body();
                            System.out.println("Response:" + detailModal.getMobile());
                            Toast.makeText(Details_Activity.this, "Successs!!!", Toast.LENGTH_SHORT).show();
                            pd.dismiss();

                            Intent intent1 = new Intent(Details_Activity.this, Dashboard_Activity.class);
                            startActivity(intent1);
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailModal> call1, Throwable t) {

                        System.out.println("error :" + t.getMessage());
                        Toast.makeText(Details_Activity.this, "Something went wrong! try again later", Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                });
            }
        });
    }

    public void getdate(TextView textView){
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        long now = System.currentTimeMillis() - 1000;

        DatePickerDialog datePickerDialog = new DatePickerDialog(

                Details_Activity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            if (monthOfYear == 1 || monthOfYear == 2 || monthOfYear == 3 || monthOfYear == 4 || monthOfYear == 5 || monthOfYear == 6 || monthOfYear == 7 || monthOfYear == 8 || monthOfYear == 9) {
                                textView.setText(year + "/0" + (monthOfYear + 1) + "/" + dayOfMonth);
                            }else if(monthOfYear == 10 || monthOfYear == 11 || monthOfYear == 12){
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

    private void selectImage(String number) {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Details_Activity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    if (ContextCompat.checkSelfPermission(Details_Activity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Details_Activity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_CAMERA);
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

                    if (ContextCompat.checkSelfPermission(Details_Activity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Details_Activity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_EXTERNAL_STORAGE);
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

                    gallaryintent=false;
                    cameraintent=false;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

                    licenceimage.setImageBitmap(bitmap);

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

                    registrationimg.setImageBitmap(bitmap1);

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

                    insuranceimg.setImageBitmap(bitmap2);

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

                    pucimg.setImageBitmap(bitmap3);

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

                    vehicleimg.setImageBitmap(bitmap4);
                } else {

                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

                }

                } else if (gallaryintent) {
                Log.e("gallary:", String.valueOf(gallaryintent));
                Log.e("camera:", String.valueOf(cameraintent));
                if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    System.out.println("URI of image" + selectedImage);
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    // imgView.setImageBitmap(bitmap);
                    imagequality(bitmap, licenceimage);

                } else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    System.out.println("URI of image1" + selectedImage);
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    // imgView.setImageBitmap(bitmap);
                    imagequality(bitmap1, registrationimg);

                } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    System.out.println("URI of image2" + selectedImage);
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    // imgView.setImageBitmap(bitmap);
                    imagequality(bitmap2, insuranceimg);

                } else if (requestCode == 3 && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    System.out.println("URI of image3" + selectedImage);
                    bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    // imgView.setImageBitmap(bitmap);
                    imagequality(bitmap3, pucimg);

                } else if (requestCode == 4 && resultCode == RESULT_OK && null != data) {

                    Uri selectedImage = data.getData();
                    System.out.println("URI of image4" + selectedImage);
                    bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    // imgView.setImageBitmap(bitmap);
                    imagequality(bitmap4, vehicleimg);

                } else {

                    Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

                }
            }else{
                Log.e("Camera :", String.valueOf(cameraintent));
                Log.e("Gallery :", String.valueOf(gallaryintent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        } catch (Exception e) {
//            Toast.makeText(this, "Something went wrong:" + e.getMessage(), Toast.LENGTH_LONG).show();
//        }
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
                    Toast.makeText(Details_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Permission Denied
                Toast.makeText(Details_Activity.this, "Required permission is disable.", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == 100) {
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
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
                Toast.makeText(Details_Activity.this, "Required permission is disable.", Toast.LENGTH_SHORT).show();
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