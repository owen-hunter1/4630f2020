package com.hunter.owen.myethics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent registerIntent = new Intent(this, RegisterActivity.class);

        final EditText emailInput = findViewById(R.id.register_email);
        final EditText passwordInput = findViewById(R.id.register_password);
        final EditText confirmInput = findViewById(R.id.register_confirm);
        final TextView errorText = findViewById(R.id.register_error);
        Button registerButton = findViewById(R.id.create_account_button);
        final DatabaseConnect dbc = new DatabaseConnect(this);

        //register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailInput.getText().toString();
                String userPassword = passwordInput.getText().toString();
                String userConfirm = confirmInput.getText().toString();
                dbc.Register(userEmail, userPassword, userConfirm);
                dbc.setErrorText(errorText);
            }
        });
    }
}