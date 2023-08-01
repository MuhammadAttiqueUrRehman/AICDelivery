package layout.newfiles;

public class Item {
    private String itemcode, itemname, itemunit,
            standardprice, totalamount, taxpercent, offerprice, itemname_a,giftmessagedetail;
    int quantity, deliverqty;

    public Item(String itemcode, String itemname, String itemunit, int quantity, String standardprice, String totalamount, String taxpercent, String offerprice, int deliverqty, String itemname_a,String giftmessagedetail) {
        this.itemcode = itemcode;
        this.itemname = itemname;
        this.itemunit = itemunit;
        this.quantity = quantity;
        this.standardprice = standardprice;
        this.totalamount = totalamount;
        this.taxpercent = taxpercent;
        this.offerprice = offerprice;
        this.deliverqty = deliverqty;
        this.itemname_a = itemname_a;
        this.giftmessagedetail = giftmessagedetail;
    }

    public String getItemcode() {
        return itemcode;
    }

    public String getItemname() {
        return itemname;
    }

    public String getItemunit() {
        return itemunit;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStandardprice() {
        return standardprice;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public String getTaxpercent() {
        return taxpercent;
    }

    public String getOfferprice() {
        return offerprice;
    }

    public int getDeliverqty() {
        return deliverqty;
    }

    public void setDeliverqty(int deliverqty) {
        this.deliverqty = deliverqty;
    }

    public String getItemname_a() {
        return itemname_a;
    }

    public String getGiftMessageDetail() {
        return giftmessagedetail;
    }

}
