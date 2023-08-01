package com.aic.aicdelivery;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class providerInfo {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    private static ArrayList<String> lists=new ArrayList<String>();
    public providerInfo(Context context) {
        this.context = context;
        pref=context.getSharedPreferences("fullname",0);
        editor = pref.edit();
    }

    //Clear all data
    public void clearAllData(){
        editor.clear();
        editor.commit();
    }

    //Add Service Category
    public void addServiceItem(String serviceitem){
            lists.add(serviceitem);
    }

    //clear All Service Items
    public void clearServiceItem(){
        lists.clear();
    }

    //remove Service Category
    public void removeServiceItem(String serviceitem){
        for (int i=0;i<lists.size();i++){
            if(lists.get(i).equals(serviceitem)){
                lists.remove(i);
            }
        }
    }

    //get Service Category
    public String getServiceItem(String serviceitem){
        String r="";
        for (int i=0;i<lists.size();i++){
            if(lists.get(i).equals(serviceitem)){
                r=lists.get(i);
            }
        }
        return r;
    }


    //Retrieve Service Items
    public ArrayList<String> getAllServiceItems(){
        return lists;
    }
    //Full Name
    public void setFullName(String fullname) {
        editor.putString("fullname", fullname);
        editor.commit();
    }

    public String getFullName() {
        return pref.getString("fullname", "");
    }

    //Entity
    public void setEntity(String entity) {
        editor.putString("entity", entity);
        editor.commit();
    }

    public String getEntity() {
        return pref.getString("entity", "");
    }

    //Year of Formation
    public void setYear(String year) {
        editor.putString("year", year);
        editor.commit();
    }

    public String getYear() {
        return pref.getString("year", "");
    }

    //Team size
    public void setTeamSize(String team) {
        editor.putString("team", team);
        editor.commit();
    }

    public String getTeamSize() {
        return pref.getString("team", "");
    }

    //geolocation
    public void setLocation(String location) {
        editor.putString("location", location);
        editor.commit();
    }

    public String getLocation() {
        return pref.getString("location", "");
    }

    //Mobile 1
    public void setMobile1(String mobile1) {
        editor.putString("mobile1", mobile1);
        editor.commit();
    }

    public String getMobile1() {
        return pref.getString("mobile1", "");
    }

    //Mobile 2
    public void setMobile2(String mobile2) {
        editor.putString("mobile2", mobile2);
        editor.commit();
    }

    public String getMobile2() {
        return pref.getString("mobile2", "");
    }

    //email
    public void setEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString("email", "");
    }

    //Address
    public void setAddress(String address) {
        editor.putString("address", address);
        editor.commit();
    }

    public String getAddress() {
        return pref.getString("address", "");
    }

    //Gender
    public void setGender(String gender) {
        editor.putString("gender", gender);
        editor.commit();
    }

    public String getGender() {
        return pref.getString("gender", "");
    }

    //Qualifiction
    public void setQualification(String qualify) {
        editor.putString("qualify", qualify);
        editor.commit();
    }

    public String getQualification() {
        return pref.getString("qualify", "");
    }

    public void setServiceCategory(String services) {
        editor.putString("services",  services);
        editor.commit();
    }

    public String getServiceCategory() {
        return pref.getString("services", "");
    }

    //Account
    public void setAccount(String account) {
        editor.putString("account", account);
        editor.commit();
    }

    public String getAccount() {
        return pref.getString("account", "");
    }

    //IFSC
    public void setIFSC(String ifsc) {
        editor.putString("ifsc", ifsc);
        editor.commit();
    }

    public String getIFSC() {
        return pref.getString("ifsc", "");
    }

    //Referral
    public void setReferral(String referral) {
        editor.putString("referral", referral);
        editor.commit();
    }

    public String getReferral() {
        return pref.getString("referral", "");
    }


    //Aadhar
    public void setAadhar(String aadhar) {
        editor.putString("aadhar", aadhar);
        editor.commit();
    }

    public String getAadhar() {
        return pref.getString("aadhar", "");
    }

    //TradeLicense
    public void setTrade(String trade) {
        editor.putString("trade", trade);
        editor.commit();
    }

    public String getTrade() {
        return pref.getString("trade", "");
    }

    //PAN
    public void setPAN(String pan) {
        editor.putString("pan", pan);
        editor.commit();
    }

    public String getPAN() {
        return pref.getString("pan", "");
    }

    //Driving license
    public void setDL(String dl) {
        editor.putString("dl", dl);
        editor.commit();
    }

    public String getDL() {
        return pref.getString("dl", "");
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


    //Profile Image
    public void setProfileImage(String profileimage) {
        editor.putString("profileimage", profileimage);
        editor.commit();
    }

    public String getProfileImage() {
        return pref.getString("profileimage", "");
    }


    //Aadhar card
    public void setAadharImage(String aadharimage) {
        editor.putString("aadharimage", aadharimage);
        editor.commit();
    }

    public String getAadharImage() {
        return pref.getString("aadharimage", "");
    }


    //Trade License
    public void setTradeImage(String tradeimage) {
        editor.putString("tradeimage", tradeimage);
        editor.commit();
    }

    public String getTradeImage() {
        return pref.getString("tradeimage", "");
    }

    //PAN
    public void setPANImage(String panimage) {
        editor.putString("panimage", panimage);
        editor.commit();
    }

    public String getPANImage() {
        return pref.getString("panimage", "");
    }


    //DL
    public void setDLImage(String dlimage) {
        editor.putString("dlimage", dlimage);
        editor.commit();
    }

    public String getDLImage() {
        return pref.getString("dlimage", "");
    }

    //GST Number
    public void setGSTNumber(String gstnumber) {
        editor.putString("gstnumber", gstnumber);
        editor.commit();
    }

    public String getGSTNumber() {
        return pref.getString("gstnumber", "");
    }

    //Contact Name
    public void setContactName(String contactname) {
        editor.putString("contactname", contactname);
        editor.commit();
    }

    public String getContactName() {
        return pref.getString("contactname", "");
    }

    //Operating Radius
    public void setRadius(String radius) {
        editor.putString("radius", radius);
        editor.commit();
    }

    public String getRadius() {
        return pref.getString("radius", "0");
    }

}

