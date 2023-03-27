// CategoryAdapter.kt

package fr.isen.legrand.androiderestaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

// Définition de la classe "CategoryAdapter" qui étend la classe "RecyclerView.Adapter"
class CategoryAdapter(private val context: Context, private val menuList: List<Items>) :
    RecyclerView.Adapter<CategoryAdapter.MenuViewHolder>() {

    // Classe interne qui contient les éléments de la vue
    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuTitle: TextView = view.findViewById(R.id.menu_title)
        val menuImage: ImageView = view.findViewById(R.id.menu_image)
    }

    // Cette méthode est appelée lorsque le RecyclerView a besoin d'un nouvel élément de vue
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    // Cette méthode est appelée pour remplir les éléments de la vue avec les données du menu
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuList[position]
        holder.menuTitle.text = menuItem.nameFr

        if (menuItem.images.isNotEmpty()) {
            Picasso.get()
                .load(menuItem.images[0])
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.menuImage)
        } else {
            holder.menuImage.setImageResource(R.drawable.error_image)
        }
    }

    // Cette méthode retourne le nombre d'éléments de la liste de menus
    override fun getItemCount(): Int {
        return menuList.size
    }
}
