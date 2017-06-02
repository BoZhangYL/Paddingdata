package bo.com.paddingdata.Tool;
    /* ---------------------------------------------------------------------------------------------
     *
     * Capital Alliance Software Confidential Proprietary
     * (c) Copyright CAS 201{x}, All Rights Reserved
     * www.pekall.com
     *
     * ----------------------------------------------------------------------------------------------
     */

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

public final class Telephony {
    private static final String TAG = "Telephony";


    public static int iccToSms(int iccStatus) {
        switch (iccStatus) {
            case SmsManager.STATUS_ON_ICC_READ:


                return Sms.MESSAGE_TYPE_INBOX;
            case SmsManager.STATUS_ON_ICC_UNREAD:


                return Sms.MESSAGE_TYPE_INBOX;
            case SmsManager.STATUS_ON_ICC_SENT:


                return Sms.MESSAGE_TYPE_SENT;
            case SmsManager.STATUS_ON_ICC_UNSENT:


                return Sms.MESSAGE_TYPE_OUTBOX;
            default:
                return Sms.MESSAGE_TYPE_ALL;
        }
    }


    public static int SmsToIcc(int smsType, boolean isRead) {
        switch (smsType) {
            case Sms.MESSAGE_TYPE_INBOX:
                if (isRead)
                    return SmsManager.STATUS_ON_ICC_READ;
                else
                    return SmsManager.STATUS_ON_ICC_UNREAD;
            case Sms.MESSAGE_TYPE_SENT:
                return SmsManager.STATUS_ON_ICC_SENT;
            case Sms.MESSAGE_TYPE_OUTBOX:
                return SmsManager.STATUS_ON_ICC_UNSENT;
            case Sms.MESSAGE_TYPE_FAILED:
                return SmsManager.STATUS_ON_ICC_UNSENT;
            default:
                return SmsManager.STATUS_ON_ICC_FREE;
        }
    }


    public interface TextBasedSmsColumns {
        /**
         * The type of the message
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String TYPE = "type";


        public static final int MESSAGE_TYPE_ALL = 0;
        public static final int MESSAGE_TYPE_INBOX = 1;
        public static final int MESSAGE_TYPE_SENT = 2;
        public static final int MESSAGE_TYPE_DRAFT = 3;
        public static final int MESSAGE_TYPE_OUTBOX = 4;
        public static final int MESSAGE_TYPE_FAILED = 5; // for failed outgoing
        // messages
        public static final int MESSAGE_TYPE_QUEUED = 6; // for messages to send
        // later


        /**
         * The thread ID of the message
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String THREAD_ID = "thread_id";


        /**
         * The address of the other party
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String ADDRESS = "address";


        /**
         * The person ID of the sender
         * <p>
         * Type: INTEGER (long)
         * </P>
         */
        public static final String PERSON_ID = "person";


        /**
         * The date the message was received
         * <p>
         * Type: INTEGER (long)
         * </P>
         */
        public static final String DATE = "date";


        /**
         * The date the message was sent
         * <p>
         * Type: INTEGER (long)
         * </P>
         */
        public static final String DATE_SENT = "date_sent";


        /**
         * Has the message been read
         * <p>
         * Type: INTEGER (boolean)
         * </P>
         */
        public static final String READ = "read";


        /**
         * Indicates whether this message has been seen by the user. The "seen"
         * flag will be used to figure out whether we need to throw up a
         * statusbar notification or not.
         */
        public static final String SEEN = "seen";


        /**
         * The TP-Status value for the message, or -1 if no status has been
         * received
         */
        public static final String STATUS = "status";


        public static final int STATUS_NONE = -1;
        public static final int STATUS_COMPLETE = 0;
        public static final int STATUS_PENDING = 32;
        public static final int STATUS_FAILED = 64;


        /**
         * The subject of the message, if present
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String SUBJECT = "subject";


        /**
         * The body of the message
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String BODY = "body";


        /**
         * The id of the sender of the conversation, if present
         * <p>
         * Type: INTEGER (reference to item in content://contacts/people)
         * </P>
         */
        public static final String PERSON = "person";


        /**
         * The protocol identifier code
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String PROTOCOL = "protocol";


        /**
         * Whether the <code>TP-Reply-Path</code> bit was set on this message
         * <p>
         * Type: BOOLEAN
         * </P>
         */
        public static final String REPLY_PATH_PRESENT = "reply_path_present";


        /**
         * The service center (SC) through which to send the message, if present
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String SERVICE_CENTER = "service_center";


        /**
         * Has the message been locked?
         * <p>
         * Type: INTEGER (boolean)
         * </P>
         */
        public static final String LOCKED = "locked";


        /**
         * Error code associated with sending or receiving this message
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String ERROR_CODE = "error_code";


        /**
         * Meta data used externally.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String META_DATA = "meta_data";
    }


    public static final class Sms implements BaseColumns, TextBasedSmsColumns {
        public static final Cursor query(ContentResolver cr, String[] projection) {
            return cr.query(CONTENT_URI, projection, null, null, DEFAULT_SORT_ORDER);
        }


        public static final Cursor query(ContentResolver cr, String[] projection, String where, String orderBy) {
            return cr.query(CONTENT_URI, projection, where, null, orderBy == null ? DEFAULT_SORT_ORDER : orderBy);
        }


        /**
         * The authority for the sms provider
         */
        public static final String AUTHORITY = "sms";
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);


        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "date DESC";


        /**
         * Add an SMS to the given URI.
         *
         * @param resolver       the content resolver to use
         * @param uri            the URI to add the message to
         * @param address        the address of the sender
         * @param body           the body of the message
         * @param subject        the psuedo-subject of the message
         * @param date           the timestamp for the message
         * @param read           true if the message has been read, false if not
         * @param deliveryReport true if a delivery report was requested, false
         *                       if not
         * @return the URI for the new message
         */
        public static Uri addMessageToUri(ContentResolver resolver, Uri uri, String address, String body,
                                          String subject, Long date, boolean read, boolean deliveryReport) {
            return addMessageToUri(resolver, uri, address, body, subject, date, read, deliveryReport, -1L);
        }


        /**
         * Add an SMS to the given URI with thread_id specified.
         *
         * @param resolver       the content resolver to use
         * @param uri            the URI to add the message to
         * @param address        the address of the sender
         * @param body           the body of the message
         * @param subject        the psuedo-subject of the message
         * @param date           the timestamp for the message
         * @param read           true if the message has been read, false if not
         * @param deliveryReport true if a delivery report was requested, false
         *                       if not
         * @param threadId       the thread_id of the message
         * @return the URI for the new message
         */
        public static Uri addMessageToUri(ContentResolver resolver, Uri uri, String address, String body,
                                          String subject, Long date, boolean read, boolean deliveryReport, long threadId) {
            ContentValues values = new ContentValues(7);


            values.put(ADDRESS, address);
            if (date != null) {
                values.put(DATE, date);
            }
            values.put(READ, read ? Integer.valueOf(1) : Integer.valueOf(0));
            values.put(SUBJECT, subject);
            values.put(BODY, body);
            if (deliveryReport) {
                values.put(STATUS, STATUS_PENDING);
            }
            if (threadId != -1L) {
                values.put(THREAD_ID, threadId);
            }
            return resolver.insert(uri, values);
        }


        // /**
        // * Move a message to the given folder.
        // *
        // * @param context the context to use
        // * @param uri the message to move
        // * @param folder the folder to move to
        // * @return true if the operation succeeded
        // */
        // public static boolean moveMessageToFolder(Context context,
        // Uri uri, int folder, int error) {
        // if (uri == null) {
        // return false;
        // }
        //
        // boolean markAsUnread = false;
        // boolean markAsRead = false;
        // switch(folder) {
        // case MESSAGE_TYPE_INBOX:
        // case MESSAGE_TYPE_DRAFT:
        // break;
        // case MESSAGE_TYPE_OUTBOX:
        // case MESSAGE_TYPE_SENT:
        // markAsRead = true;
        // break;
        // case MESSAGE_TYPE_FAILED:
        // case MESSAGE_TYPE_QUEUED:
        // markAsUnread = true;
        // break;
        // default:
        // return false;
        // }
        //
        // ContentValues values = new ContentValues(3);
        //
        // values.put(TYPE, folder);
        // if (markAsUnread) {
        // values.put(READ, Integer.valueOf(0));
        // } else if (markAsRead) {
        // values.put(READ, Integer.valueOf(1));
        // }
        // values.put(ERROR_CODE, error);
        //
        // return 1 == SqliteWrapper.update(context,
        // context.getContentResolver(),
        // uri, values, null, null);
        // }


        /**
         * Returns true iff the folder (message type) identifies an outgoing
         * message.
         */
        public static boolean isOutgoingFolder(int messageType) {
            return (messageType == MESSAGE_TYPE_FAILED) || (messageType == MESSAGE_TYPE_OUTBOX)
                    || (messageType == MESSAGE_TYPE_SENT) || (messageType == MESSAGE_TYPE_QUEUED);
        }


        /**
         * Contains all text based SMS messages in the SMS app's inbox.
         */
        public static final class Inbox implements BaseColumns, TextBasedSmsColumns {
            /**
             * The content:// style URL for this table
             */
            public static final Uri CONTENT_URI = Uri.parse("content://sms/inbox");


            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = "date DESC";


            /**
             * Add an SMS to the Draft box.
             *
             * @param resolver the content resolver to use
             * @param address  the address of the sender
             * @param body     the body of the message
             * @param subject  the psuedo-subject of the message
             * @param date     the timestamp for the message
             * @param read     true if the message has been read, false if not
             * @return the URI for the new message
             */
            public static Uri addMessage(ContentResolver resolver, String address, String body, String subject,
                                         Long date, boolean read) {
                return addMessageToUri(resolver, CONTENT_URI, address, body, subject, date, read, false);
            }
        }
    }


    /**
     * Base columns for tables that contain MMSs.
     */
    public interface BaseMmsColumns extends BaseColumns {


        public static final int MESSAGE_BOX_ALL = 0;
        public static final int MESSAGE_BOX_INBOX = 1;
        public static final int MESSAGE_BOX_SENT = 2;
        public static final int MESSAGE_BOX_DRAFTS = 3;
        public static final int MESSAGE_BOX_OUTBOX = 4;


        /**
         * The date the message was received.
         * <p>
         * Type: INTEGER (long)
         * </P>
         */
        public static final String DATE = "date";


        /**
         * The date the message was sent.
         * <p>
         * Type: INTEGER (long)
         * </P>
         */
        public static final String DATE_SENT = "date_sent";


        /**
         * The box which the message belong to, for example, MESSAGE_BOX_INBOX.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String MESSAGE_BOX = "msg_box";


        /**
         * Has the message been read.
         * <p>
         * Type: INTEGER (boolean)
         * </P>
         */
        public static final String READ = "read";


        /**
         * Indicates whether this message has been seen by the user. The "seen"
         * flag will be used to figure out whether we need to throw up a
         * statusbar notification or not.
         */
        public static final String SEEN = "seen";


        /**
         * The Message-ID of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String MESSAGE_ID = "m_id";


        /**
         * The subject of the message, if present.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String SUBJECT = "sub";


        /**
         * The character set of the subject, if present.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String SUBJECT_CHARSET = "sub_cs";


        /**
         * The Content-Type of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String CONTENT_TYPE = "ct_t";


        /**
         * The Content-Location of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String CONTENT_LOCATION = "ct_l";


        /**
         * The address of the sender.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String FROM = "from";


        /**
         * The address of the recipients.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String TO = "to";


        /**
         * The address of the cc. recipients.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String CC = "cc";


        /**
         * The address of the bcc. recipients.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String BCC = "bcc";


        /**
         * The expiry time of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String EXPIRY = "exp";


        /**
         * The class of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String MESSAGE_CLASS = "m_cls";


        /**
         * The type of the message defined by MMS spec.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String MESSAGE_TYPE = "m_type";


        /**
         * The version of specification that this message conform.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String MMS_VERSION = "v";


        /**
         * The size of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String MESSAGE_SIZE = "m_size";


        /**
         * The priority of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String PRIORITY = "pri";


        /**
         * The read-report of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String READ_REPORT = "rr";


        /**
         * Whether the report is allowed.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String REPORT_ALLOWED = "rpt_a";


        /**
         * The response-status of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String RESPONSE_STATUS = "resp_st";


        /**
         * The status of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String STATUS = "st";


        /**
         * The transaction-id of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String TRANSACTION_ID = "tr_id";


        /**
         * The retrieve-status of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String RETRIEVE_STATUS = "retr_st";


        /**
         * The retrieve-text of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String RETRIEVE_TEXT = "retr_txt";


        /**
         * The character set of the retrieve-text.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String RETRIEVE_TEXT_CHARSET = "retr_txt_cs";


        /**
         * The read-status of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String READ_STATUS = "read_status";


        /**
         * The content-class of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String CONTENT_CLASS = "ct_cls";


        /**
         * The delivery-report of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String DELIVERY_REPORT = "d_rpt";


        /**
         * The delivery-time-token of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String DELIVERY_TIME_TOKEN = "d_tm_tok";


        /**
         * The delivery-time of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String DELIVERY_TIME = "d_tm";


        /**
         * The response-text of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String RESPONSE_TEXT = "resp_txt";


        /**
         * The sender-visibility of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String SENDER_VISIBILITY = "s_vis";


        /**
         * The reply-charging of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String REPLY_CHARGING = "r_chg";


        /**
         * The reply-charging-deadline-token of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String REPLY_CHARGING_DEADLINE_TOKEN = "r_chg_dl_tok";


        /**
         * The reply-charging-deadline of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String REPLY_CHARGING_DEADLINE = "r_chg_dl";


        /**
         * The reply-charging-id of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String REPLY_CHARGING_ID = "r_chg_id";


        /**
         * The reply-charging-size of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String REPLY_CHARGING_SIZE = "r_chg_sz";


        /**
         * The previously-sent-by of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String PREVIOUSLY_SENT_BY = "p_s_by";


        /**
         * The previously-sent-date of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String PREVIOUSLY_SENT_DATE = "p_s_d";


        /**
         * The store of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String STORE = "store";


        /**
         * The mm-state of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String MM_STATE = "mm_st";


        /**
         * The mm-flags-token of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String MM_FLAGS_TOKEN = "mm_flg_tok";


        /**
         * The mm-flags of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String MM_FLAGS = "mm_flg";


        /**
         * The store-status of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String STORE_STATUS = "store_st";


        /**
         * The store-status-text of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String STORE_STATUS_TEXT = "store_st_txt";


        /**
         * The stored of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String STORED = "stored";


        /**
         * The totals of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String TOTALS = "totals";


        /**
         * The mbox-totals of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String MBOX_TOTALS = "mb_t";


        /**
         * The mbox-totals-token of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String MBOX_TOTALS_TOKEN = "mb_t_tok";


        /**
         * The quotas of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String QUOTAS = "qt";


        /**
         * The mbox-quotas of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String MBOX_QUOTAS = "mb_qt";


        /**
         * The mbox-quotas-token of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String MBOX_QUOTAS_TOKEN = "mb_qt_tok";


        /**
         * The message-count of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String MESSAGE_COUNT = "m_cnt";


        /**
         * The start of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String START = "start";


        /**
         * The distribution-indicator of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String DISTRIBUTION_INDICATOR = "d_ind";


        /**
         * The element-descriptor of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String ELEMENT_DESCRIPTOR = "e_des";


        /**
         * The limit of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String LIMIT = "limit";


        /**
         * The recommended-retrieval-mode of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String RECOMMENDED_RETRIEVAL_MODE = "r_r_mod";


        /**
         * The recommended-retrieval-mode-text of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String RECOMMENDED_RETRIEVAL_MODE_TEXT = "r_r_mod_txt";


        /**
         * The status-text of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String STATUS_TEXT = "st_txt";


        /**
         * The applic-id of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String APPLIC_ID = "apl_id";


        /**
         * The reply-applic-id of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String REPLY_APPLIC_ID = "r_apl_id";


        /**
         * The aux-applic-id of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String AUX_APPLIC_ID = "aux_apl_id";


        /**
         * The drm-content of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String DRM_CONTENT = "drm_c";


        /**
         * The adaptation-allowed of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String ADAPTATION_ALLOWED = "adp_a";


        /**
         * The replace-id of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String REPLACE_ID = "repl_id";


        /**
         * The cancel-id of the message.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String CANCEL_ID = "cl_id";


        /**
         * The cancel-status of the message.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String CANCEL_STATUS = "cl_st";


        /**
         * The thread ID of the message
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String THREAD_ID = "thread_id";


        /**
         * Has the message been locked?
         * <p>
         * Type: INTEGER (boolean)
         * </P>
         */
        public static final String LOCKED = "locked";


        /**
         * Meta data used externally.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String META_DATA = "meta_data";
    }


    /**
     * Contains all MMS messages.
     */
    public static final class Mms implements BaseMmsColumns {


        /**
         * The authority for the mms provider
         */
        public static final String AUTHORITY = "mms";
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);


        public static final Uri REPORT_REQUEST_URI = Uri.withAppendedPath(CONTENT_URI, "report-request");


        public static final Uri REPORT_STATUS_URI = Uri.withAppendedPath(CONTENT_URI, "report-status");


        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "date DESC";


        /**
         * mailbox = name-addr name-addr = [display-name] angle-addr angle-addr
         * = [CFWS] "<" addr-spec ">" [CFWS]
         */
        public static final Pattern NAME_ADDR_EMAIL_PATTERN = Pattern
                .compile("\\s*(\"[^\"]*\"|[^<>\"]+)\\s*<([^<>]+)>\\s*");


        /**
         * quoted-string = [CFWS] DQUOTE *([FWS] qcontent) [FWS] DQUOTE [CFWS]
         */
        public static final Pattern QUOTED_STRING_PATTERN = Pattern.compile("\\s*\"([^\"]*)\"\\s*");


        public static final Cursor query(ContentResolver cr, String[] projection) {
            return cr.query(CONTENT_URI, projection, null, null, DEFAULT_SORT_ORDER);
        }


        public static final Cursor query(ContentResolver cr, String[] projection, String where, String orderBy) {
            return cr.query(CONTENT_URI, projection, where, null, orderBy == null ? DEFAULT_SORT_ORDER : orderBy);
        }


        public static final String getMessageBoxName(int msgBox) {
            switch (msgBox) {
                case MESSAGE_BOX_ALL:
                    return "all";
                case MESSAGE_BOX_INBOX:
                    return "inbox";
                case MESSAGE_BOX_SENT:
                    return "sent";
                case MESSAGE_BOX_DRAFTS:
                    return "drafts";
                case MESSAGE_BOX_OUTBOX:
                    return "outbox";
                default:
                    throw new IllegalArgumentException("Invalid message box: " + msgBox);
            }
        }


        public static String extractAddrSpec(String address) {
            Matcher match = NAME_ADDR_EMAIL_PATTERN.matcher(address);


            if (match.matches()) {
                return match.group(2);
            }
            return address;
        }


        /**
         * Returns true if the address is an email address
         *
         * @param address the input address to be tested
         * @return true if address is an email address
         */
        public static boolean isEmailAddress(String address) {
            if (TextUtils.isEmpty(address)) {
                return false;
            }


            String s = extractAddrSpec(address);
            Matcher match = Patterns.EMAIL_ADDRESS.matcher(s);
            return match.matches();
        }


        /**
         * Returns true if the number is a Phone number
         *
         * @param number the input number to be tested
         * @return true if number is a Phone number
         */
        public static boolean isPhoneNumber(String number) {
            if (TextUtils.isEmpty(number)) {
                return false;
            }


            Matcher match = Patterns.PHONE.matcher(number);
            return match.matches();
        }


        /**
         * Contains all MMS messages in the MMS app's inbox.
         */
        public static final class Inbox implements BaseMmsColumns {
            /**
             * The content:// style URL for this table
             */
            public static final Uri CONTENT_URI = Uri.parse("content://mms/inbox");


            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = "date DESC";
        }


        /**
         * Contains all MMS messages in the MMS app's sent box.
         */
        public static final class Sent implements BaseMmsColumns {
            /**
             * The content:// style URL for this table
             */
            public static final Uri CONTENT_URI = Uri.parse("content://mms/sent");


            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = "date DESC";
        }


        /**
         * Contains all MMS messages in the MMS app's drafts box.
         */
        public static final class Draft implements BaseMmsColumns {
            /**
             * The content:// style URL for this table
             */
            public static final Uri CONTENT_URI = Uri.parse("content://mms/drafts");


            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = "date DESC";
        }


        /**
         * Contains all MMS messages in the MMS app's outbox.
         */
        public static final class Outbox implements BaseMmsColumns {
            /**
             * The content:// style URL for this table
             */
            public static final Uri CONTENT_URI = Uri.parse("content://mms/outbox");


            /**
             * The default sort order for this table
             */
            public static final String DEFAULT_SORT_ORDER = "date DESC";
        }


        public static final class Addr implements BaseColumns {
            /**
             * The ID of MM which this address entry belongs to.
             */
            public static final String MSG_ID = "msg_id";


            /**
             * The ID of contact entry in Phone Book.
             */
            public static final String CONTACT_ID = "contact_id";


            /**
             * The address text.
             */
            public static final String ADDRESS = "address";


            /**
             * Type of address, must be one of PduHeaders.BCC, PduHeaders.CC,
             * PduHeaders.FROM, PduHeaders.TO.
             */
            public static final String TYPE = "type";


            /**
             * Character set of this entry.
             */
            public static final String CHARSET = "charset";
        }


        public static final class Part implements BaseColumns {
            /**
             * The identifier of the message which this part belongs to.
             * <p>
             * Type: INTEGER
             * </P>
             */
            public static final String MSG_ID = "mid";


            /**
             * The order of the part.
             * <p>
             * Type: INTEGER
             * </P>
             */
            public static final String SEQ = "seq";


            /**
             * The content type of the part.
             * <p>
             * Type: TEXT
             * </P>
             */
            public static final String CONTENT_TYPE = "ct";


            /**
             * The name of the part.
             * <p>
             * Type: TEXT
             * </P>
             */
            public static final String NAME = "name";


            /**
             * The charset of the part.
             * <p>
             * Type: TEXT
             * </P>
             */
            public static final String CHARSET = "chset";


            /**
             * The file name of the part.
             * <p>
             * Type: TEXT
             * </P>
             */
            public static final String FILENAME = "fn";


            /**
             * The content disposition of the part.
             * <p>
             * Type: TEXT
             * </P>
             */
            public static final String CONTENT_DISPOSITION = "cd";


            /**
             * The content ID of the part.
             * <p>
             * Type: INTEGER
             * </P>
             */
            public static final String CONTENT_ID = "cid";


            /**
             * The content location of the part.
             * <p>
             * Type: INTEGER
             * </P>
             */
            public static final String CONTENT_LOCATION = "cl";


            /**
             * The start of content-type of the message.
             * <p>
             * Type: INTEGER
             * </P>
             */
            public static final String CT_START = "ctt_s";


            /**
             * The type of content-type of the message.
             * <p>
             * Type: TEXT
             * </P>
             */
            public static final String CT_TYPE = "ctt_t";


            /**
             * The location(on filesystem) of the binary data of the part.
             * <p>
             * Type: INTEGER
             * </P>
             */
            public static final String _DATA = "_data";


            public static final String TEXT = "text";


        }
    }


    public interface ThreadsColumns extends BaseColumns {
        /**
         * The date at which the thread was created.
         * <p>
         * Type: INTEGER (long)
         * </P>
         */
        public static final String DATE = "date";


        /**
         * A string encoding of the recipient IDs of the recipients of the
         * message, in numerical order and separated by spaces.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String RECIPIENT_IDS = "recipient_ids";


        /**
         * The message count of the thread.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String MESSAGE_COUNT = "message_count";
        /**
         * Indicates whether all messages of the thread have been read.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String READ = "read";


        /**
         * The snippet of the latest message in the thread.
         * <p>
         * Type: TEXT
         * </P>
         */
        public static final String SNIPPET = "snippet";
        /**
         * The charset of the snippet.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String SNIPPET_CHARSET = "snippet_cs";
        /**
         * Type of the thread, either Threads.COMMON_THREAD or
         * Threads.BROADCAST_THREAD.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String TYPE = "type";
        /**
         * Indicates whether there is a transmission error in the thread.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String ERROR = "error";
        /**
         * Indicates whether this thread contains any attachments.
         * <p>
         * Type: INTEGER
         * </P>
         */
        public static final String HAS_ATTACHMENT = "has_attachment";
    }


    public static final class Threads implements ThreadsColumns {
        private static final String[] ID_PROJECTION = {
                BaseColumns._ID
        };
        private static final Uri THREAD_ID_CONTENT_URI = Uri.parse("content://mms-sms/threadID");
        public static final Uri CONTENT_URI = Uri.withAppendedPath(Uri.parse("content://mms-sms/"), "conversations");
        public static final Uri OBSOLETE_THREADS_URI = Uri.withAppendedPath(CONTENT_URI, "obsolete");


        public static final int COMMON_THREAD = 0;
        public static final int BROADCAST_THREAD = 1;


        // No one should construct an instance of this class.
        private Threads() {
        }


        /**
         * This is a single-recipient version of getOrCreateThreadId. It's
         * convenient for use with SMS messages.
         */
        public static long getOrCreateThreadId(Context context, String recipient) {
            Set<String> recipients = new HashSet<String>();


            recipients.add(recipient);
            return getOrCreateThreadId(context, recipients);
        }


        /**
         * Given the recipients list and subject of an unsaved message, return
         * its thread ID. If the message starts a new thread, allocate a new
         * thread ID. Otherwise, use the appropriate existing thread ID. Find
         * the thread ID of the same set of recipients (in any order, without
         * any additions). If one is found, return it. Otherwise, return a
         * unique thread ID.
         */
        public static long getOrCreateThreadId(Context context, Set<String> recipients) {
            Uri.Builder uriBuilder = THREAD_ID_CONTENT_URI.buildUpon();


            for (String recipient : recipients) {
                if (Mms.isEmailAddress(recipient)) {
                    recipient = Mms.extractAddrSpec(recipient);
                }


                uriBuilder.appendQueryParameter("recipient", recipient);
            }


            Uri uri = uriBuilder.build();
            Log.e(TAG, "getOrCreateThreadId uri: " + uri);


            Cursor cursor = context.getContentResolver().query(uri, ID_PROJECTION, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        return cursor.getLong(0);
                    } else {
                        Log.e(TAG, "getOrCreateThreadId returned no rows!");
                    }
                } finally {
                    cursor.close();
                }
            }


            Log.e(TAG, "getOrCreateThreadId failed with uri " + uri.toString());
            throw new IllegalArgumentException("Unable to find or allocate a thread ID.");
        }
    }
}