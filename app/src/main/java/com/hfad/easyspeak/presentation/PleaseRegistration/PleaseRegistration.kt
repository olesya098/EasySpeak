package com.hfad.easyspeak.presentation.PleaseRegistration

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.hfad.easyspeak.R
import com.hfad.easyspeak.presentation.components.CustomButton
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes

@Composable
fun PleaseRegistration(navController: NavController) {
    Scaffold(
        bottomBar = {
            CustomButton(
                title = stringResource(R.string.next),
                onClick = {
                    navController.navigate(NavigationRoutes.LogInUI.route)

                },
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 60.dp)
            )
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.registration),
                contentDescription = null,
                modifier = Modifier.size(290.dp)
            )
            Text(
                text = stringResource(R.string.pleaseregistrate),
                fontSize = 20
                    .sp,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.inversePrimary
            )


        }

    }
}