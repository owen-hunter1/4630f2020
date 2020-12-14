package com.hunter.owen.myethics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

public class EthicGroupLayout extends LinearLayout {

    EthicGroup ethicGroup;
    private ImageView icon;
    private String[] attributes;
    private TextView groupNameText;
    private ImageButton removeGroupButton;
    private Button ethicGroupButton;

    public EthicGroupLayout(final Context context, final String name, final int id) {
        super(context);
        ethicGroup = new EthicGroup(name, id, null);
        init();
    }

    public EthicGroupLayout(final Context context, final EthicGroup ethicGroup) {
        super(context);
        this.ethicGroup = new EthicGroup(ethicGroup);
        init();
    }

    public void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) li.inflate(R.layout.ethic_group, this, true);
        groupNameText = layout.findViewById(R.id.ethic_group_name);
        groupNameText.setText(ethicGroup.getName());
        removeGroupButton = layout.findViewById(R.id.remove_group_button);
        ethicGroupButton = layout.findViewById(R.id.ethic_group_button);

        ethicGroupButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.i("removal: ", "remove");
                removeGroupButton.setVisibility(VISIBLE);
                return true;
            }
        });

        ethicGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewGroupActivity.class);
                intent.putExtra("name", ethicGroup.getName());
                intent.putExtra("id", ethicGroup.getId());
                getContext().startActivity(intent);
            }
        });

        removeGroupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseConnect.deleteGroup(view.getContext(), ethicGroup.getId(), new ServerCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {

                    }
                });
                EthicGroupLayout.super.setVisibility(GONE);
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
