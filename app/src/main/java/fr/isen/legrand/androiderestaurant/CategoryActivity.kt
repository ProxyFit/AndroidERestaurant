package fr.isen.legrand.androiderestaurant

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.isen.legrand.androiderestaurant.databinding.MenuActivityBinding
import org.json.JSONObject

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: MenuActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MenuActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (intent.getStringExtra("categorie")) {
            CATEGORY_ENTREES -> setTitle(getString(R.string.entrees))
            CATEGORY_PLATS -> setTitle(getString(R.string.plats))
            CATEGORY_DESSERTS -> setTitle(getString(R.string.desserts))
        }

        fetchMenuItems()
    }

    private fun fetchMenuItems() {
        val url = "http://test.api.catering.bluecodegames.com/menu"
        val requestBody = JSONObject().apply {
            put("id_shop", 1)
        }

        val request = JsonObjectRequest(Request.Method.POST, url, requestBody,
            { response ->
                Log.d("bla", "Complete JSON Response: $response")
                val items = parseItems(response)
                parseMenuItems(items)
            },
            { error ->
                Log.d("err", error.toString())
            })

        Volley.newRequestQueue(this).add(request)
    }

    private fun parseItems(response: JSONObject): List<Items> {
        val data = response.getJSONArray("data").toString()
        val itemType = object : TypeToken<List<Data>>() {}.type
        val dataList: List<Data> = Gson().fromJson(data, itemType)

        return dataList.flatMap { it.items }
    }

    private fun getMenuList(items: List<Items>, category: String): List<Items> {
        return items.filter { it.categNameFr == category }
    }

    private fun parseMenuItems(items: List<Items>) {
        val menuItems = when (intent.getStringExtra("categorie")) {
            CATEGORY_ENTREES -> getMenuList(items, getString(R.string.entrees))
            CATEGORY_PLATS -> getMenuList(items, getString(R.string.plats))
            CATEGORY_DESSERTS -> getMenuList(items, getString(R.string.desserts))
            else -> emptyList()
        }

        binding.menuRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CategoryActivity)
            adapter = CategoryAdapter(this@CategoryActivity, menuItems)
        }
    }

    companion object {
        const val CATEGORY_ENTREES = "entrees"
        const val CATEGORY_PLATS = "plats"
        const val CATEGORY_DESSERTS = "desserts"
    }
}
