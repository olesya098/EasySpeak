@file:Suppress("NAME_SHADOWING")

package com.hfad.easyspeak.presentation.wordpractice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hfad.easyspeak.R
import com.hfad.easyspeak.model.WordPair
import com.hfad.easyspeak.model.WordpractiseModel
import com.hfad.easyspeak.presentation.components.CardsAnswers
import com.hfad.easyspeak.presentation.components.CustomScaffoldTests
import com.hfad.easyspeak.presentation.components.ModalBottomSheetContent
import com.hfad.easyspeak.presentation.loadingscreen.LoadingScreen
import com.hfad.easyspeak.presentation.navigation.NavigationRoutes
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordpracticeUI(navController: NavController) {
    val context = LocalContext.current
    val database = Firebase.database
    val wordsRef = database.getReference("words")

    // Состояния для текущего слова и вариантов ответа
    val (currentWord, setCurrentWord) = remember { mutableStateOf<WordPair?>(null) }
    val (options, setOptions) = remember { mutableStateOf<List<String>>(emptyList()) }
    val (selectedOption, setSelectedOption) = remember { mutableStateOf<String?>(null) }
    val allWords = remember { mutableListOf<WordPair>() }
    val (currentQuestionIndex, setCurrentQuestionIndex) = remember { mutableStateOf(0) } // теперь индексы 0-19

    // Состояния для управления тестом
    val (correctAnswersCount, setCorrectAnswersCount) = remember { mutableStateOf(0) } // Счетчик правильных ответов
    val (wrongAnswersCount, setWrongAnswersCount) = remember { mutableStateOf(0) } // Счетчик неправильных ответов
    val (testCompleted, setTestCompleted) = remember { mutableStateOf(false) } // Флаг завершения теста
    val (selectedWords, setSelectedWords) = remember { mutableStateOf<List<WordPair>>(emptyList()) } // Список выбранных слов для теста
    var isLoading by remember { mutableStateOf(true) }
    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val (isAnswerCorrect, setIsAnswerCorrect) = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        isLoading = false
        wordsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val loadedWords = mutableListOf<WordPair>()
                for (wordSnapshot in snapshot.children) {
                    val englishWord = wordSnapshot.key ?: continue
                    val translation =
                        wordSnapshot.child("translation").getValue(String::class.java) ?: continue
                    loadedWords.add(WordPair(englishWord, translation))
                }
                allWords.addAll(loadedWords)

                if (loadedWords.isNotEmpty()) {
                    val randomWords = loadedWords.shuffled().take(20)
                    setSelectedWords(randomWords.toMutableList())
                    setCurrentWord(randomWords.first())
                    generateOptions(randomWords.first(), loadedWords, setOptions)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    if (isLoading) {
        LoadingScreen(loadingText = stringResource(R.string.loading_your_data))
        return
    }
    fun checkAnswer() {
        if (selectedOption != null && currentWord != null) {
            val correct = selectedOption == currentWord.russian
            setIsAnswerCorrect(correct)

            if (correct) {
                setCorrectAnswersCount(correctAnswersCount + 1)
            } else {
                setWrongAnswersCount(wrongAnswersCount + 1)
            }

            showBottomSheet.value = true
            setSelectedOption(null)

            if (currentQuestionIndex == selectedWords.size - 1) {
                setTestCompleted(true)
            }
        }
    }

    fun goToNextQuestion() {
        if (currentQuestionIndex < selectedWords.size - 1) {
            generateOptions(selectedWords[currentQuestionIndex + 1], allWords, setOptions)
            setCurrentWord(selectedWords[currentQuestionIndex + 1])
            setCurrentQuestionIndex(currentQuestionIndex + 1)
        } else {
            setTestCompleted(true)
        }
    }

    CustomScaffoldTests(
        title = "Word practice",
        image = R.drawable.close,
        number = currentQuestionIndex,
        textButton = stringResource(R.string.check),
        onButtonClick = { checkAnswer() },
        onClickClose = { navController.navigate(NavigationRoutes.MainScreenUI.route) },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    currentWord?.let { word ->
                        Text(
                            text = word.english,
                            modifier = Modifier.padding(bottom = 32.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.inversePrimary,
                        )

                        options.forEach { option ->
                            CardsAnswers(
                                text = option,
                                isSelected = option == selectedOption,
                                onClick = { setSelectedOption(option) },
                            )
                        }
                    }
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
            modifier = Modifier.fillMaxHeight(0.72f),
        ) {

            if (testCompleted || currentQuestionIndex == selectedWords.size - 1) {
                val testPassed = wrongAnswersCount < 2
                ModalBottomSheetContent(
                    image = if (testPassed) R.drawable.right else R.drawable.wrong,
                    imageCheck = if (testPassed) R.drawable.checkmark else R.drawable.wrongicon,
                    textCheck = if (testPassed) "Test passed! ($correctAnswersCount/20)"
                    else "Test failed! ($correctAnswersCount/20)",
                    color = if (testPassed) Color.Green else MaterialTheme.colorScheme.error,
                    isLastQuestion = true,
                    onNextClick = {
                        showBottomSheet.value = false
                        restartTest(
                            allWords,
                            setSelectedWords,
                            setCurrentWord,
                            setOptions,
                            setCurrentQuestionIndex,
                            setCorrectAnswersCount,
                            setWrongAnswersCount,
                            setTestCompleted
                        )
                    },
                    onExitClick = {
                        showBottomSheet.value = false
                        navController.popBackStack()
                        navController.navigate(NavigationRoutes.MainScreenUI.route)
                    }
                )
            } else {
                val result = if (isAnswerCorrect) {
                    WordpractiseModel(
                        R.drawable.right,
                        R.drawable.checkmark,
                        "Correct!",
                        Color.Green
                    )
                } else {
                    WordpractiseModel(
                        R.drawable.wrong,
                        R.drawable.wrongicon,
                        "Wrong! Try again",
                        MaterialTheme.colorScheme.error
                    )
                }
                ModalBottomSheetContent(
                    image = result.image,
                    imageCheck = result.imageCheck,
                    textCheck = result.textCheck,
                    onNextClick = {
                        showBottomSheet.value = false
                        goToNextQuestion()
                    },
                    color = result.color,
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

private fun generateOptions(
    correctWord: WordPair,
    allWords: List<WordPair>,
    setOptions: (List<String>) -> Unit
) {
    val allTranslations = allWords.map { it.russian }.distinct()

    val wrongOptions = (allTranslations - correctWord.russian)
        .shuffled()
        .take(3)

    val options = (wrongOptions + correctWord.russian).shuffled()
    setOptions(options)
}

private fun restartTest(
    allWords: List<WordPair>,
    setSelectedWords: (List<WordPair>) -> Unit,
    setCurrentWord: (WordPair?) -> Unit,
    setOptions: (List<String>) -> Unit,
    setCurrentQuestionIndex: (Int) -> Unit,
    setCorrectAnswersCount: (Int) -> Unit,
    setWrongAnswersCount: (Int) -> Unit,
    setTestCompleted: (Boolean) -> Unit
) {
    setCorrectAnswersCount(0)
    setWrongAnswersCount(0)
    setTestCompleted(false)
    setCurrentQuestionIndex(0)

    val newRandomWords = allWords.shuffled().take(20)
    setSelectedWords(newRandomWords)

    setCurrentWord(newRandomWords.first())

    generateOptions(newRandomWords.first(), allWords, setOptions)
}