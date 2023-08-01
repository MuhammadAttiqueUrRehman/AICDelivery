package com.aic.aicdelivery;

import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;

import static android.content.res.Resources.*;

/**
 * Created by Administrator on 09-06-2017.
 */
public class HMConstants {
    DisplayMetrics displaymetrics = new DisplayMetrics();
    public static final int texttintcolor = Color.rgb(227, 228, 232);
    public static final int backcolor = Color.rgb(4, 2, 144);
    public static final int midcolor = Color.rgb(51, 74, 188);
    public static final int mgreen = Color.rgb(9, 114, 198);
    public static final int mgray = Color.rgb(245, 245, 245);
    public static final int screenWidth = getSystem().getDisplayMetrics().widthPixels;
    public static final int screenHeight = getSystem().getDisplayMetrics().heightPixels;
    public static final int plabelheightL = screenWidth / 10;
    public static final int plabelheightS = screenWidth / 20;
    public static final int plabelheightM = screenWidth / 30;
    public static final int plabelheightP = screenWidth / 16;
    public static final int tblHeight = screenHeight;
    public static final int tblWidth = screenWidth;
    public static final int gutter = screenHeight / 200;
    public static final double pimagewidth = screenWidth / 3.5;
    public static final int navtop = 0;
    public static final int navbar = 0;
    public static final int navitem = 44;
    public static final int navitema = 44;
    public static final int cellHeight = 800;
    public static final int cellStrip = 50;
    public static final int btnStrip = 70;
    public static final int btnHeight = 36;
    public static final int shortLabel = 60;
    public static final int longLabel = 400;
    public static final int mapHeight = 150;
    public static final int textRadius = 5;
    public static final int formHeight = 800;
    public static final double menubutton = screenWidth / 5.5;
    public static final int imageheight = 250;
    public static final int cartheight = 40;
    public static final int smalllabelheight = 15;
    public static final double cellcolwidth = screenWidth / 2;
    public static final int collgutter = screenHeight / 100;
    public static final double collimagesize = cellcolwidth - (collgutter * 2);
    public static final double slideheight = (screenHeight / 4) + navitem;
    public static final double cellheight = 120;
    public static final int emptyimage = 100;
    public static final double imagelistheight = screenWidth / 4.2;
    public static final double imagelistwidth = screenWidth / 3.5;
    public static final double btnViewHeight = 50;
    public static final double stepperwidth = screenWidth / 4;
    public static final String devicemodel = Build.MODEL;
    public static final String deviceuuid = "";
    public static final String deviceversion = Build.VERSION.RELEASE;
    public static final String deviceplatform = "android";
    public static final String devicemanufacturer = Build.MANUFACTURER;
    public static final String devicecomboined = devicemodel + "^ " + deviceuuid + "^ " + deviceversion + "^" + deviceplatform + "^" + devicemanufacturer;

    public static final String applocationshort = "https://apiservices.unosolindia.com/SSMCommerceSupport1/SSMCommerceAPI.svc";
    public static final String applocationlong = "https://apiservices.unosolindia.com/SSMCommerceSupport1/SSMCommerceAPI.svc";
    public static final String appurl = "https://apiservices.unosolindia.com/SSMCommerceSupport1/SSMCommerceAPI.svc";

    public static final String appcustomeremailsubject = "Saajha Delivery";
    public static final String alertheader = "Saajha";

    public static final String appmerchantid = "FRESS02008";
    public static final String appchannelref = "APP";
    public static final String appoutlet = "99910000110";
    public static final String productcategory = "101";
    public static final String itemtypeproduct = "PR";
    public static final String aboutUS = "107012020000001";
    public static final String tandc = "107012020000009";
    public static final String dataPrivacy = "107012020000004";
    public static final String refundPolicy = "107012020000006";
    public static final String contactUS = "107012020000002";
    public static final String faqType = "P";
    public static final String partnerHeader = "Delivery Dashboard";
    public static final String partnerTopic = "aicdelivery";
    public static final String DEVELOPER_KEY = "AIzaSyBbKmS3NldYxzZX7YRnc7WE7Z5x5D4kr-U";
    public static final String mobilePrefix = "+91";
    public static final String pagelink = "saajha.page.link";
    public static final String imagetype = "data:image/jpg;base64,";
}
