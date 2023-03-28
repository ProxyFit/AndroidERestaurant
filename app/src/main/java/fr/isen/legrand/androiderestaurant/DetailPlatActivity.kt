package fr.isen.legrand.androiderestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class DetailPlatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_plat)

        val selectedItem = intent.getSerializableExtra("selected_item") as Items
        Log.d("DetailPlatActivity", "selectedItem: $selectedItem")


        // Récupérer les éléments de la vue en utilisant les nouveaux IDs
        val imageView = findViewById<ImageView>(R.id.plateImage)
        val plateName = findViewById<TextView>(R.id.plateName)
        val plateDescription = findViewById<TextView>(R.id.plateDescription)

        if (selectedItem.images.isNotEmpty()) {
            Picasso.get()
                .load(selectedItem.images[0])
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.error_image)
        }

        // Mettre à jour les TextView avec les informations du plat sélectionné
        plateName.text = selectedItem.nameFr
        plateDescription.text = selectedItem.ingredients.joinToString(", ") { it.nameFr.orEmpty() }
    }
}
