// MenuAdapter.kt

package fr.isen.legrand.androiderestaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Définition de la classe "MenuAdapter" qui étend la classe "RecyclerView.Adapter"
class CategoryAdapter(private val context: Context, private val menuList: List<String>) :
    RecyclerView.Adapter<CategoryAdapter.MenuViewHolder>() {

    // Classe interne qui contient les éléments de la vue
    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuTitle: TextView = view.findViewById(R.id.menu_title)
    }

    // Cette méthode est appelée lorsque le RecyclerView a besoin d'un nouvel élément de vue
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    // Cette méthode est appelée pour remplir les éléments de la vue avec les données du menu
    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.menuTitle.text = menuList[position]
    }

    // Cette méthode retourne le nombre d'éléments de la liste de menus
    override fun getItemCount(): Int {
        return menuList.size
    }
}
