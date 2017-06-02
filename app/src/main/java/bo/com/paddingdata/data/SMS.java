package bo.com.paddingdata.data;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.icu.text.MessageFormat;
import android.net.Uri;
import android.os.Message;
import android.os.MessageQueue;
import android.provider.Telephony;

import bo.com.paddingdata.Tool.RandomData;
import bo.com.paddingdata.Tool.RandomTool;

/**
 * Created by bo.zhang on 2017/05/04   .
 */

public class SMS extends Activity {
    public void addSMS(Context mContext,int type, boolean read,int number){
        ContentResolver resolver=mContext.getContentResolver();
        ContentValues values=new ContentValues();
        values.put(Telephony.Sms.ADDRESS,RandomTool.getTel());
        values.put(Telephony.Sms.DATE, System.currentTimeMillis());
        long dateSent=System.currentTimeMillis()-5000;
        values.put(Telephony.Sms.DATE_SENT,dateSent);
        values.put(Telephony.Sms.READ,read);
        values.put(Telephony.Sms.SEEN,true);
        values.put(Telephony.Sms.STATUS, Telephony.Sms.STATUS_COMPLETE);
        values.put(Telephony.Sms.BODY, "Test SMS_"+number+"\n"+RandomData.getRandomContactName());
        values.put(Telephony.Sms.TYPE, type);
        Uri uri=resolver.insert(Telephony.Sms.CONTENT_URI,values);
        if(uri!=null){
            long uriId= ContentUris.parseId(uri);
            System.out.println("uriId "+uriId);
        }

    }
}
