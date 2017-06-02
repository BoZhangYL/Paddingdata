package bo.com.paddingdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by bo.zhang on 2017/05/08   .
 */

public class UserProfiles extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Button Contacts = (Button) findViewById(R.id.contcts_icon);
        Button Emails = (Button) findViewById(R.id.email_icon);
        Button SMS = (Button) findViewById(R.id.sms_icon);
        Button MMS = (Button) findViewById(R.id.mms_icon);

        Contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfiles.this.startActivity(new Intent().setClass(UserProfiles.this,
                        AddContactsActivity.class));
            }
        });

        Emails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfiles.this.startActivity(new Intent().setClass(UserProfiles.this,
                        AddEmailActivity.class));
            }
        });

        SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfiles.this.startActivity(new Intent().setClass(UserProfiles.this,
                        AddSMSActivity.class));
            }
        });

        MMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfiles.this.startActivity(new Intent().setClass(UserProfiles.this,
                        AddMMSActivity.class));
            }
        });
    }
}
