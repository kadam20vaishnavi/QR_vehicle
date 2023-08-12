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
import android.view.MenuItem;
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
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView licencedate,pucdate,insurancedate,regexpdate;
    ImageButton licencecopy,puccopy,inscopy,vehregcopy,vehicleimg;
    ImageView licereg,pucreg,insreg,vehregreg,vehireg;
    EditText name,location,email,mobile,vehiclenumber;
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
    String QRString;
    Button submit,login;
    Spinner vehicletype;
    String Imagelicence="" ;
    String Imageinsuranc="" ;
    String Imageregist="" ;
    String Imagepuc="" ;
    String Imagevehicle ="" ;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        name=findViewById(R.id.name);
        location=findViewById(R.id.address);
        email=findViewById(R.id.email);
        mobile=findViewById(R.id.mobile);

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetNumber(view);
            }
        });

        submit=findViewById(R.id.idBtnRegister);
        login=findViewById(R.id.btnlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
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
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                licencedate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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

                        MainActivity.this,
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

        insurancedate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                insurancedate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },

                        year, month, day);

                datePickerDialog.show();
            }
        });

        regexpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                regexpdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },

                        year, month, day);

                datePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmptyField();

                String QRdata = vehiclenumber.getText().toString();

                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(QRdata, BarcodeFormat.QR_CODE, 1000, 1000);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmapQR = barcodeEncoder.createBitmap(bitMatrix);

                    Bitmap overlay = BitmapFactory.decodeResource(getResources(), R.drawable.sbit_logo);

                    QRString = bitmapToString(bitmapQR);

                } catch (WriterException e) {
                    e.printStackTrace();
                }

                    type=vehicletype.getSelectedItem().toString();

                    pd1 = new ProgressDialog(MainActivity.this);
                    pd1.setMessage("Please Wait.......");
                    pd1.show();

                    Imagelicence = imageToString(licereg);
                    Imageinsuranc = imageToString(insreg);
                    Imageregist = imageToString(vehregreg);
                    Imagepuc = imageToString(pucreg);
                    Imagevehicle = imageToString(vehireg);

                    System.out.println("Image.path:" + Imagelicence);

                    Api_list Ar = Api.getClient().create(Api_list.class);
                    Call<Registration_pojo> call = Ar.registrationData(name.getText().toString(),location.getText().toString(),mobile.getText().toString(),email.getText().toString(),"234567" ,vehicletype.getSelectedItem().toString(),vehiclenumber.getText().toString(),Imagevehicle,licencedate.getText().toString(),Imagelicence,pucdate.getText().toString(),Imagepuc,insurancedate.getText().toString(),Imageinsuranc,regexpdate.getText().toString(),Imageregist,QRString);
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

                                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
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
                }
            });
        }

    public void GetNumber(View v) {

        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission check

            // Create obj of TelephonyManager and ask for current telephone service
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber = telephonyManager.getLine1Number();

            mobile.setText(phoneNumber);
            return;
        } else {
            // Ask for permission
            requestPermission();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
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

//        BitmapDrawable drawable = (BitmapDrawable) img_QR_code.getDrawable();
//        BitmapDrawable drawable = (BitmapDrawable) img_QR_code.getDrawable();
        Bitmap bitmap = bitmapqr;

        ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btyeArrayOutputStream);
        }
        byte[] imgByte = btyeArrayOutputStream.toByteArray();
        System.out.println("Inside imageToString base64 : " + Base64.encodeToString(imgByte, Base64.DEFAULT));

        System.out.println("Inside imageToString Reg Mobile: " + mobile.getText().toString() + " QR String " + Base64.encodeToString(imgByte, Base64.DEFAULT));

//        SendQRTask1 sq = new SendQRTask1();
//        sq.execute(reg_mob,Base64.encodeToString(imgByte, Base64.DEFAULT));

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void selectImage(String number) {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_CAMERA);
                    } else {
                        cameraIntent();
                    }

                } else if (items[item].equals("Choose from Gallery")) {

                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS_EXTERNAL_STORAGE);
                    } else {

                        System.out.print("number:" + number);

                        if (number.equals("1")) {
                            loadImagefromGallery();
                        } else if (number.equals("2")) {
                            loadImageDocfromGallery();
                        } else if (number.equals("3")) {
                            loadImageDoc1fromGallery();
                        } else if (number.equals("4")) {
                            loadImageDoc2fromGallery();
                        } else {
                            Log.e("in else ","doc3galla");
                            loadImageDoc3fromGallery();
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

        // Using random method
//        int randomPin = (int) (Math.random() * 9000) + 1000;
//        otp = String.valueOf(randomPin);

        if (email.getText().toString().isEmpty() || mobile.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fields Are Empty..", Toast.LENGTH_LONG).show();
            return false;

        } else if (isValideEmail(email.getText().toString()) == false) {
            email.setError("Please Enter Valid Email");
            return false;

        } else if (isValideMobile(mobile.getText().toString()) == false) {
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
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    public void loadImagefromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra("key", "1");
        startActivityForResult(galleryIntent, 0);
    }

    public void loadImageDocfromGallery() {
        Intent gallerydocIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallerydocIntent.putExtra("key", "2");
        startActivityForResult(gallerydocIntent, 1);
    }

    public void loadImageDoc1fromGallery() {
        Intent gallerydoc2Intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallerydoc2Intent.putExtra("key", "3");
        startActivityForResult(gallerydoc2Intent, 2);
    }

    public void loadImageDoc2fromGallery() {
        Intent gallerydoc3Intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallerydoc3Intent.putExtra("key", "4");
        startActivityForResult(gallerydoc3Intent, 3);
    }

    public void loadImageDoc3fromGallery() {
        Intent gallerydoc4Intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallerydoc4Intent.putExtra("key", "5");
        startActivityForResult(gallerydoc4Intent, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("request code :" + requestCode + " resultCode:" + resultCode + " data:" + data);

        try {
            // When an Image is picked
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

            }else if (requestCode == 4 && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                System.out.println("URI of image4" + selectedImage);
                bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                // imgView.setImageBitmap(bitmap);
                imagequality(bitmap4, vehireg);

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
                cameraIntent();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity .this, "Required permission is disable.", Toast.LENGTH_SHORT).show();
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
                    loadImagefromGallery();
                } else if (num == 2) {
                    loadImageDocfromGallery();
                } else if (num == 3) {
                    loadImageDoc1fromGallery();
                } else if(num == 4){
                    loadImageDoc2fromGallery();
                } else if(num == 5){
                    loadImageDoc3fromGallery();
                }
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Required permission is disable.", Toast.LENGTH_SHORT).show();
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