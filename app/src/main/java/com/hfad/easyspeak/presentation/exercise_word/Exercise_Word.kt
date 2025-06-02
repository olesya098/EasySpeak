package com.hfad.easyspeak.presentation.exercise_word

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.hfad.easyspeak.presentation.components.*
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseWord(navController: NavController) {
    val context = LocalContext.current
    val database = Firebase.database
    val wordsRef = database.getReference("words")

    var wordsList by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var currentWord by remember { mutableStateOf("") }
    var shuffledWord by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var correctAnswersCount by remember { mutableStateOf(0) }
    var wrongAnswersCount by remember { mutableStateOf(0) }
    var testCompleted by remember { mutableStateOf(false) }
    var selectedWords by remember { mutableStateOf<List<String>>(emptyList()) }
    val TOTAL_QUESTIONS = 20
    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    fun selectRandomWords(words: List<String>, count: Int): List<String> {
        return if (words.size <= count) words.shuffled() else words.shuffled().take(count)
    }
    fun shuffleLetters(word: String): String {
        return word.toCharArray().apply {
            for (i in indices) {
                val j = Random.nextInt(i, size)
                val temp = this[i]
                this[i] = this[j]
                this[j] = temp
            }
        }.joinToString(" ")
    }

    LaunchedEffect(Unit) {
        wordsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val loadedWords = mutableListOf<String>()

                    for (wordSnapshot in snapshot.children) {
                        val englishWord = wordSnapshot.key ?: continue
                        val translation = wordSnapshot.child("translation").getValue(String::class.java)
                        if (translation != null) {
                            loadedWords.add(englishWord)
                        } else {
                            val wordValue = wordSnapshot.getValue(String::class.java)
                            if (wordValue != null) {
                                loadedWords.add(wordValue)
                            }
                        }
                    }

                    if (loadedWords.isEmpty()) {
                        errorMessage = "No words found in database"
                    } else {
                        wordsList = loadedWords
                        selectedWords = selectRandomWords(loadedWords, TOTAL_QUESTIONS)
                        if (selectedWords.isNotEmpty()) {
                            currentWord = selectedWords.first()
                            shuffledWord = shuffleLetters(currentWord)
                        }
                    }
                } catch (e: Exception) {
                    errorMessage = "Error loading words: ${e.localizedMessage}"
                } finally {
                    isLoading = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage = error.message
                isLoading = false
            }
        })
    }

    fun checkAnswer() {
        if (answer.isBlank()) {
            errorMessage = "Please enter your answer"
            return
        }

        isCorrect = answer.equals(currentWord, ignoreCase = true)
        if (isCorrect) correctAnswersCount++ else wrongAnswersCount++
        showBottomSheet.value = true

        if (currentQuestionIndex >= selectedWords.size - 1) {
            testCompleted = true
        }
    }

    fun nextWord() {
        if (selectedWords.isEmpty() || currentQuestionIndex >= selectedWords.size - 1) {
            testCompleted = true
            return
        }

        currentQuestionIndex++
        currentWord = selectedWords[currentQuestionIndex]
        shuffledWord = shuffleLetters(currentWord)
        answer = ""
        isCorrect = false
        showBottomSheet.value = false
    }

    fun restartTest() {
        if (wordsList.isEmpty()) return

        selectedWords = selectRandomWords(wordsList, TOTAL_QUESTIONS)
        currentQuestionIndex = 0
        currentWord = selectedWords[currentQuestionIndex]
        shuffledWord = shuffleLetters(currentWord)
        answer = ""
        correctAnswersCount = 0
        wrongAnswersCount = 0
        testCompleted = false
        isCorrect = false
        showBottomSheet.value = false
    }

    CustomScaffoldTests(
        title = "Guess the word",
        image = R.drawable.close,
        number = currentQuestionIndex,
        textButton = if (showBottomSheet.value && isCorrect && !testCompleted)
            stringResource(R.string.next)
        else stringResource(R.string.check),
        onButtonClick = {
            if (showBottomSheet.value && isCorrect && !testCompleted) {
                nextWord()
            } else {
                checkAnswer()
            }
        },
        onClickClose = { navController.navigate(NavigationRoutes.MainScreenUI.route) },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when {
                    isLoading -> LoadingScreen()
                    errorMessage != null -> ErrorScreen(error = errorMessage)
                    selectedWords.isEmpty() -> EmptyScreen()
                    else -> WordExerciseContent(
                        shuffledWord = shuffledWord,
                        answer = answer,
                        onAnswerChange = { answer = it },
                        isCorrect = isCorrect,
                        currentWord = currentWord
                    )
                }
            }
        }
    )

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
            sheetState = sheetState,
            containerColor = Color.White,
            scrimColor = Color.Black.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxHeight(0.7f)
        ) {
            if (testCompleted) {
                val testPassed = correctAnswersCount > wrongAnswersCount
                ModalBottomSheetContent(
                    image = if (testPassed) R.drawable.right else R.drawable.wrong,
                    imageCheck = if (testPassed) R.drawable.checkmark else R.drawable.wrongicon,
                    textCheck = if (testPassed)
                        "Test passed! ($correctAnswersCount/$TOTAL_QUESTIONS)"
                    else "Test failed! ($correctAnswersCount/$TOTAL_QUESTIONS)",
                    color = if (testPassed) Color.Green else MaterialTheme.colorScheme.error,
                    isLastQuestion = true,
                    onNextClick = {
                        showBottomSheet.value = false
                        restartTest()
                    },
                    onExitClick = {
                        showBottomSheet.value = false
                        navController.popBackStack()
                        navController.navigate(NavigationRoutes.MainScreenUI.route)
                    }
                )
            } else {
                ModalBottomSheetContent(
                    image = if (isCorrect) R.drawable.right else R.drawable.wrong,
                    imageCheck = if (isCorrect) R.drawable.checkmark else R.drawable.wrongicon,
                    textCheck = if (isCorrect) "Correct!" else "Wrong! Correct: $currentWord",
                    color = if (isCorrect) Color.Green else MaterialTheme.colorScheme.error,
                    isLastQuestion = false,
                    onNextClick = {
                        nextWord()
                    },
                    onExitClick = {
                        showBottomSheet.value = false
                        navController.popBackStack()
                        navController.navigate(NavigationRoutes.MainScreenUI.route)
                    }
                )
            }
        }
    }
}

@Composable
private fun WordExerciseContent(
    shuffledWord: String,
    answer: String,
    onAnswerChange: (String) -> Unit,
    isCorrect: Boolean,
    currentWord: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.decipher_the_word_using_all_the_letters),
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp, top = 20.dp),
            fontSize = 16.sp
        )

        Text(
            text = shuffledWord,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp),
            fontSize = 24.sp
        )

        TextAndTextField(
            title = stringResource(R.string.your_answer),
            value = answer,
            text = stringResource(R.string.type_the_word_here),
            onvalChange = onAnswerChange
        )
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(
            text = "Loading words...",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun ErrorScreen(error: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun EmptyScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No words available",
            modifier = Modifier.padding(16.dp)
        )
    }
}