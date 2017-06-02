package bo.com.paddingdata;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

import bo.com.paddingdata.Tool.WifiAdmin;
import bo.com.paddingdata.data.WIFI;

/**
 * Created by bo.zhang on 2017/05/08   .
 */

public class AddEmailActivity extends Activity {
    private WifiManager mWifiManager;
    private CheckBox gmailAccount;
    private CheckBox qqAccount;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_email);
        gmailAccount = (CheckBox) findViewById(R.id.email_gmail);
        qqAccount = (CheckBox) findViewById(R.id.email_qq);
        start = (Button) findViewById(R.id.addemail);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  loginWifi();
                Toast.makeText(AddEmailActivity.this, "功能未实现！", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loginWifi() {
        WifiAdmin wifiAdmin = new WifiAdmin(AddEmailActivity.this);
        wifiAdmin.openWifi();
        WifiConfiguration ckt = wifiAdmin.CreateWifiInfo(WIFI.NORMAL_ACCOUNT, WIFI.NORMAL_PASSWORD, 3);
        wifiAdmin.addNetwork(ckt);
    }

    public void loginGmail() {
        if (gmailAccount.isChecked()) {

        }
    }

    public void loginQQ() {

        if (qqAccount.isChecked()) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAndRequestForRunntimePermission(AddEmailActivity.this, new String[]{
                "android.permission.CHANGE_NETWORK_STATE", "android.permission.CHANGE_WIFI_STATE"
                , "android.permission.ACCESS_NETWORK_STATE", "android.permission.ACCESS_WIFI_STATE"});
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
