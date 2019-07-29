package com.pavelprymak.propodcast;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.pavelprymak.propodcast.databinding.ActivityMainBinding;
import com.pavelprymak.propodcast.utils.otto.EventUpdateDurationAndCurrentPos;
import com.pavelprymak.propodcast.utils.otto.EventUpdatePlayerVisibility;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {
    private NavController mNavController;
    private ActivityMainBinding mBinding;
    private boolean isPlayerVisible = false;
    public static final String SAVE_INSTANCE_PLAYER_VISIBILITY = "SaveInstancePlayerVisibility";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(mBinding.navView, mNavController);
        if (savedInstanceState != null) {
            isPlayerVisible = savedInstanceState.getBoolean(SAVE_INSTANCE_PLAYER_VISIBILITY, false);
            setPlayerVisibility(isPlayerVisible);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_INSTANCE_PLAYER_VISIBILITY, isPlayerVisible);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.eventBus.unregister(this);
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

    @Subscribe
    public void onUpdatePlayerVisibility(EventUpdatePlayerVisibility eventUpdatePlayerVisibility) {
        isPlayerVisible = eventUpdatePlayerVisibility.isVisible();
        setPlayerVisibility(isPlayerVisible);
    }

    @Subscribe
    public void onPlayerUIUpdated(EventUpdateDurationAndCurrentPos eventUpdateDurationAndCurrentPos) {
        isPlayerVisible = true;
        setPlayerVisibility(isPlayerVisible);
    }

    private void setPlayerVisibility(boolean isPlayerVisible) {
        if (isPlayerVisible) {
            mBinding.playerContainer.setVisibility(View.VISIBLE);
        } else {
            mBinding.playerContainer.setVisibility(View.GONE);
        }
    }

    public void setNavViewVisibility(boolean isVisible) {
        if (isVisible) {
            mBinding.navView.setVisibility(View.VISIBLE);
        } else {
            mBinding.navView.setVisibility(View.GONE);
        }
    }

}
