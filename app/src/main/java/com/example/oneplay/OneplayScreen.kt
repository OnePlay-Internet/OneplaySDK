package com.example.oneplay

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OneplayScreen(
    onStartGameClick: (
        gameId: String,
        userId: String,
        token: String,
        partnerId: String,
        oplayId: String,
        selectedStore: String,
        listener: (Boolean, String) -> Unit
    ) -> Unit,
    onTerminateGameClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var isStoreSelectorVisible by remember { mutableStateOf(false) }
    var userId by remember { mutableStateOf(TextFieldValue("944f8416-175a-4863-b8ca-97f116f90366")) }
    var token by remember { mutableStateOf(TextFieldValue("OTQ0Zjg0MTYtMTc1YS00ODYzLWI4Y2EtOTdmMTE2ZjkwMzY2OjEzMDc4YzRhLWY1Y2MtNDk0ZC1hYWU3LTIxN2QwOWE4NDExMg==")) }
    var partnerId by remember { mutableStateOf(TextFieldValue("2a6c7483-0ca9-4860-a04d-646ecc32e099")) }
    var gameId by remember { mutableStateOf(TextFieldValue("accessonespacecloudworkstationplatformbyoneplayforusersofoneplay")) }
    var oplayId by remember { mutableStateOf(TextFieldValue("")) }
    var oneplayAuth by remember { mutableStateOf(true) }
    var isGameList by remember { mutableStateOf(false) }
    var ownAuth by remember { mutableStateOf(false) }
    var gamingSdk by remember { mutableStateOf(true) }
    var vdiSdk by remember { mutableStateOf(false) }
    val resultMessage = remember { mutableStateOf("") }
    val isSuccess = remember { mutableStateOf(false) }

    // Store selection
    val storeOptions = listOf("Epic", "Steam")
    var selectedStore by remember { mutableStateOf(storeOptions[0]) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Select SDK Type",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = gamingSdk,
                    onCheckedChange = {
                        gamingSdk = it
                        if (it) vdiSdk = false
                    }
                )
                Text("Gaming", modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(16.dp))
                Checkbox(
                    checked = vdiSdk,
                    onCheckedChange = {
                        vdiSdk = it
                        if (it) gamingSdk = false
                        isGameList = true
                    }
                )
                Text("Game List", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Text(
                text = "Select Authentication Type",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = oneplayAuth,
                    onCheckedChange = {
                        oneplayAuth = it
                        if (it) ownAuth = false
                    }
                )
                Text("Oneplay Authentication", modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(16.dp))
                Checkbox(
                    checked = ownAuth,
                    onCheckedChange = {
                        ownAuth = it
                        if (it) oneplayAuth = false
                    }
                )
                Text("Authentication", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Store Dropdown
            val alphaValue = if (isStoreSelectorVisible) 1f else 0f

          /*  Text("Select Store", fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.alpha(alphaValue))
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .alpha(alphaValue) ) {
                OutlinedButton(onClick = { expanded = true }) {
                    Text(selectedStore)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    storeOptions.forEach { store ->
                        DropdownMenuItem(
                            text = { Text(store) },
                            onClick = {
                                selectedStore = store
                                expanded = false
                            }
                        )
                    }
                }
            }*/

            CustomTextField(value = userId, onValueChange = { userId = it }, hint = "User ID", oneplayAuth)
            CustomTextField(value = token, onValueChange = { token = it }, hint = "Partner Token")
            CustomTextField(value = partnerId, onValueChange = { partnerId = it }, hint = "Partner ID")
            CustomTextField(value = gameId, onValueChange = { gameId = it }, hint = "Game ID", gamingSdk)
            CustomTextField(value = oplayId, onValueChange = { oplayId = it }, hint = "OPlay ID", ownAuth)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (!oneplayAuth && !ownAuth) {
                            Toast.makeText(context, "Please select an authentication type.", Toast.LENGTH_SHORT).show()
                        } else {
                            val selectedGame = if (isGameList) "list" else gameId.text
                            if (token.text.isNotEmpty() && partnerId.text.isNotEmpty()) {
                                onStartGameClick(selectedGame, userId.text, token.text, partnerId.text, oplayId.text,selectedStore) { success, message ->
                                    isSuccess.value = success
                                    resultMessage.value = message
                                }
                            }
                            Toast.makeText(context, "Starting game...", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Start Game", fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        if (!oneplayAuth && !ownAuth) {
                            Toast.makeText(context, "Please select an authentication type.", Toast.LENGTH_SHORT).show()
                        } else {
                            if (token.text.isNotEmpty() && partnerId.text.isNotEmpty()) {
                                onTerminateGameClick()
                            }
                            Toast.makeText(context, "Terminating game...", Toast.LENGTH_SHORT).show()
                        }
                    },
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
    OneplayScreen(onStartGameClick = { _, _, _, _, _, _, _ -> }, onTerminateGameClick = {})
}
