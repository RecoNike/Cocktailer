package com.recon.coctailer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.json.JSONObject

class SearchActivity : AppCompatActivity() {

    lateinit var volleyRequest: VolleyRequest
    lateinit var getRandomTvBt: TextView
    lateinit var searchByNameBt: ImageButton
    lateinit var searchByCategoryBt: ImageButton
    lateinit var spinner: Spinner
    lateinit var nameField: EditText
    var choosenCat = "Ordinary Drink"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        getRandomTvBt = findViewById(R.id.getRandomTvBt)
        searchByNameBt = findViewById(R.id.searchByNameBt)
        searchByCategoryBt = findViewById(R.id.searchByCategoryBt)
        spinner = findViewById(R.id.spinner)
        nameField = findViewById(R.id.nameField)
        volleyRequest = VolleyRequest(this)

        searchByNameBt.setOnClickListener{
            searchByName(nameField.text.toString())
        }
        getRandomTvBt.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            var initialized = false

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (initialized) {
                    choosenCat = parent?.getItemAtPosition(position).toString()
                } else {
                    initialized = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


    }

    fun searchByName(name: String){
        lifecycleScope.launch{
            try{
                val url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + name
                val response = volleyRequest.getJsonFromUrl(url)
                val data = response.toString()
                val jsonObject = JSONObject(data)
//                val drinks = jsonObject.getJSONArray("drinks")
                Log.d("", jsonObject.toString())

//                var ingrString: String = "Ingredients\n\n"
//                if (drinks.length() > 0){
//                    val coctail = drinks.getJSONObject(0)
//                    name.text = coctail.getString("strDrink")
//                    val coctailImageUrl = coctail.getString("strDrinkThumb")
//                    cookingTv.text = coctail.getString("strInstructions")
//                    Glide.with(this@MainActivity).load(coctailImageUrl).into(imageView)
//
//                    for (i in 1..12){
//                        val ingredientKey = "strIngredient$i"
//                        val measureKey = "strMeasure$i"
//                        val ingredient = coctail.getString(ingredientKey)
//                        val measure = coctail.getString(measureKey)
//                        // If ingredient is not empty, add it to the string
//                        if (ingredient != "null") {
//                            ingrString += "► $ingredient - $measure\n"
//                        }
//
//                    }
//                    ingredients.text = ingrString
//                    ingredients.visibility = View.VISIBLE
//                    name.visibility = View.VISIBLE
//                    cookingTv.visibility = View.VISIBLE
//                }

            } catch (e: Error){

            }
        }
    }

}