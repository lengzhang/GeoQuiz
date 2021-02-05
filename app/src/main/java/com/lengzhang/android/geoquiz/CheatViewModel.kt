package com.lengzhang.android.geoquiz

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModel

//private const val TAG = "CheatViewModel"

private const val KEY_ANSWER_SHOWN = "answer_shown"

class CheatViewModel : ViewModel() {

    private var isInitialized = false

    var answerIsTrue = false
    var answerShown = false

    fun handleOnCreate(savedInstanceState: Bundle?, intent: Intent) {
        if (isInitialized) return

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerShown =
                savedInstanceState?.getBoolean(KEY_ANSWER_SHOWN)
            ?:  intent.getBooleanExtra(EXTRA_IS_CHEATER, false)

        isInitialized = true
    }

    fun handleOnSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, answerShown)
    }

    companion object {
        const val EXTRA_ANSWER_IS_TRUE = "com.lengzhang.android.geoquiz.answer_is_true"
        const val EXTRA_IS_CHEATER = "com.lengzhang.android.geoquiz.is_cheater"
    }
}