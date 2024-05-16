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

class FavoriteAdapter(private val productList: List<Product>):RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val fName : TextView = itemView.findViewById(R.id.FavProductName)
        val fPrice: TextView = itemView.findViewById(R.id.FavProductPrice)
        val fImage: ImageView =itemView.findViewById(R.id.FavProductImg)
        val button: Button = itemView.findViewById(R.id.FavDelete)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.favorite_view_holder, parent, false )

        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val currentItem = productList[position]
        if (currentItem.image.isNotEmpty()) {
            var imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.image)
            //holder.cName.id = currentItem.id
            holder.fName.text = currentItem.name
            //holder.vDesc.text = currentItem.description
            holder.fPrice.text = currentItem.price.toString()
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
                    holder.fImage.setImageBitmap(bitmap)
                }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}