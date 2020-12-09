package com.hunter.owen.myethics;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

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
        //register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String userEmail = emailInput.getText().toString();
                final String userPassword = passwordInput.getText().toString();
                String userConfirm = confirmInput.getText().toString();
                DatabaseConnect.Register(view.getContext(), userEmail, userPassword, userConfirm, new ServerCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        int success = result.get("success").getAsInt();

                        if (success == 1) {
                            DatabaseConnect.Login(view.getContext(), userEmail, userPassword, new ServerCallback() {
                                @Override
                                public void onSuccess(JsonObject result) {
                                    int success = result.get("success").getAsInt();
                                    if (success == 1) {
                                        int userId = result.get("user_id").getAsInt();
                                        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                                        SharedPreferences.Editor spEditor = sp.edit();

                                        spEditor.putString("email", userEmail);
                                        spEditor.putString("password", userPassword);
                                        spEditor.putInt("user_id", userId);
                                        spEditor.apply();

                                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        view.getContext().startActivity(intent);
                                        ((Activity) view.getContext()).finish();
                                    } else {
                                        if(result.has("error")){
                                            String error = result.get("error").getAsString();
                                            errorText.setText(error);
                                            errorText.setVisibility(View.VISIBLE);
                                        }else{
                                            errorText.setText("Can't connect to servers");
                                            errorText.setVisibility(View.VISIBLE);

                                        }
                                    }
                                }
                            });
                        } else {
                            String error = result.get("error").getAsString();
                            errorText.setText(error);
                            errorText.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }
}