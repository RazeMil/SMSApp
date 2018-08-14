package sg.edu.rp.c346.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tvTo;
    TextView tvContent;
    EditText etTo;
    EditText etContent;
    Button btnSend;
    Button btnVia;

    BroadcastReceiver br = new MessageReceiver(){

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTo = findViewById(R.id.textViewTo);
        tvContent = findViewById(R.id.textViewContent);
        etTo = findViewById(R.id.editTextTo);
        etContent = findViewById(R.id.editTextContent);
        btnSend = findViewById(R.id.buttonSend);
        btnVia = findViewById(R.id.buttonVia);
        
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br,filter);
        checkPermission();

         btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] address = etTo.getText().toString().split(",");

                    for (String add : address) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(add, null, etContent.getText().toString(), null, null);
                        Toast toast = Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG);
                        toast.show();

                }
            }
        });
        btnVia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + etTo.getText().toString()));
                smsIntent.putExtra("sms_body", etContent.getText().toString());
                startActivity(smsIntent);
            }
        });

    }
    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }

}


