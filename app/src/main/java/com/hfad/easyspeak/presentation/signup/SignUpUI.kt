package com.hfad.easyspeak.presentation.signup

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hfad.easyspeak.R
import com.hfad.easyspeak.presentation.components.CustomScaffold
import com.hfad.easyspeak.presentation.components.TextAndTextField
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes

@Composable
fun SignUpUI(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    // Инициализация Firebase
    val auth: FirebaseAuth = Firebase.auth
    val database: DatabaseReference = Firebase.database.reference

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
                        navController.navigate(NavigationRoutes.SignUpUI2.route)
                    }
            )
        },
        textButton = stringResource(R.string.signupTitle),
        text1 = stringResource(R.string.already_registered),
        text2 = stringResource(R.string.login),
        onText2Click = {
            navController.navigate(NavigationRoutes.LogInUI.route)
        },
        onButtonClick = {
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                val userData = hashMapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "email" to user.email
                )

                database.child("users").child(user.uid).setValue(userData)
                    .addOnSuccessListener {
                        navController.navigate(NavigationRoutes.LogInUI.route)
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Error saving user data", e)
                    }
            } ?: run {
                Log.e("Firebase", "User not authenticated")
            }
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
                            text = stringResource(R.string.create_an_account),
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
                        title = stringResource(R.string.first_name),
                        value = firstName,
                        text = stringResource(R.string.your_first_name),
                        onvalChange = { newValue ->
                            firstName = newValue
                        }
                    )

                    TextAndTextField(
                        title = stringResource(R.string.last_name),
                        value = lastName,
                        text = stringResource(R.string.your_last_name),
                        onvalChange = { newValue ->
                            lastName = newValue
                        }
                    )
                }
            }
        }
    )
}