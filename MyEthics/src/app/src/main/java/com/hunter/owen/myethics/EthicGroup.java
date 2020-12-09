package com.hunter.owen.myethics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

public class EthicGroup extends LinearLayout {

    public String name;
    public int id;
    private ImageView icon;
    private String[] attributes;
    private TextView groupNameText;
    private ImageButton removeGroupButton;
    private Button ethicGroupButton;

    public EthicGroup(final Context context, final String name, final int id) {
        super(context);
        this.name = name;
        this.id = id;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) li.inflate(R.layout.ethic_group, this, true);
        groupNameText = layout.findViewById(R.id.ethic_group_name);
        groupNameText.setText(name);
        removeGroupButton = layout.findViewById(R.id.remove_group_button);
        ethicGroupButton = layout.findViewById(R.id.ethic_group_button);

        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                removeGroupButton.setVisibility(VISIBLE);
                return true;
            }
        });

        ethicGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewGroupActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("id", id);
                getContext().startActivity(intent);
            }
        });

        removeGroupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseConnect.deleteGroup(view.getContext(), id, new ServerCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {

                    }
                });
                EthicGroup.super.setVisibility(GONE);
            }
        });
    }


    public void setIcon() {

    }

    public void addAttribute() {

    }

    public void removeAttribute() {

    }

    public void hideRemoveGroupButton(){
        removeGroupButton.setVisibility(GONE);
    }
}
