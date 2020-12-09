package com.hunter.owen.myethics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class CreatePurchaseActivity extends FragmentActivity {

    private EditText inputProduct;
    private EditText inputDate;
    private Button buttonCreatePurchase;
    private AddTagLayout addTagLayout;
    private List<EthicTag> ethicTags;
    private LinearLayout tagContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_purchase);

        ethicTags = new ArrayList<EthicTag>();

        addTagLayout = new AddTagLayout(this, ethicTags);

        inputDate = findViewById(R.id.create_purchase_date);
        inputProduct = findViewById(R.id.create_purchase_product);
        buttonCreatePurchase = findViewById(R.id.create_purchase_button);
        tagContainer = findViewById(R.id.create_purchase_add_tag_container);

        tagContainer.addView(addTagLayout);
        final DatePickerFragment newFragment = new DatePickerFragment();

        inputDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                    Calendar date = newFragment.getDate();
                }
            }
        });

        buttonCreatePurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(!inputProduct.getText().toString().isEmpty() && !inputDate.getText().toString().isEmpty()){
                    DatabaseConnect.createPurchase(view.getContext(), inputProduct.getText().toString(), newFragment.getDate(), ethicTags, new ServerCallback() {
                        @Override
                        public void onSuccess(JsonObject result) {
                            if(result.has("success")) {
                                int success = result.get("success").getAsInt();
                                if (success == 1) {
                                    CreatePurchaseActivity.super.onBackPressed();
                                } else {
                                    String error = result.get("error").getAsString();
                                    addTagLayout.setError(error);
                                }
                            }else{
                                addTagLayout.setError("couldn't reach servers");
                            }
                        }
                    });
                }
            }
        });


    }

    public void setDateInput(Calendar date) {
        inputDate.setText(String.valueOf(date.get(Calendar.MONTH)) +  "/" + String.valueOf(date.get(Calendar.DATE)) +  "/" + String.valueOf(date.get(Calendar.YEAR)));
    }
}
