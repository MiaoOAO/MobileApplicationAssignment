package com.example.mobileapplicationassignment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbRef : DatabaseReference
    private var galleryUri: Uri? = null
    private lateinit var storageRef : StorageReference
    private lateinit var mImg:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_edit, container, false)
        mImg= view.findViewById(R.id.mImgPhoto)
        var mId:TextView = view.findViewById(R.id.mEditId)
        var mName:TextView = view.findViewById(R.id.mEditName)
        var cPassword:TextView = view.findViewById(R.id.changePassword)
        var conPassword:TextView = view.findViewById(R.id.confirmPassword)
        var btnMod:Button = view.findViewById(R.id.btnModify)
        val id = arguments?.getString("id").toString()
        var backButton = view.findViewById<FloatingActionButton>(R.id.EBackButton)

        storageRef = FirebaseStorage.getInstance().reference
        // Inflate the layout for this fragment

        mImg.setOnClickListener(){
            galleryLauncher.launch("image/*")
        }

        backButton.setOnClickListener{
            val fragment = ProfileFragment()
            val bundle = Bundle()
            bundle.putString("id",id)
            fragment.arguments = bundle
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentContainerView, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()

        }

        dbRef = FirebaseDatabase.getInstance().getReference("User")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var imgP = snapshot.child(id).child("profileImage").getValue()
                var img = imgP.toString()
                var stuId = snapshot.child(id).child("id").getValue()
                var stuName = snapshot.child(id).child("name").getValue()
                var imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(img)
                val ONE_MEGABYTE: Long = 5120 * 5120
                imgRef.getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener { bytes ->
                        // Convert the bytes to a Bitmap
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                            // Display the Bitmap in an ImageView
                        //imgPhoto.setImageBitmap(bitmap)
                        mImg.setImageBitmap(bitmap)
                    }

                mId.text = stuId.toString()
                mName.text = stuName.toString()


            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
            }
        })

        btnMod.setOnClickListener(){
            var cP =cPassword.text.toString()
            var conP = conPassword.text.toString()
            if( cP == conP && mName.text.toString().isNotEmpty()){
                val imgId = UUID.randomUUID()
                val imgRef = storageRef.child("image/${imgId}.png")
                if(galleryUri != null){
                    imgRef.putFile(galleryUri!!)
                        .addOnSuccessListener { taskSnapshot ->
                            // Get download URL
                            imgRef.downloadUrl.addOnSuccessListener { uri ->
                                dbRef.child(id).child("profileImage").setValue(uri.toString())
                            }
                        }

                }
                if(cP.isNotEmpty()){
                    dbRef.child(id).child("password").setValue(cP)
                }
                dbRef.child(id).child("name").setValue(mName.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "edit successful", Toast.LENGTH_LONG).show()
                        val fragment = ProfileFragment()
                        val bundle = Bundle()
                        bundle.putString("id",id)
                        fragment.arguments = bundle
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(R.id.fragmentContainerView, fragment)
                        transaction?.addToBackStack(null)
                        transaction?.commit()
                    }
                    .addOnFailureListener{
                        Toast.makeText(requireContext(), "Fail to edit profile", Toast.LENGTH_LONG).show()
                    }

            }else{

                Toast.makeText(requireContext(), "Please don't leave out blank", Toast.LENGTH_LONG).show()
            }







        }



        return view
    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        galleryUri = it!!
        try{
            mImg.setImageURI(galleryUri)
        }catch(e:Exception){
            e.printStackTrace()
        }

    }


}