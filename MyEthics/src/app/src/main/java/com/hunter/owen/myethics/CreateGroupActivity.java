package com.hunter.owen.myethics;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CreateGroupActivity extends Activity {
    private Spinner defaultsSpinner;
    private TextView errorText;
    private EditText inputGroupName;
    private SearchView inputGroupTag;
    private Button buttonAddTag;
    private Button buttonCreateGroup;
    private ListView listViewTag;
    private RadioButton radioLiked;
    private RadioButton radioDisliked;
    private RatingBar ratingBar;
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
        inputGroupTag = findViewById(R.id.input_group_tag);
        buttonAddTag = findViewById(R.id.add_tag_button);
        buttonCreateGroup = findViewById(R.id.create_group_button);
        listViewTag = findViewById(R.id.add_tag_list);
        errorText = findViewById(R.id.create_group_error_text);
        radioLiked = findViewById(R.id.create_group_like);
        radioDisliked = findViewById(R.id.create_group_dislike);
        ratingBar = findViewById(R.id.create_group_ratingBar);

        //list adapter
        final EthicArrayAdapter listAdapter = new EthicArrayAdapter(this, android.R.layout.simple_list_item_1, listEthicTags);
        listViewTag.setAdapter(listAdapter);

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

                listAdapter.notifyDataSetChanged();
                errorText.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //database connection
        final DatabaseConnect dbc = new DatabaseConnect(this);

        inputGroupTag.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                dbc.queryTags(inputGroupName.getText().toString(), new ServerCallback() {
                    @Override
                    public void onSuccess(JsonObject result) {

                        JsonArray jsonArray = result.get("tags").getAsJsonArray();
                        for(JsonElement e: jsonArray){
                            listQueryResult.add(e.getAsString());
                        }
                    }
                });
            }
        });

        //add tag
        buttonAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EthicTag ethicTag = null;

                if(radioLiked.isChecked()){
                    ethicTag = new EthicTag(true, (int) Math.ceil((double)ratingBar.getRating() * 2), inputGroupTag.getQuery().toString().replace(' ','-').toLowerCase());
                }else if(radioDisliked.isChecked()){
                    ethicTag = new EthicTag(false, (int) Math.ceil((double)ratingBar.getRating() * 2), inputGroupTag.getQuery().toString().replace(' ','-').toLowerCase());
                }else{
                    errorText.setText("Like or dislike the tag.");
                    return;
                }
                if(ethicTag.getTag().isEmpty()){
                    errorText.setText("Enter a tag.");
                }else if(!pattern.matcher(ethicTag.getTag()).find()){
                    for(EthicTag e: listEthicTags){
                        if(ethicTag.getTag().equals(e.getTag())) {
                            errorText.setText("Duplicate tag.");
                            return;
                        }
                    }
                    Log.i("Logged tag: ", ethicTag.getTag());
                    listEthicTags.add(ethicTag);
                    listAdapter.notifyDataSetChanged();
                    errorText.setText("");
                }else{
                    errorText.setText("Please enter only valid characters (A-Z, 0-9, spaces, and dashes)");
                }
            }
        });


        //create group
        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputGroupName.getText().toString();
                if(name.isEmpty()){
                    errorText.setText("Enter a group name.");
                }else{
                    dbc.setErrorText(errorText);
                    dbc.createGroup(name, listEthicTags);
                }
            }
        });


        //listview remove item
        listViewTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listEthicTags.remove(i);
                listAdapter.notifyDataSetChanged();
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