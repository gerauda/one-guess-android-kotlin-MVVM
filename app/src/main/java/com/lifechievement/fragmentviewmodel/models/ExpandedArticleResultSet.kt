package com.lifechievement.fragmentviewmodel.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.util.HashMap


class ExpandedArticleResultSet (
    val basepath: String? = null,
    val items: HashMap<Int, ExpandedArticle>

){
    class Deserializer: ResponseDeserializable<ExpandedArticleResultSet> {
        override fun deserialize(content: String): ExpandedArticleResultSet? =
        Gson().fromJson(content, ExpandedArticleResultSet::class.java)
    }
}