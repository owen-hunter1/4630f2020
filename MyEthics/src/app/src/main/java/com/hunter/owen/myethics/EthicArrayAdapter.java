package com.hunter.owen.myethics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class EthicArrayAdapter extends ArrayAdapter<EthicTag> {
    private final int MAX_TAG_LENGTH = 20;
    private static class ViewHolder{
        TextView tag;
        TextView rating;
        ImageView liked;
    }

    public EthicArrayAdapter(@NonNull Context context, int id, List<EthicTag> tags) {
        super(context, id, tags);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EthicTag ethicTag = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tag_list, parent, false);
            viewHolder.tag = convertView.findViewById(R.id.item_tag_list_tag);
            viewHolder.liked = convertView.findViewById(R.id.item_tag_list_liked);
            viewHolder.rating =  convertView.findViewById(R.id.item_tag_list_rating);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(ethicTag.getName().length() > MAX_TAG_LENGTH){
            viewHolder.tag.setText(ethicTag.getName().substring(0, MAX_TAG_LENGTH - 3) + "...");
        }else{
            viewHolder.tag.setText(ethicTag.getName());
        }
        viewHolder.rating.setText(String.valueOf(ethicTag.getRating()));
        viewHolder.liked.setImageResource(ethicTag.isLiked() ? R.drawable.thumb_up : R.drawable.thumb_down);

        return convertView;
    }
}
