package bo.com.paddingdata.data;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.*;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import bo.com.paddingdata.R;
import android.provider.ContactsContract.Contacts.Data;
/**
 * Created by bo.zhang on 2017/05/04   .
 */

public class Contacts {
    /**
     * 往数据库中新增联系人
     *
     * @param mContext
     * @param name
     * @param
     */
    public static void AddContact(Context mContext, String name, String mobilephone,
                                  String homephone, String pagerphone, String otherphone,
                                  String callbackphone, String workphone, String homeemail,
                                  String workemail, String mobileemail, String otheremail,
                                  String note, String address) {
        //创建一个空的ContentValues
        ContentValues values = new ContentValues();
        //向ContactsContract.RawContacts.CONTENT_URI执行一个空值插入
        //目的是获取系统返回的rawContactId，以便添加联系人名字和电话使用同一个id
        Uri rawContactUri = mContext.getContentResolver().insert(
                ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人姓名
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        //向联系人URI添加联系人姓名
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人电话
        //设置电话类型-mobile
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, mobilephone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人电话
        //设置电话类型-Home
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, homephone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);


        //清空values
        //设置id
        //设置内容类型
        //设置联系人电话
        //设置电话类型-pager
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, pagerphone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_PAGER);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人电话
        //设置电话类型-other
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, otherphone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_OTHER);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人电话
        //设置电话类型-callback
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, callbackphone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人电话
        //设置电话类型-other
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, workphone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人email
        //设置Email类型-Home
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, homeemail);
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人email
        //设置Email类型-WORK
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, workemail);
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人email
        //设置Email类型-mobile
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, mobileemail);
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人email
        //设置Email类型-ther
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, otheremail);
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_OTHER);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        //清空values
        //设置id
        //设置内容类型
        //设置联系人note
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Note.NOTE, note);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        // 向data表插入头像数据
        Bitmap sourceBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.lxr);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将Bitmap压缩成PNG编码，质量为100%存储
        sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] avatar = os.toByteArray();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, avatar);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                values);
        //清空values
        //设置id
        //设置内容类型
        //设置联系人note
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE, address);
        mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
    }

    /**
     * 得到数据库数量信息
     */
    public static String getContactsDataNum(Context mContext) {
        //使用ContentResolver查找联系人数据
        Cursor cursor = mContext.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int count = cursor.getCount();

        //使用完毕关闭Cursor
        cursor.close();
        return String.valueOf(count);
    }

    /**
     * 得到数据库信息
     */
    public static List<HashMap> getContactsData(Context mContext) {
        //使用ContentResolver查找联系人数据
        Cursor cursor = mContext.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        CopyOnWriteArrayList<HashMap> mListItems = new CopyOnWriteArrayList<>();
        //便利查询结果，获取系统中的所有人
        while (cursor.moveToNext()) {
            //获取联系人id
            String contactId = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.Contacts._ID));
            // LogUtils.i("huangxiaoguo", contactId);
            /**
             * 获取联系人姓名
             */
            String name = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME));
            //  LogUtils.i("huangxiaoguo", name);

            //使用过ContentResolver通过id查找联系人的电话
            //此处为了方便显示，只取联系人的第一个号码（可能有多个号码）
            Cursor phones = mContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId
                    , null, null);
            phones.moveToNext();
            /**
             * 联系人的电话
             */
            String phone = phones.getString(phones.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));
            //    LogUtils.i("huangxiaoguo", phone);
            //使用完毕关闭Cursor
            phones.close();

            //创建Map添加到mListItems中用于创建SimpleAdapter
            HashMap<String, String> listItem = new HashMap<String, String>();
            listItem.put("name", name);
            listItem.put("phone", phone);
            mListItems.add(listItem);
        }
        //使用完毕关闭Cursor
        cursor.close();
        return mListItems;
    }

    /**
     * 批量添加通讯录

     */
    public void BatchAddContact(Context mContext,List<Tb_contacts> list)
            throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = 0;
        for (Tb_contacts contact : list) {
            rawContactInsertIndex = ops.size(); // 有了它才能给真正的实现批量添加

            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true).build());

            // 添加姓名
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .withYieldAllowed(true).build());
            // 添加号码
            ops.add(ContentProviderOperation
                    .newInsert(
                            android.provider.ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, "").withYieldAllowed(true).build());
        }
        if (ops != null) {
            // 真正添加

            ContentProviderResult[] results = mContext.getContentResolver()
                    .applyBatch(ContactsContract.AUTHORITY, ops);
            // for (ContentProviderResult result : results) {
            // GlobalConstants
            // .PrintLog_D("[GlobalVariables->]BatchAddContact "
            // + result.uri.toString());
            // }
        }
    }

    public class Tb_contacts {
        private String name;
        private String number;

        public Tb_contacts() {
            super();
        }

        public Tb_contacts(String name, String number) {
            super();
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "Tb_contacts [name=" + name + ", number=" + number + "]";
        }

    }


}
