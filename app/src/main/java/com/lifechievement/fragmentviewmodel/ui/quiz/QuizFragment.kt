package com.lifechievement.fragmentviewmodel.ui.quiz

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.lifechievement.fragmentviewmodel.R
import com.lifechievement.fragmentviewmodel.models.ExpandedArticle
import com.lifechievement.fragmentviewmodel.models.UnexpandedArticle
import kotlinx.android.synthetic.main.quiz_fragment.*
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.lifechievement.fragmentviewmodel.DetailActivity


class QuizFragment : Fragment() {

    companion object {
        fun newInstance() = QuizFragment()
    }

    private lateinit var viewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.quiz_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(QuizViewModel::class.java)
        viewModel.getAllCharacters()

        viewModel.characters.observe(this, Observer<ArrayList<UnexpandedArticle>> {

            answer1.text = it[0].title
            answer2.text = it[1].title
            answer3.text = it[2].title
            answer4.text = it[3].title
        })

        viewModel.isLoading.observe(this, Observer<Boolean> { isLoading ->

            if (isLoading) {
                progress_bar.visibility = VISIBLE
            } else {
                progress_bar.visibility = GONE
            }
        })

        viewModel.correctCharacter.observe(this, Observer<ExpandedArticle> { correctCharacter ->

            Log.i("Antoine", "correctCharacter.thumbnail = $correctCharacter.thumbnail")
            Glide.with(this)
                .load(correctCharacter.thumbnail)
                .placeholder(R.drawable.placeholder)
                .into(character_picture)
        })

        viewModel.score.observe(this, Observer<Int> { score ->
            current_score.text = "score: $score"
        })
        viewModel.highScore.observe(this, Observer<Int> { score ->
            high_score.text = "high score: $score"
        })

        viewModel.isDisplayingAnswer.observe(this, Observer<Boolean> { isDisplayingAnswer ->
            if (isDisplayingAnswer) {
                AlertDialog.Builder(this.activity!!)
                    .setTitle("Wrong answer")
                    .setMessage("The correct answer was ${viewModel.correctCharacter.value!!.title}")
                    .setPositiveButton(
                        "More info",
                        DialogInterface.OnClickListener { dialog, which ->
                            // Continue with delete operation
                            val correctCharacter = viewModel.correctCharacter.value

                            val intent = Intent(this.activity, DetailActivity::class.java)
                            intent.putExtra("character", correctCharacter)
                            startActivity(intent)
                            viewModel.generateNextQuestion()
                        })

                    .setNegativeButton("Restart game", DialogInterface.OnClickListener { dialog, which ->
                        viewModel.generateNextQuestion()
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            }
        })

        answer1.setOnClickListener {
            viewModel.answerClicked(0)
        }
        answer2.setOnClickListener {
            viewModel.answerClicked(1)
        }
        answer3.setOnClickListener {
            viewModel.answerClicked(2)
        }
        answer4.setOnClickListener {
            viewModel.answerClicked(3)
        }
    }

}
