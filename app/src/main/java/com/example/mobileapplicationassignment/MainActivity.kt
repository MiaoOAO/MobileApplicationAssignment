package com.example.mobileapplicationassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var dbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Hi
        //testing
        //lol
        //XinYi test test test
        //Hi my name is wong chen yong
        dbRef = FirebaseDatabase.getInstance().getReference("Product")
        dbRef.child("Testing").setValue("Hello World")
            .addOnCompleteListener{
                Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Error ${it.toString()}", Toast.LENGTH_LONG).show()
            }

    }
}