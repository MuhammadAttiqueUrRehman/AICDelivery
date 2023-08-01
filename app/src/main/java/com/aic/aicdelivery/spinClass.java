package com.aic.aicdelivery;

public class spinClass {

    private String id;
    private String name;
    private int position;
    private boolean isChecked;

    public spinClass(String id, String name,int position,Boolean check) {
        this.id = id;
        this.name = name;
        this.position=position;
        this.isChecked=check;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}