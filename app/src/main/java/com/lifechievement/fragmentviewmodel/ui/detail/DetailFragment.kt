package com.lifechievement.fragmentviewmodel.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.lifechievement.fragmentviewmodel.R
import com.lifechievement.fragmentviewmodel.models.ExpandedArticle
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.detail_fragment.character_picture



class DetailFragment : Fragment() {

    private var character: ExpandedArticle? = null

    companion object {

        const val ARG_CHARACTER = "character"

        fun newInstance(character: ExpandedArticle): DetailFragment {
            val fragment = DetailFragment()

            val bundle = Bundle().apply {
                putParcelable(ARG_CHARACTER, character)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        character = arguments?.getParcelable(ARG_CHARACTER)


        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        if (character != null) {
            character_name.text = character!!.title

            Glide.with(this)
                .load(character!!.thumbnail)
                .placeholder(R.drawable.placeholder)
                .into(character_picture)

            character_abstract.text = character!!.abstract

            see_more.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://onepiece.fandom.com${character!!.url}")
                startActivity(i)
            }
        }


    }

}
