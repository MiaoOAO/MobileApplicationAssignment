package com.example.mobileapplicationassignment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class Register : AppCompatActivity() {
    private lateinit var dbRef : DatabaseReference
    private lateinit var  galleryUri : Uri
    private lateinit var storageRef : StorageReference
    private lateinit var rImg: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var backButton = findViewById<FloatingActionButton>(R.id.RBackButton)
        var rId: TextView = findViewById(R.id.rEditId)
        var rName: TextView = findViewById((R.id.rEditId))
        var rPassword: TextView = findViewById(R.id.rPassword)
        var conPassword: TextView = findViewById(R.id.rConfirmPassword)
        var btnR: Button = findViewById(R.id.btnRegister)
        rImg = findViewById(R.id.rImgPhoto)
        storageRef = FirebaseStorage.getInstance().reference

        rImg.setOnClickListener(){
            galleryLauncher.launch("image/*")
        }

        backButton.setOnClickListener{
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }

        btnR.setOnClickListener(){
            var rP =rPassword.text.toString()
            var conP = conPassword.text.toString()
            if( rP == conP && rName.text.toString().isNotEmpty() && rId.toString().isNotEmpty()){
                val imgId = UUID.randomUUID()
                val imgRef = storageRef.child("image/${imgId}.png")
                imgRef.putFile(galleryUri)
                    .addOnSuccessListener { taskSnapshot ->
                        // Get download URL
                        imgRef.downloadUrl.addOnSuccessListener { uri ->
                            var id = rId.toString()
                            dbRef.child(id).child("Id").setValue(id)
                            dbRef.child(id).child("Password").setValue(rP)
                            dbRef.child(id).child("Name").setValue(rName.text.toString())
                            dbRef.child(id).child("ProfileImage").setValue(uri.toString())
                                .addOnSuccessListener {
                                    Toast.makeText(this, "register successful", Toast.LENGTH_LONG).show()
                                    var intent = Intent(this,MainActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener{
                                    Toast.makeText(this, "Fail to edit profile", Toast.LENGTH_LONG).show()
                                }

                        }
                    }

            }else{
                Toast.makeText(this, "Please don't leave out blank", Toast.LENGTH_LONG).show()

            }







        }



    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it!!.toString().isNotEmpty()) {
            try {
                galleryUri = it!!
                rImg.setImageURI(galleryUri)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to set image from gallery", Toast.LENGTH_SHORT).show()
            }
        } else {

            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }

    }
}