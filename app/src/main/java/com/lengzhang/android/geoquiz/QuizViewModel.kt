package com.lengzhang.android.geoquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    var currentIndex = 0
    var isCheater = false

    private val questionBank = mutableListOf<Question>()

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: String
        get() = questionBank[currentIndex].question

    fun moveToPrevious() {
        currentIndex = when (currentIndex) {
            0 -> questionBank.size - 1
            else -> currentIndex - 1
        }
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun setQuestionBank(questions: Array<String>, answers: Array<String>) {
        for ((index, question) in questions.withIndex()) {
            val answer =
                    if (index < answers.size) answers[index].toLowerCase() == "true"
                    else true
            questionBank.add(Question(question, answer))
        }
    }
}