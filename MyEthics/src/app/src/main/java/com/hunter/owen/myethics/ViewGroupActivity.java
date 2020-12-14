package com.hunter.owen.myethics;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ViewGroupActivity extends Activity {

    private TextView viewGroupTitle;
    private TextView viewGroupScore;
    private ListView tagsListView;
    private EthicGroup ethicGroup;
    private List<Purchase> purchaseList;
    private List<EthicTag> purchaseTags;
    private int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);
        tagsListView = findViewById(R.id.view_group_tag_listview);
        viewGroupTitle = findViewById(R.id.view_group_title);
        viewGroupScore = findViewById(R.id.view_group_score);

        score = 0;
        ethicGroup = new EthicGroup( getIntent().getStringExtra("name"), getIntent().getIntExtra("id", 0));

        final EthicArrayAdapter arrayAdapter = new EthicArrayAdapter(this, android.R.layout.simple_list_item_1, ethicGroup.getTagsList());
        tagsListView.setAdapter(arrayAdapter);

        purchaseList = new ArrayList<Purchase>();
        purchaseTags = new ArrayList<EthicTag>();

        DatabaseConnect.getGroupTag(this, ethicGroup.getId(), new ServerCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                JsonArray jsonArray = result.get("tags").getAsJsonArray();
                for(JsonElement jsonElement: jsonArray){
                    EthicTag tEthicTag = new Gson().fromJson(jsonElement.getAsJsonObject(), EthicTag.class);
                    ethicGroup.addTag(tEthicTag);
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });

        DatabaseConnect.getPurchases(this, new ServerCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                JsonArray jsonArray = result.get("purchases").getAsJsonArray();
                for(JsonElement jsonElement: jsonArray){
                    Log.i("purchase", jsonElement.toString());
                    Purchase tPurchase = new Gson().fromJson(jsonElement.getAsJsonObject(), Purchase.class);
                    purchaseList.add(tPurchase);
                }
                arrayAdapter.notifyDataSetChanged();
                DatabaseConnect.getGroupScore(getApplicationContext(), ethicGroup.getId(), purchaseList, new ServerCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {
                        score = result.get("score").getAsInt();
                        Log.i("score", String.valueOf(score));
                        viewGroupScore.setText(String.valueOf(score));
                    }
                });
            }
        });




    }
}