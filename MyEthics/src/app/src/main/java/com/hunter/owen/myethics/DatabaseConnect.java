package com.hunter.owen.myethics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class DatabaseConnect {
    protected Context ctx;
    private String lastRequestResult;
    private TextView errorText;

    public DatabaseConnect(Context ctx) {
        //avoid memory leaks
        this.ctx = ctx.getApplicationContext();
    }

    public void Login(final String email, final String password) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

                int success = jsonObject.get("success").getAsInt();
                if (success == 1) {
                    int userId = jsonObject.get("user_id").getAsInt();
                    SharedPreferences sp = ctx.getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor spEditor = sp.edit();

                    spEditor.putString("email", email);
                    spEditor.putString("password", password);
                    spEditor.putInt("user_id", userId);
                    spEditor.apply();

                    Intent intent = new Intent(ctx, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                } else {
                    String error = jsonObject.get("error").getAsString();
                    errorText.setText(error);
                    errorText.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void Register(final String email, final String password, final String confirm) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

                int success = jsonObject.get("success").getAsInt();

                if (success == 1) {
                    Login(email, password);
                    return;
                } else {
                    String error = jsonObject.get("error").getAsString();
                    errorText.setText(error);
                    errorText.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "Failed to connect to server. Please try again later", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("confirm_password", confirm);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void createGroup(final String name, final List<EthicTag> ethicTags){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/createGroup.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

                int success = jsonObject.get("success").getAsInt();

                if(success == 1) {
                    ((Activity) ctx).finish();
                }else{
                    String error = jsonObject.get("error").getAsString();
                    errorText.setText(error);
                    errorText.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = ctx.getSharedPreferences("login", MODE_PRIVATE);
                Log.e("No user ID: ", String.valueOf(sp.getInt("user_id", 0)));
                params.put("user_id", String.valueOf(sp.getInt("user_id", 0)));
                params.put("name", name);
                params.put("tags", new Gson().toJson(ethicTags));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void getDashboardGroups(final ServerCallback callback){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/getDashboardGroups.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

                int success = jsonObject.get("success").getAsInt();

                if(success == 1) {
                    callback.onSuccess(jsonObject);
                }else{
                    String error = jsonObject.get("error").getAsString();
                    Toast.makeText(ctx, error, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = ctx.getSharedPreferences("login", MODE_PRIVATE);
                params.put("user_id", String.valueOf(sp.getInt("user_id", 0)));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public String getLastRequestResult() {
        return lastRequestResult;
    }

    public void setErrorText(TextView errorText) {
        this.errorText = errorText;
    }

}