package bo.com.paddingdata.data;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.Telephony;
import android.provider.Telephony.Mms;
import android.provider.Telephony.Mms.Addr;
import android.provider.Telephony.Mms.Part;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import bo.com.paddingdata.Tool.RandomTool;

/**
 * Created by bo.zhang on 2017/05/08   .
 */

public class MMS extends Activity {
    private static final String IMAGE_CID = "<img_cid>";
    private static String phoneNumb = null;

    public void addMMS(/*Context mContext, int msgBoxType, boolean read, String number*/
                       Context mContext, int msgBoxType, String subject, String number, int ids) throws IOException {
        long threadId = Telephony.Threads.getOrCreateThreadId(mContext, number);
        ContentValues cvMain = new ContentValues();
        cvMain.put(Telephony.Mms.THREAD_ID, threadId);
        cvMain.put(Mms.MESSAGE_BOX, msgBoxType);
        cvMain.put(Mms.READ, 1);
        cvMain.put(Mms.DATE, System.currentTimeMillis() / 1000);
        cvMain.put(Mms.SUBJECT_CHARSET, 106);
        cvMain.put(Mms.CONTENT_TYPE, "application/vnd.wap.multipart.mixed");
        cvMain.put(Mms.MESSAGE_CLASS, "personal");
        cvMain.put(Mms.MESSAGE_TYPE, 132); // Retrive-Conf Mms
        cvMain.put(Mms.MESSAGE_SIZE, 512);
        cvMain.put(Mms.PRIORITY, String.valueOf(129));
        cvMain.put(Mms.READ_REPORT, String.valueOf(129));
        cvMain.put(Mms.DELIVERY_REPORT, String.valueOf(129));
        Random random = new Random();
        cvMain.put(Mms.SUBJECT, subject);
        cvMain.put(Mms.MESSAGE_ID, String.valueOf(random.nextInt(100000)));
        cvMain.put(Mms.TRANSACTION_ID, String.valueOf(random.nextInt(120000)));
        long msgId = 0;
        try {
            msgId = ContentUris.parseId(mContext.getContentResolver().insert(
                    Mms.CONTENT_URI, cvMain));
        } catch (Exception e) {
            Log.e("", "insert pdu record failed", e);
            return;
        }

        // to address
        ContentValues cvAddr = new ContentValues();
        cvAddr.put(Addr.MSG_ID, msgId);
        cvAddr.put(Addr.ADDRESS, number);
        cvAddr.put(Addr.TYPE, "151");
        cvAddr.put(Addr.CHARSET, 106);
        mContext.getContentResolver().insert(
                Uri.parse("content://mms/" + msgId + "/addr"), cvAddr);
        // 将数据库彩信信息关联到发件人
        phoneNumb = RandomTool.getTel();
        cvAddr.clear();
        cvAddr.put(Addr.MSG_ID, msgId);
        cvAddr.put(Addr.ADDRESS, phoneNumb);
        cvAddr.put(Addr.TYPE, "137");
        cvAddr.put(Addr.CHARSET, 106);
        mContext.getContentResolver().insert(Uri.parse("content://mms/" + msgId + "/addr"), cvAddr);

        /*
        * 添加图片附件
        * */
        Uri partUri = Uri.parse("content://mms/" + msgId + "/part");
        String pathP = Environment.getExternalStorageDirectory() + "/Pictures/background.png";
        File file = new File(pathP);
        String pic = String.valueOf(Uri.fromFile(file));
        File smilFile = new File(Environment.getExternalStorageDirectory() + "/Pictures/smil.xml");
        String smil = openText(smilFile.getAbsolutePath());
        System.out.println("smil: " + smil);
        ContentValues cvs = createPartRecord(-1, "application/smil",
                "smil.xml", "<siml>", "smil.xml", null, smil);
        cvs.put(Part.MSG_ID, msgId);
        // 把数据插入数据库
        mContext.getContentResolver().insert(partUri, cvs);
        ContentValues cvSmil = createPartRecord(0, "image/png",
                pic, IMAGE_CID, pic, null, null);
        cvSmil.put(Part.MSG_ID, msgId);
        mContext.getContentResolver().insert(partUri, cvSmil);

    }

    private byte[] getPicture(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    private static ContentValues createPartRecord(int seq, String ct,
                                                  String name, String cid, String cl, String data, String text) {
        ContentValues cv = new ContentValues(8);
        cv.put(Part.SEQ, seq);
        cv.put(Part.CONTENT_TYPE, ct);
        cv.put(Part.NAME, name);
        cv.put(Part.CONTENT_ID, cid);
        cv.put(Part.CONTENT_LOCATION, cl);
        if (data != null)
            cv.put(Part._DATA, data);
        if (text != null)
            cv.put(Part.TEXT, text);
        return cv;
    }

    /**
     * 读取文件内容
     *
     * @param filename 文件名称
     */
    public static String read(String filename) {
        byte[] data = null;
        try {
            byte[] buffer = new byte[1024];
            File file = new File(filename);
            if (!file.exists()) {
              /*  Toast.makeText(mContext, R.string.mms_pak_error,
                        Toast.LENGTH_LONG).show();*/
                return new String(data);
            }
            FileInputStream inStream = new FileInputStream(file);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
            return new String(data, "gbk");
        } catch (IOException e) {
            Log.i("IOException", e.toString());

        }
        return null;
    }

    public String getFromAssets(String fileName) {
        String Result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";

            while ((line = bufReader.readLine()) != null)
                Result += line;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }

    public String openText(String UrlStr) {
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(UrlStr), "UTF-8")); //这里可以控制编码
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String data = new String(sb); //StringBuffer ==> String
        return data;
    }
}