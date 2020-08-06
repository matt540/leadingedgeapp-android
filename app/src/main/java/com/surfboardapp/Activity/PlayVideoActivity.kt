package com.surfboardapp.Activity

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.surfboardapp.Config
import com.surfboardapp.Language.AppUtils
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import java.util.*

class PlayVideoActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private lateinit var youTubePlayerView: YouTubePlayerView
    private val RECOVERY_DIALOG_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Preference().getSelectedLanguage(this)
        Preference().getSelectedLanguage(this)
        AppUtils().setLocale(Locale(Preference().getSelectedLanguage(this)))
        AppUtils().setConfigChange(this)
        setContentView(R.layout.activity_play_video)

        init()
    }

    private fun init() {
        youTubePlayerView = findViewById(R.id.youTubePlayerView)

        youTubePlayerView.initialize(Config().key, this)
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        if (!p2) {
            val YOUTUBE_VIDEO_CODE: String = intent.getStringExtra("key")!!

            p1!!.loadVideo(YOUTUBE_VIDEO_CODE)

            p1.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        if (p1!!.isUserRecoverableError) {
            p1.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage =
                getString(R.string.something_went_wrong)

            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
