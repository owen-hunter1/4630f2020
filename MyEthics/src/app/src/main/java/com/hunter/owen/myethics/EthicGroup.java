package com.hunter.owen.myethics;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EthicGroup extends LinearLayout {


    public String name;
    public String id;
    private ImageView icon;
    private String[] attributes;

    public EthicGroup(final Context context, String name, String id) {
        super(context);
        this.name = name;
        this.id = "group" + id;
        LinearLayout content = new LinearLayout(context);
        inflate(context, R.id., true);
        this.addView(content);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewGroupActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public void setIcon() {

    }

    public void addAttribute() {

    }

    public void removeAttribute() {

    }
}
