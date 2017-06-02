package bo.com.paddingdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import bo.com.paddingdata.Tool.RandomData;
import bo.com.paddingdata.Tool.RandomTool;
import bo.com.paddingdata.data.Contacts;

/**
 * Created by bo.zhang on 2017/05/08   .
 */

public class AddContactsActivity extends Activity {
    private EditText ContactNumbers;
    private CheckBox homephone;
    private CheckBox callbackphone;
    private CheckBox mobilephone;
    private CheckBox otherphone;
    private CheckBox pagerphone;
    private CheckBox workphone;
    private Button Starts;
    private RadioButton english;
    private RadioButton chinese;
    private CheckBox homeemail;
    private CheckBox workemail;
    private CheckBox mobileemail;
    private CheckBox otheremail;
    private CheckBox address;
    private CheckBox note;
    int i = 0, j = 0;
    private ProgressDialog mypDialog;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (j++ < i) {
                Contacts c = new Contacts();
                c.AddContact(AddContactsActivity.this, setContactName(), setMobilePhone(),
                        setHomePhone(), setPagerPhone(), setOtherPhone(), setCallbackPhone(),
                        setWorkPhone(), setHomeEmail(), setWorkEmail(), setMobileEmail(),
                        setOherEmail(), setNote(), setAddress());
                mypDialog.setProgress(j);
            } else {
                mypDialog.setMessage("联系人数据添加完成！");
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
        setContentView(R.layout.add_contacts);
        ContactNumbers = (EditText) findViewById(R.id.contacts_number);
        english = (RadioButton) findViewById(R.id.english);
        chinese = (RadioButton) findViewById(R.id.chinese);
        homephone = (CheckBox) findViewById(R.id.homephone);
        callbackphone = (CheckBox) findViewById(R.id.callbackphone);
        mobilephone = (CheckBox) findViewById(R.id.mobilephone);
        otherphone = (CheckBox) findViewById(R.id.otherphone);
        pagerphone = (CheckBox) findViewById(R.id.pagerphone);
        workphone = (CheckBox) findViewById(R.id.workphone);
        Starts = (Button) findViewById(R.id.addcontacts);
        homeemail = (CheckBox) findViewById(R.id.homeemail);
        workemail = (CheckBox) findViewById(R.id.workemail);
        mobileemail = (CheckBox) findViewById(R.id.mobileemail);
        otheremail = (CheckBox) findViewById(R.id.otheremail);
        address = (CheckBox) findViewById(R.id.address);
        note = (CheckBox) findViewById(R.id.note);

        Starts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = setContactNumber();
                mypDialog = new ProgressDialog(AddContactsActivity.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mypDialog.setTitle("填充数据");
                mypDialog.setMessage("正在添加联系人，请稍等。。");
                mypDialog.setIcon(R.mipmap.contacts);
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

    private int setContactNumber() {
        return Integer.valueOf(String.valueOf(ContactNumbers.getText()));
    }

    private String setContactName() {
        String contaactname = null;
        if (english.isChecked()) {
            contaactname = RandomData.getRandomContactName();
        } else if (chinese.isChecked()) {
            contaactname = RandomTool.getChineseName();
        }
        return contaactname;
    }

    private String setMobilePhone() {
        String Phone = null;
        if (mobilephone.isChecked())
            Phone = RandomTool.getTel();
        return Phone;
    }

    private String setHomePhone() {
        String Phone = null;
        if (homephone.isChecked())
            Phone = RandomTool.getTel();
        return Phone;
    }

    private String setPagerPhone() {
        String Phone = null;
        if (pagerphone.isChecked())
            Phone = RandomTool.getTel();
        return Phone;
    }

    private String setWorkPhone() {
        String Phone = null;
        if (workphone.isChecked())
            Phone = RandomTool.getTel();
        return Phone;
    }

    private String setCallbackPhone() {
        String Phone = null;
        if (callbackphone.isChecked())
            Phone = RandomTool.getTel();
        return Phone;
    }

    private String setOtherPhone() {
        String Phone = null;
        if (otherphone.isChecked())
            Phone = RandomTool.getTel();
        return Phone;
    }

    private String setHomeEmail() {
        String Emails = null;
        if (homeemail.isChecked())
            Emails = RandomTool.getEmail(4, 12);
        return Emails;
    }

    private String setWorkEmail() {
        String Emails = null;
        if (workemail.isChecked())
            Emails = RandomTool.getEmail(4, 12);
        return Emails;
    }

    private String setMobileEmail() {
        String Emails = null;
        if (mobileemail.isChecked())
            Emails = RandomTool.getEmail(4, 12);
        return Emails;
    }

    private String setOherEmail() {
        String Emails = null;
        if (otheremail.isChecked())
            Emails = RandomTool.getEmail(4, 12);
        return Emails;
    }

    private String setAddress() {
        String Addresss = null;
        if (address.isChecked())
            Addresss = RandomTool.getRoad();
        return Addresss;
    }

    private String setNote() {
        String notes = null;
        if (note.isChecked())
            notes = RandomData.getRandomContactNote();
        return notes;
    }
}
