package fr.isen.ramone.androiderestaurant.category

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.ramone.androiderestaurant.HomeActivity
import fr.isen.ramone.androiderestaurant.R
import fr.isen.ramone.androiderestaurant.databinding.ActivityCategoryBinding
import fr.isen.ramone.androiderestaurant.network.Item
import fr.isen.ramone.androiderestaurant.network.MenuResult
import fr.isen.ramone.androiderestaurant.network.NetworkConstant
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
        resultFromCache()?.let{
            //la requete est en cache
            parseResult(it, selectItem)
        }?: run {
            //la requete n'est pas en cache
            val queue = Volley.newRequestQueue(this)
            val url = NetworkConstant.BASE_URL + NetworkConstant.PATH_MENU

            val jsonData = JSONObject()
            jsonData.put(NetworkConstant.ID_SHOP, "1")

            var request = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonData,
                    { response ->
                        cacheResult(response.toString())
                        parseResult(response.toString(), selectItem)
                    },
                    { error ->
                        error.message?.let {
                            Log.d("request", it)
                        } ?: run {
                            Log.d("request", error.toString())
                        }
                    }
            )
            queue.add(request)
        }
    }

    private fun loadList(item: List<Item>?) {
        //val titles = list.map{it.name} //conversion
        item?.let {
            val adapter = CategoryAdapter(it) { dish ->
                Log.d("dish", "selected dish ${dish.name}")
            }
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = adapter
        }
    }

    private fun resultFromCache():String? {
        val sharedPreferences = getSharedPreferences(UPN, Context.MODE_PRIVATE)
        return sharedPreferences.getString(RC, null)
    }

    private fun cacheResult(response: String) {
        val sharedPreferences = getSharedPreferences(UPN, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(RC, response)
        editor.apply()
    }

    private fun parseResult(response: String, selectedItem: ItemType?) {
        val menuResult = GsonBuilder().create().fromJson(response.toString(), MenuResult::class.java)
        val items = menuResult.data.firstOrNull(){it.name == ItemType.categoryTitle(selectedItem)}
        loadList(items?.items)
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

    companion object {
        const val UPN ="USER_PREFERENCES_NAME"
        const val RC = "REQUEST_CACHE"
    }
}