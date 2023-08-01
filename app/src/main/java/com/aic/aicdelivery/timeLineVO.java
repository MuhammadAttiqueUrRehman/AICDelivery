package com.aic.aicdelivery;

public class timeLineVO {
    private String orderdate, customercode, ordernumber, orderdes, status, statusdes, vendorid, price, vendorname, statusid, statusname, maxstatus, remarks, materialcharges, materialgst,customername;
    private String deliverydate, paymenttype, numberofitems, vendorcode, vehiclename, address, mobilenumber, geolocation, deliveryleadtime, orderstatus, itemdetail;
    String giftmessage;

    public timeLineVO(String orderdate, String customercode, String ordernumber, String orderdes, String status, String statusdes, String vendorid, String price, String vendorname, String maxstatus, String remarks, String materialcharges, String materialgst, String deliverydate, String paymenttype, String numberofitems, String vendorcode, String vehiclename, String address, String mobilenumber, String geolocation, String deliveryleadtime, String orderstatus, String itemdetail, String giftmessage,String customername) {
        this.orderdate = orderdate;
        this.customercode = customercode;
        this.ordernumber = ordernumber;
        this.orderdes = orderdes;
        this.status = status;
        this.statusdes = statusdes;
        this.vendorid = vendorid;
        this.vendorname = vendorname;
        this.maxstatus = maxstatus;
        this.remarks = remarks;
        this.materialcharges = materialcharges;
        this.materialgst = materialgst;
        this.price = price.equals("") ? "0" : price;
        this.customername=customername;
        this.deliverydate = deliverydate;
        this.paymenttype = paymenttype;
        this.numberofitems = numberofitems;
        this.vendorcode = vendorcode;
        this.vehiclename = vehiclename;
        this.address = address;
        this.mobilenumber = mobilenumber;
        this.geolocation = geolocation;
        this.deliveryleadtime = deliveryleadtime;
        this.orderstatus = orderstatus;
        this.itemdetail = itemdetail;

        this.giftmessage = giftmessage;
    }

    public String getOrderDate() {
        return orderdate;
    }

    public void setOrderDate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getCustomercode() {
        return customercode;
    }

    public String getOrderNumber() {
        return ordernumber;
    }

    public void setOrderNumber(String ordernumber) {
        this.orderdate = ordernumber;
    }

    public String getOrderDes() {
        return orderdes;
    }

    public void setOrderDes(String orderdes) {
        this.orderdes = orderdes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusdes() {
        return statusdes;
    }

    public void setStatusdes(String statusdes) {
        this.statusdes = statusdes;
    }

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String vendorid) {
        this.price = price;
    }

    public String getVendorName() {
        return vendorname;
    }

    public void setVendorName(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getMaxStatus() {
        return maxstatus;
    }

    public void setMaxStatus(String maxstatus) {
        this.maxstatus = maxstatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMaterialCharges() {
        return materialcharges;
    }

    public void setMaterialCharges(String materialcharges) {
        this.materialcharges = materialcharges;
    }

    public String getMaterialGST() {
        return materialgst;
    }

    public void setMaterialGST(String materialgst) {
        this.materialgst = materialgst;
    }

    public String getDeliverydate() {
        return deliverydate;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public String getNumberofitems() {
        return numberofitems;
    }

    public String getVendorcode() {
        return vendorcode;
    }

    public String getVehiclename() {
        return vehiclename;
    }


    public String getAddress() {
        return address;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public String getDeliveryleadtime() {
        return deliveryleadtime;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public String getItemdetail() {
        return itemdetail;
    }

    public String getGiftmessage() {
        return giftmessage;
    }

    public String getCustomerName() {
        return customername;
    }

    public timeLineVO updateData(String deliveryDate, String address, String geolocation) {
        this.deliverydate = deliveryDate;
        this.address = address;
        this.geolocation = geolocation;
        return this;
    }
}
