package com.hunter.owen.myethics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CreateGroupActivity extends Activity {
    private Spinner defaultsSpinner;
    private TextView errorText;
    private EditText inputGroupName;
    private EditText inputGroupTag;
    private Button buttonAddTag;
    private Button buttonCreateGroup;
    private ListView listViewTagLiked;
    private ListView listViewTagDisliked;

    //tags
    private List<EthicTag> listEthicTags = new ArrayList<EthicTag>();
    private List<String> listLikedTags = new ArrayList<String>();
    private List<String> listDislikedTags = new ArrayList<String>();

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
        listViewTagLiked = findViewById(R.id.add_tag_list_liked);
        listViewTagDisliked = findViewById(R.id.add_tag_list_disliked);
        errorText = findViewById(R.id.create_group_error_text);

        //list adapter
        final ArrayAdapter<String> listAdapterLiked = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listLikedTags);
        final ArrayAdapter<String> listAdapterDisliked = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listDislikedTags);

        listViewTagLiked.setAdapter(listAdapterLiked);
        listViewTagLiked.setAdapter(listAdapterDisliked);

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
                        setTags(DefaultGroups.DEFAULT_CLIMATE_TAGS);
                        break;
                    case 2:
                        setTags(DefaultGroups.DEFAULT_VEGAN_TAGS);
                        break;
                    case 3:
                        setTags(DefaultGroups.DEFAULT_WORKERS_RIGHTS_TAGS);
                        break;
                }

                listAdapterLiked.notifyDataSetChanged();
                listAdapterDisliked.notifyDataSetChanged();
                errorText.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //database connection
        final DatabaseConnect dbc = new DatabaseConnect(this);

        //add tag
        buttonAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = inputGroupTag.getText().toString();
                tag = tag.replace(' ','-');
                if(tag.isEmpty()){
                    errorText.setText("Enter a tag.");
                }else if(!pattern.matcher(tag).find()){
                    if(listLikedTags.contains(tag) || listDislikedTags.contains(tag)) {
                        errorText.setText("Duplicate tag.");
                    }else {
                        listLikedTags.add(tag);
                        listDislikedTags.add(tag);
                        listAdapterLiked.notifyDataSetChanged();
                        listAdapterDisliked.notifyDataSetChanged();
                        errorText.setText("");
                    }
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
        listViewTagLiked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listLikedTags.remove(i);
                listAdapterLiked.notifyDataSetChanged();
            }
        });
        listViewTagDisliked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listDislikedTags.remove(i);
                listAdapterDisliked.notifyDataSetChanged();
            }
        });
    }

    private void setTags(EthicTag[] ethicTags) {
        listLikedTags.clear();
        listDislikedTags.clear();
        listEthicTags.clear();
        for(EthicTag et: ethicTags){
            listEthicTags.add(et);
            if(et.isLiked()) {
                listLikedTags.add(et.getTag());
            }else{
                listDislikedTags.add(et.getTag());
            }
        }
    }
}