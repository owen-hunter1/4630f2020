package com.hunter.owen.myethics;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class PurchasesFragment extends Fragment {

    private ListView purchaseListView;
    private Button addPurchaseButton;

    private List<Purchase> purchaseList;
    PurchaseArrayAdapter purchaseArrayAdapter;

    public static PurchasesFragment newInstance() {
        return new PurchasesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.purchases_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        purchaseListView = view.findViewById(R.id.purchases_list);
        addPurchaseButton = view.findViewById(R.id.add_purchase_button);

        purchaseArrayAdapter = new PurchaseArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, purchaseList);
        purchaseList = new ArrayList<Purchase>();

        final PurchaseArrayAdapter purchaseArrayAdapter = new PurchaseArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, purchaseList);
        purchaseListView.setAdapter(purchaseArrayAdapter);

        addPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreatePurchaseActivity.class);
                startActivity(intent);
            }
        });

        DatabaseConnect.getPurchases(view.getContext(), new ServerCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                Log.i("result: ", result.toString());
                JsonArray jsonArray = result.get("purchases").getAsJsonArray();
                purchaseList.clear();
                for (JsonElement jsonElement : jsonArray){
                    Log.i("json element", jsonElement.getAsJsonObject().toString());
                    Purchase t_purchase = new Gson().fromJson(jsonElement.getAsJsonObject(), Purchase.class);
                    Log.i("t_purchase: ", t_purchase.getProduct_name() +  ", " + t_purchase.getPurchase_date().getTimeInMillis() +  ", " + t_purchase.getScore());
                    purchaseList.add(t_purchase);
                    purchaseArrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("resumed: ","resumed");
        DatabaseConnect.getPurchases(getContext(), new ServerCallback() {
            @Override
            public void onSuccess(JsonObject result) {
                Log.i("result: ", result.toString());
                JsonArray jsonArray = result.get("purchases").getAsJsonArray();
                purchaseList.clear();
                for (JsonElement jsonElement : jsonArray){
                    Log.i("json element", jsonElement.getAsJsonObject().toString());
                    Purchase t_purchase = new Gson().fromJson(jsonElement.getAsJsonObject(), Purchase.class);
                    Log.i("t_purchase: ", t_purchase.getProduct_name() +  ", " + t_purchase.getPurchase_date().getTimeInMillis() +  ", " + t_purchase.getScore());
                    purchaseList.add(t_purchase);
                    purchaseArrayAdapter.notifyDataSetChanged();
                }
            }
        });
        purchaseArrayAdapter.notifyDataSetChanged();
    }
}
