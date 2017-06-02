package bo.com.paddingdata;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import bo.com.paddingdata.Tool.TestActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Padding_Button = (Button) findViewById(R.id.pading_button);
        Button More_Button = (Button) findViewById(R.id.more_button);

        Padding_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, UserProfiles.class);
                MainActivity.this.startActivity(intent);
            }
        });

        More_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestActivity.class);
                MainActivity.this.startActivity(intent);*/
                Toast.makeText(MainActivity.this,"功能未实现！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAndRequestForRunntimePermission(MainActivity.this,
                new String[]{
                        "android.permission.READ_SMS"});
        checkAndRequestForRunntimePermission(MainActivity.this, new String[]
                {"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS"});
        checkAndRequestForRunntimePermission(MainActivity.this, new String[]
                {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"});

    }

    public static boolean checkAndRequestForRunntimePermission(Activity activity, String[] permissionsNeeded) {

        ArrayList<String> permissionsNeedRequest = new ArrayList<String>();
        for (String permission : permissionsNeeded) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            permissionsNeedRequest.add(permission);
        }
        if (permissionsNeedRequest.size() == 0) {
            return true;
        } else {
            String[] permissions = new String[permissionsNeedRequest.size()];
            permissions = permissionsNeedRequest.toArray(permissions);
            ActivityCompat.requestPermissions(activity, permissions, 0);
            return false;
        }
    }
}
