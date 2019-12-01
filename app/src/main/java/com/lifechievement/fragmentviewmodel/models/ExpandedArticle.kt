package com.lifechievement.fragmentviewmodel.models

import android.os.Parcelable
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExpandedArticle(
    //    private var originalDimensions: OriginalDimension? = null,
    val url: String? = null,
    val ns: String? = null,
    val abstract: String? = null,
    val thumbnail: String? = null,
    //    private var revision: Revision? = null,
    var id: String? = null,
    var title: String? = null,
    var type: String? = null,
    var comments: String? = null
) : Parcelable {
    class Deserializer: ResponseDeserializable<ExpandedArticle> {
        override fun deserialize(content: String): ExpandedArticle? = Gson().fromJson(content, ExpandedArticle::class.java)
    }
}