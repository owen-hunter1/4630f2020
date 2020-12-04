package com.hunter.owen.myethics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DashboardFragment extends Fragment {

    private MainViewModel mViewModel;
    private LinearLayout ethicGroupContainer;
    private ImageButton buttonCreateGroup;
    private TextView errorText;
    private List<EthicGroup> ethicGroups;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ethicGroupContainer = view.findViewById(R.id.ethic_group_container);
        buttonCreateGroup = view.findViewById(R.id.dashboard_create_group_button);

        DatabaseConnect dbc = new DatabaseConnect(this.getContext());
        dbc.setErrorText(errorText);
        dbc.getDashboardGroups(new ServerCallback(){
            @Override
            public void onSuccess(JsonObject result) {
                if(result.has("groups")){
                    JsonArray resultAsJsonArray = result.getAsJsonArray("groups");
                    for(JsonElement name: resultAsJsonArray){
                        final EthicGroup ethicGroup = new EthicGroup(getContext(), name.getAsString(), 0);
                        ethicGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), ViewGroupActivity.class);
                                intent.putExtra("name", ethicGroup.name);
                                intent.putExtra("id", ethicGroup.id);
                                startActivity(intent);
                            }
                        });
                        LayoutInflater.from(getContext()).inflate(R.layout.ethic_group, ethicGroupContainer, false);
                        ethicGroupContainer.addView(ethicGroup);
                    }
                }else{
                    Log.e("No result", String.valueOf(result.has("groups")));
                }
            }
        });

        //todo: populate dashboard object


        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
            }
        });

    }
}