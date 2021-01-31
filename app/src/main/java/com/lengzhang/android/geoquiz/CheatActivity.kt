package com.lengzhang.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders

private const val TAG = "CheatActivity"
private const val KEY_IS_CHEATER = "IS_CHEATER"
const val EXTRA_ANSWER_SHOWN = "com.lengzhang.android.geoquiz.anser_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.lengzhang.android.geoquiz.answer_is_true"
private const val EXTRA_IS_CHEATER = "com.lengzhang.android.geoquiz.is_cheater"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private var answerIsTrue = false

    private val cheatViewModel: CheatViewModel by lazy {
        ViewModelProviders.of(this).get(CheatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreaqte(Bundle?) called" )
        setContentView(R.layout.activity_cheat)

        val isCheater = savedInstanceState?.getBoolean(KEY_IS_CHEATER, false)
                ?: intent.getBooleanExtra(EXTRA_IS_CHEATER, false)
        cheatViewModel.isCheater = isCheater

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)

        if (cheatViewModel.isCheater) {
            updateAnswer()
            setAnswerShowResult(true)
        }

        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            updateAnswer()
            setAnswerShowResult(true)
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
        savedInstanceState.putBoolean(KEY_IS_CHEATER, cheatViewModel.isCheater)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateAnswer() {
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
    }

    private fun setAnswerShowResult(isAnswerShown: Boolean) {
        cheatViewModel.isCheater = true
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, isCheater: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_IS_CHEATER, isCheater)
            }
        }
    }
}