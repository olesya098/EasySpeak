package com.hfad.easyspeak.presentation.exerciseListening

import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hfad.easyspeak.R
import com.hfad.easyspeak.ui.theme.blue
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Exercise_ListeningUI(navController: NavController) {
    val context = LocalContext.current
    val database = Firebase.database
    val wordsRef = database.getReference("words")

    // Состояния для работы с базой данных
    var englishWords by remember { mutableStateOf<List<String>>(emptyList()) }
    var transcriptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Состояния для упражнения
    var currentIndex by remember { mutableStateOf(0) }
    val currentWord = remember { derivedStateOf {
        englishWords.getOrNull(currentIndex) ?: ""
    } }
    val currentTranscription = remember { derivedStateOf {
        transcriptions.getOrNull(currentIndex) ?: ""
    } }

    // Состояния распознавания речи
    var isRecording by remember { mutableStateOf(false) }
    var userAnswer by remember { mutableStateOf("") }
    var isAnswerCorrect by remember { mutableStateOf(false) }
    var speechError by remember { mutableStateOf<String?>(null) }
    var hasRecordPermission by remember { mutableStateOf(false) }

    // Загрузка слов из Firebase
    LaunchedEffect(Unit) {
        wordsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val loadedWords = mutableListOf<String>()
                    val loadedTranscriptions = mutableListOf<String>()

                    snapshot.children.forEach { wordSnapshot ->
                        when (val value = wordSnapshot.value) {
                            is Map<*, *> -> {
                                wordSnapshot.key?.let { word ->
                                    loadedWords.add(word)
                                    loadedTranscriptions.add((value["transcription"] as? String).orEmpty())
                                }
                            }
                            is String -> {
                                wordSnapshot.key?.let { word ->
                                    loadedWords.add(word)
                                    loadedTranscriptions.add("")
                                }
                            }
                        }
                    }

                    if (loadedWords.isEmpty()) {
                        errorMessage = "No words found in database"
                    } else {
                        englishWords = loadedWords.shuffled()
                        transcriptions = loadedTranscriptions
                        currentIndex = Random.nextInt(englishWords.size)
                    }
                } catch (e: Exception) {
                    errorMessage = "Error loading words: ${e.localizedMessage}"
                    Log.e("ExerciseListening", "Error loading words", e)
                } finally {
                    isLoading = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage = "Database error: ${error.message}"
                isLoading = false
                Log.e("ExerciseListening", "Database error", error.toException())
            }
        })
    }

    // Запрос разрешения на запись аудио
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasRecordPermission = isGranted
        if (!isGranted) {
            speechError = "Microphone permission required"
        }
    }

    // Проверяем разрешение при запуске
    LaunchedEffect(Unit) {
        permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    // Анимация пульсации микрофона
    val pulseValue = remember { Animatable(1f) }
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    // Слушатель распознавания речи
    val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: android.os.Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() { isRecording = false }

        override fun onError(error: Int) {
            isRecording = false
            speechError = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Audio error"
                SpeechRecognizer.ERROR_CLIENT -> "Client error"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "No permissions"
                SpeechRecognizer.ERROR_NETWORK -> "Network error"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
                SpeechRecognizer.ERROR_SERVER -> "Server error"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected"
                else -> "Unknown error: $error"
            }
        }

        override fun onResults(results: android.os.Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!matches.isNullOrEmpty()) {
                userAnswer = matches[0]
                isAnswerCorrect = isPronunciationCorrect(userAnswer, currentWord.value)
            } else {
                speechError = "Speech not recognized"
            }
            isRecording = false
        }

        override fun onPartialResults(partialResults: android.os.Bundle?) {}
        override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
    }

    // Управление жизненным циклом SpeechRecognizer
    DisposableEffect(Unit) {
        speechRecognizer.setRecognitionListener(recognitionListener)
        onDispose { speechRecognizer.destroy() }
    }

    // Анимация пульсации при записи
    LaunchedEffect(isRecording) {
        if (isRecording) {
            pulseValue.animateTo(
                targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            pulseValue.stop()
            pulseValue.snapTo(1f)
        }
    }

    // Функция для начала записи
    fun startListening() {
        if (!isRecording) {
            if (!hasRecordPermission) {
                speechError = "Microphone permission required"
                permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                return
            }

            if (!SpeechRecognizer.isRecognitionAvailable(context)) {
                speechError = "Speech recognition service not available"
                return
            }

            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Say '${currentWord.value}'")
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                }
                speechRecognizer.startListening(intent)
                isRecording = true
                userAnswer = ""
                speechError = null
                isAnswerCorrect = false
            } catch (e: Exception) {
                speechError = "Error: ${e.localizedMessage}"
                isRecording = false
            }
        }
    }

    // Функция для перехода к следующему слову
    fun goToNextWord() {
        if (englishWords.isEmpty()) return
        currentIndex = (currentIndex + 1) % englishWords.size
        userAnswer = ""
        isAnswerCorrect = false
        speechError = null
    }

    // Функция для повторной попытки произношения
    fun retryPronunciation() {
        userAnswer = ""
        isAnswerCorrect = false
        speechError = null
        startListening()
    }

    // Градиент для кнопок
    val gradient = Brush.verticalGradient(
        colors = List(9) { blue.copy(alpha = if (it == 0) 0.6f else 1f) }
    )

    // Отображение загрузки или ошибки
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { isLoading = true; errorMessage = null }) {
                    Text("Retry")
                }
            }
        }
        return
    }

    if (englishWords.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "No words available", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listening", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(
                            painter = painterResource(id = R.drawable.strelka),
                            contentDescription = "Back",
                            modifier = Modifier.size(37.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(blue)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .offset(y = (-40).dp)
            ) {
                when {
                    isRecording -> {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(blue)
                                .align(Alignment.Center)
                                .scale(pulseValue.value),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.microfon),
                                contentDescription = "Recording...",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { speechRecognizer.stopListening() }
                            )
                        }
                    }
                    isAnswerCorrect -> {
                        Button(
                            onClick = { goToNextWord() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = blue,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(15.dp)
                        ) {
                            Text(
                                text = "Correct! Next word",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                    else -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Основная кнопка для проверки/повторного произношения
                            Button(
                                onClick = {
                                    if (userAnswer.isEmpty()) {
                                        startListening()
                                    } else {
                                        retryPronunciation()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = blue,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(15.dp)
                            ) {
                                Text(
                                    text = if (userAnswer.isEmpty()) "Check pronunciation" else "Try again",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Кнопка пропуска слова
                            Button(
                                onClick = { goToNextWord() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.LightGray,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(15.dp)
                            ) {
                                Text(
                                    text = "Skip this word",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Отображение текущего слова
            Text(
                text = currentWord.value,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Отображение транскрипции
            if (currentTranscription.value.isNotEmpty()) {
                Text(
                    text = currentTranscription.value,
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Инструкции для пользователя
            Text(
                text = when {
                    isRecording -> "Speak the word clearly into the microphone"
                    userAnswer.isEmpty() -> "Press the button below to check your pronunciation"
                    else -> "Your pronunciation result"
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                textAlign = TextAlign.Center
            )

            // Отображение ошибок распознавания
            if (speechError != null) {
                Text(
                    text = speechError!!,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Отображение результата
            if (userAnswer.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isAnswerCorrect)
                            Color.Green.copy(alpha = 0.2f)
                        else
                            Color.Red.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "You said:",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = userAnswer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAnswerCorrect) Color.Green else Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = if (isAnswerCorrect) "✓ Perfect pronunciation!"
                            else "✗ Doesn't match, try again",
                            color = if (isAnswerCorrect) Color.Green else Color.Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

// Функция проверки произношения
private fun isPronunciationCorrect(recognized: String, expected: String): Boolean {
    val recognizedClean = recognized.trim().lowercase()
    val expectedClean = expected.trim().lowercase()

    // 1. Точное совпадение
    if (recognizedClean == expectedClean) return true

    // 2. Совпадение с учетом множественного числа
    if (recognizedClean == "${expectedClean}s" || recognizedClean == "${expectedClean}es") return true

    // 3. Совпадение без артиклей
    val recognizedNoArticles = recognizedClean
        .removePrefix("a ")
        .removePrefix("an ")
        .removePrefix("the ")
        .trim()
    if (recognizedNoArticles == expectedClean) return true

    return false
}