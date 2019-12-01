package com.lifechievement.fragmentviewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lifechievement.fragmentviewmodel.ui.quiz.QuizFragment

class QuizActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.quiz_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, QuizFragment.newInstance())
                .commitNow()
        }
    }
}