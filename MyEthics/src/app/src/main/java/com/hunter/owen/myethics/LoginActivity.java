package com.hunter.owen.myethics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Intent registerIntent = new Intent(this, RegisterActivity.class);

        final EditText emailInput = findViewById(R.id.login_email);
        final EditText passwordInput = findViewById(R.id.login_password);
        final TextView errorText = findViewById(R.id.login_error);
        Button register = findViewById(R.id.register);
        Button loginButton = findViewById(R.id.login_button);
        final DatabaseConnect dbc = new DatabaseConnect(this);
        //login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailInput.getText().toString();
                String userPassword = passwordInput.getText().toString();
                if (userEmail != "" && userPassword != "") {
                    dbc.Login(userEmail, userPassword);
                    dbc.setErrorText(errorText);
                } else {
                    errorText.setText("Please enter your email or password or register a new account");
                }
            }
        });

        //register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(registerIntent);
            }
        });

    }
}