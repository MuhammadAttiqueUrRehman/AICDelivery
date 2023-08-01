package com.aic.aicdelivery;

public class reviewVO {
    private String imageurl,outletcode,outletname,reviewtext,customer,city,reviewdate,servicecategory;
    private int numOfItems,starrating,numberreviews;

    public reviewVO(String imageurl,int numOfItems,String outletcode,String outletname,int starrating,int numberreviews,String reviewtext,String customer,String city,String reviewdate,String servicecategory ){
        this.imageurl=imageurl;
        this.numOfItems=numOfItems;
        this.outletcode=outletcode;
        this.outletname=outletname;
        this.starrating=starrating;
        this.numberreviews=numberreviews;
        this.reviewtext=reviewtext;
        this.customer=customer;
        this.city=city;
        this.reviewdate=reviewdate;
        this.servicecategory=servicecategory;

    }

    public String getImageUrl(){
        return imageurl;
    }

    public void setImageUrl(String imageurl){
        this.imageurl=imageurl;
    }

    public int getNumOfItems(){
        return numOfItems;
    }

    public void setNumOfItems(int numOfItems){
        this.numOfItems=numOfItems;
    }


    public int getStarrating(){
        return starrating;
    }

    public void setStarrating(int starrating){
        this.starrating=starrating;
    }

    public int getNumberreviews(){
        return numberreviews;
    }

    public void setNumberreviews(int numberreviews){
        this.numberreviews=numberreviews;
    }

    public String getOutletcode(){
        return outletcode;
    }

    public void setOutletcode(String outletcode){
        this.outletcode=outletcode;
    }

    public String getOutletname(){
        return outletname;
    }

    public void setOutletname(String outletname){
        this.outletcode=outletname;
    }

    public String getReviewtext(){
        return reviewtext;
    }

    public void setReviewtext(String reviewtext){
        this.reviewtext=reviewtext;
    }

    public String getCustomer(){
        return customer;
    }

    public void setCustomer(String customer){
        this.customer=customer;
    }

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city=city;
    }

    public String getReviewdate(){
        return reviewdate;
    }

    public void setReviewdate(String reviewdate){
        this.reviewdate=reviewdate;
    }

    public String getServicecategory(){
        return servicecategory;
    }

    public void SetServicecategory(String servicecategory){
        this.servicecategory=servicecategory;
    }
}


