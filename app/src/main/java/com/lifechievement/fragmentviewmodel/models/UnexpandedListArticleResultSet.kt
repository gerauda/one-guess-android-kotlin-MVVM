package com.lifechievement.fragmentviewmodel.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class UnexpandedListArticleResultSet (
    val items: ArrayList<UnexpandedArticle> = ArrayList(),
    val basepath: String? = null
){
    class Deserializer: ResponseDeserializable<UnexpandedListArticleResultSet> {
        override fun deserialize(content: String): UnexpandedListArticleResultSet? = Gson().fromJson(content, UnexpandedListArticleResultSet::class.java)
    }
}