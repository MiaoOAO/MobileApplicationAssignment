package com.example.mobileapplicationassignment.data
import androidx.room.PrimaryKey
data class Product (@PrimaryKey(autoGenerate = true) var id :String="", var name:String="", var status:Boolean=true ,var description: String="",var price:Int=0 ,var image:String="")