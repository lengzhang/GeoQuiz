package com.lengzhang.android.geoquiz

import android.os.Bundle
//import android.util.Log
import androidx.lifecycle.ViewModel

//private const val TAG = "QuizViewModel"
private const val KEY_INDEX = "index"
private const val KEY_RESPONSES = "responses"
private const val KEY_GRADE = "grade"
private const val KEY_RESPONSE_COUNT = "response_count"

class QuizViewModel : ViewModel() {

    private var isInitialized = false
    private val questionBank = mutableListOf<Question>()

    var currentIndex = 0

    var grade = 0
    var responseCount = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: String
        get() = questionBank[currentIndex].question

    val currentUserAnswer: Boolean?
        get() = when(questionBank[currentIndex].response) {
            1 -> true
            2 -> false
            else -> null
        }

    val currentIsCheater: Boolean
        get() = questionBank[currentIndex].response == 3

    val numberOfQuestions: Int
        get() = questionBank.size

    fun moveToPrevious() {
        currentIndex = when (currentIndex) {
            0 -> questionBank.size - 1
            else -> currentIndex - 1
        }
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun setUserAnswer(userAnswer: Boolean) {
        if (questionBank[currentIndex].response == 0) {
            questionBank[currentIndex].response =
                    when (userAnswer) {
                        true -> 1
                        false -> 2
                    }
            responseCount++
            if (userAnswer == currentQuestionAnswer) grade++
        }
    }

    fun setIsCheater() {
        if (questionBank[currentIndex].response == 0) {
            questionBank[currentIndex].response = 3
            responseCount++
        }
    }

    fun handleOnCreate(savedInstanceState: Bundle?, resources: android.content.res.Resources) {
        if (isInitialized) return

        currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        grade = savedInstanceState?.getInt(KEY_GRADE, 0) ?: 0
        responseCount = savedInstanceState?.getInt(KEY_RESPONSE_COUNT, 0) ?: 0

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
        savedInstanceState.putInt(KEY_GRADE, grade)
        savedInstanceState.putInt(KEY_RESPONSE_COUNT, responseCount)

        val responses = questionBank.map { question -> question.response }
        savedInstanceState.putIntArray(KEY_RESPONSES, responses.toIntArray())
    }
}