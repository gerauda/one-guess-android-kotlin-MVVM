package com.lifechievement.fragmentviewmodel.ui.quiz

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.lifechievement.fragmentviewmodel.models.ExpandedArticle
import com.lifechievement.fragmentviewmodel.models.ExpandedArticleResultSet
import com.lifechievement.fragmentviewmodel.models.UnexpandedArticle
import com.lifechievement.fragmentviewmodel.models.UnexpandedListArticleResultSet
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.ViewModelProvider



class QuizViewModel(application: Application) : AndroidViewModel(application) {

    val sharedPreferencesOneGuess = "ONE_GUESS"
    val sharedPreferencesHighScore = "HIGH_SCORE"

    var unexpandedListArticleResultSet: UnexpandedListArticleResultSet? = null
    var correctAnswer: Int = 0
    var correctId: Int = 0

    var correctCharacter: MutableLiveData<ExpandedArticle> = MutableLiveData()
    var characters: MutableLiveData<ArrayList<UnexpandedArticle>> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isDisplayingAnswer: MutableLiveData<Boolean> = MutableLiveData()
    var score: MutableLiveData<Int> = MutableLiveData()
    var highScore: MutableLiveData<Int> = MutableLiveData()


    fun getAllCharacters()
    {
        isLoading.postValue(true)
        score.postValue(0)
        highScore.postValue(getHighScore())
        isDisplayingAnswer.postValue(false)
        search("Male_Characters", 1000) { wikiResult ->

            Log.i("Antoine", "Request done")
            unexpandedListArticleResultSet = wikiResult

            generateNextQuestion()
            Unit
        }

    }

    fun generateNextQuestion() {
        var copyList = unexpandedListArticleResultSet
        if (copyList != null) {


            var newCharacters: ArrayList<UnexpandedArticle> = ArrayList()
            newCharacters.add(copyList.items.get(Random().nextInt(copyList.items.size)))
            newCharacters.add(copyList.items.get(Random().nextInt(copyList.items.size)))
            newCharacters.add(copyList.items.get(Random().nextInt(copyList.items.size)))
            newCharacters.add(copyList.items.get(Random().nextInt(copyList.items.size)))

            characters.postValue(newCharacters)


            correctAnswer = Random().nextInt(4)
            correctId = newCharacters.get(correctAnswer).id

            getDetail(correctId, 500) { wikiResult ->

                Log.i("Antoine", "Request 2 done")
                correctCharacter.postValue(wikiResult.items[correctId])
                isLoading.postValue(false)

                Unit
            }
        }
    }

    fun answerClicked(answer: Int) {
        Log.i("Antoine", "Answer $answer")

        if (answer == correctAnswer) {
            if ((score.value!! +1) > getHighScore()) {
                setNewHighScore(score.value!! +1);
            }
            score.postValue(score.value!!+1)
            generateNextQuestion()
        } else {
            score.postValue(0)
            isDisplayingAnswer.postValue(true)
        }
    }


    fun getHighScore() : Int {
        val sharedPref: SharedPreferences = getApplication<Application>().getSharedPreferences(sharedPreferencesOneGuess, Context.MODE_PRIVATE)
        return sharedPref.getInt(sharedPreferencesHighScore, 0)
    }

    fun setNewHighScore(newHighScore: Int) {
        val sharedPref: SharedPreferences = getApplication<Application>().getSharedPreferences(sharedPreferencesOneGuess, Context.MODE_PRIVATE)
        var editor = sharedPref.edit()
        editor.putInt(sharedPreferencesHighScore, newHighScore)
        editor.commit()
        highScore.postValue(newHighScore)


    }

    fun getDetailUrl(characterId: Int, abstract: Int): String {
        val BaseUrl = "https://onepiece.wikia.com/api/v1/Articles/Details"
        return BaseUrl + "?ids=$characterId" +
                "&abstract=$abstract"
    }

    fun getSearchUrl(term: String, limit: Int): String {
        val BaseUrl = "https://onepiece.wikia.com/api/v1/Articles/List"
        return BaseUrl + "?category=$term" +
                "&limit=$limit"
    }

    fun getDetail(characterId: Int, abstract: Int, responseHandler : (result: ExpandedArticleResultSet) -> Unit?) {
        getDetailUrl(characterId, abstract).httpGet()
            .responseObject(ExpandedArticleResultSet.Deserializer()){ _, _, result ->

                when(result){
                    is Result.Failure ->{
                        Log.e("ErrorMsg", result.getException().message)
                        result.getException().stackTrace
                        throw Exception(result.getException())
                    }

                    is Result.Success ->{
                        val(data, _) = result
                        Log.i("Antoine", "result = $result")
                        responseHandler.invoke(data as ExpandedArticleResultSet)
                    }
                }
            }
    }

    fun search(term: String, limit: Int, responseHandler : (result: UnexpandedListArticleResultSet) -> Unit?){
        getSearchUrl(term, limit).httpGet()
            .responseObject(UnexpandedListArticleResultSet.Deserializer()){ _, _, result->

                when(result){
                    is Result.Failure ->{
                        Log.e("ErrorMsg", result.getException().message)
                        result.getException().stackTrace
                        throw Exception(result.getException())
                    }

                    is Result.Success ->{
                        val(data, _) = result
                        Log.i("Antoine", "result = $result")
                        responseHandler.invoke(data as UnexpandedListArticleResultSet)
                    }
                }
            }
    }
}