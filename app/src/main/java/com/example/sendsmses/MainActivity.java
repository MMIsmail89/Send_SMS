package com.example.sendsmses;

import static com.example.sendsmses.R.*;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.example.sendsmses.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    //
    ActivityMainBinding binding;
    //
    private static final int REQUEST_CODE_SEND_SMS = 1;
    public static final String permissionWHAT = Manifest.permission.SEND_SMS;

    public static final String SENT = "SMS_SENT";
    public static final String DELIVERED = "SMS_DELIVERED";

    public static final int REQ_Code_SENT = 11;
    public static final int REQ_CODE_DELIVERED = 22;

    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

    boolean permissionSEND_SMS = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //
        //
        if (check_permission(permissionWHAT)) {permissionSEND_SMS = true; }
        else {permissionSEND_SMS = false;}
        //
        //
        ActivityResultLauncher<String> arl = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean wasGranted) {
                        if (wasGranted) {

                            permissionSEND_SMS = true;


                            Toast.makeText(MainActivity.this,
                                    getString(string.granted_permission_R2),
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            permissionSEND_SMS = false;


                            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                    permissionWHAT)) {
                                // create Alert Dialog to convince the user of the necessary of this permission
                                showingAlertDialogConvinceUser();

                            }
                            else {
                                permissionSEND_SMS = false;


                                Toast.makeText(MainActivity.this,
                                        getString(string.never_show),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
        //
        arl.launch(permissionWHAT);
        //
        binding.mainBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arl.launch(permissionWHAT);
            }
        });
        //


        //
        binding.mainBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sending_SMS();
            }
        });
        //


    }
    //

    @Override
    protected void onResume() {
        super.onResume();
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                    {Toast.makeText(context, getString(string.SMS_SENT), Toast.LENGTH_SHORT).show();
                        binding.mainTvSentFeedback.setText(getString(string.SMS_SENT) + ", RESULT_OK");
                        break;
                    }
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    {Toast.makeText(context, getString(string.GENERIC_FAILURE), Toast.LENGTH_SHORT).show();
                        binding.mainTvSentFeedback.setText(getString(string.GENERIC_FAILURE) + ", RESULT_ERROR_GENERIC_FAILURE");
                        break;
                    }
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                    {Toast.makeText(context, getString(string.NO_SERVICE), Toast.LENGTH_SHORT).show();
                        binding.mainTvSentFeedback.setText(getString(string.NO_SERVICE) + ", RESULT_ERROR_NO_SERVICE");
                        break;
                    }
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                    {Toast.makeText(context, getString(string.NULL_PDU), Toast.LENGTH_SHORT).show();
                        binding.mainTvSentFeedback.setText(getString(string.NULL_PDU) + ", RESULT_ERROR_NULL_PDU");
                        break;
                    }
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    {Toast.makeText(context, getString(string.RADIO_OFF), Toast.LENGTH_SHORT).show();
                        binding.mainTvSentFeedback.setText(getString(string.RADIO_OFF) + ", RESULT_ERROR_RADIO_OFF");
                        break;
                    }
                }
            }
        };
        //
        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                    {Toast.makeText(context, getString(string.SMS_DELIVERED), Toast.LENGTH_SHORT).show();
                        binding.mainTvDeliveredFeedback.setText(getString(string.SMS_DELIVERED) + ", RESULT_OK");
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {Toast.makeText(context, getString(string.DELIVERED_CANCEL), Toast.LENGTH_SHORT).show();
                        binding.mainTvDeliveredFeedback.setText(getString(string.DELIVERED_CANCEL) + ", RESULT_CANCELED");
                        break;
                    }

                }
            }
        };
        //
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }
    //


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    public void sending_SMS( ) {

        if (permissionSEND_SMS){
            String msg = binding.mainEtMessage.getText().toString().trim();
            String phoneNumber = binding.mainEtNumber.getText().toString().trim();

            sentPI = PendingIntent.getBroadcast(MainActivity.this,
                    REQ_Code_SENT, new Intent(SENT), 0);

            deliveredPI = PendingIntent.getBroadcast(MainActivity.this,
                    REQ_CODE_DELIVERED, new Intent(DELIVERED), 0);

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliveredPI);



        } else {Toast.makeText(MainActivity.this,
                getString(R.string.till_now_no_permission), Toast.LENGTH_SHORT).show(); }

    }
    private boolean check_permission(String permission) {
        boolean granted = false;
        if (permission != null) {

            if( ContextCompat.checkSelfPermission(MainActivity.this, permissionWHAT)
                    != PackageManager.PERMISSION_GRANTED ) {

                //                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{permissionWHAT}, REQUEST_CODE_SEND_SMS);
            }
            else {
                Toast.makeText(MainActivity.this,
                        getString(string.granted_permission_R2),
                        Toast.LENGTH_SHORT).show();

            }

        }

        return granted;
    }

    public void showingAlertDialogConvinceUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(string.please_permit_it)
                .setTitle(string.dialog_title);

        builder.setPositiveButton(string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{permissionWHAT}, REQUEST_CODE_SEND_SMS);

            }
        });
        builder.setNegativeButton(string.no_thanks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this,
                        getString(string.Cannot_be_done),
                        Toast.LENGTH_SHORT).show();

            }
        });
        builder.show();
    }
}