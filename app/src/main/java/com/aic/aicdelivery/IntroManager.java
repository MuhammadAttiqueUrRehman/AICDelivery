package com.aic.aicdelivery;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class IntroManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    private static ArrayList<spinClass2> lists = new ArrayList<spinClass2>();
    private static ArrayList<spinClass2> listactive = new ArrayList<spinClass2>();

    public IntroManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("first1", 0);
        editor = pref.edit();
    }

    public void setLogged(Boolean logged) {
        editor.putBoolean("logged", logged);
        editor.commit();
    }

    public boolean getLogged() {
        return pref.getBoolean("logged", false);
    }


    public void setFirst1(Boolean first1) {
        editor.putBoolean("first1", first1);
        editor.commit();
    }

    public boolean getIsInit() {
        return pref.getBoolean("isint", false);
    }


    public void setIsInit(Boolean isInit) {
        editor.putBoolean("isint", isInit);
        editor.commit();
    }

    public boolean getFirst1() {
        return pref.getBoolean("first1", true);
    }

    public void setFirst2(Boolean first2) {
        editor.putBoolean("first2", first2);
        editor.commit();
    }

    public boolean getFirst2() {
        return pref.getBoolean("first2", true);
    }

    public void setLocation(String location) {
        editor.putString("location", location);
        editor.commit();
    }

    public String getLocation() {
        return pref.getString("location", "");
    }

    public void setLatLon(String latlon) {
        editor.putString("latlon", latlon);
        editor.commit();
    }

    public String getLatLon() {
        return pref.getString("latlon", "");
    }

    public void setHomeOP(String output) {
        editor.putString("output", output);
        editor.commit();
    }

    public String getHomeOP() {
        return pref.getString("output", "");
    }

    public void setServiceOP(String output) {
        editor.putString("serviceoutput", output);
        editor.commit();
    }

    public String getServiceOP() {
        return pref.getString("serviceoutput", "");
    }

    //Device ID
    public void setDevice(String device) {
        editor.putString("device", device);
        editor.commit();
    }

    public String getDevice() {
        return pref.getString("device", "");
    }


    //Catalog Code
    public void setCatalogcode(String catalogcode) {
        editor.putString("catalogcode", catalogcode);
        editor.commit();
    }

    public String getCatalogcode() {
        return pref.getString("catalogcode", "");
    }


    //City
    public void setCity(String city) {
        editor.putString("city", city);
        editor.commit();
    }

    public String getCity() {
        return pref.getString("city", "");
    }


    //Address Landmark
    public void setAddressLandMark(String addresslandmark) {
        editor.putString("addresslandmark", addresslandmark);
        editor.commit();
    }

    public String getAddressLandMark() {
        return pref.getString("addresslandmark", "");
    }


    //Address Register or New
    public void setAddresType(String addresstype) {
        editor.putString("addresstype", addresstype);
        editor.commit();
    }

    public String getAddresType() {
        return pref.getString("addresstype", "");
    }

    //Book Date Time
    public void setBookTime(String booktime) {
        editor.putString("booktime", booktime);
        editor.commit();
    }

    public String getBookTime() {
        return pref.getString("booktime", "");
    }

    //Geolocation
    public void setGeoLocation(String geolocation) {
        editor.putString("geolocation", geolocation);
        editor.commit();
    }

    public String getGeoLocation() {
        return pref.getString("geolocation", "");
    }

    //QA1
    public void setQAOption1(String qaoption1) {
        editor.putString("qaoption1", qaoption1);
        editor.commit();
    }

    public String getQAOption1() {
        return pref.getString("qaoption1", "");
    }

    //QA2
    public void setQAOption2(String qaoption2) {
        editor.putString("qaoption2", qaoption2);
        editor.commit();
    }

    public String getQAOption2() {
        return pref.getString("qaoption2", "");
    }

    //QA3
    public void setQAOption3(String qaoption3) {
        editor.putString("qaoption3", qaoption3);
        editor.commit();
    }

    public String getQAOption3() {
        return pref.getString("qaoption3", "");
    }

    //QA4
    public void setQAOption4(String qaoption4) {
        editor.putString("qaoption4", qaoption4);
        editor.commit();
    }

    public String getQAOption4() {
        return pref.getString("qaoption4", "");
    }

    //QA5
    public void setQAOption5(String qaoption5) {
        editor.putString("qaoption5", qaoption5);
        editor.commit();
    }

    public String getQAOption5() {
        return pref.getString("qaoption5", "");
    }

    //Additional Info
    public void setAdditionalInfo(String addinfo) {
        editor.putString("addinfo", addinfo);
        editor.commit();
    }

    public String getAdditionalInfo() {
        return pref.getString("addinfo", "");
    }


    //ServiceImage 1
    public void setServiceImage1(String serviceimage1) {
        editor.putString("serviceimage1", serviceimage1);
        editor.commit();
    }

    public String getServiceImage1() {
        return pref.getString("serviceimage1", "");
    }

    //ServiceImage 2
    public void setServiceImage2(String serviceimage2) {
        editor.putString("serviceimage2", serviceimage2);
        editor.commit();
    }

    public String getServiceImage2() {
        return pref.getString("serviceimage2", "");
    }

    //ServiceImage 3
    public void setServiceImage3(String serviceimage3) {
        editor.putString("serviceimage3", serviceimage3);
        editor.commit();
    }

    public String getServiceImage3() {
        return pref.getString("serviceimage3", "");
    }

    //ServiceImage 4
    public void setServiceImage4(String serviceimage4) {
        editor.putString("serviceimage4", serviceimage4);
        editor.commit();
    }

    public String getServiceImage4() {
        return pref.getString("serviceimage4", "");
    }

    //Item Code
    public void setItemCode(String itemcode) {
        editor.putString("itemcode", itemcode);
        editor.commit();
    }

    public String getItemCode() {
        return pref.getString("itemcode", "");
    }

    //Mobile Number
    public void setMobile(String mobile) {
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public String getMobile() {
        return pref.getString("mobile", "");
    }

    //Visiting Charges
    public void setVisitingCharges(String charges) {
        editor.putString("charges", charges);
        editor.commit();
    }

    public String getVisitingCharges() {
        return pref.getString("charges", "0");
    }

    //Firebase Key
    public void setFCMKey(String fcmkey) {
        editor.putString("fcmkey", fcmkey);
        editor.commit();
    }

    public String getFCMKey() {
        return pref.getString("fcmkey", "");
    }

    //Item Subcategory
    public void setSubcategory(String subcategory) {
        editor.putString("subcategory", subcategory);
        editor.commit();
    }

    public String getSubcategory() {
        return pref.getString("subcategory", "");
    }

    //App Sharing String
    public void setAppSharing(String appsharing) {
        editor.putString("appsharing", appsharing);
        editor.commit();
    }

    public String getAppSharing() {
        return pref.getString("appsharing", "");
    }

    //Login OP String
    public void setLoginOP(String loginop) {
        editor.putString("loginop", loginop);
        editor.commit();
    }

    public String getLoginOP() {
        return pref.getString("loginop", "");
    }

    //Service Category
    public void setCategory(String category) {
        editor.putString("category", category);
        editor.commit();
    }

    public String getCategory() {
        return pref.getString("category", "");
    }

    //Employee Id
    public void setEmployeeID(String employee) {
        editor.putString("employee", employee);
        editor.commit();
    }

    public String getEmployeeID() {
        return pref.getString("employee", "");
    }

    //Certificate
    public void setCertificate(String certificate) {
        editor.putString("certificate", certificate);
        editor.commit();
    }

    public String getCertificate() {
        return pref.getString("certificate", "");
    }

    //Company tpye
    public void setCompanyType(String companytype) {
        editor.putString("companytype", companytype);
        editor.commit();
    }

    public String getCompanyType() {
        return pref.getString("companytype", "");
    }

    //Checkin Status
    public void setCheckinStatus(String checkinstatus) {
        editor.putString("checkinstatus", checkinstatus);
        editor.commit();
    }

    public String getCheckinStatus() {
        return pref.getString("checkinstatus", "");
    }

    //Outlet Id
    public void setOutletID(String outletid) {
        editor.putString("outletid", outletid);
        editor.commit();
    }

    public String getOutletID() {
        return pref.getString("outletid", "");
    }

    //Employee Name
    public void setEmployeeName(String employeename) {
        editor.putString("employeename", employeename);
        editor.commit();
    }

    public String getEmployeeName() {
        return pref.getString("employeename", "");
    }

    //Admin
    public void setAdmin(String admin) {
        editor.putString("admin", admin);
        editor.commit();
    }

    public String getAdmin() {
        return pref.getString("admin", "");
    }

    //No City Limit
    public void setCityNoLimit(String citynolimit) {
        editor.putString("citynolimit", citynolimit);
        editor.commit();
    }

    public String getCityNoLimit() {
        return pref.getString("citynolimit", "");
    }

    //Push Offers
    public void setPushOffers(String pushoffers) {
        editor.putString("pushoffers", pushoffers);
        editor.commit();
    }

    public String getPushOffers() {
        return pref.getString("pushoffers", "");
    }


    //Clear all data
    public void clearAllData() {
        editor.clear();
        editor.commit();
    }

    //Add Service Category
    public void addEmployeeActive(spinClass2 employee) {
        listactive.add(employee);
    }

    //clear All Service Items
    public void clearEmployeeActive() {
        listactive.clear();
    }

    //remove Service Category
    public void removeEmployeeActive(spinClass2 employee) {
        for (int i = 0; i < lists.size(); i++) {
            if (listactive.get(i).equals(employee)) {
                listactive.remove(i);
            }
        }
    }

    //get Service Category
    public spinClass2 getEmployeeActive(spinClass2 employee) {
        spinClass2 r = new spinClass2(employee.getId(), employee.getName(), employee.getStatus());
        for (int i = 0; i < listactive.size(); i++) {
            if (listactive.get(i).equals(employee)) {
                r = listactive.get(i);
            }
        }
        return r;
    }

    //Retrieve Service Items
    public ArrayList<spinClass2> getAllEmployeesActive() {
        return listactive;
    }


    //Add Service Category
    public void addEmployee(spinClass2 employee) {
        lists.add(employee);
    }

    //clear All Service Items
    public void clearEmployee() {
        lists.clear();
    }

    //remove Service Category
    public void removeEmployee(spinClass2 employee) {
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).equals(employee)) {
                lists.remove(i);
            }
        }
    }

    //get Service Category
    public spinClass2 getEmployee(spinClass2 employee) {
        spinClass2 r = new spinClass2(employee.getId(), employee.getName(), employee.getStatus());
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).equals(employee)) {
                r = lists.get(i);
            }
        }
        return r;
    }

    //Retrieve Service Items
    public ArrayList<spinClass2> getAllEmployees() {
        return lists;
    }


    //App sharing text
    public void setSharingText(String sharingtext) {
        editor.putString("sharingtext", sharingtext);
        editor.commit();
    }

    public String getSharingText() {
        return pref.getString("sharingtext", "");
    }

    //App sharing url
    public void setSharingURL(String sharingurl) {
        editor.putString("sharingurl", sharingurl);
        editor.commit();
    }

    public String getSharingURL() {
        return pref.getString("sharingurl", "");
    }

    //Referrer Mobile
    public void setReferMobile(String refermobile) {
        editor.putString("refermobile", refermobile);
        editor.commit();
    }

    public String getReferMobile() {
        return pref.getString("refermobile", "");
    }

    //Referrer Device Id
    public void setReferDevice(String referdevice) {
        editor.putString("referdevice", referdevice);
        editor.commit();
    }

    public String getReferDevice() {
        return pref.getString("referdevice", "");
    }

    //New Customer Device Id
    public void setNewDevice(String newdevice) {
        editor.putString("newdevice", newdevice);
        editor.commit();
    }

    public String getNewDevice() {
        return pref.getString("newdevice", "");
    }

    public void setFirstTime(Boolean firsttime) {
        editor.putBoolean("firsttime", firsttime);
        editor.commit();
    }

    public boolean getFirstTime() {
        return pref.getBoolean("firsttime", true);
    }

    //Share Link
    public void setSharingLink(String sharinglink) {
        editor.putString("sharinglink", sharinglink);
        editor.commit();
    }

    public String getSharingLink() {
        return pref.getString("sharinglink", "");
    }

    public Float getLat() {
        return pref.getFloat("lat", 0);
    }

    public void setLat(Float lat) {
        editor.putFloat("lat", lat);
        editor.commit();
    }

    public Float getLon() {
        return pref.getFloat("lon", 0);
    }

    public void setLon(Float lon) {
        editor.putFloat("lon", lon);
        editor.commit();
    }

    public void setAppStoreURL(String appstoreurl) {
        editor.putString("appstoreurl", appstoreurl);
        editor.commit();
    }

    public String getAppStoreURL() {
        return pref.getString("appstoreurl", "");
    }

    public void setSupportEmail(String supportemail) {
        editor.putString("supportemail", supportemail);
        editor.commit();
    }

    public String getSupportEmail() {
        return pref.getString("supportemail", "");
    }

    public void setSupportCall(String supportcall) {
        editor.putString("supportcall", supportcall);
        editor.commit();
    }

    public String getSupportCall() {
        return pref.getString("supportcall", "");
    }

    public void setOutletName(String outletname) {
        editor.putString("outletname", outletname);
        editor.commit();
    }

    public String getOutletName() {
        return pref.getString("outletname", "");
    }

    public void setOrderId(String orderid) {
        editor.putString("orderid", orderid);
        editor.commit();
    }

    public String getOrderId() {
        return pref.getString("orderid", "");
    }

    public void setImageCount(Integer image1) {
        editor.putInt("image1", image1);
        editor.commit();
    }

    public Integer getImageCount() {
        return pref.getInt("image1", 0);
    }

    public void setQRCode(String qrcode) {
        editor.putString("qrcode", qrcode);
        editor.commit();
    }

    public String getQRCode() {
        return pref.getString("qrcode", "");
    }

    public void setPaymentMobile(String paymentmobile) {
        editor.putString("paymentmobile", paymentmobile);
        editor.commit();
    }

    public String getPaymentMobile() {
        return pref.getString("paymentmobile", "");
    }

    public void setPaymentBank(String paymentbank) {
        editor.putString("paymentbank", paymentbank);
        editor.commit();
    }

    public String getPaymentBank() {
        return pref.getString("paymentbank", "");
    }

    public void setCustomerCall(String customercall) {
        editor.putString("customercall", customercall);
        editor.commit();
    }

    public String getCustomerCall() {
        return pref.getString("customercall", "");
    }

    public void setIOSStoreURL(String iosstoreurl) {
        editor.putString("iosstoreurl", iosstoreurl);
        editor.commit();
    }

    public String getIOSStoreURL() {
        return pref.getString("iosstoreurl", "");
    }

    public void setLoginJson(String loginJson) {
        editor.putString("loginjson", loginJson);
        editor.commit();
    }

    public String getLoginJson() {
        return pref.getString("loginjson", "");
    }

    public void setMerchantCode(String MerchantCode) {
        editor.putString("merchant_code", MerchantCode);
        editor.commit();
    }

    public String getMerchantCode() {
        return pref.getString("merchant_code", HMConstants.appmerchantid);
    }

    public void setMobilePrefix(String MobilePrefix) {
        editor.putString("mobileprefix", MobilePrefix);
        editor.commit();
    }

    public String getMobilePrefix() {
        return pref.getString("mobileprefix", HMConstants.appmerchantid);
    }

    public void setMerchantName(String MerchantName) {
        editor.putString("merchant_name", MerchantName);
        editor.commit();
    }

    public String getMerchantName() {
        return pref.getString("merchant_name", HMConstants.appmerchantid);
    }
}
