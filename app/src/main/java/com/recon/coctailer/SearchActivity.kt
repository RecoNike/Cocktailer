package com.recon.coctailer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
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
        searchByCategoryBt.setOnClickListener{
            searchByCat(choosenCat)
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
            if(name.isNotBlank()){
                try {
                    name.replace(Regex("[^\\wа-яёА-ЯЁ ]"), "")
                    val url = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" + name
                    val response = volleyRequest.getJsonFromUrl(url)
                    val data = response.toString()
                    val jsonObject = JSONObject(data)
                    Log.d("", jsonObject.toString())

                    if(data != "{\"drinks\":null}") {
                        val intent = Intent(this@SearchActivity, ResultActivity::class.java)
                        intent.putExtra("cocktailData", data)
                        startActivity(intent)
                    } else {
                        hideKeyboard(nameField)
                        Snackbar.make(
                            this@SearchActivity,
                            searchByNameBt,
                            "Nothing found",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Error) {

                }
            }
        }
    }
    fun searchByCat(cat: String){
        lifecycleScope.launch{
            if(cat.isNotBlank()){
                try {
                    val formattedCat = cat.replace(" ", "_")
                    val url = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=" + formattedCat
                    val response = volleyRequest.getJsonFromUrl(url)
                    val data = response.toString()
                    val jsonObject = JSONObject(data)
                    Log.d("", jsonObject.toString())

                    // Создайте Intent для перехода к новой активити
                    val intent = Intent(this@SearchActivity, ResultActivity::class.java)

                    // Поместите JSON как дополнение к Intent
                    intent.putExtra("catData", data)

                    // Запустите новую активити
                    startActivity(intent)
                } catch (e: Error) {

                }
            }
        }
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}