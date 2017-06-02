package bo.com.paddingdata.data;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.wifi.WifiConfiguration;
import android.provider.ContactsContract;

/**
 * Created by bo.zhang on 2017/05/10   .
 */

public class Email extends Activity {
    public static final String GMAIL_ACCOUNT = "cktcd1@gmail.com";
    public static final String GMAIL_PASSWORD = "cKt@cd1!1";

    public static final String QQ_ACCOUNT = "1095513258@qq.com";
    public static final String QQ_PASSWORD = "ckt88cd!";

    public void addGmailAccount() {
        ContentResolver resolver = Email.this.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, GMAIL_ACCOUNT);
        values.put("", ContactsContract.CommonDataKinds.Email.TYPE_WORK);

    }

    public void addQQAccount() {
    
    }
}
