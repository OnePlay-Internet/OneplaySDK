package com.example.oneplay

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.oneplay.ui.theme.OneplayTheme
import `in`.oneplay.sdk.GameLaunchSession
import `in`.oneplay.sdk.InputData
import `in`.oneplay.sdk.OnePlayResponseData
import `in`.oneplay.sdk.OneplayGameFactory
import `in`.oneplay.sdk.OneplayGameSessionListener
import org.json.JSONObject

class OneplayComposeActivity : AppCompatActivity(), OneplayGameSessionListener {
    private var session: GameLaunchSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OneplayGameFactory.initialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            OneplayTheme {
                OneplayScreen(
                    onStartGameClick = { gameId, userId, token, partnerId, onResult ->
                        if(gameId.isNotEmpty()) {
                            start_game_api(gameId, userId, token, partnerId, onResult)
                        } else {
                            start_game_api("accessonespacecloudworkstationplatformbyoneplayforusersofoneplay", userId, token, partnerId, onResult)
                        }
                    },
                    onTerminateGameClick = {
                        session?.terminateOnePlaySession()
                    }
                )
            }
        }
    }

    fun start_game_api(
        gameId: String,
        userId: String,
        sessionToken: String,
        partner: String,
        listener: (Boolean, String) -> Unit
    ) {
        val jsonObj = JSONObject().apply {
            put("resolution", "1280x720")
            put("is_vsync_enabled", true)
            put("fps", "60")
            put("bitrate", 10000)
            put("show_stats", false)
            put("fullscreen", true)
            put("onscreen_controls", true)
            put("audio_type", "stereo")
            put("stream_codec", "forceh265")
            put("video_decoder_selection", "auto")
            put("store",
                if (gameId == "accessonespacecloudworkstationplatformbyoneplayforusersofoneplay") "oneplay" else "steam"
            )
        }

        val sdkContext = OneplayGameFactory.getSdkContext()

        if (sdkContext != null) {
            session = OneplayGameFactory.createOnePlaySession(applicationContext, this)

            val inputData = InputData(
                partner,
                gameId,
                jsonObj.toString(),
                "1.0.0",
                applicationContext.packageName
            ).apply {
                if (userId.isNotEmpty()) {
                    // Oneplay Authentication
                    onePlayUserId = userId
                    onePlayAuthToken = sessionToken
                } else {
                    // Own Authentication
                    partnerUserAuthToken = sessionToken
                    oPlayId = "7690ebae422f4d3f1ef15dace7733240f389e8d863d4328a7f262edff67c3296"
                }

            }

            session?.setInputData(inputData)
            Toast.makeText(applicationContext, "Game session started successfully.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "DK context is null.", Toast.LENGTH_LONG).show()
        }
    }

    override fun sendEvent(onePlayResponseData: OnePlayResponseData) {
        when {
            onePlayResponseData.code == 404 -> handleError(onePlayResponseData.message) //Connection error
            onePlayResponseData.code == 1000 && onePlayResponseData.isProgressed == true ->
                handleProgress(onePlayResponseData.percentage)
            onePlayResponseData.code == 1001 -> handleGameConnected()
            onePlayResponseData.code == 1002 -> handleGameDisconnected()
            onePlayResponseData.code == 1003 -> handleGameTerminated()
            onePlayResponseData.code == 801 -> handleQueue(onePlayResponseData.percentage, onePlayResponseData.message) // percentage value is the queue count
        }
    }

    private fun handleGameTerminated() {
        runOnUiThread {
            Toast.makeText(this, "Game Terminated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleProgress(percentage: Int) {
        runOnUiThread {
            Toast.makeText(this, "Loading.. $percentage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleQueue(percentage: Any, message: Any) {
        runOnUiThread {
            Toast.makeText(this, "Queue ahead $percentage user", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleGameDisconnected() {
        runOnUiThread {
            Toast.makeText(this, "Game Disconnected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleGameConnected() {
        runOnUiThread {
            Toast.makeText(this, "Game Connected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleError(error: String) {
        runOnUiThread {
            Toast.makeText(this, "Game Error: $error", Toast.LENGTH_SHORT).show()
        }
    }
}
