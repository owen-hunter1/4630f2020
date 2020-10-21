package com.hunter.owen.myethics;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class DatabaseConnect {
    protected Context ctx;

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
                //Toast.makeText(ctx, "Yay! this worked", Toast.LENGTH_LONG).show();
                Toast.makeText(ctx, response, Toast.LENGTH_LONG).show();
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
                Toast.makeText(ctx, response.toString(), Toast.LENGTH_LONG).show();
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
}
