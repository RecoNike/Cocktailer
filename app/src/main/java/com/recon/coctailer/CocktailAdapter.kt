package com.recon.coctailer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CocktailAdapter(private val cocktails: List<Cocktail>) : RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CocktailViewHolder(view)
    }

    override fun onBindViewHolder(holder: CocktailViewHolder, position: Int) {
        val cocktail = cocktails[position]
        holder.bind(cocktail)
    }

    override fun getItemCount(): Int {
        return cocktails.size
    }

    inner class CocktailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val ingredientsTextView: TextView = itemView.findViewById(R.id.ingredientsTextView)
        private val dialogHelper = DialogHelper(itemView.context)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val cocktail = cocktails[position] // Используем объект cocktail, который был передан в onBindViewHolder
                    dialogHelper.showNotificationDialog(
                        "How to make it:",
                        cocktail.instructions,
                        "",
                        null
                    )
                }
            }
        }

        fun bind(cocktail: Cocktail) {
            Glide.with(itemView.context).load(cocktail.imageUrl).into(imageView)
            nameTextView.text = cocktail.name
            ingredientsTextView.text = cocktail.ingredients.joinToString("\n")
        }
    }
}
