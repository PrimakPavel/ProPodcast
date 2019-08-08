package com.pavelprymak.propodcast

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.pavelprymak.propodcast.services.COMMAND_CONTINUE_LAST_TRACK
import com.pavelprymak.propodcast.services.EXTRA_COMMAND_PLAYER
import com.pavelprymak.propodcast.services.PlayerService
import com.pavelprymak.propodcast.utils.otto.player.EventUpdateDurationAndCurrentPos
import com.pavelprymak.propodcast.utils.otto.player.EventUpdatePlayerVisibility
import com.pavelprymak.propodcast.utils.widget.LastTrackPreferenceManager
import com.squareup.otto.Subscribe
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private val mLastTrackPreferenceManager: LastTrackPreferenceManager by inject()
    private val eventBus = App.eventBus


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(navView, navController)
        fabContinueLastTrack.setOnClickListener { view -> continueLastTrack() }
    }


    override fun onResume() {
        super.onResume()
        eventBus.register(this)
        setPlayerVisibility(PlayerService.isStartService)
        setFabVisibility(!PlayerService.isStartService && mLastTrackPreferenceManager.trackAudioUrl != null)
    }

    override fun onPause() {
        super.onPause()
        eventBus.unregister(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController.popBackStack()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    @Subscribe
    fun onUpdatePlayerVisibility(eventUpdatePlayerVisibility: EventUpdatePlayerVisibility) {
        setPlayerVisibility(eventUpdatePlayerVisibility.isVisible)
        setFabVisibility(!eventUpdatePlayerVisibility.isVisible && mLastTrackPreferenceManager.trackAudioUrl != null)
    }

    @Subscribe
    fun onPlayerUIUpdated(eventUpdateDurationAndCurrentPos: EventUpdateDurationAndCurrentPos) {
        setPlayerVisibility(true)
        setFabVisibility(false)
    }

    private fun setPlayerVisibility(isPlayerVisible: Boolean) {
        if (isPlayerVisible) playerContainer.visibility = View.VISIBLE else playerContainer.visibility = View.GONE
    }

    private fun setFabVisibility(isFabVisible: Boolean) {
        if (isFabVisible) fabContinueLastTrack.show() else fabContinueLastTrack.hide()
    }

    fun setNavViewVisibility(isVisible: Boolean) {
        if (isVisible) navView.visibility = View.VISIBLE else navView.visibility = View.GONE
    }

    private fun continueLastTrack() {
        if (!PlayerService.isStartService) {
            val serviceIntent = Intent(applicationContext, PlayerService::class.java)
            serviceIntent.putExtra(EXTRA_COMMAND_PLAYER, COMMAND_CONTINUE_LAST_TRACK)
            ContextCompat.startForegroundService(this, serviceIntent)
        }
    }
}
