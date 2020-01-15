package com.example.sih.Models;

import android.net.Uri;

public class PostModel {
    private String mText1;
    private String mText2;
    private String mText3;
    private int mText4;

    public PostModel(String text1, String text2,String text3,int text4) {
        mText3 = text3;
        mText1 = text1;
        mText2 = text2;
        mText4 = text4;
    }

    public PostModel() {
    }


    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

    public String getText3() {
        return mText3;
    }

    public int getText4() {
        return mText4;
    }
}
