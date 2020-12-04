package com.hunter.owen.myethics;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EthicGroup extends LinearLayout {

    public String name;
    public int id;
    private ImageView icon;
    private String[] attributes;
    private TextView groupNameText;

    public EthicGroup(final Context context, String name, int id) {
        super(context);
        this.name = name;
        this.id = id;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) li.inflate(R.layout.ethic_group, this, true);
        groupNameText = layout.findViewById(R.id.ethic_group_name);
        groupNameText.setText(name);
    }

    public void setIcon() {

    }

    public void addAttribute() {

    }

    public void removeAttribute() {

    }
}
