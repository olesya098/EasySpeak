package com.hfad.easyspeak.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.hfad.easyspeak.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffoldTests(
    content: @Composable (innerPadding: androidx.compose.foundation.layout.PaddingValues) -> Unit,
    title: String,
    number: String

){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

    topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            title = {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* do something */ }) {
                    Image(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "Localized description",
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.primary
                        )
                    )
                }
            },
            actions = {
                Text(
                    text = number,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            scrollBehavior = scrollBehavior,
        )
    },
    ) { paddingValues ->
        content(paddingValues)
    }
}
