package com.example.mobileapplicationassignment.dataAdapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val button: Button = itemView.findViewById(R.id.deleteCart)

//        init {
//            button.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    listener.onButtonClick(position)
//                }
//            }
//        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_view_holder, parent, false )

        return CartViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = productList[position]
        if (currentItem.image.isNotEmpty()) {
            var imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.image)
            //holder.cName.id = currentItem.id
            holder.cName.text = currentItem.name
            //holder.vDesc.text = currentItem.description
            holder.cPrice.text = currentItem.price.toString()
//            if(currentItem.status == true){
//                holder.vStatus.text = "Unsold"
//            }else{
//                holder.vStatus.text = "Sold"
//            }
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

//    interface ButtonClickListener {
//        fun onButtonClick(position: Int)
//    }
}