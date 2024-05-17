package com.example.mobileapplicationassignment

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapplicationassignment.data.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var dbRef : DatabaseReference
    private lateinit var fRef : DatabaseReference
    private lateinit var cdbRef:DatabaseReference
    private lateinit var imgRef: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var userid:TextView = findViewById(R.id.enterStudentId)
        var password:TextView = findViewById(R.id.enterPassword)
        var submit:Button = findViewById(R.id.loginBtn)
        var test:TextView = findViewById(R.id.textView)
        var btnR: ImageButton = findViewById(R.id.registerBtn)
        cdbRef = FirebaseDatabase.getInstance().getReference("Cart")
        cdbRef.removeValue()
        fRef = FirebaseDatabase.getInstance().getReference("User")
        fRef.child("2204107").child("password").setValue("1234")
        fRef.child("2204107").child("name").setValue("Wong Cheng Yi")
        fRef.child("2204107").child("profileImage").setValue("gs://campus-marketplace-8cc1c.appspot.com/image/Yashiro.png")
        fRef.child("2204107").child("id").setValue("2204107")
        var product = Product("1","Gaming Chair",true,"condition 80% new, bought 2 months ago, TTracing brand",120,"gs://campus-marketplace-8cc1c.appspot.com/Product1.png")
        dbRef = FirebaseDatabase.getInstance().getReference("User")

        btnR.setOnClickListener{
            var intent = Intent(this,Register::class.java)
            startActivity(intent)

        }

        submit.setOnClickListener {
            val vUserId = userid.text.toString()
            val vPassword = password.text.toString()


            // Validate username and password (this is a basic example, you should use secure methods)
            if (vUserId != "" && vPassword != "") {
                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val storedPassword = dataSnapshot.child(vUserId).child("password").getValue()
                        if (vPassword == storedPassword) {
                            // Login successful


                            Toast.makeText(this@MainActivity, "Valid", Toast.LENGTH_LONG).show()
                            var intent = Intent(this@MainActivity,MainMenu::class.java)
                            intent.putExtra("Fragment", "Home") // or any identifier for the fragment
                            intent.putExtra("Id", vUserId)
                            startActivity(intent)

                        } else {
                            // Login failed
                            Toast.makeText(this@MainActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                        Toast.makeText(this@MainActivity, "Database error", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Empty username or password
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
        }
        //Hi
        //testing
        //lol
        //XinYi test test test
        //Hi my name is wong chen yong
        //var imgPhoto:ImageView
        //dbRef = FirebaseDatabase.getInstance().getReference("Product")
        //dbRef.child("Testing").setValue("Bye Bye")
        //    .addOnCompleteListener{
        //        //Toast.makeText(this, "Data saved", Toast.LENGTH_LONG).show()
        //    }
        //    .addOnFailureListener{
        //        Toast.makeText(this, "Error ${it.toString()}", Toast.LENGTH_LONG).show()
        //    }
        //imgRef = FirebaseStorage.getInstance().reference
        //imgRef.child("image/Yashiro.png")
        //val file = File.createTempFile("temp","png")

        //imgRef.getFile(file)
        //    .addOnSuccessListener{
        //        imgPhoto = findViewById(R.id.imageView)
        //        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        //        imgPhoto.setImageBitmap(bitmap)
        //        Toast.makeText(this, "File Retrived", Toast.LENGTH_LONG).show()
        //    }

}
