package com.hunter.owen.myethics;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        Intent intent;
        //todo: Account login
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        DatabaseConnect dbc = new DatabaseConnect(this);
        if (sp.contains("email") && sp.contains("password")) {
            dbc.Login(sp.getString("email", ""), sp.getString("password", ""));
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            //switch to main activity
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
