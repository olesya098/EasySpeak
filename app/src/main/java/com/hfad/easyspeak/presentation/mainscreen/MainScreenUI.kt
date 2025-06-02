package com.hfad.easyspeak.presentation.mainscreen

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hfad.easyspeak.R
import com.hfad.easyspeak.model.TimeTracker
import com.hfad.easyspeak.presentation.components.Cards
import com.hfad.easyspeak.presentation.components.CardsExersize
import com.hfad.easyspeak.presentation.components.CustomScaffoldMainScreen
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes
import kotlinx.coroutines.delay

@Composable
fun MainScreenUI(navController: NavController) {
    var userName by remember { mutableStateOf("") }
    var sessionTimeText by remember { mutableStateOf("0 минут") }
    var totalTimeText by remember { mutableStateOf("0 минут") }

    val context = LocalContext.current
    val activity = context as Activity

    val auth = Firebase.auth
    val database = Firebase.database.reference

    LaunchedEffect(Unit) {
        val user = auth.currentUser
        user?.let {
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

        val timeTracker = TimeTracker(activity)
        timeTracker.startSession()

        try {
            while (true) {
                sessionTimeText = timeTracker.getCurrentSessionTimeFormatted()
                totalTimeText = timeTracker.getTotalTimeTodayFormatted()
                delay(1000)
            }
        } finally {
            timeTracker.endSession()
        }
    }

    CustomScaffoldMainScreen(
        onProfilClick = { navController.navigate(NavigationRoutes.ProfilUI.route) },
        text = stringResource(R.string.are_you_ready_for_learning_today),
        name = if (userName.isNotEmpty()) "Hello, $userName" else "Hello",
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.your_progress),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontSize = 18.sp,
                )

                Cards(
                    text = stringResource(R.string.correct_tests),
                    image = R.drawable.correcttests,
                    number = "4"
                )
                Spacer(modifier = Modifier.height(10.dp))
                Cards(
                    text = stringResource(R.string.time_in_the_app),
                    image = R.drawable.clock,
                    number = totalTimeText // Отображаем общее время за день
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Секция "Available exercises"
                Text(
                    text = stringResource(R.string.available_exercises),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontSize = 18.sp,
                )

                // Список упражнений в виде троек (название, иконка, цвет)
                val exercises = listOf(
                    Triple("Animals", R.drawable.animal, MaterialTheme.colorScheme.surface),
                    Triple("Word practice", R.drawable.pencil, MaterialTheme.colorScheme.error),
                    Triple("Audition", R.drawable.audition, MaterialTheme.colorScheme.outline),
                    Triple("Guess the word", R.drawable.guesstheword, MaterialTheme.colorScheme.onSecondaryContainer),
                    Triple("Texts", R.drawable.text, MaterialTheme.colorScheme.onTertiary)
                )

                Column {
                    exercises.chunked(2).forEach { rowItems ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowItems.forEach { (name, iconRes, color) ->
                                CardsExersize(
                                    text = name,
                                    image = iconRes,
                                    color = color,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .weight(1f),
                                    onClick = {
                                        // Обработка нажатия для каждой карточки
                                        when (name) {
                                            "Animals" -> navController.navigate("animals_screen")
                                            "Word practice" -> navController.navigate(NavigationRoutes.WordpracticeUI.route)
                                            "Audition" -> navController.navigate("audition_screen")
                                            "Guess the word" -> navController.navigate(NavigationRoutes.Exercise_Word.route)
                                            "Texts" -> navController.navigate(NavigationRoutes.TextsUI.route)
                                        }
                                    }
                                )
                            }
                            if (rowItems.size % 2 != 0) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    )
}