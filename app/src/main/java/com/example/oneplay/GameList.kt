package com.example.oneplay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import `in`.oneplay.sdk.GameLaunchSession
import `in`.oneplay.sdk.InputData
import `in`.oneplay.sdk.OnePlayResponseData
import `in`.oneplay.sdk.OneplayGameFactory
import `in`.oneplay.sdk.OneplayGameSessionListener
import org.json.JSONObject

class GameList : AppCompatActivity(), OneplayGameSessionListener {
    private var session: GameLaunchSession? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OneplayGameFactory.INSTANCE.initialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    OneplayGameList { gameId, listener ->
                        // Simulate a game start
                        val userId = intent.getStringExtra("userId") ?: "default"
                        val token = intent.getStringExtra("token") ?: "default"
                        val partnerId = intent.getStringExtra("partner") ?: "default"
                        val oplayId = intent.getStringExtra("oplayId") ?: "default"

                        Toast.makeText(this, "Starting $gameId", Toast.LENGTH_SHORT).show()
                        // Simulate success callback
                        start_game_api(gameId, userId, token, partnerId, oplayId)
                        listener(true, "$gameId launched successfully")
                    }
                }
            }
        }
    }


    fun start_game_api(
        gameId: String,
        userId: String,
        sessionToken: String,
        partner: String,
        oplayId: String,
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

        val sdkContext = OneplayGameFactory.INSTANCE.sdkContext

        if (sdkContext != null) {
            session = OneplayGameFactory.INSTANCE.createOnePlaySession(applicationContext, this)

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
                    //oPlayId = oplayId
                }
                forceStart = true

            }

            session?.setInputData(inputData)
            Toast.makeText(applicationContext, "Game session started successfully.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "DK context is null.", Toast.LENGTH_LONG).show()
        }

        val intent = Intent(this, GameList::class.java)
        intent.putExtra("gameId", gameId)
        intent.putExtra("userId", userId)
        intent.putExtra("partner", partner)
        intent.putExtra("sessionToken", sessionToken)
        intent.putExtra("oplayId", oplayId)
        startActivity(intent)
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