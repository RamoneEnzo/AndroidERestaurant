package fr.isen.ramone.androiderestaurant.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Transformations.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.ramone.androiderestaurant.HomeActivity
import fr.isen.ramone.androiderestaurant.R
import fr.isen.ramone.androiderestaurant.databinding.ActivityCategoryBinding
import fr.isen.ramone.androiderestaurant.databinding.DishesCellBinding
import fr.isen.ramone.androiderestaurant.models.Ingredient
import fr.isen.ramone.androiderestaurant.models.Item
import fr.isen.ramone.androiderestaurant.models.MenuResult
import org.json.JSONObject

enum class ItemType {
    STARTER, MAIN, DESSERT;

    companion object {
        fun categoryTitle(item: ItemType?) : String {
            return when(item) {
                STARTER -> "Entrées"
                MAIN -> "Plats"
                DESSERT -> "Desserts"
                else -> ""
            }
        }
    }
}

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedItem = intent.getSerializableExtra(HomeActivity.CATEGORY_NAME) as? ItemType
        binding.categoryTitle.text = getCategoryTitle(selectedItem)

        loadList(listOf<Item>())
        makeRequest(selectedItem)

        Log.d("lifecycle", "onCreate")
    }

    private fun makeRequest(selectItem: ItemType?) {
        val queue = Volley.newRequestQueue(this)
        val jsondata= JSONObject()
        jsondata.put("id_shop", 1)
        val url = "http://test.api.catering.bluecodegames.com/menu"
        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsondata,
            { response ->
                //success
                Log.d("request", response.toString(2))
                val menu = GsonBuilder().create().fromJson(response.toString(), MenuResult::class.java)
                val items = menu.data.firstOrNull{
                    it.name == ItemType.categoryTitle(selectItem)}
                if(items != null)
                    loadList(items?.items)
                else
                    Log.d("CategoryActivity", "no category")
                /*menu.data.forEach{
                    Log.d("Request", it.name)
                }*/
            },
            { error ->
                //error
                error.message?.let {
                    Log.d("Request", it)
                } ?: run {
                    Log.d("Request", error.toString())
                }
                //Log.d("Request", error.localizedMessage)
            }
        )
        /*val request = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener { response ->
                //success
                Log.d("Request", response)
            },
            Response.ErrorListener { error ->
                //error
                Log.d("Request", error.localizedMessage )
            }
        )*/
                queue.add(request)
    }

    private fun loadList(item: List<Item>) {
        //val titles = list.map{it.name} //conversion
        item?.let {
            val adapter = CategoryAdapter(it) { dish ->
                Log.d("dish", "selected dish ${dish.name}")
            }
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = adapter
        }
    }

    private fun getCategoryTitle(item: ItemType?): String {
        return when(item) {
            ItemType.STARTER -> getString(R.string.starter)
            ItemType.MAIN -> getString(R.string.main)
            ItemType.DESSERT -> "Desserts"
            else -> ""
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycle", "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("lifecycle", "onRestart")
    }

    override fun onDestroy() {
        Log.d("lifecycle", "onDestroy")
        super.onDestroy()
    }
}