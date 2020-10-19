package com.hunter.owen.myethics;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar init
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setNavigationIcon(R.drawable.baseline_menu_black_18dp);
        setSupportActionBar(toolbar);

        //navbar init
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //handle menu button presses
                selectMenuItem(menuItem);
                return true;
            }
        });

        //drawer init
        drawer = findViewById(R.id.drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Fragment manager (default is dashboard)
        Fragment mainFragment = DashboardFragment.newInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.viewer, mainFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    //Handle a menu item selection
    public void selectMenuItem(@NonNull MenuItem menuItem) {
        Fragment updateFragment = null;
        //get the fragment corresponding with each menu item
        switch (menuItem.getItemId()) {
            case R.id.nav_dashboard:
                updateFragment = DashboardFragment.newInstance();
                break;
            case R.id.nav_ethics_score:
                updateFragment = ScoreFragment.newInstance();
                break;
            case R.id.nav_settings:
                updateFragment = SettingsFragment.newInstance();
                break;
        }

        //change fragment
        if (updateFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.viewer, updateFragment);
            fragmentTransaction.commit();
            drawer.closeDrawers();
        }
    }

}
