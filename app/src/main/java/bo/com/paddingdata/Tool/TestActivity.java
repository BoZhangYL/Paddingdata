package bo.com.paddingdata.Tool;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import bo.com.paddingdata.R;

public class TestActivity extends Activity {
    /** Called when the activity is first created. */
    private Button sendButton;
    private ImageView imageview;
    private Bitmap bitmap;
    private EditText fromText;
    private EditText toText;
    private EditText contentText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testreceiver);
        sendButton = (Button)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new BroadcastListener());
        imageview=(ImageView)findViewById(R.id.imageView1);
        fromText=(EditText)findViewById(R.id.fromText);
        toText=(EditText)findViewById(R.id.toText);
        contentText=(EditText)findViewById(R.id.contentText);

    }
    class BroadcastListener implements OnClickListener{

        @Override
        public void onClick(View v) {
//			String selection2 = new String("");
            Cursor cur = getContentResolver().query(Uri.parse("content://mms"),null, null, null, null);
            int key=cur.getColumnIndex("_id");

            //只查看第一条，如需遍历，需修改
            if(cur.moveToFirst())
            {
                key=cur.getInt(key);
                System.out.println("whb:"+key);

                //根据彩信id从addr表中查出发送人电话号码，其中msg_id对应pdu表的id；
                String selectionAddr = new String("msg_id=" + key );
                Uri uriAddr = Uri.parse("content://mms/" + key + "/addr");
                Cursor addr = getContentResolver().query(uriAddr, null, selectionAddr, null, null);
                //每条记录一般对应两个号码，发送人和接受人
                if(addr.moveToFirst()){
                    String number= addr.getString(addr.getColumnIndex("address"));
                    fromText.setText(number);
                    if(addr.moveToNext())
                    {
                        number= addr.getString(addr.getColumnIndex("address"));
                        toText.setText(number);
                    }
                }

                String selection = new String("mid=" + key );//这个key就是pdu里面的_id。
                cur = getContentResolver().query(Uri.parse("content://mms/part"), null, selection, null, null);
                if (cur.moveToFirst())
                    do {
                        //读取id
                        int _partID = cur.getInt(cur.getColumnIndex("_id"));
                        String partID = String.valueOf(_partID);
                        System.out.println("whb:"+partID);
                        //读取text内容
//	        			String text = cur.getString(cur.getColumnIndex("ct"));
//	        			if(text.contentEquals("text/plain"))
//	        				text=cur.getString(cur.getColumnIndex("text"));
//	        			System.out.println("whb"+text);

                        //读取类型
                        String text = cur.getString(cur.getColumnIndex("ct"));
                        if(text.startsWith("image"))
                        {//读取彩信图片
                            bitmap=getMmsImage(partID);
                            if(bitmap!=null)
                                imageview.setImageBitmap(bitmap);
                            System.out.println("whb:image/jpeg");
                        }
                        else if(text.startsWith("text"))
                        {//读取彩信文本信息
                            String text2=getMmsText(partID);
                            contentText.setText(text2);
                            System.out.println("whb:"+text2);
                        }
                    } while(cur.moveToNext());
            }
        }

    }
    //发送彩信
    private void sendSms()
    {
        String imagePath1 = "/sdcard/image1.png";
        String imagePath2 = "/sdcard/image2.png";
        Uri uri1 = Uri.parse("file://" + imagePath1);
        Uri uri2 = Uri.parse("file://" + imagePath2);
        ArrayList<Uri> uris = new ArrayList<Uri>();
        uris.add(uri1);
        uris.add(uri2);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_STREAM, uris);//uri为你的附件的uri
//    	intent.putExtra("subject", subString);
        //intent.putExtra("sms_body", "sdfsdf");
        intent.putExtra(Intent.EXTRA_TEXT, "sdfsdf");
        intent.setType("image/*");//彩信附件类型
        intent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
        startActivity(intent);
    }
    private String getMmsText(String _id){ //读取文本附件
        Uri partURI = Uri.parse("content://mms/part/" + _id );
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = getContentResolver().openInputStream(partURI);
            if(is!=null){
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp=reader.readLine();//在网上看到很多把InputStream转成string的文章，没有这关键的一句，几乎千遍一律的复制粘贴，该处如果不加上这句的话是会内存溢出的
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
            Log.v("whb", "读取附件异常"+e.getMessage());
        }finally{
            if (is != null){
                try {
                    is.close();
                }catch (IOException e){
                    Log.v("whb", "读取附件异常"+e.getMessage());
                }
            }
        }
        return sb.toString();
    }
    private Bitmap getMmsImage(String _id){ //读取图片附件
        Uri partURI = Uri.parse("content://mms/part/" + _id );
        InputStream is = null;
        Bitmap bitmap=null;
        try {
            is = getContentResolver().openInputStream(partURI);
            if(is==null)
            {
                System.out.println("whb:null InputStream");
                return bitmap;
            }
            bitmap = BitmapFactory.decodeStream(is);
        }catch (IOException e) {
            System.out.println("whb:"+"读取图片异常"+e.getMessage());
            e.printStackTrace();
        }finally{
            if (is != null){
                try {
                    is.close();
                }catch (IOException e){
                    System.out.println("whb:"+"读取图片异常"+e.getMessage());
                }
            }
        }
        return bitmap;
    }
}