package bo.com.paddingdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import bo.com.paddingdata.data.SMS;

/**
 * Created by bo.zhang on 2017/05/08   .
 */

public class AddSMSActivity extends Activity {
    private EditText SMSNumber;
    private RadioButton Inbox;
    private RadioButton Outbox;
    private RadioButton Draftbox;
    private RadioButton Sentbox;
    private RadioButton ReadState;
    private RadioButton unReadState;
    private Button Start;

    int i = 0, j = 0;
    private ProgressDialog mypDialog;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (j++ < i) {
                SMS s = new SMS();
                s.addSMS(AddSMSActivity.this, setType(), setReadState(), j);
                mypDialog.setProgress(j);
            } else {
                mypDialog.setMessage("短信数据添加完成！");
                // mypDialog.cancel();
                handler.removeCallbacks(runnable);
            }
            handler.post(runnable);
            // handler.postDelayed(runnable, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sms);
        SMSNumber = (EditText) findViewById(R.id.sms_number);
        Inbox = (RadioButton) findViewById(R.id.inbox);
        Outbox = (RadioButton) findViewById(R.id.outbox);
        Draftbox = (RadioButton) findViewById(R.id.draft);
        Sentbox = (RadioButton) findViewById(R.id.sent);
        ReadState = (RadioButton) findViewById(R.id.state_read);
        unReadState = (RadioButton) findViewById(R.id.state_unread);
        Start = (Button) findViewById(R.id.addSMS);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = setSMSNumber();
                mypDialog = new ProgressDialog(AddSMSActivity.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mypDialog.setTitle("填充数据");
                mypDialog.setMessage("正在添加短信，请稍等。。");
                mypDialog.setIcon(R.mipmap.sms);
                mypDialog.setProgress(j);
                mypDialog.setMax(i);
                mypDialog.setCanceledOnTouchOutside(true);
                mypDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.removeCallbacks(runnable);
                                mypDialog.cancel();
                            }
                        });
                mypDialog.setIndeterminate(false);
                mypDialog.setCancelable(true);
                mypDialog.show();
                handler.post(runnable);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final String myPackageName = getPackageName();
        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                    myPackageName);
            startActivity(intent);
        }


       /* final String myPackageName = getPackageName();
        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
            // App is not default.
            // Show the "not currently set as the default SMS app" interface
            Intent intent =
                    new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                    myPackageName);
            startActivity(intent);
           *//* View viewGroup = findViewById(R.id.not_default_app);
            viewGroup.setVisibility(View.VISIBLE);

            // Set up a button that allows the user to change the default SMS app
            Button button = (Button) findViewById(R.id.change_default_app);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent =
                            new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                            myPackageName);
                    startActivity(intent);
                }
            });*//*
        } else {
          *//*  // App is the default.
            // Hide the "not currently set as the default SMS app" interface
            View viewGroup = findViewById(R.id.not_default_app);
            viewGroup.setVisibility(View.GONE);*//*
        }
    }
*/
    }

    private int setSMSNumber() {
        if (SMSNumber.getText() == null)
            return 0;
        else
            return Integer.valueOf(String.valueOf(SMSNumber.getText()));
    }

    private int setType() {
        int i = 1;
        if (Inbox.isChecked()) {
            i = Telephony.Sms.MESSAGE_TYPE_INBOX;
        } else if (Outbox.isChecked()) {
            i = Telephony.Sms.MESSAGE_TYPE_OUTBOX;
        } else if (Sentbox.isChecked()) {
            i = Telephony.Sms.MESSAGE_TYPE_SENT;
        } else if (Draftbox.isChecked()) {
            i = Telephony.Sms.MESSAGE_TYPE_DRAFT;
        }
        return i;
    }

    private boolean setReadState() {
        boolean read = false;
        if (ReadState.isChecked())
            read = false;
        else if (unReadState.isChecked())
            read = true;
        return read;
    }
}