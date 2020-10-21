package com.hunter.owen.myethics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DashboardFragment extends Fragment {

    private MainViewModel mViewModel;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //todo: fetch dashboards from DB
        //DatabaseConnect databaseConnect = new DatabaseConnect(this.getContext());
        //databaseConnect.getDashboardGroups();

        //todo: populate dashboard objects


        return inflater.inflate(R.layout.dashboard_fragment, container, false);
    }
}