package com.recon.coctailer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class ResultActivity : AppCompatActivity() {
    lateinit var jsonData: String
    lateinit var cocktailsList: MutableList<Cocktail>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Получение JSON из Intent
        if (intent.getStringExtra("cocktailData") != null) {
            jsonData = intent.getStringExtra("cocktailData") ?: ""
            parseJson()
        } else {
            jsonData = intent.getStringExtra("catData")?:""
            parseJsonCat()
        }
        // Вызов метода parseJson()


        // В вашей активити ResultActivity:
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val adapter = CocktailAdapter(cocktailsList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    private fun parseJsonCat() {
        cocktailsList = mutableListOf<Cocktail>()
        // Преобразование строки JSON в JSONObject
        val jsonObject = JSONObject(jsonData)
        val drinks = jsonObject.getJSONArray("drinks")
        for (i in 0 until drinks.length()) {
            val cocktailObj = drinks.getJSONObject(i)
            val ingredients = ""
            val cocktail = Cocktail(
                name = cocktailObj.getString("strDrink"),
                instructions = "Search it by name",
                imageUrl = cocktailObj.getString("strDrinkThumb"),
                ingredients = emptyList()
            )
            cocktailsList.add(cocktail)
        }
    }

    fun parseJson() {
        cocktailsList = mutableListOf<Cocktail>()
        // Преобразование строки JSON в JSONObject
        val jsonObject = JSONObject(jsonData)
        val drinks = jsonObject.getJSONArray("drinks")
        for (i in 0 until drinks.length()) {
            val cocktailObj = drinks.getJSONObject(i)
            val ingredients = mutableListOf<String>()
            for (j in 1..12) {
                val ingredient = cocktailObj.getString("strIngredient$j")
                if (/*ingredient.isNotEmpty() && */ingredient != "null") {
                    ingredients.add(ingredient)
                }
            }
            val cocktail = Cocktail(
                name = cocktailObj.getString("strDrink"),
                instructions = cocktailObj.getString("strInstructions"),
                imageUrl = cocktailObj.getString("strDrinkThumb"),
                ingredients = ingredients
            )
            cocktailsList.add(cocktail)
        }
        // Теперь у вас есть список коктейлей, который можно использовать для отображения в RecyclerView
    }
}

