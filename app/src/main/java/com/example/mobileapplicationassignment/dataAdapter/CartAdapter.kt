package com.example.mobileapplicationassignment.dataAdapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.data.Product
import com.google.firebase.storage.FirebaseStorage

class CartAdapter (private val productList: List<Product>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    class CartViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val cName : TextView = itemView.findViewById(R.id.CartProductName)
        val cPrice: TextView = itemView.findViewById(R.id.CartProductPrice)
        val cImage: ImageView =itemView.findViewById(R.id.CartProductImg)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_view_holder, parent, false )

        return CartAdapter.CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        val currentItem = productList[position]
        if (currentItem.image.isNotEmpty()) {
            var imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.image)
            holder.cName.text = currentItem.name
            holder.cPrice.text = currentItem.price.toString()

            val ONE_MEGABYTE: Long = 1024 * 1024
            imgRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { bytes ->
                    // Convert the bytes to a Bitmap
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    // Display the Bitmap in an ImageView
                    holder.cImage.setImageBitmap(bitmap)
                }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}