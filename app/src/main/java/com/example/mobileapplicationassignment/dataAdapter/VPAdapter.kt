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

class VPAdapter (private val productList: List<Product>,private val listener: ButtonClickListener) : RecyclerView.Adapter <VPAdapter.MyViewHolder>(){

    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val vName : TextView = itemView.findViewById(R.id.vPName)
        val vImage: ImageView = itemView.findViewById(R.id.vPPhoto)
        val vId : TextView = itemView.findViewById(R.id.vPId)
        val vDesc:TextView = itemView.findViewById(R.id.vPDescription)
        val vPrice:TextView = itemView.findViewById(R.id.vPPrice)
        val button: Button = itemView.findViewById(R.id.deleteBtn)
        val vStatus: TextView = itemView.findViewById(R.id.vPStatus)
        init {
            button.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onButtonClick(position)
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_product_view_holder, parent, false )

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
            if(currentItem.status == true){
                holder.vStatus.text = "Unsold"
            }else{
                holder.vStatus.text = "Sold"
            }
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
    interface ButtonClickListener {
        fun onButtonClick(position: Int)
    }


}