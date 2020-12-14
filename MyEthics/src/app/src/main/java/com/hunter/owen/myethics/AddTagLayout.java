package com.hunter.owen.myethics;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Pattern;

public class AddTagLayout extends LinearLayout {

    private Button buttonAddTag;
    private ListView listViewTag;
    private RadioButton radioLiked;
    private RadioButton radioDisliked;
    private RatingBar ratingBar;
    private SearchView inputPurchaseTag;
    private TextView errorText;

    final private Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\-]");
    private EthicArrayAdapter listAdapter;
    public AddTagLayout(Context context, final List<EthicTag> ethicTags) {
        super(context);

        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) li.inflate(R.layout.tag_adder, this, true);

        errorText = layout.findViewById(R.id.create_group_error_text);
        radioLiked = layout.findViewById(R.id.create_group_like);
        radioDisliked = layout.findViewById(R.id.create_group_dislike);
        ratingBar = layout.findViewById(R.id.create_group_ratingBar);
        listViewTag = layout.findViewById(R.id.add_tag_list);
        inputPurchaseTag = layout.findViewById(R.id.input_group_tag);
        buttonAddTag = layout.findViewById(R.id.add_tag_button);

        //list adapter
         listAdapter = new EthicArrayAdapter(context, android.R.layout.simple_list_item_1, ethicTags);

        listViewTag.setAdapter(listAdapter);
        buttonAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EthicTag ethicTag = null;

                if(radioLiked.isChecked()){
                    ethicTag = new EthicTag(inputPurchaseTag.getQuery().toString().replace(' ','-').toLowerCase(), true, (int) Math.ceil((double)ratingBar.getRating() * 2));
                }else if(radioDisliked.isChecked()){
                    ethicTag = new EthicTag(inputPurchaseTag.getQuery().toString().replace(' ','-').toLowerCase(), false, (int) Math.ceil((double)ratingBar.getRating() * 2));
                }else{
                    errorText.setText("Like or dislike the tag.");
                    return;
                }
                if(ethicTag.getName().isEmpty()){
                    errorText.setText("Enter a tag.");
                }else if(!pattern.matcher(ethicTag.getName()).find()){
                    for(EthicTag e: ethicTags){
                        if(ethicTag.getName().equals(e.getName())) {
                            errorText.setText("Duplicate tag.");
                            return;
                        }
                    }
                    Log.i("Logged tag: ", ethicTag.getName());
                    ethicTags.add(ethicTag);
                    listAdapter.notifyDataSetChanged();
                    errorText.setText("");
                }else{
                    errorText.setText("Please enter only valid characters (A-Z, 0-9, spaces, and dashes)");
                }
            }
        });


    }

    public void setError(String error) {
        if(error.isEmpty()){
            errorText.setVisibility(View.INVISIBLE);
        }else {
            errorText.setVisibility(View.VISIBLE);
        }
        errorText.setText(error);
    }

    public void notifyDataSetChanged() {
        listAdapter.notifyDataSetChanged();
    }


}
