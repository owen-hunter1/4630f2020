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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DashboardFragment extends Fragment {

    private MainViewModel mViewModel;
    private Button removeGroupsButton;
    private LinearLayout ethicGroupContainer;
    private LinearLayout leftEthicGroupContainer;
    private LinearLayout rightEthicGroupContainer;

    private ImageButton buttonCreateGroup;

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

        removeGroupsButton = view.findViewById(R.id.hide_remove_groups_button);
        ethicGroupContainer = view.findViewById(R.id.ethic_group_container);
        leftEthicGroupContainer = view.findViewById(R.id.left_ethic_group_container);
        rightEthicGroupContainer = view.findViewById(R.id.right_ethic_group_container);
        buttonCreateGroup = view.findViewById(R.id.dashboard_create_group_button);

        removeGroupsButton.setVisibility(View.GONE);

        DatabaseConnect.getGroups(view.getContext(), new ServerCallback(){
            @Override
            public void onSuccess(JsonObject result) {
                if(result.has("groups")){
                    leftEthicGroupContainer.removeAllViews();
                    rightEthicGroupContainer.removeAllViews();
                    JsonArray resultAsJsonArray = result.getAsJsonArray("groups");
                    boolean isLeft = true;
                    for(JsonElement jsonElement: resultAsJsonArray){
                        EthicGroup group = new Gson().fromJson(jsonElement, EthicGroup.class);
                        final EthicGroupLayout ethicGroup = new EthicGroupLayout(getContext(), group);

                        if(isLeft){
                            LayoutInflater.from(getContext()).inflate(R.layout.ethic_group, leftEthicGroupContainer, false);
                            leftEthicGroupContainer.addView(ethicGroup);
                        }else{
                            LayoutInflater.from(getContext()).inflate(R.layout.ethic_group, rightEthicGroupContainer, false);
                            rightEthicGroupContainer.addView(ethicGroup);
                        }
                        isLeft = !isLeft;
                    }
                }else{
                    Log.e("No result", String.valueOf(result.has("groups")));
                }
            }
        });

        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        removeGroupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < leftEthicGroupContainer.getChildCount(); i++){
                    ((EthicGroupLayout)leftEthicGroupContainer.getChildAt(i)).hideRemoveGroupButton();
                }
                for(int i = 0; i < rightEthicGroupContainer.getChildCount(); i++){
                    ((EthicGroupLayout)rightEthicGroupContainer.getChildAt(i)).hideRemoveGroupButton();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseConnect.getGroups(getContext(), new ServerCallback(){
            @Override
            public void onSuccess(JsonObject result) {
                if(result.has("groups")){
                    leftEthicGroupContainer.removeAllViews();
                    rightEthicGroupContainer.removeAllViews();
                    JsonArray jsonArray = result.get("groups").getAsJsonArray();
                    boolean isLeft = true;
                    for(JsonElement jsonElement: jsonArray){
                        final EthicGroupLayout ethicGroup = new EthicGroupLayout(getContext(), new Gson().fromJson(jsonElement.toString(), EthicGroup.class));
                        if(isLeft){
                            LayoutInflater.from(getContext()).inflate(R.layout.ethic_group, leftEthicGroupContainer, false);
                            leftEthicGroupContainer.addView(ethicGroup);
                        }else{
                            LayoutInflater.from(getContext()).inflate(R.layout.ethic_group, rightEthicGroupContainer, false);
                            rightEthicGroupContainer.addView(ethicGroup);
                        }
                        isLeft = !isLeft;
                    }
                }else{
                    Log.e("No result", String.valueOf(result.has("groups")));
                }
            }
        });
    }
    void toggleButtonVisibility(){
        if(removeGroupsButton.getVisibility() == View.GONE){
            removeGroupsButton.setVisibility(View.VISIBLE);
        }else{
            removeGroupsButton.setVisibility(View.GONE);
        }
    }
}