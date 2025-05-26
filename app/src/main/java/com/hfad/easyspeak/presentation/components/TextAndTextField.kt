package com.hfad.easyspeak.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TextAndTextField(
    title: String,
    value: String,
    text: String,
    onvalChange: (String) -> Unit
) {
    Column() {
        Text(
            title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.inversePrimary
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            value = value,
            onValueChange = { onvalChange(it) },
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                unfocusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
            ),

            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            placeholder = {
                Text(
                    text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
        Spacer(modifier = Modifier.height(15.dp))

    }
}