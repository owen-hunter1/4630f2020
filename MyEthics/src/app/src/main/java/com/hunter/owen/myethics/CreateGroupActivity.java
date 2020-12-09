package com.hunter.owen.myethics;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CreateGroupActivity extends Activity {
    private Spinner defaultsSpinner;
    private EditText inputGroupName;
    private Button buttonCreateGroup;
    private LinearLayout tagContainer;
    private AddTagLayout addTagLayout;


    //tags
    private List<EthicTag> listEthicTags = new ArrayList<EthicTag>();

    //query
    private List<String> listQueryResult = new ArrayList<String>();

    //string util
    final private Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\-]");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        defaultsSpinner = findViewById(R.id.defaults_spinner);
        inputGroupName = findViewById(R.id.input_group_name);
        buttonCreateGroup = findViewById(R.id.create_group_button);
        tagContainer = findViewById(R.id.create_group_add_tag_container);

        addTagLayout = new AddTagLayout(this, listEthicTags);
        tagContainer.addView(addTagLayout);

        //defaults list spinner adapter
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.defaults_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defaultsSpinner.setAdapter(spinnerAdapter);

        //spinner selection
        defaultsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputGroupName.setText(i > 0 ? spinnerAdapter.getItem(i) : "");
                switch (i){
                    //default
                    default:
                        setTags(new EthicTag[]{});
                        break;
                    case 1:
                        setTags(DefaultGroups.DEFAULT_VEGAN_TAGS);
                        break;
                    case 2:
                        setTags(DefaultGroups.DEFAULT_CLIMATE_TAGS);
                        break;
                    case 3:
                        setTags(DefaultGroups.DEFAULT_WORKERS_RIGHTS_TAGS);
                        break;
                }

                addTagLayout.notifyDataSetChanged();
                addTagLayout.setError("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //create group
        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputGroupName.getText().toString();
                if(name.isEmpty()){
                    addTagLayout.setError("Enter a group name.");
                }else{
                    DatabaseConnect.createGroup(getApplicationContext(), name, listEthicTags, new ServerCallback() {
                        @Override
                        public void onSuccess(JsonObject result) {
                            int success = result.get("success").getAsInt();
                            if(success == 1) {
                                CreateGroupActivity.super.onBackPressed();
                            }else{
                                String error = result.get("error").getAsString();
                                addTagLayout.setError(error);
                            }
                        }
                    });
                }
            }
        });
    }

    private void setTags(EthicTag[] ethicTags) {
        listEthicTags.clear();
        listEthicTags.clear();
        for(EthicTag et: ethicTags){
            listEthicTags.add(et);
        }
    }
}