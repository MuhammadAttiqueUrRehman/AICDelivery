package com.aic.aicdelivery;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 10-06-2017.
 */
public class HMCoreData extends SQLiteOpenHelper {
    SQLiteDatabase db;
    HMAppVariables hmappvariables;
    Context ctx;
    private static final String HM_DB = "HMApp.db";
    private static final String HM_USER = "User";
    private static final String HM_CART = "Cart";

    //Cart Fields
    public static final String CART_COL1 = "category";
    public static final String CART_COL2 = "deviceid";
    public static final String CART_COL3 = "expiry";
    public static final String CART_COL4 = "imageurll";
    public static final String CART_COL5 = "imageurls";
    public static final String CART_COL6 = "isfav";
    public static final String CART_COL7 = "itemcode";
    public static final String CART_COL8 = "itemdescription";
    public static final String CART_COL9 = "itemname";
    public static final String CART_COL10 = "itemtype";
    public static final String CART_COL11 = "outletcode";
    public static final String CART_COL12 = "price";
    public static final String CART_COL13 = "qty";
    public static final String CART_COL14 = "remark";
    public static final String CART_COL15 = "smessage";
    public static final String CART_COL16 = "recur";

    //User Fields
    public static final String USER_COL1 = "appcustomeremailbody";
    public static final String USER_COL2 = "appcustomeremailid";
    public static final String USER_COL3 = "appcustomeremailsubject";
    public static final String USER_COL4 = "appcustomertelephone";
    public static final String USER_COL5 = "appdeviceexist";
    public static final String USER_COL6 = "appenrolmetnamount";
    public static final String USER_COL7 = "appgoogleapikey";
    public static final String USER_COL8 = "applat";
    public static final String USER_COL9 = "applocationshort";
    public static final String USER_COL10 = "applocationlong";
    public static final String USER_COL11 = "applon";
    public static final String USER_COL12 = "appmerchantid";
    public static final String USER_COL13 = "appsocialmessagelong";
    public static final String USER_COL14 = "appsocialmessageshort";
    public static final String USER_COL15 = "appurl";
    public static final String USER_COL16 = "ualcohol";
    public static final String USER_COL17 = "uareaname";
    public static final String USER_COL18 = "uautolocation";
    public static final String USER_COL19 = "ubirthdate";
    public static final String USER_COL20 = "ucertificate";
    public static final String USER_COL21 = "ucity";
    public static final String USER_COL22 = "ucityname";
    public static final String USER_COL23 = "ucountry";
    public static final String USER_COL24 = "ucountryname";
    public static final String USER_COL25 = "ucurrency";
    public static final String USER_COL26 = "ucuspic";
    public static final String USER_COL27 = "ucusqr";
    public static final String USER_COL28 = "ucustomerid";
    public static final String USER_COL29 = "ucustomername";
    public static final String USER_COL30 = "udisplaybirthdate";
    public static final String USER_COL31 = "uemailid";
    public static final String USER_COL32 = "ufbid";
    public static final String USER_COL33 = "ufirstname";
    public static final String USER_COL34 = "ufullname";
    public static final String USER_COL35 = "uhomecountry";
    public static final String USER_COL36 = "uhomecountryname";
    public static final String USER_COL37 = "uinitdate";
    public static final String USER_COL38 = "ulastname";
    public static final String USER_COL39 = "ulocationname";
    public static final String USER_COL40 = "ulogged";
    public static final String USER_COL41 = "umagicnumber";
    public static final String USER_COL42 = "umaxspend";
    public static final String USER_COL43 = "umemberexpiry";
    public static final String USER_COL44 = "umobilenumber";
    public static final String USER_COL45 = "unationalitycode";
    public static final String USER_COL46 = "upinnumber";
    public static final String USER_COL47 = "upointvalue";
    public static final String USER_COL48 = "upushoffer";
    public static final String USER_COL49 = "uremindexpiry";
    public static final String USER_COL50 = "uresidentcity";
    public static final String USER_COL51 = "uresidentcountry";
    public static final String USER_COL52 = "uresidentcountryname";
    public static final String USER_COL53 = "usegmentcode";
    public static final String USER_COL54 = "usegmentimage";
    public static final String USER_COL55 = "usegmentname";
    public static final String USER_COL56 = "ushowprofile";
    public static final String USER_COL57 = "uspend";
    public static final String USER_COL58 = "udevicecomboined";
    public static final String USER_COL59 = "firsttimeaccess";
    public static final String USER_COL60 = "sendnotification";
    public static final String USER_COL61 = "sendmailer";
    public static final String USER_COL62 = "sendsms";

    public HMCoreData(Context context) {
        super(context, HM_DB, null, 1);
        this.ctx=context;
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + HM_CART + " (" +
                    CART_COL1 + " TEXT NOT NULL , " +
                    CART_COL2 + " TEXT NOT NULL, " +
                    CART_COL3 + " TEXT NOT NULL, " +
                    CART_COL4 + " TEXT NOT NULL, " +
                    CART_COL5 + " TEXT NOT NULL, " +
                    CART_COL6 + " TEXT NOT NULL, " +
                    CART_COL7 + " TEXT NOT NULL, " +
                    CART_COL8 + " TEXT NOT NULL, " +
                    CART_COL9 + " TEXT NOT NULL, " +
                    CART_COL10 + " TEXT NOT NULL, " +
                    CART_COL11 + " TEXT NOT NULL, " +
                    CART_COL12 + " TEXT NOT NULL, " +
                    CART_COL13 + " TEXT NOT NULL, " +
                    CART_COL14 + " TEXT NOT NULL, " +
                    CART_COL15 + " TEXT NOT NULL, " +
                    CART_COL16 + " TEXT NOT NULL)");
            db.execSQL("CREATE TABLE " + HM_USER + " (" +
                    USER_COL1 + "  TEXT NOT NULL, " +
                    USER_COL2 + "  TEXT NOT NULL, " +
                    USER_COL3 + "  TEXT NOT NULL, " +
                    USER_COL4 + "  TEXT NOT NULL, " +
                    USER_COL5 + "  TEXT NOT NULL, " +
                    USER_COL6 + "  DOUBLE NOT NULL, " +
                    USER_COL7 + "  TEXT NOT NULL, " +
                    USER_COL8 + "  DOUBLE NOT NULL, " +
                    USER_COL9 + "  TEXT NOT NULL, " +
                    USER_COL10 + "  TEXT NOT NULL, " +
                    USER_COL11 + "  DOUBLE NOT NULL, " +
                    USER_COL12 + "  TEXT NOT NULL, " +
                    USER_COL13 + "  TEXT NOT NULL, " +
                    USER_COL14 + "  TEXT NOT NULL, " +
                    USER_COL15 + "  TEXT NOT NULL, " +
                    USER_COL16 + "  TEXT NOT NULL, " +
                    USER_COL17 + "  TEXT NOT NULL, " +
                    USER_COL18 + "  TEXT NOT NULL, " +
                    USER_COL19 + "  TEXT NOT NULL, " +
                    USER_COL20 + "  TEXT NOT NULL, " +
                    USER_COL21 + "  TEXT NOT NULL, " +
                    USER_COL22 + "  TEXT NOT NULL, " +
                    USER_COL23 + "  TEXT NOT NULL, " +
                    USER_COL24 + "  TEXT NOT NULL, " +
                    USER_COL25 + "  TEXT NOT NULL, " +
                    USER_COL26 + "  TEXT NOT NULL, " +
                    USER_COL27 + "  TEXT NOT NULL, " +
                    USER_COL28 + "  TEXT NOT NULL, " +
                    USER_COL29 + "  TEXT NOT NULL, " +
                    USER_COL30 + "  TEXT NOT NULL, " +
                    USER_COL31 + "  TEXT NOT NULL, " +
                    USER_COL32 + "  TEXT NOT NULL, " +
                    USER_COL33 + "  TEXT NOT NULL, " +
                    USER_COL34 + "  TEXT NOT NULL, " +
                    USER_COL35 + "  TEXT NOT NULL, " +
                    USER_COL36 + "  TEXT NOT NULL, " +
                    USER_COL37 + "  TEXT NOT NULL, " +
                    USER_COL38 + "  TEXT NOT NULL, " +
                    USER_COL39 + "  TEXT NOT NULL, " +
                    USER_COL40 + "  BOOLEAN NOT NULL, " +
                    USER_COL41 + "  TEXT NOT NULL, " +
                    USER_COL42 + "  DOUBLE NOT NULL, " +
                    USER_COL43 + "  TEXT NOT NULL, " +
                    USER_COL44 + "  TEXT NOT NULL, " +
                    USER_COL45 + "  TEXT NOT NULL, " +
                    USER_COL46 + "  TEXT NOT NULL, " +
                    USER_COL47 + "  DOUBLE NOT NULL, " +
                    USER_COL48 + "  TEXT NOT NULL, " +
                    USER_COL49 + "  TEXT NOT NULL, " +
                    USER_COL50 + "  TEXT NOT NULL, " +
                    USER_COL51 + "  TEXT NOT NULL, " +
                    USER_COL52 + "  TEXT NOT NULL, " +
                    USER_COL53 + "  TEXT NOT NULL, " +
                    USER_COL54 + "  TEXT NOT NULL, " +
                    USER_COL55 + "  TEXT NOT NULL, " +
                    USER_COL56 + "  TEXT NOT NULL, " +
                    USER_COL57 + "  DOUBLE NOT NULL, " +
                    USER_COL58 + "  TEXT NOT NULL, " +
                    USER_COL59 + "  TEXT NOT NULL, " +
                    USER_COL60 + "  TEXT NOT NULL, " +
                    USER_COL61 + "  TEXT NOT NULL, " +
                    USER_COL62 + "  TEXT NOT NULL)" );

            ContentValues contentValues = new ContentValues();
            hmappvariables = new HMAppVariables();

            contentValues.put(USER_COL1, hmappvariables.appcustomeremailbody);
            contentValues.put(USER_COL2, hmappvariables.appcustomeremailid);
            contentValues.put(USER_COL3, hmappvariables.appcustomeremailsubject);
            contentValues.put(USER_COL4, hmappvariables.appcustomertelephone);
            contentValues.put(USER_COL5, hmappvariables.appdeviceexist);
            contentValues.put(USER_COL6, hmappvariables.appenrolmetnamount);
            contentValues.put(USER_COL7, hmappvariables.appgoogleapikey);
            contentValues.put(USER_COL8, hmappvariables.applat);
            contentValues.put(USER_COL9, hmappvariables.applocationshort);
            contentValues.put(USER_COL10, hmappvariables.applocationlong);
            contentValues.put(USER_COL11, hmappvariables.applon);
            contentValues.put(USER_COL12, hmappvariables.appmerchantid);
            contentValues.put(USER_COL13, hmappvariables.appsocialmessagelong);
            contentValues.put(USER_COL14, hmappvariables.appsocialmessageshort);
            contentValues.put(USER_COL15, hmappvariables.appurl);
            contentValues.put(USER_COL16, hmappvariables.ualcohol);
            contentValues.put(USER_COL17, hmappvariables.uareaname);
            contentValues.put(USER_COL18, hmappvariables.uautolocation);
            contentValues.put(USER_COL19, hmappvariables.ubirthdate);
            contentValues.put(USER_COL20, hmappvariables.ucertificate);
            contentValues.put(USER_COL21, hmappvariables.ucity);
            contentValues.put(USER_COL22, hmappvariables.ucityname);
            contentValues.put(USER_COL23, hmappvariables.ucountry);
            contentValues.put(USER_COL24, hmappvariables.ucountryname);
            contentValues.put(USER_COL25, hmappvariables.ucurrency);
            contentValues.put(USER_COL26, hmappvariables.ucuspic);
            contentValues.put(USER_COL27, hmappvariables.ucusqr);
            contentValues.put(USER_COL28, hmappvariables.ucustomerid);
            contentValues.put(USER_COL29, hmappvariables.ucustomername);
            contentValues.put(USER_COL30, hmappvariables.udisplaybirthdate);
            contentValues.put(USER_COL31, hmappvariables.uemailid);
            contentValues.put(USER_COL32, hmappvariables.ufbid);
            contentValues.put(USER_COL33, hmappvariables.ufirstname);
            contentValues.put(USER_COL34, hmappvariables.ufullname);
            contentValues.put(USER_COL35, hmappvariables.uhomecountry);
            contentValues.put(USER_COL36, hmappvariables.uhomecountryname);
            contentValues.put(USER_COL37, hmappvariables.uinitdate);
            contentValues.put(USER_COL38, hmappvariables.ulastname);
            contentValues.put(USER_COL39, hmappvariables.ulocationname);
            contentValues.put(USER_COL40, hmappvariables.ulogged);
            contentValues.put(USER_COL41, hmappvariables.umagicnumber);
            contentValues.put(USER_COL42, hmappvariables.umaxspend);
            contentValues.put(USER_COL43, hmappvariables.umemberexpiry);
            contentValues.put(USER_COL44, hmappvariables.umobilenumber);
            contentValues.put(USER_COL45, hmappvariables.unationalitycode);
            contentValues.put(USER_COL46, hmappvariables.upinnumber);
            contentValues.put(USER_COL47, hmappvariables.upointvalue);
            contentValues.put(USER_COL48, hmappvariables.upushoffer);
            contentValues.put(USER_COL49, hmappvariables.uremindexpiry);
            contentValues.put(USER_COL50, hmappvariables.uresidentcity);
            contentValues.put(USER_COL51, hmappvariables.uresidentcountry);
            contentValues.put(USER_COL52, hmappvariables.uresidentcountryname);
            contentValues.put(USER_COL53, hmappvariables.usegmentcode);
            contentValues.put(USER_COL54, hmappvariables.usegmentimage);
            contentValues.put(USER_COL55, hmappvariables.usegmentname);
            contentValues.put(USER_COL56, hmappvariables.ushowprofile);
            contentValues.put(USER_COL57, hmappvariables.uspend);
            contentValues.put(USER_COL58, hmappvariables.udevicecomboined);
            contentValues.put(USER_COL59, hmappvariables.firsttimeaccess);
            contentValues.put(USER_COL60, hmappvariables.sendNotification);
            contentValues.put(USER_COL61, hmappvariables.sendMailer);
            contentValues.put(USER_COL62, hmappvariables.sendSMS);
            long result = db.insert(HM_USER, null, contentValues);
        } catch (Exception e) {
                         throw e;

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public HMAppVariables updateUserData(HMAppVariables appVariables) throws HMOwnException, JSONException {
        HMAppVariables hmappvariables=new HMAppVariables();
        //parse json into object and then write to SQL
        Boolean userLogged=appVariables.ulogged;
        IntroManager intromanager = new IntroManager(ctx);

        hmappvariables.udevicecomboined= HMConstants.devicemodel + "^ " + appVariables.appdeviceexist + "^ " + HMConstants.deviceversion + "^" + HMConstants.deviceplatform + "^" + HMConstants.devicemanufacturer;
        hmappvariables.appcustomeremailbody="";
        hmappvariables.appcustomeremailid=intromanager.getSupportEmail();
        hmappvariables.appcustomeremailsubject=HMConstants.appcustomeremailsubject;
        hmappvariables.appcustomertelephone=intromanager.getSupportCall();
        hmappvariables.appenrolmetnamount=appVariables.appenrolmetnamount;
        hmappvariables.appgoogleapikey=appVariables.appgoogleapikey;
        hmappvariables.applat=appVariables.applat;
        hmappvariables.applocationshort=appVariables.applocationshort;
        hmappvariables.applocationlong=appVariables.applocationlong;
        hmappvariables.applon=appVariables.applon;
        hmappvariables.appmerchantid=appVariables.appmerchantid;
        hmappvariables.appsocialmessagelong="";
        hmappvariables.appsocialmessageshort="";
        hmappvariables.appurl=HMConstants.appurl;
        hmappvariables.ucurrency=appVariables.ucurrency;
        hmappvariables.appdeviceexist=appVariables.appdeviceexist;
        hmappvariables.ualcohol="";
        hmappvariables.ubirthdate="";
        hmappvariables.ucertificate="";
        hmappvariables.ucityname=appVariables.ucityname;
        hmappvariables.ucountryname=appVariables.ucountryname;
        hmappvariables.uareaname=appVariables.uareaname;
        hmappvariables.ulocationname=appVariables.ulocationname;
        hmappvariables.umobilenumber=appVariables.umobilenumber;
        hmappvariables.firsttimeaccess=appVariables.firsttimeaccess;
        hmappvariables.ucuspic="";
        hmappvariables.ucusqr="";
        hmappvariables.ucustomerid="";
        hmappvariables.ucustomername="";
        hmappvariables.udisplaybirthdate="";
        hmappvariables.uemailid="";
        hmappvariables.ufbid="";
        hmappvariables.ufirstname="";
        hmappvariables.ufullname="";
        hmappvariables.uhomecountry="";
        hmappvariables.uhomecountryname="";
        hmappvariables.uinitdate="";
        hmappvariables.ulastname="";
        hmappvariables.ulogged=false;
        hmappvariables.umagicnumber="";
        hmappvariables.umaxspend=0;
        hmappvariables.umemberexpiry="";
        hmappvariables.unationalitycode="";
        hmappvariables.upinnumber="";
        hmappvariables.upointvalue=0;
        hmappvariables.upushoffer="";
        hmappvariables.uremindexpiry="";
        hmappvariables.uresidentcity="";
        hmappvariables.uresidentcountry="";
        hmappvariables.uresidentcountryname="";
        hmappvariables.usegmentcode="";
        hmappvariables.usegmentimage="";
        hmappvariables.usegmentname="";
        hmappvariables.ushowprofile="";
        hmappvariables.uspend=0;
        hmappvariables.uautolocation="";
        hmappvariables.ucity=appVariables.ucity;
        hmappvariables.ucountry=appVariables.ucountry;
        if (userLogged){
            hmappvariables.ualcohol=appVariables.ualcohol;
            hmappvariables.ubirthdate=appVariables.ubirthdate;
            hmappvariables.ucertificate=appVariables.ucertificate;
            hmappvariables.ucuspic=appVariables.ucuspic;
            hmappvariables.ucusqr=appVariables.ucusqr;
            hmappvariables.ucustomerid=appVariables.ucustomerid;
            hmappvariables.ucustomername=appVariables.ucustomername;
            hmappvariables.udisplaybirthdate=appVariables.udisplaybirthdate;
            hmappvariables.uemailid=appVariables.uemailid;
            hmappvariables.ufbid=appVariables.ufbid;
            hmappvariables.ufirstname=appVariables.ufirstname;
            hmappvariables.ufullname=appVariables.ufullname;
            hmappvariables.uhomecountry=appVariables.uhomecountry;
            hmappvariables.uhomecountryname=appVariables.uhomecountryname;
            hmappvariables.uinitdate=appVariables.uinitdate;
            hmappvariables.ulastname=appVariables.ulastname;
            hmappvariables.ulocationname=appVariables.ulocationname;
            hmappvariables.ulogged=appVariables.ulogged;
            hmappvariables.umagicnumber=appVariables.umagicnumber;
            hmappvariables.umaxspend=appVariables.umaxspend;
            hmappvariables.umemberexpiry=appVariables.umemberexpiry;
            hmappvariables.unationalitycode=appVariables.unationalitycode;
            hmappvariables.upinnumber=appVariables.upinnumber;
            hmappvariables.upointvalue=appVariables.upointvalue;
            hmappvariables.upushoffer=appVariables.upushoffer;
            hmappvariables.uremindexpiry=appVariables.uremindexpiry;
            hmappvariables.uresidentcity=appVariables.uresidentcity;
            hmappvariables.uresidentcountry=appVariables.uresidentcountry;
            hmappvariables.uresidentcountryname=appVariables.uresidentcountryname;
            hmappvariables.usegmentcode=appVariables.usegmentcode;
            hmappvariables.usegmentimage=appVariables.usegmentimage;
            hmappvariables.usegmentname=appVariables.usegmentname;
            hmappvariables.ushowprofile=appVariables.ushowprofile;
            hmappvariables.uspend=appVariables.uspend;
            hmappvariables.sendNotification=appVariables.sendNotification;
            hmappvariables.sendMailer=appVariables.sendMailer;
            hmappvariables.sendSMS=appVariables.sendSMS;
        }




        try {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(USER_COL1, hmappvariables.appcustomeremailbody);
            contentValues.put(USER_COL2, hmappvariables.appcustomeremailid);
            contentValues.put(USER_COL3, hmappvariables.appcustomeremailsubject);
            contentValues.put(USER_COL4, hmappvariables.appcustomertelephone);
            contentValues.put(USER_COL5, hmappvariables.appdeviceexist);
            contentValues.put(USER_COL6, hmappvariables.appenrolmetnamount);
            contentValues.put(USER_COL7, hmappvariables.appgoogleapikey);
            contentValues.put(USER_COL8, hmappvariables.applat);
            contentValues.put(USER_COL9, hmappvariables.applocationshort);
            contentValues.put(USER_COL10, hmappvariables.applocationlong);
            contentValues.put(USER_COL11, hmappvariables.applon);
            contentValues.put(USER_COL12, hmappvariables.appmerchantid);
            contentValues.put(USER_COL13, hmappvariables.appsocialmessagelong);
            contentValues.put(USER_COL14, hmappvariables.appsocialmessageshort);
            contentValues.put(USER_COL15, hmappvariables.appurl);
            contentValues.put(USER_COL16, hmappvariables.ualcohol);
            contentValues.put(USER_COL17, hmappvariables.uareaname);
            contentValues.put(USER_COL18, hmappvariables.uautolocation);
            contentValues.put(USER_COL19, hmappvariables.ubirthdate);
            contentValues.put(USER_COL20, hmappvariables.ucertificate);
            contentValues.put(USER_COL21, hmappvariables.ucity);
            contentValues.put(USER_COL22, hmappvariables.ucityname);
            contentValues.put(USER_COL23, hmappvariables.ucountry);
            contentValues.put(USER_COL24, hmappvariables.ucountryname);
            contentValues.put(USER_COL25, hmappvariables.ucurrency);
            contentValues.put(USER_COL26, hmappvariables.ucuspic);
            contentValues.put(USER_COL27, hmappvariables.ucusqr);
            contentValues.put(USER_COL28, hmappvariables.ucustomerid);
            contentValues.put(USER_COL29, hmappvariables.ucustomername);
            contentValues.put(USER_COL30, hmappvariables.udisplaybirthdate);
            contentValues.put(USER_COL31, hmappvariables.uemailid);
            contentValues.put(USER_COL32, hmappvariables.ufbid);
            contentValues.put(USER_COL33, hmappvariables.ufirstname);
            contentValues.put(USER_COL34, hmappvariables.ufullname);
            contentValues.put(USER_COL35, hmappvariables.uhomecountry);
            contentValues.put(USER_COL36, hmappvariables.uhomecountryname);
            contentValues.put(USER_COL37, hmappvariables.uinitdate);
            contentValues.put(USER_COL38, hmappvariables.ulastname);
            contentValues.put(USER_COL39, hmappvariables.ulocationname);
            contentValues.put(USER_COL40, hmappvariables.ulogged);
            contentValues.put(USER_COL41, hmappvariables.umagicnumber);
            contentValues.put(USER_COL42, hmappvariables.umaxspend);
            contentValues.put(USER_COL43, hmappvariables.umemberexpiry);
            contentValues.put(USER_COL44, hmappvariables.umobilenumber);
            contentValues.put(USER_COL45, hmappvariables.unationalitycode);
            contentValues.put(USER_COL46, hmappvariables.upinnumber);
            contentValues.put(USER_COL47, hmappvariables.upointvalue);
            contentValues.put(USER_COL48, hmappvariables.upushoffer);
            contentValues.put(USER_COL49, hmappvariables.uremindexpiry);
            contentValues.put(USER_COL50, hmappvariables.uresidentcity);
            contentValues.put(USER_COL51, hmappvariables.uresidentcountry);
            contentValues.put(USER_COL52, hmappvariables.uresidentcountryname);
            contentValues.put(USER_COL53, hmappvariables.usegmentcode);
            contentValues.put(USER_COL54, hmappvariables.usegmentimage);
            contentValues.put(USER_COL55, hmappvariables.usegmentname);
            contentValues.put(USER_COL56, hmappvariables.ushowprofile);
            contentValues.put(USER_COL57, hmappvariables.uspend);
            contentValues.put(USER_COL58, hmappvariables.udevicecomboined);
            contentValues.put(USER_COL59, hmappvariables.firsttimeaccess);
            contentValues.put(USER_COL60, hmappvariables.sendNotification);
            contentValues.put(USER_COL61, hmappvariables.sendMailer);
            contentValues.put(USER_COL62, hmappvariables.sendSMS);
            long result = db.update(HM_USER, contentValues, null, null);
            if (result != 1) {
                throw new HMOwnException("Error Updating User Data");
            }
            return hmappvariables;
        } catch (HMOwnException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            db.close();
        }
    }

    public void updateCartItem(String qty,String item){
        try{
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CART_COL13, qty);
        long result = db.update(HM_CART, contentValues, "itemcode = ?", new String [] {item});

    } catch (Exception e) {
            throw e;
    }finally {
            db.close();
        }
    }

    public void deleteCartItem(String item){
        try{
            db = this.getWritableDatabase();
            long result = db.delete(HM_CART,  "itemcode = ?", new String [] {item});

        } catch (Exception e) {
            throw e;
        }finally {
            db.close();
        }
    }

    public void deleteCartAll(){
        try{
            db = this.getWritableDatabase();
            long result = db.delete(HM_CART, null, null);

        } catch (Exception e) {
            throw e;
        }finally {
            db.close();
        }
    }


    public void addCart(HMProductList hmProductList) throws HMOwnException {
        if (getItemCountExists(hmProductList.itemcode)==0) {
            try {
                db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(CART_COL1, hmProductList.categorycode);
                contentValues.put(CART_COL2, MainActivity.deviceid);
                contentValues.put(CART_COL3, "");
                contentValues.put(CART_COL4, hmProductList.imagelargeurl);
                contentValues.put(CART_COL5, hmProductList.imagesmallurl);
                contentValues.put(CART_COL6, hmProductList.isfav);
                contentValues.put(CART_COL7, hmProductList.itemcode);
                contentValues.put(CART_COL8, hmProductList.itemdescription);
                contentValues.put(CART_COL9, hmProductList.itemname);
                contentValues.put(CART_COL10, hmProductList.itemtype);
                contentValues.put(CART_COL11, hmProductList.outletcode);
                contentValues.put(CART_COL12, hmProductList.unitprice);
                contentValues.put(CART_COL13, hmProductList.availableqty);
                contentValues.put(CART_COL14, "");
                contentValues.put(CART_COL15, "");
                contentValues.put(CART_COL16, hmProductList.recurring);
                long result = db.insert(HM_CART, null, contentValues);
              //  if (result != 1) {
               //     throw new HMOwnException("Error Updating Cart");
               // }

            } catch (Exception e) {
                throw e;
            }finally {
                db.close();
            }
        }
    }

    public HMAppVariables getUserData() throws HMOwnException {
        HMAppVariables hmappvariables=new HMAppVariables();
        try {
            db = this.getWritableDatabase();
            Cursor res = db.rawQuery("SELECT * FROM " + HM_USER, null);
            if (res.getCount() == 0) {
                throw new HMOwnException("No Records Available to Retrieve");
            }
            while (res.moveToNext()) {
                hmappvariables.appcustomeremailbody = res.getString(0);
                hmappvariables.appcustomeremailid = res.getString(1);
                hmappvariables.appcustomeremailsubject = res.getString(2);
                hmappvariables.appcustomertelephone = res.getString(3);
                hmappvariables.appdeviceexist = res.getString(4);
                hmappvariables.appenrolmetnamount = res.getDouble(5);
                hmappvariables.appgoogleapikey = res.getString(6);
                hmappvariables.applat = res.getDouble(7);
                hmappvariables.applocationshort = res.getString(8);
                hmappvariables.applocationlong = res.getString(9);
                hmappvariables.applon = res.getDouble(10);
                hmappvariables.appmerchantid = res.getString(11);
                hmappvariables.appsocialmessagelong = res.getString(12);
                hmappvariables.appsocialmessageshort = res.getString(13);
                hmappvariables.appurl = res.getString(14);
                hmappvariables.ualcohol = res.getString(15);
                hmappvariables.uareaname = res.getString(16);
                hmappvariables.uautolocation = res.getString(17);
                hmappvariables.ubirthdate = res.getString(18);
                hmappvariables.ucertificate = res.getString(19);
                hmappvariables.ucity = res.getString(20);
                hmappvariables.ucityname = res.getString(21);
                hmappvariables.ucountry = res.getString(22);
                hmappvariables.ucountryname = res.getString(23);
                hmappvariables.ucurrency = res.getString(24);
                hmappvariables.ucuspic = res.getString(25);
                hmappvariables.ucusqr = res.getString(26);
                hmappvariables.ucustomerid = res.getString(27);
                hmappvariables.ucustomername = res.getString(28);
                hmappvariables.udisplaybirthdate = res.getString(29);
                hmappvariables.uemailid = res.getString(30);
                hmappvariables.ufbid = res.getString(31);
                hmappvariables.ufirstname = res.getString(32);
                hmappvariables.ufullname = res.getString(33);
                hmappvariables.uhomecountry = res.getString(34);
                hmappvariables.uhomecountryname = res.getString(35);
                hmappvariables.uinitdate = res.getString(36);
                hmappvariables.ulastname = res.getString(37);
                hmappvariables.ulocationname = res.getString(38);
                boolean b = res.getInt(39) > 0;
                hmappvariables.ulogged = b;
                hmappvariables.umagicnumber = res.getString(40);
                hmappvariables.umaxspend = res.getDouble(41);
                hmappvariables.umemberexpiry = res.getString(42);
                hmappvariables.umobilenumber = res.getString(43);
                hmappvariables.unationalitycode = res.getString(44);
                hmappvariables.upinnumber = res.getString(45);
                hmappvariables.upointvalue = res.getDouble(46);
                hmappvariables.upushoffer = res.getString(47);
                hmappvariables.uremindexpiry = res.getString(48);
                hmappvariables.uresidentcity = res.getString(49);
                hmappvariables.uresidentcountry = res.getString(50);
                hmappvariables.uresidentcountryname = res.getString(51);
                hmappvariables.usegmentcode = res.getString(52);
                hmappvariables.usegmentimage = res.getString(53);
                hmappvariables.usegmentname = res.getString(54);
                hmappvariables.ushowprofile = res.getString(55);
                hmappvariables.uspend = res.getDouble(56);
                hmappvariables.udevicecomboined = res.getString(57);
                hmappvariables.firsttimeaccess = res.getString(58);
                hmappvariables.sendNotification = res.getString(59);
                hmappvariables.sendMailer = res.getString(60);
                hmappvariables.sendSMS = res.getString(61);
            }
            res.close();
            return hmappvariables;
        } catch (Exception e) {
            throw e;
        }finally {
            db.close();
        }

    }

    public  Map<String, String> statusValues(String jsondata) throws JSONException {
        Map<String,String> statusv = new HashMap<String,String>();
        JSONObject jObj = new JSONObject(jsondata);

        if (jObj.has("statuscode")){
            statusv.put("statuscode",jObj.getString("statuscode"));
        }
        if (jObj.has("statusdesc")){
            statusv.put("statusdesc",jObj.getString("statusdesc"));
        }
        if (jObj.has("transactionref")){
            statusv.put("transactionref",jObj.getString("transactionref"));
        }
        if (jObj.has("rewardbalance")){
            statusv.put("rewardbalance",jObj.getString("rewardbalance"));
        }
        if (jObj.has("walletbalance")){
            statusv.put("walletbalance",jObj.getString("walletbalance"));
        }
        if (jObj.has("paymentref")){
            statusv.put("paymentref",jObj.getString("paymentref"));
        }
        return statusv;
    }



    public void alertBox(Context context,String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(HMConstants.alertheader);
        alert.setMessage(msg);
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    public void showToast(Context ctx,String msg){
        Toast toast = Toast.makeText(ctx,msg, Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 0);
//        LinearLayout toastLayout = (LinearLayout) toast.getView();
//        TextView toastTV = (TextView) toastLayout.getChildAt(0);
//        toastTV.setTextSize(14);
        toast.show();
    }

    public int getItemCount() {
        String countQuery = "SELECT  * FROM " + HM_CART;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return cnt;
    }

    public int getItemCountExists(String item) {
        String countQuery = "SELECT * FROM " + HM_CART + " WHERE " + CART_COL7 + "='" + item  +"'";;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return cnt;
    }

    public String getTotalAmount(String ccy) throws HMOwnException {
        Float f=0f;
        String s="";
        db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT IFNULL(SUM(qty*price),0) FROM " + HM_CART, null);
        if (res.getCount() == 0) {
            throw new HMOwnException("No Cart Items Available to Retrieve 4");
        }
        while (res.moveToNext()) {
        f=Float.parseFloat(res.getString(0));
        }
        s= String.format("%.02f", f);
        res.close();
        db.close();
        return "Total " + ccy + " " + s ;
    }

    public Float getTotalAmount() throws HMOwnException {
        Float f=0f;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT IFNULL(SUM(qty*price),0) FROM " + HM_CART, null);
        if (res.getCount() == 0) {
            throw new HMOwnException("No Cart Items Available to Retrieve 3");
        }
        while (res.moveToNext()) {
            f=Float.parseFloat(res.getString(0));
        }
        res.close();
        db.close();
        return f;
    }

    public JSONObject getcartItemJson() throws HMOwnException, JSONException {
         JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();
        try {
            db = this.getWritableDatabase();
            Cursor res = db.rawQuery("SELECT * FROM " + HM_CART, null);
            if (res.getCount() == 0) {
                throw new HMOwnException("No Cart Items Available to Retrieve 2");
            }
            while (res.moveToNext()) {
                JSONObject cartJSON = new JSONObject();
                cartJSON.put("category", res.getString(0));
                cartJSON.put("deviceid", res.getString(1));
                cartJSON.put("expiry", res.getString(2));
                cartJSON.put("imageurll", res.getString(3));
                cartJSON.put("imageurls", res.getString(4));
                cartJSON.put("isfav", res.getString(5));
                cartJSON.put("itemcode", res.getString(6));
                cartJSON.put("itemdescription", res.getString(7));
                cartJSON.put("itemname", res.getString(8));
                cartJSON.put("itemtype", res.getString(9));
                cartJSON.put("outletcode", res.getString(10));
                cartJSON.put("price", res.getString(11));
                cartJSON.put("itemqty", res.getString(12));
                cartJSON.put("remark", res.getString(13));
                cartJSON.put("smessage", res.getString(14));
                cartJSON.put("recur", res.getString(15));
                jArray.put(cartJSON);
            }
            jObject.put("cartlist", jArray);
            res.close();
            return jObject;
        } catch (Exception e) {
            throw e;
        }finally {
            db.close();
        }
    }

    public String getcartItemOrder() throws HMOwnException, JSONException {
        String jObject="";
        Integer i=1;
        JSONArray jArray = new JSONArray();
        try {
            db = this.getWritableDatabase();
            Cursor res = db.rawQuery("SELECT * FROM " + HM_CART, null);
            if (res.getCount() == 0) {
                throw new HMOwnException("No Cart Items Available to Retrieve 1");
            }
            while (res.moveToNext()) {
                if (i == 1 && res.getCount()==1){
                    jObject = "{'itemcode':'"+res.getString(6)+"','itemqty':'"+res.getString(12)+"','itemtype':'"+res.getString(9)+"','outletcode':'"+res.getString(10)+"'}";
                }else if ((i==1) && ((res.getCount() > 1) && (i < res.getCount()))){
                    jObject = "{'itemcode':'"+res.getString(6)+"','itemqty':'"+res.getString(12)+"','itemtype':'"+res.getString(9)+"','outletcode':'"+res.getString(10)+"'}^";
                }else if ((i>1) && ((res.getCount() > 1) && (i < res.getCount()))){
                    jObject = jObject + "{'itemcode':'"+res.getString(6)+"','itemqty':'"+res.getString(12)+"','itemtype':'"+res.getString(9)+"','outletcode':'"+res.getString(10)+"'}^";
                }else{
                    jObject = jObject + "{'itemcode':'"+res.getString(6)+"','itemqty':'"+res.getString(12)+"','itemtype':'"+res.getString(9)+"','outletcode':'"+res.getString(10)+"'}";
                }
                i++;
            }
            res.close();
            return jObject;
        } catch (Exception e) {
            throw e;
        } finally {
            db.close();
        }
    }

    public boolean checkForm(Context context,String str){
        if (str.equals("0000")) {
            alertBox(context, "Please select atleast one option");
            return false;
        } else{
            return true;
        }

    }

    public boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}
