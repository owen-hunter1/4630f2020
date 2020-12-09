package com.hunter.owen.myethics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;


public class PurchaseArrayAdapter extends ArrayAdapter<Purchase> {
    private final int MAX_TAG_LENGTH = 20;
    private static class ViewHolder{
        TextView product;
        TextView date;
        TextView score;
    }

    public PurchaseArrayAdapter(@NonNull Context context, int id, List<Purchase> tags) {
        super(context, id, tags);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Purchase purchase = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_purchase_list, parent, false);
            viewHolder.product = convertView.findViewById(R.id.item_purchase_list_item);
            viewHolder.date = convertView.findViewById(R.id.item_purchase_list_date);
            viewHolder.score = convertView.findViewById(R.id.item_purchase_list_score);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(purchase.getProduct_name().length() > MAX_TAG_LENGTH){
            viewHolder.product.setText(purchase.getProduct_name().substring(0, MAX_TAG_LENGTH - 3) + "...");
        }else{
            Log.i("product name: ", purchase.getProduct_name());
            viewHolder.product.setText(purchase.getProduct_name());
        }
        viewHolder.score.setText(String.valueOf(purchase.getScore()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm-dd-yyyy");
        viewHolder.date.setText(simpleDateFormat.format(purchase.getPurchase_date().getTime()));

        return convertView;
    }
}
