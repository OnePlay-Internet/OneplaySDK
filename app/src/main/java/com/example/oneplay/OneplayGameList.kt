package com.example.oneplay

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OneplayGameList(
    onStartGameClick: (
        gameId: String,
        listener: (Boolean, String) -> Unit
    ) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Game 1 Box
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onStartGameClick("5ec7e78dac434510b2a459bbdac603c60f256a86e30c4680847c9345c658f34e") { success, message ->
                        Log.d("GameStart", "Success: $success, Message: $message")
                    }
                }
                .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Game 1 Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 8.dp)
                )
                Text(text = "Game 1", style = MaterialTheme.typography.bodyLarge)
            }
        }

        // Game 2 Box
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onStartGameClick("93ea74ddbfddba46234bc8b5fc0ab22765f0d3009f9a13f64e788278a8a5bde7") { success, message ->
                        Log.d("GameStart", "Success: $success, Message: $message")
                    }
                }
                .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Game 2 Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 8.dp)
                )
                Text(text = "Game 2", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewOneplayGameList() {
    OneplayGameList(onStartGameClick = { _, _ -> })
}