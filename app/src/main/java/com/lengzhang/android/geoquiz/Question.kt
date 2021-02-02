package com.lengzhang.android.geoquiz


data class Question(
        val question: String,
        val answer: Boolean,
        var response: Int = 0   // 0 - no response (init value)
                                // 1 - true
                                // 2 - false
                                // 3 - cheat
)