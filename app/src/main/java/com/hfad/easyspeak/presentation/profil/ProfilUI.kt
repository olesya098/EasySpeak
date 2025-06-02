package com.hfad.easyspeak.presentation.profil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hfad.easyspeak.R
import com.hfad.easyspeak.presentation.components.*
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes

@Composable
fun ProfilUI(navController: NavController) {
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = Firebase.auth
    val database = Firebase.database.reference

    LaunchedEffect(Unit) {
        val user = auth.currentUser
        user?.let {
            userEmail = user.email ?: ""
            database.child("users").child(it.uid).get().addOnSuccessListener { snapshot ->
                val firstName = snapshot.child("firstName").value as? String ?: ""
                val lastName = snapshot.child("lastName").value as? String ?: ""
                userName = if (firstName.isNotEmpty() || lastName.isNotEmpty()) {
                    "$firstName $lastName".trim()
                } else {
                    "User"
                }
            }
        }
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.extraLarge
                ),
            shape = MaterialTheme.shapes.extraLarge,
            containerColor = Color.White,
            title = {

                Text(
                    text = "Подтвердите удаление",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Column(
                ) {
                    Text("Введите ваш текущий пароль для подтверждения удаления аккаунта")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Пароль") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    errorMessage?.let {
                        Text(it, color = Color.Red)
                    }
                }
            },
            confirmButton = {
                CustomButton(
                    title = "Подтвердите",
                    onClick = {
                        if (password.isBlank()) {
                            errorMessage = "Пароль не может быть пустым"
                        } else {
                            auth.currentUser?.email?.let { email ->
                                val credential = EmailAuthProvider.getCredential(email, password)
                                auth.currentUser?.reauthenticate(credential)
                                    ?.addOnCompleteListener { reauthTask ->
                                        if (reauthTask.isSuccessful) {
                                            // После успешной аутентификации удаляем аккаунт
                                            auth.currentUser?.delete()
                                                ?.addOnCompleteListener { deleteTask ->
                                                    if (deleteTask.isSuccessful) {
                                                        navController.navigate(NavigationRoutes.LogInUI.route) {
                                                            popUpTo(0)
                                                        }
                                                    } else {
                                                        errorMessage = "Ошибка удаления: ${deleteTask.exception?.message}"
                                                    }
                                                }
                                        } else {
                                            errorMessage = "Неверный пароль"
                                        }
                                    }
                            }
                        }
                    }

                )
            },
            dismissButton = {
                TextButton(
                    onClick = { showPasswordDialog = false },
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Text("Отмена",
                        textAlign = TextAlign.Center)
                }
            }
        )
    }

    CustomScaffoldMainScreen(
        text = "",
        name = if (userName.isNotEmpty()) "Hello, $userName" else "Hello",
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.information),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = "Email: $userEmail",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CustomButton(
                        title = stringResource(R.string.change_your_image),
                        onClick = { navController.navigate(NavigationRoutes.SignUpUI2.route) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    CustomButton(
                        title = stringResource(R.string.delete_an_account),
                        onClick = {
                            showPasswordDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    CustomButton(
                        title = stringResource(R.string.log_out),
                        onClick = {
                            SignOut(auth)
                            navController.navigate(NavigationRoutes.LogInUI.route) {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    CustomButton(
                        title = stringResource(R.string.exit),
                        onClick = { navController.navigate(NavigationRoutes.MainScreenUI.route) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

fun SignOut(auth: FirebaseAuth) {
    auth.signOut()
}