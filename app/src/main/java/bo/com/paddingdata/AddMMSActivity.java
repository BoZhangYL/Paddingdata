package bo.com.paddingdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import bo.com.paddingdata.Tool.RandomTool;
import bo.com.paddingdata.data.Contacts;
import bo.com.paddingdata.data.InsertMMS;
import bo.com.paddingdata.data.MMS;
import bo.com.paddingdata.data.MMS_DATA;

/**
 * Created by bo.zhang on 2017/05/08   .
 */

public class AddMMSActivity extends Activity {
    private EditText MMS_number;
    private CheckBox mmsSubject;
    private CheckBox mmsBody;
    private RadioButton mmsImage;
    private RadioButton mmsVideo;
    private RadioButton mmsAudio;
    private Button starts;
    private RadioButton Inbox;
    private RadioButton Outbox;
    private RadioButton Sentbox;
    private RadioButton Draftbox;
    private RadioButton Read;
    private RadioButton unRead;
    private static final String IMAGE_NAME_1 = "image_1.jpeg";
    private static final String IMAGE_NAME_2 = "image_2.jpeg";
    private static final String SMIL_TEXT_IMAGE = "<smil><head><layout><root-layout width=\"320px\" height=\"480px\"/><region id=\"Text\" left=\"0\" top=\"320\" width=\"320px\" height=\"160px\" fit=\"meet\"/><region id=\"Image\" left=\"0\" top=\"0\" width=\"320px\" height=\"320px\" fit=\"meet\"/></layout></head><body><par dur=\"2000ms\"><text src=\"text_1.txt\" region=\"Text\"/><img src=\"%s\" region=\"Image\"/></par><par dur=\"2000ms\"><text src=\"text_2.txt\" region=\"Text\"/><img src=\"%s\" region=\"Image\"/></par></body></smil>";
    private static final String IMAGE_CID = "<img_cid>";

    private static final String AUDIO_NAME = "audio_1.ogg";
    private static final String SMIL_TEXT_AUDIO = "<smil><head><layout><root-layout width=\"320px\" height=\"480px\"/><region id=\"Text\" left=\"0\" top=\"320\" width=\"320px\" height=\"160px\" fit=\"meet\"/><region id=\"Image\" left=\"0\" top=\"0\" width=\"320px\" height=\"320px\" fit=\"meet\"/></layout></head><body><par dur=\"21500ms\"><text src=\"text_1.txt\" region=\"Text\"/><audio src=\""
            + AUDIO_NAME + "\" dur=\"21500\" /></par></body></smil>";
    private static final String AUDIO_CID = "<300k>";

    private static final String VIDEO_NAME = "video_1.3gp";
    private static final String SMIL_TEXT_VIDEO = "<smil><head><layout><root-layout width=\"320px\" height=\"480px\"/><region id=\"Text\" left=\"0\" top=\"320\" width=\"320px\" height=\"160px\" fit=\"meet\"/><region id=\"Image\" left=\"0\" top=\"0\" width=\"320px\" height=\"320px\" fit=\"meet\"/></layout></head><body><par dur=\"21500ms\"><text src=\"text_1.txt\" region=\"Text\"/><VIDEO src=\""
            + VIDEO_NAME + "\" dur=\"21500\" /></par></body></smil>";
    private static final String VIDEO_CID = "<300k>";

    private String FROM_NUM = RandomTool.getTel();
    int i = 0, j = 0, k = 0;
    private ProgressDialog mypDialog;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (j++ < i) {
                insert(setType(), getAttchemnts(), setRead(), j);
                mypDialog.setProgress(j);
            } else {
                mypDialog.setMessage("彩信数据添加完成！");
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
        setContentView(R.layout.add_mms);
        MMS_number = (EditText) findViewById(R.id.mms_number);
        mmsBody = (CheckBox) findViewById(R.id.mms_body);
        mmsImage = (RadioButton) findViewById(R.id.mms_picture);
        mmsVideo = (RadioButton) findViewById(R.id.mms_video);
        mmsAudio = (RadioButton) findViewById(R.id.mms_music);
        starts = (Button) findViewById(R.id.addMMS);
        Inbox = (RadioButton) findViewById(R.id.mms_inbox);
        Outbox = (RadioButton) findViewById(R.id.mms_outbox);
        Sentbox = (RadioButton) findViewById(R.id.mms_sent);
        Draftbox = (RadioButton) findViewById(R.id.mms_draft);
        Read = (RadioButton) findViewById(R.id.mms_read);
        unRead = (RadioButton) findViewById(R.id.mms_unread);
        starts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = setMMSNumber();
                mypDialog = new ProgressDialog(AddMMSActivity.this);
                mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mypDialog.setTitle("填充数据");
                mypDialog.setMessage("正在添加彩信，请稍等。。");
                mypDialog.setIcon(R.mipmap.mms);
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
            Intent intent =
                    new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                    myPackageName);
            startActivity(intent);
        }
    }

    private int setMMSNumber() {
        if (MMS_number.getText() == null)
            return 0;
        else
            return Integer.valueOf(String.valueOf(MMS_number.getText()));
    }

    private int setType() {
        int i = 1;
        if (Inbox.isChecked()) {
            i = Telephony.Mms.MESSAGE_BOX_INBOX;
        } else if (Outbox.isChecked()) {
            i = Telephony.Mms.MESSAGE_BOX_OUTBOX;
        } else if (Sentbox.isChecked()) {
            i = Telephony.Mms.MESSAGE_BOX_SENT;
        } else if (Draftbox.isChecked()) {
            i = Telephony.Mms.MESSAGE_BOX_DRAFTS;
        }
        return i;
    }

    private AttachmentType getAttchemnts() {
        AttachmentType x = null;
        if (mmsImage.isChecked()) {
            x = AttachmentType.IMAGE;
        } else if (mmsVideo.isChecked()) {
            x = AttachmentType.VIDEO;
        } else if (mmsAudio.isChecked()) {
            x = AttachmentType.AUDIO;
        }
        return x;
    }

    private boolean setRead() {
        boolean readsate = false;
        if (Read.isChecked()) {
            readsate = true;
        } else if (unRead.isChecked()) {
            readsate = false;
        }
        return readsate;
    }

    private void insert(int msgBoxType, AttachmentType type, boolean readState, int idx) {
        long threadId = Telephony.Threads.getOrCreateThreadId(AddMMSActivity.this, FROM_NUM + idx);
        Log.e("", "threadId = " + threadId);

        String name_1 = null;
        String name_2 = null;
        String smil_text = null;
        ContentValues cv_part_1 = null;
        ContentValues cv_part_2 = null;

        switch (type) {
            case IMAGE:
                name_1 = IMAGE_NAME_1;
                name_2 = IMAGE_NAME_2;
                smil_text = String.format(SMIL_TEXT_IMAGE, name_1, name_2);
                cv_part_1 = createPartRecord(0, "image/jpeg", name_1, IMAGE_CID, name_1, null, null);
                cv_part_2 = createPartRecord(0, "image/jpeg", name_2, IMAGE_CID.replace("cid", "cid_2"), name_2, null, null);
                break;
            case AUDIO:
                name_1 = AUDIO_NAME;
                smil_text = String.format(SMIL_TEXT_AUDIO, name_1);
                cv_part_1 = createPartRecord(0, "audio/ogg", AUDIO_NAME, AUDIO_CID, AUDIO_NAME, null, null);
                break;
            case VIDEO:
                name_1 = VIDEO_NAME;
                smil_text = String.format(SMIL_TEXT_VIDEO, name_1);
                cv_part_1 = createPartRecord(0, "video/3gpp", VIDEO_NAME, VIDEO_CID, VIDEO_NAME, null, null);
                break;
        }

        // make MMS record
        ContentValues cvMain = new ContentValues();
        cvMain.put(Telephony.Mms.THREAD_ID, threadId);

        cvMain.put(Telephony.Mms.MESSAGE_BOX, msgBoxType);
        cvMain.put(Telephony.Mms.READ, 1);
        cvMain.put(Telephony.Mms.DATE, System.currentTimeMillis() / 1000);
        cvMain.put(Telephony.Mms.SUBJECT, "my subject " + idx);
        cvMain.put(Telephony.Mms.CONTENT_TYPE, "application/vnd.wap.multipart.related");
        cvMain.put(Telephony.Mms.MESSAGE_CLASS, "personal");
        cvMain.put(Telephony.Mms.MESSAGE_TYPE, 132); // Retrive-Conf Mms
        if (type.equals(AttachmentType.IMAGE)) {
            cvMain.put(Telephony.Mms.MESSAGE_SIZE, getSize(name_1) + getSize(name_2) + 512);
        } else if (type.equals(AttachmentType.AUDIO)) {
            cvMain.put(Telephony.Mms.MESSAGE_SIZE, getSize(name_1) + 512);
        } else if (type.equals(AttachmentType.VIDEO)) {
            cvMain.put(Telephony.Mms.MESSAGE_SIZE, getSize(name_1) + 512);
        }
        // cvMain.put(Telephony.Mms.MESSAGE_SIZE, getSize(name_1) + getSize(name_2) + 512);  // suppose have 512 bytes extra text size
        cvMain.put(Telephony.Mms.PRIORITY, String.valueOf(129));
        cvMain.put(Telephony.Mms.READ_REPORT, String.valueOf(129));
        cvMain.put(Telephony.Mms.DELIVERY_REPORT, String.valueOf(129));
        Random random = new Random();
        cvMain.put(Telephony.Mms.MESSAGE_ID, String.valueOf(random.nextInt(100000)));
        cvMain.put(Telephony.Mms.TRANSACTION_ID, String.valueOf(random.nextInt(120000)));
        cvMain.put(Telephony.Mms.SEEN, true);
        cvMain.put(Telephony.Mms.READ, readState);

        long msgId = 0;
        try {
            msgId = ContentUris.parseId(getContentResolver().insert(Telephony.Mms.CONTENT_URI, cvMain));
        } catch (Exception e) {
            Log.e("", "insert pdu record failed", e);
            return;
        }
        if (type.equals(AttachmentType.IMAGE)) {
            // make parts
            ContentValues cvSmil = createPartRecord(-1, "application/smil", "smil.xml", "<siml>", "smil.xml", null, smil_text);
            cvSmil.put(Telephony.Mms.Part.MSG_ID, msgId);

            cv_part_1.put(Telephony.Mms.Part.MSG_ID, msgId);
            cv_part_2.put(Telephony.Mms.Part.MSG_ID, msgId);

            ContentValues cv_text_1 = createPartRecord(0, "text/plain", "text_1.txt", "<text_1>", "text_1.txt", null, null);
            cv_text_1.put(Telephony.Mms.Part.MSG_ID, msgId);
            cv_text_1.remove(Telephony.Mms.Part.TEXT);
            cv_text_1.put(Telephony.Mms.Part.TEXT, "slide 1 text");
            cv_text_1.put(Telephony.Mms.Part.CHARSET, "106");

            ContentValues cv_text_2 = createPartRecord(0, "text/plain", "text_2.txt", "<text_2>", "text_2.txt", null, null);
            cv_text_2.put(Telephony.Mms.Part.MSG_ID, msgId);
            cv_text_2.remove(Telephony.Mms.Part.TEXT);
            cv_text_2.put(Telephony.Mms.Part.TEXT, "slide 2 text");
            cv_text_2.put(Telephony.Mms.Part.CHARSET, "106");

            // insert parts
            Uri partUri = Uri.parse("content://mms/" + msgId + "/part");
            try {
                getContentResolver().insert(partUri, cvSmil);

                Uri dataUri_1 = getContentResolver().insert(partUri, cv_part_1);
                if (!copyData(dataUri_1, name_1)) {
                    Log.e("", "write " + name_1 + " failed");
                    return;
                }
                getContentResolver().insert(partUri, cv_text_1);

                Uri dataUri_2 = getContentResolver().insert(partUri, cv_part_2);
                if (!copyData(dataUri_2, name_2)) {
                    Log.e("", "write " + name_2 + " failed");
                    return;
                }
                getContentResolver().insert(partUri, cv_text_2);
            } catch (Exception e) {
                Log.e("", "insert part failed", e);
                return;
            }
        } else if (type.equals(AttachmentType.AUDIO)) {
            // make parts
            ContentValues cvSmil = createPartRecord(-1, "application/smil", "smil.xml", "<siml>", "smil.xml", null, smil_text);
            cvSmil.put(Telephony.Mms.Part.MSG_ID, msgId);

            cv_part_1.put(Telephony.Mms.Part.MSG_ID, msgId);

            ContentValues cv_text_1 = createPartRecord(0, "text/plain", "text_1.txt", "<text_1>", "text_1.txt", null, null);
            cv_text_1.put(Telephony.Mms.Part.MSG_ID, msgId);
            cv_text_1.remove(Telephony.Mms.Part.TEXT);
            cv_text_1.put(Telephony.Mms.Part.TEXT, "slide 1 text");
            cv_text_1.put(Telephony.Mms.Part.CHARSET, "106");

            // insert parts
            Uri partUri = Uri.parse("content://mms/" + msgId + "/part");
            try {
                getContentResolver().insert(partUri, cvSmil);

                Uri dataUri_1 = getContentResolver().insert(partUri, cv_part_1);
                if (!copyData(dataUri_1, name_1)) {
                    Log.e("", "write " + name_1 + " failed");
                    return;
                }
                getContentResolver().insert(partUri, cv_text_1);

            } catch (Exception e) {
                Log.e("", "insert part failed", e);
                return;
            }

        } else if (type.equals(AttachmentType.VIDEO)) {
            ; // make parts
            ContentValues cvSmil = createPartRecord(-1, "application/smil", "smil.xml", "<siml>", "smil.xml", null, smil_text);
            cvSmil.put(Telephony.Mms.Part.MSG_ID, msgId);

            cv_part_1.put(Telephony.Mms.Part.MSG_ID, msgId);

            ContentValues cv_text_1 = createPartRecord(0, "text/plain", "text_1.txt", "<text_1>", "text_1.txt", null, null);
            cv_text_1.put(Telephony.Mms.Part.MSG_ID, msgId);
            cv_text_1.remove(Telephony.Mms.Part.TEXT);
            cv_text_1.put(Telephony.Mms.Part.TEXT, "slide 1 text");
            cv_text_1.put(Telephony.Mms.Part.CHARSET, "106");

            // insert parts
            Uri partUri = Uri.parse("content://mms/" + msgId + "/part");
            try {
                getContentResolver().insert(partUri, cvSmil);

                Uri dataUri_1 = getContentResolver().insert(partUri, cv_part_1);
                if (!copyData(dataUri_1, name_1)) {
                    Log.e("", "write " + name_1 + " failed");
                    return;
                }
                getContentResolver().insert(partUri, cv_text_1);

            } catch (Exception e) {
                Log.e("", "insert part failed", e);
                return;
            }
        }

        // to address
        ContentValues cvAddr = new ContentValues();
        cvAddr.put(Telephony.Mms.Addr.MSG_ID, msgId);
        cvAddr.put(Telephony.Mms.Addr.ADDRESS, "703");
        cvAddr.put(Telephony.Mms.Addr.TYPE, "151");
        cvAddr.put(Telephony.Mms.Addr.CHARSET, 106);
        getContentResolver().insert(Uri.parse("content://mms/" + msgId + "/addr"), cvAddr);

        // from address
        cvAddr.clear();
        cvAddr.put(Telephony.Mms.Addr.MSG_ID, msgId);
        cvAddr.put(Telephony.Mms.Addr.ADDRESS, FROM_NUM);
        cvAddr.put(Telephony.Mms.Addr.TYPE, "137");
        cvAddr.put(Telephony.Mms.Addr.CHARSET, 106);
        getContentResolver().insert(Uri.parse("content://mms/" + msgId + "/addr"), cvAddr);
    }

    private int getSize(final String name) {
        InputStream is = null;
        int size = 0;

        try {
            is = getAssets().open(name);
            byte[] buffer = new byte[1024];
            for (int len = 0; (len = is.read(buffer)) != -1; )
                size += len;
            return size;
        } catch (FileNotFoundException e) {
            Log.e("", "failed to found file?", e);
            return 0;
        } catch (IOException e) {
            Log.e("", "write failed..", e);
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                Log.e("", "close failed...");
            }
        }
        return 0;
    }

    private ContentValues createPartRecord(int seq, String ct, String name, String cid, String cl, String data,
                                           String text) {
        ContentValues cv = new ContentValues(8);
        cv.put(Telephony.Mms.Part.SEQ, seq);
        cv.put(Telephony.Mms.Part.CONTENT_TYPE, ct);
        cv.put(Telephony.Mms.Part.NAME, name);
        cv.put(Telephony.Mms.Part.CONTENT_ID, cid);
        cv.put(Telephony.Mms.Part.CONTENT_LOCATION, cl);
        if (data != null)
            cv.put(Telephony.Mms.Part._DATA, data);
        if (text != null)
            cv.put(Telephony.Mms.Part.TEXT, text);
        return cv;
    }

    private boolean copyData(final Uri dataUri, final String name) {
        InputStream input = null;
        OutputStream output = null;

        try {
            input = getAssets().open(name);
            output = getContentResolver().openOutputStream(dataUri);

            byte[] buffer = new byte[1024];
            for (int len = 0; (len = input.read(buffer)) != -1; )
                output.write(buffer, 0, len);
        } catch (FileNotFoundException e) {
            Log.e("", "failed to found file?", e);
            return false;
        } catch (IOException e) {
            Log.e("", "write failed..", e);
            return false;
        } finally {
            try {
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
            } catch (IOException e) {
                Log.e("", "close failed...");
                return false;
            }
        }
        return true;
    }


    enum AttachmentType {
        IMAGE, AUDIO, VIDEO;
    }
}
