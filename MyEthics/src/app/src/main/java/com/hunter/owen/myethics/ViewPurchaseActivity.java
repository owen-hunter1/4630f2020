package com.hunter.owen.myethics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ViewPurchaseActivity extends AppCompatActivity {

    private TextView purchaseNameTextView;
    private TextView purchaseDateTextView;
    private ListView tagsListView;
    Purchase purchase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_purchase);
    }
}