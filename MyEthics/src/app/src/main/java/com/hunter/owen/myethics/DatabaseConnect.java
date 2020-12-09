package com.hunter.owen.myethics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class DatabaseConnect {
    private String lastRequestResult;

    public DatabaseConnect() {
    }

    public static void Login(final Context ctx, final String email, final String password, final ServerCallback serverCallback) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                serverCallback.onSuccess(JsonParser.parseString(response).getAsJsonObject());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "Error: cannot connect to server :: " + error.getMessage(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ctx, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
                ((Activity) ctx).finish();
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

    public static void Register(final Context ctx, final String email, final String password, final String confirm, final ServerCallback serverCallback) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/register.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                serverCallback.onSuccess(JsonParser.parseString(response).getAsJsonObject());

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

    public static void createGroup(final Context ctx, final String name, final List<EthicTag> ethicTags, final ServerCallback serverCallback){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/createGroup.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                serverCallback.onSuccess(JsonParser.parseString(response).getAsJsonObject());
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
                params.put("name", name);
                params.put("tags", new Gson().toJson(ethicTags));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public static void createPurchase(final Context ctx, final String name, final Calendar date, final List<EthicTag> ethicTags, final ServerCallback serverCallback){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/createPurchase.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                serverCallback.onSuccess(JsonParser.parseString(response).getAsJsonObject());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sp = ctx.getSharedPreferences("login", MODE_PRIVATE);
                params.put("user_id", String.valueOf(sp.getInt("user_id", 0)));
                params.put("name", name);
                params.put("date", String.valueOf(date.getTimeInMillis()));
                params.put("tags", new Gson().toJson(ethicTags));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public static void deleteGroup(final Context ctx, final int groupId, final ServerCallback serverCallback){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/deleteGroups.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                serverCallback.onSuccess(JsonParser.parseString(response).getAsJsonObject());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx, "error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("group_id", String.valueOf(groupId));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public static void getGroups(final Context ctx, final ServerCallback callback){
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

    public static void getPurchases(final Context ctx, final ServerCallback callback){
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/getPurchases.php";
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

    public static void queryTags(final Context ctx, final String text, final ServerCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "http://www.treatyelm.com/ohunter/queryTags.php";
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
                params.put("query", text);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public String getLastRequestResult() {
        return lastRequestResult;
    }
}