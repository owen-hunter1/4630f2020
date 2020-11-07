package com.hunter.owen.myethics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DashboardFragment extends Fragment {

    private MainViewModel mViewModel;
    private LinearLayout ethicGroupContainer;
    private List<EthicGroup> ethicGroups;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ethicGroupContainer = getActivity().findViewById(R.id.ethic_group_container);
        //todo: fetch dashboards from DB
        //DatabaseConnect databaseConnect = new DatabaseConnect(this.getContext());
        //databaseConnect.getDashboardGroups();

        //todo: populate dashboard objects

        //for each ethicGroup
        {
            ethicGroups.add(createEthicGroup());
        }

        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }

    private EthicGroup createEthicGroup(){
        Inflater groupInflater = new Inflater();
        EthicGroup ethicGroup = (EthicGroup) groupInflater.inflate(R.layout.ethic_group, null);
        return ethicGroup;
    }
}