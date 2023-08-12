package com.sbit.qr_vehicle;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

import androidx.annotation.RequiresApi;
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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
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
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Activity extends AppCompatActivity {

    String Reg_mob,vehicle_num;
    EditText name,email,address;
    TextView ldate,rdate,idate,pucdate;
    TextView mobile,vehicaltype;
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

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        vehicletype=findViewById(R.id.vehispinnere);

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
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        Home_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                ldate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },

                        year, month, day);

                datePickerDialog.show();
            }
        });

        pucdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        Home_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                pucdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },

                        year, month, day);

                datePickerDialog.show();
            }
        });

        idate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        Home_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                idate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },

                        year, month, day);

                datePickerDialog.show();
            }
        });

        rdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        Home_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                rdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },

                        year, month, day);

                datePickerDialog.show();
            }
        });

        Intent intent = getIntent();    // print current registered user mobile to editext
        Reg_mob = intent.getStringExtra("mobile");
        vehicle_num = intent.getStringExtra("veh_num");
        Log.e("mobile//:", "" + Reg_mob);

        Api_list Ar = Api.getClient().create(Api_list.class);

        Call<DetailModal> call = Ar.getdetails(vehicle_num);

        pd = new ProgressDialog(Home_Activity.this);
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
                        Toast.makeText(Home_Activity.this, "Image Not Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DetailModal> call1, Throwable t) {
                System.out.println("error :" + t.getMessage());
                Toast.makeText(Home_Activity.this, "Something went wrong! try again later", Toast.LENGTH_LONG).show();
                pd.dismiss();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Api_list Ar = Api.getClient().create(Api_list.class);

                    Imagelicence = imageToString(licenceimage);
                    Imageinsuranc = imageToString(insuranceimg);
                    Imageregist = imageToString(registrationimg);
                    Imagepuc = imageToString(pucimg);
                    Imagevehicle = imageToString(vehicleimg);

                Log.e("Mobiletext",""+mobile.getText().toString());
                Call<DetailModal> call = Ar.updatedetails(mobile.getText().toString(), name.getText().toString(),email.getText().toString(),address.getText().toString(),ldate.getText().toString(),pucdate.getText().toString(),idate.getText().toString(),rdate.getText().toString(),Imagevehicle,Imagelicence,Imagepuc,Imageinsuranc,Imageregist);

                pd = new ProgressDialog(Home_Activity.this);
                pd.setTitle("Updating Data!");
                pd.setMessage("Please Wait..");
                pd.show();

                call.enqueue(new Callback<DetailModal>() {

                    @Override
                    public void onResponse(Call<DetailModal> call, Response<DetailModal> response) {
                        if (response.body() != null) {
                            DetailModal detailModal = response.body();
                            System.out.println("Response:" + detailModal.getMobile());
                            Toast.makeText(Home_Activity.this, "Successs!!!", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
//                            Intent intent1 = new Intent(ShowDetailsActivity.this, HomeActivity.class);
//                            startActivities(intent1);
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailModal> call1, Throwable t) {

                        System.out.println("error :" + t.getMessage());
                        Toast.makeText(Home_Activity.this, "Something went wrong! try again later", Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                });
            }
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Home_Activity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    if (ContextCompat.checkSelfPermission(Home_Activity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Home_Activity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_CAMERA);
                    } else {
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

                    if (ContextCompat.checkSelfPermission(Home_Activity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Home_Activity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_EXTERNAL_STORAGE);
                    } else {

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

    private void cameraIntent(int number) {
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
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

//                String requiredValue = data.getStringExtra("key");
//                System.out.print("No:"+requiredValue);
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

            }else if (requestCode == 4 && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                System.out.println("URI of image4" + selectedImage);
                bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                // imgView.setImageBitmap(bitmap);
                imagequality(bitmap4, vehicleimg);

            }else {

                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(Home_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Permission Denied
                Toast.makeText(Home_Activity .this, "Required permission is disable.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Home_Activity.this, "Required permission is disable.", Toast.LENGTH_SHORT).show();
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