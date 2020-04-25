package com.example.vps.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.example.vps.R;
import com.example.vps.ui.adapter.CategoryFragmentAdpater;
import com.example.vps.ui.home.HomeFragment;
import com.example.vps.ui.setting.SettingActivity;
import com.example.vps.ui.util.AnalyticsUtil;
import com.example.vps.ui.util.common.Constants;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainActivityViewModel mainActivityViewModel;
    private ViewPager viewPager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private HomeFragment homeFragment;
    private DrawerLayout drawerLayout;
    private static final int SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if(savedInstanceState == null){
            homeFragment = HomeFragment.getInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, homeFragment)
                    .commitAllowingStateLoss();
        }
        setUpToolBar();
        //Find the view pager that will allow the user to swipe between fragments
        viewPager = findViewById(R.id.viewpager);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Set category fragment pager adapter


        NavigationView navigationView = findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0).setCheckable(true));
        CategoryFragmentAdpater fragmentAdpater = new CategoryFragmentAdpater(this, getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdpater);
        AnalyticsUtil.getInstance(this).setUserProperty(FirebaseAnalytics.Event.APP_OPEN, "open");
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(getString(R.string.app_name));
            toolbar.setContentInsetsAbsolute(SIZE, SIZE);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Switch Fragments in a ViewPager on clicking items in Navigation Drawer
        switch (id){
            case R.id.nav_home:
                viewPager.setCurrentItem(Constants.HOME);
                break;
            case R.id.nav_world:
                viewPager.setCurrentItem(Constants.WORLD);
                break;
            case R.id.nav_science:
                viewPager.setCurrentItem(Constants.SCIENCE);
                break;
            case R.id.nav_sport:
                viewPager.setCurrentItem(Constants.SPORT);
                break;
            case R.id.nav_environment:
                viewPager.setCurrentItem(Constants.ENVIRONMENT);
                break;
            case R.id.nav_society:
                viewPager.setCurrentItem(Constants.SOCIETY);
                break;
            case R.id.nav_fashion:
                viewPager.setCurrentItem(Constants.FASHION);
                break;
            case R.id.nav_business:
                viewPager.setCurrentItem(Constants.BUSINESS);
                break;
            case R.id.nav_culture:
                viewPager.setCurrentItem(Constants.CULTURE);
                break;
            default:
                viewPager.setCurrentItem(Constants.HOME);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
