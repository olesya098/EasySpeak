package com.hfad.easyspeak.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hfad.easyspeak.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffoldMainScreen (
    content: @Composable (innerPadding: androidx.compose.foundation.layout.PaddingValues) -> Unit,

){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Наш кастомный заголовок
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface) // Цвет фона заголовка
                    .padding(vertical = 32.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Серый кружок
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            MaterialTheme.colorScheme.onErrorContainer,
                            CircleShape
                        )
                        .clickable {

                        }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Hello, Emil",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Are you ready for learning today?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
