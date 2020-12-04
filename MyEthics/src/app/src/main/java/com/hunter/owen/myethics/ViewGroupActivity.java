package com.hunter.owen.myethics;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class ViewGroupActivity extends Activity {
    String name;
    int id;
    TextView viewGroupTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        viewGroupTitle = findViewById(R.id.view_group_title);

        name = getIntent().getStringExtra("name");
        id = getIntent().getIntExtra("id", 0);

        //todo: get database group info

        viewGroupTitle.setText(name);
    }
}