package com.example.kanghaeseok.foodmap1;

/**
 * Created by KangHaeSeok on 2018-06-07.
 */

public class restaurant {
    private String  rName, rNum, rX, rY, rAdd, rImage;
    private int rId;

    public String getrImage() {
        return rImage;
    }

    public void setrImage(String rImage) {
        this.rImage = rImage;
    }

    public int getrId() {
        return rId;

    }

    public void setrId(int rId) {
        this.rId = rId;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getrNum() {
        return rNum;
    }

    public void setrNum(String rNum) {
        this.rNum = rNum;
    }

    public String getrX() {
        return rX;
    }

    public void setrX(String rX) {
        this.rX = rX;
    }

    public String getrY() {
        return rY;
    }

    public void setrY(String rY) {
        this.rY = rY;
    }

    public String getrAdd() {
        return rAdd;
    }

    public void setrAdd(String rAdd) {
        this.rAdd = rAdd;
    }
}
