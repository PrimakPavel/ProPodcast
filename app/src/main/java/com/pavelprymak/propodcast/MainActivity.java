package com.pavelprymak.propodcast;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.pavelprymak.propodcast.databinding.ActivityMainBinding;
import com.pavelprymak.propodcast.services.PlayerService;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdateDurationAndCurrentPos;
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayerVisibility;
import com.pavelprymak.propodcast.utils.widget.LastTrackPreferenceManager;
import com.squareup.otto.Subscribe;

import static com.pavelprymak.propodcast.services.PlayerService.EXTRA_COMMAND_PLAYER;

public class MainActivity extends AppCompatActivity {
    private NavController mNavController;
    private ActivityMainBinding mBinding;
    private final LastTrackPreferenceManager mLastTrackPreferenceManager = App.mLastTrackSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(mBinding.navView, mNavController);
        mBinding.fabContinueLastTrack.setOnClickListener(v -> continueLastTrack());
    }


    @Override
    protected void onResume() {
        super.onResume();
        App.eventBus.register(this);
        setPlayerVisibility(PlayerService.isStartService);
        setFabVisibility(!PlayerService.isStartService && mLastTrackPreferenceManager.getTrackAudioUrl() != null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.eventBus.unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavController.popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onUpdatePlayerVisibility(EventUpdatePlayerVisibility eventUpdatePlayerVisibility) {
        setPlayerVisibility(eventUpdatePlayerVisibility.isVisible());
        setFabVisibility(!eventUpdatePlayerVisibility.isVisible() && mLastTrackPreferenceManager.getTrackAudioUrl() != null);
    }

    @Subscribe
    public void onPlayerUIUpdated(EventUpdateDurationAndCurrentPos eventUpdateDurationAndCurrentPos) {
        setPlayerVisibility(true);
        setFabVisibility(false);
    }

    private void setPlayerVisibility(boolean isPlayerVisible) {
        if (isPlayerVisible) {
            mBinding.playerContainer.setVisibility(View.VISIBLE);
        } else {
            mBinding.playerContainer.setVisibility(View.GONE);
        }
    }

    private void setFabVisibility(boolean isFabVisible) {
        if (isFabVisible) {
            mBinding.fabContinueLastTrack.show();
        } else {
            mBinding.fabContinueLastTrack.hide();
        }
    }

    public void setNavViewVisibility(boolean isVisible) {
        if (isVisible) {
            mBinding.navView.setVisibility(View.VISIBLE);
        } else {
            mBinding.navView.setVisibility(View.GONE);
        }
    }

    private void continueLastTrack() {
        if (!PlayerService.isStartService) {
            Intent serviceIntent = new Intent(getApplicationContext(), PlayerService.class);
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, PlayerService.COMMAND_CONTINUE_LAST_TRACK);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

    public NavController getNavController() {
        return mNavController;
    }
}
