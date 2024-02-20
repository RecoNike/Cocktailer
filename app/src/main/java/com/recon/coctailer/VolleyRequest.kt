package com.recon.coctailer

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class VolleyRequest(private val context: Context) {
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    suspend fun getJsonFromUrl(url: String): JSONObject = withContext(Dispatchers.IO){
        suspendCoroutine { continuation ->
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                {
                    response ->
                    continuation.resume(response)
                },
                { error ->
                    continuation.resumeWithException(Exception(error.message))
                }
            )
            requestQueue.add(jsonObjectRequest)
        }
    }
}