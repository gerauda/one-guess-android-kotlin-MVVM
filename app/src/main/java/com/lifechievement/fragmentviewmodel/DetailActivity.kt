package com.lifechievement.fragmentviewmodel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lifechievement.fragmentviewmodel.models.ExpandedArticle
import com.lifechievement.fragmentviewmodel.ui.detail.DetailFragment

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        var character: ExpandedArticle = intent.getParcelableExtra("character")

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DetailFragment.newInstance(character))
                .commitNow()
        }
    }

}