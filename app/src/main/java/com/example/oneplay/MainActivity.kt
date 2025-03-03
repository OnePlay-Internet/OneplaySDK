package com.example.oneplay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import `in`.oneplay.sdk.InputData
import `in`.oneplay.sdk.OnePlayResponseData
import `in`.oneplay.sdk.OneplayGameFactory
import `in`.oneplay.sdk.OneplayGameSessionListener
import org.json.JSONObject


//AAR file Demo created by Jaaveeth
//This application is created for demo

class MainActivity :  AppCompatActivity(), OneplayGameSessionListener {

    val authToken = "e7fb1f1e-8929-11ed-90bc-02205a62d5b0" // Replace with your actual authorization token
    var serverdataLong : String = ""
    var userIDStrore : String = ""
    lateinit var myButton: Button
    lateinit var onePlayUserId: EditText
    lateinit var onePlayToken: EditText
    lateinit var onePlayGameId: EditText
    lateinit var onePlayPartnerId: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OneplayGameFactory.initialize(applicationContext)

        myButton = findViewById(R.id.button_start)
        onePlayUserId = findViewById(R.id.userId)
        onePlayToken = findViewById(R.id.oneplayToken)
        onePlayGameId = findViewById(R.id.gameId)
        onePlayPartnerId = findViewById(R.id.oneplayId)
        /*"response": {
     "user_id": "7846f2a4-a50d-4a20-b17b-6801a1d8c0b4",
     "session_id": "850a8a69-aa9a-4e96-850e-3dcf169fe5a6",
     "session": "Nzg0NmYyYTQtYTUwZC00YTIwLWIxN2ItNjgwMWExZDhjMGI0Ojg1MGE4YTY5LWFhOWEtNGU5Ni04NTBlLTNkY2YxNjlmZTVhNg=="
   }*/
        start_game_api(
            "7690ebae422f4d3f1ef15dace7733240f389e8d863d4328a7f262edff67c3296",
            "e5bb84d1-77ad-4236-adf1-78e8ad12b7c1",
            "ZTViYjg0ZDEtNzdhZC00MjM2LWFkZjEtNzhlOGFkMTJiN2MxOmNkODNiNmNhLTU5YTYtNGNjOS1hMWZjLTJhY2ZmYmNiN2VkMw==", " "
        )

        myButton.setOnClickListener {
            val gameId = onePlayGameId.text.toString()
            val userId = onePlayUserId.text.toString()
            val token = onePlayToken.text.toString()
            val partner = onePlayPartnerId.text.toString()

            // Check if any of the fields are empty
            if (gameId.isEmpty() || token.isEmpty() || partner.isEmpty()) {
                Toast.makeText(this, "All fields must be filled out", Toast.LENGTH_SHORT).show()
            } else {
                // Proceed with starting the game
                start_game_api(gameId, userId, token, partner)
            }
        }

    }



    //Start game api function call
    fun start_game_api(gameId: String, userId: String, sessionToken: String, partner: String) {

        val jsonObj = JSONObject()
        jsonObj.put("resolution", "1280x720")
        jsonObj.put("is_vsync_enabled", true)
        jsonObj.put("fps", "60")
        jsonObj.put("bitrate", 10000)
        jsonObj.put("show_stats", false)
        jsonObj.put("fullscreen", true)
        jsonObj.put("onscreen_controls", true)
        jsonObj.put("audio_type", "stereo")
        jsonObj.put("stream_codec", "forceh265")
        jsonObj.put("video_decoder_selection", "auto")
        jsonObj.put("store", "epic")

        if (OneplayGameFactory.getSdkContext() != null) {
        val session = OneplayGameFactory.createOnePlaySession(applicationContext, this)
        val inputData = InputData(
            partner,
            gameId,
            jsonObj.toString(),
            "1.0.0",
            packageName
        )
        /* Either the partner details or OnePlay details required */
        // Partner having their own Authentication
        //inputData.partnerUserAuthToken = sessionToken

        // Partner registered with OnePlay
        inputData.onePlayAuthToken = sessionToken
        inputData.onePlayUserId = userId

        session.setInputData(inputData)
        myButton.text = "Login Done"
            }
    }


    override fun sendEvent(onePlayResponseData: OnePlayResponseData) {
        runOnUiThread {
            Toast.makeText(this, onePlayResponseData.message, Toast.LENGTH_LONG).show()
        }
    }


}