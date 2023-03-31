package fr.isen.legrand.androiderestaurant

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

class DetailPlatActivity : AppCompatActivity() {

    private lateinit var quantitySpinner: Spinner
    private lateinit var priceButton: Button
    private lateinit var selectedItem: Items
    private var cart = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_plat)

        selectedItem = intent.getSerializableExtra("selected_item") as Items
        Log.d("DetailPlatActivity", "selectedItem: $selectedItem")

        // Récupérer les éléments de la vue en utilisant les nouveaux IDs
        val imageView = findViewById<ImageView>(R.id.plateImage)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val plateName = findViewById<TextView>(R.id.plateName)
        val plateDescription = findViewById<TextView>(R.id.plateDescription)

        // Afficher les images du plat dans le ViewPager
        val imageAdapter = ImageViewAdapter(this, selectedItem.images)
        viewPager.adapter = imageAdapter

        // Afficher la première image du plat dans l'ImageView
        selectedItem.images.firstOrNull()?.let {
            Picasso.get().load(it).into(imageView)
        }

        // Mettre à jour les TextView avec les informations du plat sélectionné
        plateName.text = selectedItem.nameFr
        plateDescription.text = selectedItem.ingredients.joinToString(", ") { it.nameFr.orEmpty() }

        // Ajouter le sélecteur de quantité
        quantitySpinner = findViewById(R.id.quantitySpinner)
        val quantityAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.quantities_array,
            android.R.layout.simple_spinner_item
        )
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        quantitySpinner.adapter = quantityAdapter

        // Ajouter le bouton de prix
        priceButton = findViewById(R.id.priceButton)
        priceButton.setOnClickListener {
            val quantity = quantitySpinner.selectedItem.toString().toInt()
            val price = selectedItem.prices.firstOrNull()?.price ?: "0"
            val totalPrice = quantity * price.toInt()

            // Ajouter l'article au panier
            cart.add(selectedItem.nameFr?.let { CartItem(it, quantity, totalPrice) } ?: return@setOnClickListener)
            saveCart()

            // Afficher un message de confirmation
            val message =
                "$quantity x ${selectedItem.nameFr} ajouté(s) au panier. Total: $totalPrice €"
            AlertDialog.Builder(this)
                .setTitle("Ajout au panier")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        }

        // Charger le panier précédemment sauvegardé
        loadCart()
    }

    /**
     * Sauvegarde le contenu du panier dans un fichier JSON.
     */
    private fun saveCart() {
        val gson = Gson()
        val json = gson.toJson(cart)
        val file = getFileStreamPath("cart.json")

        try {
            openFileOutput(file.name, MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Charge le contenu du panier depuis le fichier JSON.
     */
    private fun loadCart() {
        val gson = Gson()
        val file = getFileStreamPath("cart.json")

        if (file.exists()) {
            try {
                val json = openFileInput(file.name).bufferedReader().use {
                    it.readText()
                }
                val itemType = object : TypeToken<MutableList<CartItem>>() {}.type
                cart = gson.fromJson(json, itemType)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}