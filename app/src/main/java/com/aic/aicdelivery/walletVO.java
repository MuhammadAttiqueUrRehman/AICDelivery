package com.aic.aicdelivery;

public class walletVO {

    private String date;
    private String narration1;
    private String narration2;
    private String amount;

    public walletVO(String date,String narration1,String narration2,String amount){
        this.date=date;
        this.narration1=narration1;
        this.narration2=narration2;
        this.amount=amount;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date=date;
    }

    public String getNarration1(){
        return narration1;
    }

    public void setNarration1(String narration1){
        this.narration1=narration1;
    }

    public String getNarration2(){
        return narration2;
    }

    public void setNarration2(String narration2){
        this.narration2=narration2;
    }

    public String getAmount(){
        return amount;
    }

    public void setAmount(String amount){
        this.amount=amount;
    }

}
