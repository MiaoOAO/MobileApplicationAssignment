package com.example.mobileapplicationassignment.dataAdapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplicationassignment.HomepageFragment
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.data.Product
import com.google.firebase.storage.FirebaseStorage

class ListAdapter(private val personList: List<Product>, private val listener: OnItemClickListener) : RecyclerView.Adapter <ListAdapter.MyViewHolder>(){

    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val image : ImageView = itemView.findViewById(R.id.image)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val tvRM : TextView = itemView.findViewById(R.id.tvRM)
        val productPrice : TextView = itemView.findViewById(R.id.productPrice)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            val position:Int = adapterPosition
            if (position != RecyclerView.NO_POSITION)
                listener.itemClick(position)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.homepage_view_holder, parent, false )

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = personList[position]
        if (currentItem.image.isNotEmpty()) {
            var imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(currentItem.image)
            holder.productName.text = currentItem.name
            holder.productPrice.text = currentItem.price.toString()
            val ONE_MEGABYTE: Long = 5120 * 5120
            imgRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener { bytes ->
                    // Convert the bytes to a Bitmap
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    // Display the Bitmap in an ImageView
                    holder.image.setImageBitmap(bitmap)
                }
        }

    }
    interface OnItemClickListener{
        fun itemClick(position: Int)
    }
}