package fr.isen.legrand.androiderestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CategoryAdapter(private val context: Context, private val menuList: List<Items>) :
    RecyclerView.Adapter<CategoryAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuTitle: TextView = view.findViewById(R.id.menu_title)
        val menuPrice: TextView = view.findViewById(R.id.menu_price)
        val menuImage: ImageView = view.findViewById(R.id.menu_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        try {
            val currentItem = menuList[position]
            holder.menuTitle.text = currentItem.nameFr

            // Affichage du prix
            val price = currentItem.prices.firstOrNull()?.price ?: "N/A"
            holder.menuPrice.text = context.getString(R.string.price_format, price)

            // Chargement et affichage de l'image
            if (currentItem.images.isNotEmpty()) {
                Picasso.get().load(currentItem.images[0]).placeholder(R.drawable.placeholder_image).error(R.drawable.error_image).into(holder.menuImage)
            } else {
                holder.menuImage.setImageResource(R.drawable.error_image)
            }

            Log.d("CategoryAdapter", "Current item: ${currentItem.nameFr}")

            // OnClickListener pour ouvrir DetailPlatActivity
            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailPlatActivity::class.java)
                intent.putExtra("selected_item", currentItem)
                context.startActivity(intent)
            }
        } catch (e: IndexOutOfBoundsException) {
            Log.e("CategoryAdapter", "Index out of bounds at position $position")
        } catch (e: IllegalArgumentException) {
            Log.e("CategoryAdapter", "IllegalArgumentException: ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        Log.d("CategoryAdapter", "Menu list size: ${menuList.size}")
        return menuList.size
    }

}
