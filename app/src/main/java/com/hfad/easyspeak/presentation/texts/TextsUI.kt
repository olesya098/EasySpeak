package com.hfad.easyspeak.presentation.texts

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hfad.easyspeak.R
import com.hfad.easyspeak.model.TextModel
import com.hfad.easyspeak.presentation.components.TextComponent
import com.hfad.easyspeak.presentation.loadingscreen.LoadingScreen
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextsUI(navController: NavController) {
    val database = Firebase.database
    val textsRef = database.getReference("texts")
    var isLoading by remember { mutableStateOf(true) }

    val (texts, setTexts) = remember { mutableStateOf<Map<String, List<TextModel>>>(emptyMap()) }

    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
        textsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loadedTexts = mutableMapOf<String, List<TextModel>>()
                snapshot.children.forEach { levelSnapshot ->
                    val level = levelSnapshot.key ?: return@forEach
                    val textList = mutableListOf<TextModel>()

                    levelSnapshot.children.forEach { textSnapshot ->
                        val title = textSnapshot.child("title").getValue(String::class.java) ?: ""
                        val author = textSnapshot.child("author").getValue(String::class.java) ?: ""
                        val text = textSnapshot.child("text").getValue(String::class.java) ?: ""

                        textList.add(TextModel(title, author, text))
                    }

                    loadedTexts[level] = textList
                }
                setTexts(loadedTexts)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TextsUI", "Error loading texts", error.toException())
            }
        })
    }
    if (isLoading) {
        LoadingScreen(loadingText = stringResource(R.string.loading_your_data))
        return
    }
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
                            text = "Texts",
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
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                navController.navigate(NavigationRoutes.MainScreenUI.route)
                            }
                    )
                },

                )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            texts.forEach { (level, textList) ->
                Text(
                    text = level.capitalize(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(16.dp)
                )

                textList.forEach { textData ->
                    TextComponent(
                        title = textData.title,
                        author = textData.author,
                        onClick = {
                            navController.navigate(
                                NavigationRoutes.TextRead.createRoute(
                                    title = textData.title,
                                    text = textData.text
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}