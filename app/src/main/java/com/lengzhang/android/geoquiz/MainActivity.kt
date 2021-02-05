package com.lengzhang.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var previousButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionLabelView: TextView
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        quizViewModel.handleOnCreate(savedInstanceState, resources)

        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        previousButton = findViewById(R.id.previous_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionLabelView = findViewById(R.id.question_label_view)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
            updateAnswerButton()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            updateAnswerButton()
        }

        previousButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val isCheater = quizViewModel.currentIsCheater
            val intent = CheatActivity.newIntent(
                    this@MainActivity,
                    answerIsTrue,
                    isCheater)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            val isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            if (!quizViewModel.currentIsCheater && isCheater) {
                quizViewModel.setIsCheater()
                if (quizViewModel.currentIsCheater) {
                    updateQuestion()
                    showGrade()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        quizViewModel.handleOnSavedInstanceState(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionLabel = "Question ${quizViewModel.currentIndex + 1}："
        questionLabelView.text = questionLabel
        val questionText = quizViewModel.currentQuestionText
        questionTextView.text = questionText
        updateAnswerButton()
    }

    private fun updateAnswerButton() {
        val isEnabled = quizViewModel.currentUserAnswer == null
        trueButton.isEnabled = isEnabled
        falseButton.isEnabled = isEnabled
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (quizViewModel.currentIsCheater) {
            Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT)
                    .show()
            return
        }

        val correctAnswer  = quizViewModel.currentQuestionAnswer

        val messageResId =
                if (userAnswer == correctAnswer) R.string.correct_toast
                else R.string.incorrect_toast

        quizViewModel.setUserAnswer(userAnswer)

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show()

        updateQuestion()
        showGrade()
    }

    private fun showGrade() {
        if (quizViewModel.responseCount == quizViewModel.numberOfQuestions) {
            val grade = quizViewModel.grade.toFloat() / quizViewModel.numberOfQuestions * 100
            val message = "Your grade is ${String.format("%.2f", grade)}%"
            Toast.makeText(this, message, Toast.LENGTH_SHORT)
                    .show()
        }
    }
}