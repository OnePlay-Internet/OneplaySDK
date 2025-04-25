package com.example.oneplay

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import `in`.oneplay.sdk.InputData
import `in`.oneplay.sdk.OneplayGameFactory
import org.json.JSONObject

@Composable
fun OneplayScreen(
    onStartGameClick: (
    gameId: String,
    userId: String,
    token: String,
    partnerId: String,
    oplayId: String,
    listener: (Boolean, String) -> Unit
) -> Unit,
    onTerminateGameClick : () -> Unit
) {
    val context = LocalContext.current

    var userId by remember { mutableStateOf(TextFieldValue("")) }
    var token by remember { mutableStateOf(TextFieldValue("")) }
    var partnerId by remember { mutableStateOf(TextFieldValue("")) }
    var gameId by remember { mutableStateOf(TextFieldValue("")) }
    var oplayId by remember { mutableStateOf(TextFieldValue("")) }
    var oneplayAuth by remember { mutableStateOf(false) }
    var ownAuth by remember { mutableStateOf(false) }
    var gamingSdk by remember { mutableStateOf(false) }
    var vdiSdk by remember { mutableStateOf(false) }
    val resultMessage = remember { mutableStateOf("") }
    val isSuccess = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.5f)
                .padding(50.dp)
        )

            Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

                // SDK Type Selector Section
                Text(
                    text = "Select SDK Type",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Gaming SDK Checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = gamingSdk,
                            onValueChange = {
                                gamingSdk = it
                                if (it) vdiSdk = false  // Deselect VDI if Gaming is selected
                            }
                        )
                ) {
                    Checkbox(
                        checked = gamingSdk,
                        onCheckedChange = {
                            gamingSdk = it
                            if (it) vdiSdk = false
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Gaming",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                // VDI SDK Checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .toggleable(
                            value = vdiSdk,
                            onValueChange = {
                                vdiSdk = it
                                if (it) gamingSdk = false  // Deselect Gaming if VDI is selected
                            }
                        )
                ) {
                    Checkbox(
                        checked = vdiSdk,
                        onCheckedChange = {
                            vdiSdk = it
                            if (it) gamingSdk = false
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Vdi",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Partner Select Toggle
            Text(
                text = "Select Authentication Type",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Oneplay Authentication Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = oneplayAuth,
                        onValueChange = {
                            oneplayAuth = it
                            if (it) ownAuth = false  // Deselect ownAuth if selected
                        }
                    )
            ) {
                Checkbox(
                    checked = oneplayAuth,
                    onCheckedChange = {
                        oneplayAuth = it
                        if (it) ownAuth = false
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Oneplay authentication",
                    modifier = Modifier.align(Alignment.CenterVertically))
            }

            // Own Authentication Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .toggleable(
                        value = ownAuth,
                        onValueChange = {
                            ownAuth = it
                            if (it) oneplayAuth = false  // Deselect oneplayAuth if selected
                        }
                    )
            ) {
                Checkbox(
                    checked = ownAuth,
                    onCheckedChange = {
                        ownAuth = it
                        if (it) oneplayAuth = false
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Own authentication",
                    modifier = Modifier.align(Alignment.CenterVertically))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Conditionally show User ID field only if Oneplay auth is selected

            CustomTextField(value = userId, onValueChange = { userId = it }, hint = "Oneplay User ID", oneplayAuth)

            CustomTextField(value = token, onValueChange = { token = it }, hint = "Oneplay Partner Token")
            CustomTextField(value = partnerId, onValueChange = { partnerId = it }, hint = "Oneplay Partner ID")
            CustomTextField(value = gameId, onValueChange = { gameId = it }, hint = "Oneplay Game ID", gamingSdk)
            CustomTextField(value = oplayId, onValueChange = { oplayId = it }, hint = "OPlay ID", ownAuth)

            Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .toggleable(
                            value = ownAuth,
                            onValueChange = {
                                ownAuth = it
                                if (it) oneplayAuth = false  // Deselect oneplayAuth if selected
                            }
                        )
                ) {
                    // Submit Button
                    Button(
                        onClick = {
                            if (!oneplayAuth && !ownAuth) {
                                Toast.makeText(
                                    context,
                                    "Please select an authentication type.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Proceed with the game logic
                                if (token.text.isNotEmpty() && partnerId.text.isNotEmpty()) {
                                    onStartGameClick(
                                        gameId.text,
                                        userId.text,
                                        token.text,
                                        partnerId.text,
                                        oplayId.text,
                                    ) { success, message ->
                                        isSuccess.value = success
                                        resultMessage.value = message
                                    }
                                }
                                Toast.makeText(context, "Starting game...", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        modifier = Modifier
                            .width(200.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Start Game", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            if (!oneplayAuth && !ownAuth) {
                                Toast.makeText(
                                    context,
                                    "Please select an authentication type.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Proceed with the game logic
                                if (token.text.isNotEmpty() && partnerId.text.isNotEmpty()) {
                                    onTerminateGameClick()
                                }
                                Toast.makeText(context, "Terminating game...", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        modifier = Modifier
                            .width(200.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Terminate", fontSize = 16.sp)
                    }
                }
        }
    }
}


@Composable
fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    hint: String,
    enabled: Boolean = true
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .background(if (enabled) Color.White else Color.LightGray)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .alpha(if (enabled) 1f else 0.5f), // Add transparency when disabled
        enabled = enabled,
        decorationBox = { innerTextField ->
            if (value.text.isEmpty()) {
                Text(hint, color = Color.Gray)
            }
            innerTextField()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewOneplayScreen() {
    OneplayScreen(onStartGameClick = { _, _, _, _, _, _ -> }, onTerminateGameClick = {})
}
