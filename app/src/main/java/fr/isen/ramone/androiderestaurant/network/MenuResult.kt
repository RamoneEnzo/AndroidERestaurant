package fr.isen.ramone.androiderestaurant.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MenuResult (val data: List<Category>){}

class Category (@SerializedName("name_fr") val name:String, val items:List<Item>){}

class Item (
        @SerializedName("name_fr") val name:String,
        val images:List<String>,
        val ingredients:List<Ingredient>,
        val prices:List<Price>
): Serializable{

    fun getPrice() = prices[0].price + "€"
    fun getPriceItem() = prices[0].price
    fun getFormattedPrice() = prices[0].price + "€"
    fun getImage() = if (images.isNotEmpty() && images[0].isNotEmpty()) {
        images[0]
    } else {
        null
    }

    fun getAllPictures() = if (images.isNotEmpty() && images.any { it.isNotEmpty() }) {
        images.filter { it.isNotEmpty() }
    } else {
        null
    }

    fun getThumbnailUrl(): String? {
        return if (images.isNotEmpty() && images[0].isNotEmpty()) {
            images[0]
        } else {
            null
        }
    }
}

class Ingredient (@SerializedName("name_fr") val name:String){}

class Price (val price:String){}

class NetworkConstant {

    companion object {
        const val BASE_URL = "http://test.api.catering.bluecodegames.com/"
        const val PATH_MENU = "menu"

        const val ID_SHOP = "id_shop"
    }
}