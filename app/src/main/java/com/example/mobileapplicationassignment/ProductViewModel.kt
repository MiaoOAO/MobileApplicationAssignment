package com.example.mobileapplicationassignment

import androidx.lifecycle.ViewModel

class ProductViewModel: ViewModel() {
    private var id:String = ""

    fun getId(): String{
        return id

    }

    fun setId(pId:String){
        id = pId
    }

    fun clearId(){
        id = ""

    }

}