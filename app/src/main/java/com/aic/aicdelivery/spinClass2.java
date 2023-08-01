package com.aic.aicdelivery;

public class spinClass2 {

    private String id,name,status;
    private int position;
    private boolean isChecked;

    public spinClass2(String name, String id,String status) {
        this.id = id;
        this.name = name;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof spinClass){
            spinClass c = (spinClass )obj;
            if(c.getName().equals(name) && c.getId()==id ) return true;
        }

        return false;
    }


}