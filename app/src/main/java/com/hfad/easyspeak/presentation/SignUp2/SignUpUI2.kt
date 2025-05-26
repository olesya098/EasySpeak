package com.hfad.easyspeak.presentation.SignUp2

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.hfad.easyspeak.R
import com.hfad.easyspeak.presentation.components.CustomScaffold
import com.hfad.easyspeak.presentation.components.TextAndTextField
import com.hfad.easyspeak.presentation.components.TextAndTextFieldPassword
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes
import com.hfad.easyspeak.ui.theme.blue
import com.hfad.easyspeak.ui.theme.grayDark

@Composable
fun SignUpUI2(navController: NavController) {
    var passwordfirst by remember { mutableStateOf("") }
    var passwordsecond by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }


    var isPasswordVisiblefirst by remember { mutableStateOf(false) }
    var isPasswordVisiblesecond by remember { mutableStateOf(false) }

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
            val auth = FirebaseAuth.getInstance()
            SignUp(
                auth = auth,
                email = email,
                password = passwordsecond,
                onResult = { success ->
                    if (success) {
                        navController.navigate(NavigationRoutes.SignUpUI.route)
                    } else {

                    }
                }
            )
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
                        onvalChange = { newValue ->
                            email = newValue
                        }
                    )

                    TextAndTextFieldPassword(
                        title = stringResource(R.string.password),
                        value = passwordfirst,
                        text = "******",
                        onvalChange = { passwordfirst = it },
                        visible = isPasswordVisiblefirst,
                        onVisibilityChange = { isPasswordVisiblefirst = it }
                    )
                    TextAndTextFieldPassword(
                        title = stringResource(R.string.confirm_password),
                        value = passwordsecond,
                        text = "******",
                        onvalChange = { passwordsecond = it },
                        visible = isPasswordVisiblesecond,
                        onVisibilityChange = { isPasswordVisiblesecond = it }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp,)
                            .clickable { checked = !checked },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = null,
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