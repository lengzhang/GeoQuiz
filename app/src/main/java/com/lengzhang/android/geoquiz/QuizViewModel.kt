package com.lengzhang.android.geoquiz

import android.os.Bundle
//import android.util.Log
import androidx.lifecycle.ViewModel

//private const val TAG = "QuizViewModel"
private const val KEY_INDEX = "index"
private const val KEY_RESPONSES = "responses"

class QuizViewModel : ViewModel() {

    private var isInitialized = false

    var currentIndex = 0
    var isCheater = false

    private val questionBank = mutableListOf<Question>()

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: String
        get() = questionBank[currentIndex].question

    val currentResponse: Int
        get() = questionBank[currentIndex].response

    fun moveToPrevious() {
        currentIndex = when (currentIndex) {
            0 -> questionBank.size - 1
            else -> currentIndex - 1
        }
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun setResponse(value: Boolean) {
        questionBank[currentIndex].response = when (value) {
            true -> 1
            false -> -1
        }
    }

    fun handleOnCreate(savedInstanceState: Bundle?, resources: android.content.res.Resources) {
        if (isInitialized) return

        currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0

        val questions: Array<String> = resources.getStringArray(R.array.questions)
        val answers: Array<String> = resources.getStringArray(R.array.answers)
        val responses = savedInstanceState?.getIntArray(KEY_RESPONSES) ?: IntArray(0)

        for ((index, question) in questions.withIndex()) {
            val answer =
                    if (index < answers.size) answers[index] == "TRUE"
                    else true
            val response =
                    if (index < responses.size) responses[index]
                    else 0

            questionBank.add(Question(question, answer, response))
        }
    }

    fun handleOnSavedInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt(KEY_INDEX, currentIndex)

        val responses = questionBank.map { question -> question.response }
        savedInstanceState.putIntArray(KEY_RESPONSES, responses.toIntArray())
    }
}