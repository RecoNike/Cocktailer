package com.recon.coctailer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {


    lateinit var volleyRequest: VolleyRequest
    lateinit var url: String
    lateinit var name: TextView
    lateinit var ingredients: TextView
    lateinit var imageView: ImageView
    lateinit var tryAgainTvBt: TextView
    lateinit var cookingTv: TextView
    lateinit var backTvBt: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        volleyRequest = VolleyRequest(this)

        url = "https://www.thecocktaildb.com/api/json/v1/1/random.php"

        name = findViewById(R.id.coctailNameTv)

        ingredients = findViewById(R.id.ingridientsTV)
        imageView = findViewById(R.id.imageView)
        tryAgainTvBt = findViewById(R.id.tryAgainTvBt)
        cookingTv = findViewById(R.id.cookingTv)
        backTvBt = findViewById(R.id.backTvBt)


        imageView.setBackgroundResource(R.drawable.baseline_cyclone_24)
        getRandom()

        tryAgainTvBt.setOnClickListener {
            getRandom()
        }
        backTvBt.setOnClickListener{
            val i = Intent(this, SearchActivity::class.java)
            startActivity(i)
            finish()
        }

    }

    fun getRandom(){
        lifecycleScope.launch{
            try{
                val response = volleyRequest.getJsonFromUrl(url)
                val data = response.toString()
                val jsonObject = JSONObject(data)
                val drinks = jsonObject.getJSONArray("drinks")

                var ingrString: String = "Ingredients\n\n"
                if (drinks.length() > 0){
                    val coctail = drinks.getJSONObject(0)
                    name.text = coctail.getString("strDrink")
                    val coctailImageUrl = coctail.getString("strDrinkThumb")
                    cookingTv.text = coctail.getString("strInstructions")
                    Glide.with(this@MainActivity).load(coctailImageUrl).into(imageView)

                    for (i in 1..12){
                        val ingredientKey = "strIngredient$i"
                        val measureKey = "strMeasure$i"
                        val ingredient = coctail.getString(ingredientKey)
                        val measure = coctail.getString(measureKey)
                        // If ingredient is not empty, add it to the string
                        if (ingredient != "null") {
                            ingrString += "► $ingredient - $measure\n"
                        }

                    }
                    ingredients.text = ingrString
                    ingredients.visibility = View.VISIBLE
                    name.visibility = View.VISIBLE
                    cookingTv.visibility = View.VISIBLE
                }

            } catch (e: Error){

            }
        }
    }

}