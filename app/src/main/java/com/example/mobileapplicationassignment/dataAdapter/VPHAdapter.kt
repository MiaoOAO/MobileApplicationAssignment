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

class VPHAdapter (private val productList: List<Product>) : RecyclerView.Adapter <VPHAdapter.MyViewHolder>(){

    class MyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val vName : TextView = itemView.findViewById(R.id.vPHName)
        val vImage: ImageView = itemView.findViewById(R.id.vPHPhoto)
        val vId : TextView = itemView.findViewById(R.id.vPHId)
        val vDesc: TextView = itemView.findViewById(R.id.vPHDescription)
        val vPrice: TextView = itemView.findViewById(R.id.vPHPrice)
        init {
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.purchase_history_view_holder, parent, false )

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productList[position]
        if (currentItem.image.isNotEmpty()) {
            var imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.image)
            holder.vId.text = currentItem.id
            holder.vName.text = currentItem.name
            holder.vDesc.text = currentItem.description
            holder.vPrice.text = currentItem.price.toString()
            val ONE_MEGABYTE: Long = 1024 * 1024
            imgRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { bytes ->
                    // Convert the bytes to a Bitmap
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    // Display the Bitmap in an ImageView
                    holder.vImage.setImageBitmap(bitmap)
                }
        }

    }



}