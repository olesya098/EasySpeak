package com.hfad.easyspeak.presentation.signup2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hfad.easyspeak.R
import com.hfad.easyspeak.presentation.signup.SignUpViewModel
import com.hfad.easyspeak.presentation.components.CustomScaffold
import com.hfad.easyspeak.presentation.components.TextAndTextField
import com.hfad.easyspeak.presentation.components.TextAndTextFieldPassword
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes
import kotlinx.coroutines.launch

@Composable
fun SignUpUI2(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel()
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val termsAccepted by viewModel.termsAccepted.collectAsState()
    val signUpState by viewModel.signUpState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpViewModel.SignUpState.Success -> {
                navController.navigate(NavigationRoutes.SignUpUI.route)
            }
            is SignUpViewModel.SignUpState.Error -> {
                val error = (signUpState as SignUpViewModel.SignUpState.Error).message
                scope.launch {
                    snackbarHostState.showSnackbar(error)
                }
            }
            else -> {}
        }
    }

    CustomScaffold(
        title = stringResource(R.string.signupTitle),
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.strelka),
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp)
                    .padding(start = 10.dp)
                    .clickable {
                        navController.navigate(NavigationRoutes.LogInUI.route)
                    }
            )
        },
        textButton = stringResource(R.string.next),
        text1 = stringResource(R.string.already_registered),
        text2 = stringResource(R.string.login),
        onText2Click = {
            navController.navigate(NavigationRoutes.LogInUI.route)
        },
        onButtonClick = {
            viewModel.signUp()
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp, end = 20.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.choose_a_password),
                            fontSize = 18.sp,
                            modifier = Modifier.padding(
                                start = 10.dp,
                                end = 10.dp,
                                top = 30.dp,
                                bottom = 30.dp
                            ),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                    }

                    TextAndTextField(
                        title = stringResource(R.string.email_address),
                        value = email,
                        text = "Email",
                        onvalChange = { viewModel.email.value = it }
                    )

                    TextAndTextFieldPassword(
                        title = stringResource(R.string.password),
                        value = password,
                        text = "******",
                        onvalChange = { viewModel.password.value = it },
                        visible = viewModel.isPasswordVisible.value,
                        onVisibilityChange = { viewModel.isPasswordVisible.value = it }
                    )

                    TextAndTextFieldPassword(
                        title = stringResource(R.string.confirm_password),
                        value = confirmPassword,
                        text = "******",
                        onvalChange = { viewModel.confirmPassword.value = it },
                        visible = viewModel.isConfirmPasswordVisible.value,
                        onVisibilityChange = { viewModel.isConfirmPasswordVisible.value = it }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clickable { viewModel.toggleTermsAccepted() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = termsAccepted,
                            onCheckedChange = { viewModel.toggleTermsAccepted() },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.surface,
                                uncheckedColor = MaterialTheme.colorScheme.surface,
                                checkmarkColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onErrorContainer)) {
                                    append(stringResource(R.string.i))
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.surface)) {
                                    append(stringResource(R.string.have_made_myself_acquainted_with_the_rules))
                                }
                                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onErrorContainer)) {
                                    append(stringResource(R.string.and_accept_all_its_provisions))
                                }
                            },
                            fontSize = 17.sp,
                            modifier = Modifier.fillMaxWidth(),
                            softWrap = true,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                }
            }
        }
    )
}