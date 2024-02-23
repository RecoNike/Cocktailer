package com.recon.coctailer

data class Cocktail(
    val name: String,
    val instructions: String,
    val imageUrl: String,
    val ingredients: List<String>
)
