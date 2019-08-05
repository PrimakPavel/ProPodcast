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
import com.pavelprymak.propodcast.services.PlayerService;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdateDurationAndCurrentPos;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayerVisibility;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {
    private NavController mNavController;
    private ActivityMainBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(mBinding.navView, mNavController);
    }


    @Override
    protected void onResume() {
        super.onResume();
        App.eventBus.register(this);
        setPlayerVisibility(PlayerService.isStartService);
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
        setPlayerVisibility(eventUpdatePlayerVisibility.isVisible());
    }

    @Subscribe
    public void onPlayerUIUpdated(EventUpdateDurationAndCurrentPos eventUpdateDurationAndCurrentPos) {
        setPlayerVisibility(true);
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

    public NavController getNavController() {
        return mNavController;
    }
}
