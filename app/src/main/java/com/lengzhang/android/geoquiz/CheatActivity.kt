package com.lengzhang.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders

//private const val TAG = "CheatActivity"

const val EXTRA_ANSWER_SHOWN = "com.lengzhang.android.geoquiz.answer_shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private val cheatViewModel: CheatViewModel by lazy {
        ViewModelProviders.of(this).get(CheatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        cheatViewModel.handleOnCreate(savedInstanceState, intent)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            cheatViewModel.answerShown = true
            showAnswer()
            setAnswerShowResult()
        }

        if (cheatViewModel.answerShown) {
            showAnswer()
            setAnswerShowResult()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        cheatViewModel.handleOnSaveInstanceState((savedInstanceState))
    }

    private fun showAnswer() {
        val answerText = when {
            cheatViewModel.answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
        showAnswerButton.isEnabled = false
    }

    private fun setAnswerShowResult() {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, true)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, isCheater: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(CheatViewModel.EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(CheatViewModel.EXTRA_IS_CHEATER, isCheater)
            }
        }
    }
}