package com.rider.hire_me;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.app_controller.AppController;
import com.custom.CustomProgressDialog;
import com.grepix.grepixutils.Validations;
import com.grepix.grepixutils.WebService;
import com.utils.Constants;
import com.utils.DriverInfo;
import com.utils.ParseJson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Settings extends Activity {

    @BindView(R.id.settg_contact1)
    EditText contact1;
    @BindView(R.id.settg_contact2)
    EditText contact2;
    @BindView(R.id.settg_contact3)
    EditText contact3;
    @BindView(R.id.settg_email1)
    EditText email1;
    @BindView(R.id.settg_email2)
    EditText email2;
    @BindView(R.id.settg_email3)
    EditText email3;
    @BindView(R.id.settg_bt_update_contact)
    Button btUpdateContact;
    @BindView(R.id.settg_bt_update_email)
    Button btUpdateEmail;


    protected static final String TAG = "Settings";
    private CustomProgressDialog dialog;

    Button Edit, Signout;

    private AppController controller;
    private DriverInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActionBar().hide();
        ButterKnife.bind(this);
        controller = (AppController) getApplication();
        ParseJson parseJson = new ParseJson(this);
        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
        setUserInfo();

    }

    private void setUserInfo() {

        contact1.setText(Validations.checkNull(userInfo.getEmergency_contact_1()) ? userInfo.getEmergency_contact_1() : "");
        contact2.setText(Validations.checkNull(userInfo.getEmergency_contact_2()) ? userInfo.getEmergency_contact_2() : "");
        contact3.setText(Validations.checkNull(userInfo.getEmergency_contact_3()) ? userInfo.getEmergency_contact_3() : "");
        email1.setText(Validations.checkNull(userInfo.getEmergency_email_1()) ? userInfo.getEmergency_email_1() : "");
        email2.setText(Validations.checkNull(userInfo.getEmergency_email_2()) ? userInfo.getEmergency_email_2() : "");
        email3.setText(Validations.checkNull(userInfo.getEmergency_email_3()) ? userInfo.getEmergency_email_3() : "");
    }


    @OnClick(R.id.settg_bt_update_contact)
    public void onUpdateContact(View view) {


        final String contact1_ = contact1.getText().toString();
        final String contact2_ = contact2.getText().toString();
        final String contact3_ = contact3.getText().toString();

        if (contact1_.length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.atleast_one_contact_require), Toast.LENGTH_LONG).show();
            contact1.requestFocus();
        } else if (contact1_.length() <= 6 || contact1_.length() >= 12) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_mobile_number), Toast.LENGTH_LONG).show();
            contact1.requestFocus();

        } else if (contact2_.length() != 0) {
            if (contact2_.length() <= 6 || contact2_.length() >= 12) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_mobile_number), Toast.LENGTH_LONG).show();
                contact2.requestFocus();
            } else {
                Updateds(contact1_, contact2_, contact3_);
            }

        } else if (contact3_.length() != 0) {
            if (contact3_.length() <= 6 || contact3_.length() >= 12) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_mobile_number), Toast.LENGTH_LONG).show();
                contact3.requestFocus();
            } else {
                Updateds(contact1_, contact2_, contact3_);
            }
        } else {
            Updateds(contact1_, contact2_, contact3_);
        }
    }


    private void Updateds(String contact1_, String contact2_, String contact3_) {

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
        params.put("user_id", controller.pref.getUserID());
        params.put("emergency_contact_1", contact1_);
        params.put("emergency_contact_2", contact2_);
        params.put("emergency_contact_3", contact3_);
        showProgress();
        WebService.excuteRequest(this, params, Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
            @Override
            public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                hideProgress();
                if (isUpdate) {
                    if (data != null) {
                        controller.pref.saveUserLogin(data.toString());
                        ParseJson parseJson = new ParseJson(getApplication());
                        userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
                        setUserInfo();

                        Toast.makeText(getApplication(), R.string.updated, Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplication(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (error == null) {
                        Toast.makeText(Settings.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }




    @OnClick(R.id.settg_bt_update_email)
    public void onUpdateEmail(View view) {


        final String email1_ = email1.getText().toString();
        final String email2_ = email2.getText().toString();
        final String email3_ = email3.getText().toString();


        if (email1_.length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.atleast_one_email_required), Toast.LENGTH_LONG).show();
            email1.requestFocus();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email1_).matches()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_email_1_field), Toast.LENGTH_LONG).show();
            email1.requestFocus();
        } else if(email2_.length()!=0){
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email2_).matches()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_email_2_field), Toast.LENGTH_LONG).show();
                email2.requestFocus();
            }
        } else if(email3_.length()!=0){
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email3_).matches()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_email_3_field), Toast.LENGTH_LONG).show();
                email3.requestFocus();
            }
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put(Constants.Keys.API_KEY, controller.pref.getUserApiKey());
            params.put("user_id", controller.pref.getUserID());
            params.put("emergency_email_1", email1_);
            params.put("emergency_email_2", email2_);
            params.put("emergency_email_3", email3_);
            showProgress();
            WebService.excuteRequest(this, params, Constants.Urls.URL_USER_UPDEATE_PROFILE, new WebService.DeviceTokenServiceListener() {
                @Override
                public void onUpdateDeviceTokenOnServer(Object data, boolean isUpdate, VolleyError error) {
                    hideProgress();
                    if (isUpdate) {
                        if (data != null) {
                            controller.pref.saveUserLogin(data.toString());
                            ParseJson parseJson = new ParseJson(getApplication());
                            userInfo = parseJson.ParseLoginResponce(controller.pref.getUserLogin());
                            Toast.makeText(getApplication(), R.string.updated, Toast.LENGTH_LONG).show();

                            setUserInfo();
                        } else {
                            Toast.makeText(getApplication(), R.string.internet_connection_failed, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (error == null) {
                            Toast.makeText(Settings.this, R.string.internet_error, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        }
    }



    @OnClick(R.id.settg_back)
    public void onBack(View view) {
        finish();
    }








    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




    private void showProgress() {
        dialog = new CustomProgressDialog(this);
        dialog.showDialog();

    }

    private void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }




}
