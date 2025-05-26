package com.hfad.easyspeak.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.hfad.easyspeak.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    title: String,
    navigationIcon: @Composable () -> Unit,
    content: @Composable (innerPadding: androidx.compose.foundation.layout.PaddingValues) -> Unit,
    textButton: String,
    text1: String,
    text2: String,
    onText2Click: () -> Unit = {},
    onButtonClick: () -> Unit = {}

) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                        contentAlignment = Alignment.Center,

                        ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                navigationIcon = navigationIcon,

                )
        },
        bottomBar = {
            Column(
                modifier = Modifier.padding(bottom = 55.dp)
            ) {
                CustomButton(
                    title = textButton,
                    onClick = onButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = text1,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                    Text(
                        text = text2,
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable { onText2Click() }, // Добавляем возможность нажатия
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}