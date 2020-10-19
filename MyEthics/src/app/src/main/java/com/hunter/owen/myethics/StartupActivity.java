package com.hunter.owen.myethics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        //todo: Display logo
        //todo: Account login

        //switch to main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
