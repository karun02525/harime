package com.rider.hire_me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.app_controller.AppController;
import com.bumptech.glide.Glide;
import com.custom.CustomProgressDialog;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebService;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;


    protected static final String TAG = "EditProfileActivity";


    String User_id;
    String fbuserproimg;


    Bitmap editBitmap;

    String WhoLogin;
    String checkpassword;


    String accept;
    boolean isPasswordChange;


    private static final int RESULT_Gallery_IMAGE = 323;
    private static final int CAMERA_REQUEST = 1;
    private long lastClickTime = 0;
    private static final int PICK_FROM_CAMERA = 1;



    @BindView(R.id.edit_switch)
    Switch editSwitch;
    @BindView(R.id.edit_first_name)
    EditText firstName;
    @BindView(R.id.edit_last_name)
    EditText lastName;
    @BindView(R.id.edit_email)
    TextView email;
    @BindView(R.id.edit_mobile)
    EditText mobile;
    @BindView(R.id.edit_old_password)
    EditText oldPassword;
    @BindView(R.id.edit_new_password)
    EditText newPassword;
    @BindView(R.id.edit_confirm_password)
    EditText confirmPassword;
    @BindView(R.id.edit_change_password_layout)
    LinearLayout cahnegPasswordLayout;

    CircleImageView profileImage;

    private AppController controller;
    private DriverInfo userInfo;
    private CustomProgressDialog progressDialog;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        progressDialog=new CustomProgressDialog(this);
        //  profileImage = (CircularImageView1) findViewById(R.id.edit_profile_image);

        ButterKnife.bind(this);

        profileImage =  findViewById(R.id.profile_img);
        findViewById(R.id.img_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });




        cahnegPasswordLayout.setVisibility(View.GONE);
        editSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cahnegPasswordLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                isPasswordChange=isChecked?true:false;
            }
        });
        controller = (AppController) getApplication();
        ParseJson parseJson = new ParseJson(this);
        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        setUserInfo();
        email.setClickable(false);





    }


    public void setUserInfo() {
        firstName.setText(userInfo.getUFname());
        lastName.setText(userInfo.getULname());
        email.setText(userInfo.getUEmail());
        mobile.setText(userInfo.getUPhone());







        if(userInfo.getUProfileImagePath()==null||userInfo.getUProfileImagePath().equalsIgnoreCase("null")||userInfo.getUProfileImagePath().equalsIgnoreCase(""))
        {

        }else{
            Glide.with(this)
                    .load(Constants.Urls.IMAGE_BASE_URL + userInfo.getUProfileImagePath())
                    // .placeholder(R.drawable.placeholder)
                    //   .error(R.drawable.imagenotfound)
                    .into(profileImage);

        }

       /* Glide.with(this)
                 .load(Constants.Urls.IMAGE_BASE_URL + userInfo.getUProfileImagePath())
               // .placeholder(R.drawable.circleuser)
              //  .error(R.drawable.profileuser)
                .into(profileImage);
*/

        //   Picasso.get().load(Constants.Urls.IMAGE_BASE_URL + userInfo.getUProfileImagePath()).into(profileImage);

/*
        profileImage.setImageUrl(Constants.Urls.IMAGE_BASE_URL + userInfo.getUProfileImagePath(), imageLoader);
        profileImage.setScaleType(ImageView.ScaleType.MATRIX);
        // profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);*/
    }


    @OnClick(R.id.recancel)
    public void onCancel(View view) {
        onBackPressed();
    }

    @OnClick(R.id.edit_bt_save)
    public void saveProfile(View view) {
        if (Validations.isVaildateEditProfile(EditProfileActivity.this,firstName,lastName,mobile,oldPassword,newPassword,confirmPassword,controller.pref.getPassword(),isPasswordChange)) {
            updateProfile();
        }
    }

    private void updateProfile() {
        progressDialog.showDialog();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put(Constants.Keys.U_FName, firstName.getText().toString());
        params.put(Constants.Keys.U_LName, lastName.getText().toString());
        params.put(Constants.Keys.U_PHONE, mobile.getText().toString());

        if (editSwitch.isChecked()) {
            params.put("u_password",newPassword.getText().toString());

        }
        params.put("user_id", controller.pref.getUserID());
        params.put("is_send_email", String.valueOf(1));
        WebService.excuteRequest(this, params,Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                progressDialog.dismiss();
                if (isUpdate) {
                    if(editSwitch.isChecked()){
                        controller.pref.savePassword(newPassword.getText().toString());
                        newPassword.setText("");
                        oldPassword.setText("");
                        confirmPassword.setText("");
                    }
                    controller.pref.saveUserLogin(data.toString());
                    ParseJson parseJson = new ParseJson(EditProfileActivity.this);
                    userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
                    setUserInfo();
                    Toast.makeText(getApplication(), R.string.profile_updated, Toast.LENGTH_LONG).show();
                } else {
                    if (error == null) {
                        Toast.makeText(EditProfileActivity.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }




    /*........................camera runtime permisstion......................*/
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please allow camera permission first")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        makeRequest();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                    final CharSequence[] options = {"Camera", "Gallery", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                    builder.setTitle("Chosse Image");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Camera")) {
                                dialog.dismiss();
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            } else if (options[item].equals("Gallery")) {
                                dialog.dismiss();
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, RESULT_Gallery_IMAGE);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                } else
                    Toast.makeText(this, "Please provide camera permission", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
    }





    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.pback:
//                cancel();
                return true;
            case R.id.done1:
//                done1();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            editBitmap = photo;
            profileImage.setImageBitmap(photo);
            updateProfileImage(editBitmap);


        } else if (requestCode == RESULT_Gallery_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                // profileImage.setImageBitmap(selectedImage);
                profileImage.setImageBitmap(getResizedBitmap(rotateImageIfRequired(selectedImage,EditProfileActivity.this,imageUri),100));
                updateProfileImage(getResizedBitmap(rotateImageIfRequired(selectedImage,EditProfileActivity.this,imageUri),100));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(EditProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    /*  Image rotate.......................*/
    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.d("orientation: %s", "->>>>>>>>  "+ orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width/ (float) height;
        if (bitmapRatio < 1 && width > maxSize) {

            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else if(height > maxSize){
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /*........................Image Operation................*/





    private void updateProfileImage(Bitmap bitmap) {
        showProgress();
        final String bitmap64 = convert(bitmap);
        Map<String, String> params = new HashMap<>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put("user_image", bitmap64);
        params.put("image_type", "jpg");
        params.put("user_id", controller.pref.getUserID());
        WebService.excuteRequest(this, params,Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                if (isUpdate) {
                    hideProgress();
                    if (data != null) {
                        controller.pref.saveUserLogin(data.toString());
                        ParseJson parseJson = new ParseJson(EditProfileActivity.this);
                        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
                        setUserInfo();
                    } else {
                        Toast.makeText(getApplication(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void showProgress() {
        progressDialog.showDialog();
    }

    private void hideProgress() {
        progressDialog.dismiss();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("NewApi")
    public void onBackPressed() {
        finish();
    }




    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}