package fr.isen.ramone.androiderestaurant.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import fr.isen.ramone.androiderestaurant.R
import fr.isen.ramone.androiderestaurant.basket.Basket
import fr.isen.ramone.androiderestaurant.basket.BasketItem
import fr.isen.ramone.androiderestaurant.databinding.ActivityDetailBinding
import fr.isen.ramone.androiderestaurant.network.Item
import kotlin.math.max

class DetailActivity : AppCompatActivity() {
    companion object {
        const val DISH_EXTRA = "DISH_EXTRA"
    }

    lateinit var binding: ActivityDetailBinding
    private var itemCount = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dish = intent.getSerializableExtra(DISH_EXTRA) as? Item
        dish?.let {
            setupView(it)
        }
    }

    private fun setupView(dish: Item) {
        binding.dishTitleTextView.text = dish.name
        binding.ingredientTextView.text = dish.ingredients.map { it.name }?.joinToString(", ")
        binding.viewPager.adapter = PhotoAdapter(this, dish.images)
        refreshShop(dish)

        binding.less.setOnClickListener {
            itemCount = max(1, itemCount - 1)
            refreshShop(dish)
        }

        binding.more.setOnClickListener {
            itemCount += 1
            refreshShop(dish)
        }

        binding.shopButton.setOnClickListener {
            addToBasket(dish, itemCount)
        }
    }

    private fun refreshShop(dish: Item) {
        val price = itemCount * dish.prices.first().price.toFloat()
        binding.itemCount.text = itemCount.toString()
        binding.shopButton.text = "${"Total"} $price€"
    }

    private fun addToBasket(dish: Item, count: Int) {
        val basket = Basket.getBasket(this)
        basket.addItem(BasketItem(dish, count))
        basket.save(this)
        val json = GsonBuilder().create().toJson(basket)
        Log.d("basket", json)
    }
}