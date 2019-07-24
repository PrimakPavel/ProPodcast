package com.pavelprymak.propodcast;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private NavController mNavController;
    private BottomNavigationView mNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavView = findViewById(R.id.nav_view);
        mNavController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(mNavView, mNavController);
        //NavigationUI.setupActionBarWithNavController(this, mNavController);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mNavController.popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setNavViewVisibility(boolean isVisible) {
        if (isVisible) {
            mNavView.setVisibility(View.VISIBLE);
        } else {
            mNavView.setVisibility(View.GONE);
        }
    }

}
