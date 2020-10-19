package com.hunter.owen.myethics;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;

public class EthicGroup {

    private static int unique_id = 0;

    public String name;
    public String id;
    private ImageView icon;
    private String[] attributes;

    public EthicGroup(String name){
        this.name = name;
        this.id = "group" + unique_id;
        //icon = new ImageView();
    }

    public void setIcon(){

    }

    public void addAttribute(){

    }

    public void removeAttribute(){

    }
}
