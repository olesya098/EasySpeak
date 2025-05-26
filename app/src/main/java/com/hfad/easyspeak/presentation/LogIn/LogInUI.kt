package com.hfad.easyspeak.presentation.LogIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hfad.easyspeak.R
import com.hfad.easyspeak.presentation.components.CustomScaffold
import com.hfad.easyspeak.presentation.components.TextAndTextField
import com.hfad.easyspeak.presentation.components.TextAndTextFieldPassword
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInUI(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val viewModel: LogInViewModel = viewModel()
    val loginState by viewModel.loginState.collectAsState()

    // Обработка состояний логина
    LaunchedEffect(loginState) {
        when (loginState) {
            is LogInViewModel.LoginState.Success -> {
                navController.navigate(NavigationRoutes.MainScreenUI.route) {
                    popUpTo(NavigationRoutes.LogInUI.route) { inclusive = true }
                }
            }

            is LogInViewModel.LoginState.Error -> {
                errorMessage = (loginState as LogInViewModel.LoginState.Error).message
                showErrorDialog = true
            }

            else -> {}
        }
    }

    CustomScaffold(
        title = stringResource(R.string.login),
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.strelka),
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp)
                    .padding(start = 10.dp)
                    .clickable {
                        navController.navigate(NavigationRoutes.PleaseRegistration.route)
                    }
            )
        },
        textButton = stringResource(R.string.login),
        text1 = stringResource(R.string.not_you_member),
        text2 = stringResource(R.string.signupTitle),
        onText2Click = {
            navController.navigate(NavigationRoutes.SignUpUI2.route)
        },
        onButtonClick = {
            viewModel.login(email, password)
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.login),
                            contentDescription = null,
                            modifier = Modifier.size(130.dp)
                        )

                        Text(
                            text = stringResource(R.string.join_now),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.inversePrimary,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 30.dp)
                        )
                    }

                    TextAndTextField(
                        title = stringResource(R.string.email_address),
                        value = email,
                        text = "Email",
                        onvalChange = { email = it }
                    )

                    TextAndTextFieldPassword(
                        title = stringResource(R.string.password),
                        value = password,
                        text = "******",
                        onvalChange = { password = it },
                        visible = isPasswordVisible,
                        onVisibilityChange = { isPasswordVisible = it }
                    )

                    Text(
                        text = stringResource(R.string.forgot_password),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    )


    if (showErrorDialog) {
        val dialogShape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(16.dp),
            topEnd = CornerSize(16.dp),
            bottomStart = CornerSize(16.dp),
            bottomEnd = CornerSize(16.dp)
        ) as CornerBasedShape

        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            shape = dialogShape,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onPrimary,
                            MaterialTheme.colorScheme.surface
                        )
                    ),
                    shape = dialogShape
                ),
            title = {
                Text(
                    text = stringResource(R.string.error),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    ),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            },
            containerColor = Color.White,
            tonalElevation = 8.dp,
            titleContentColor = MaterialTheme.colorScheme.error,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}