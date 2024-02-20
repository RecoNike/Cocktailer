package com.recon.coctailer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val volleyRequest = VolleyRequest(this)
        val url = "https://www.thecocktaildb.com/api/json/v1/1/random.php"
        val tv: TextView = findViewById(R.id.textView)
        val name: TextView = findViewById(R.id.coctailNameTv)
        val ingredients: TextView = findViewById(R.id.ingridientsTV)
        val imageView: ImageView = findViewById(R.id.imageView)


        lifecycleScope.launch{
            try{
                val response = volleyRequest.getJsonFromUrl(url)
                val data = response.toString()
                val jsonObject = JSONObject(data)
                val drinks = jsonObject.getJSONArray("drinks")

                var ingrString: String = "Ingredients\n"
                if (drinks.length() > 0){
                    val coctail = drinks.getJSONObject(0)
                    name.text = coctail.getString("strDrink")
//                    ingredients.text = coctail.getString("strInstructions")
                    val coctailImageUrl = coctail.getString("strDrinkThumb")
                    Glide.with(this@MainActivity).load(coctailImageUrl).into(imageView)

                    for (i in 1..12){
                        val ingredientKey = "strIngredient$i"
                        val measureKey = "strMeasure$i"
                        val ingredient = coctail.getString(ingredientKey)
                        val measure = coctail.getString(measureKey)
                        // If ingredient is not empty, add it to the string
                        if (ingredient != "null") {
                            ingrString += "$ingredient - $measure\n"
                        }
                    }
                    ingredients.text = ingrString
                }

            } catch (e: Error){

            }
        }
    }
}