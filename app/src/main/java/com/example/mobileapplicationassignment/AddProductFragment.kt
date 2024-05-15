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
import com.example.mobileapplicationassignment.data.Product
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
 * Use the [AddProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddProductFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbRef : DatabaseReference
    private lateinit var pdbRef : DatabaseReference
    private lateinit var  galleryUri : Uri
    private lateinit var storageRef : StorageReference
    private lateinit var aImg: ImageView

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
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_add_product, container, false)
        aImg= view.findViewById(R.id.imgProduct)
        var aPrice: TextView = view.findViewById(R.id.editPrice)
        var aName: TextView = view.findViewById(R.id.editName)
        var aDesc:TextView = view.findViewById(R.id.editDescription)
        var btnMod: Button = view.findViewById(R.id.btnAdd)
        val id = arguments?.getString("id").toString()
        storageRef = FirebaseStorage.getInstance().reference
        // Inflate the layout for this fragment

        aImg.setOnClickListener(){
            galleryLauncher.launch("image/*")
        }

        dbRef = FirebaseDatabase.getInstance().getReference("User")
        pdbRef = FirebaseDatabase.getInstance().getReference("Product")


        btnMod.setOnClickListener(){
            if( aPrice.text.toString().isNotEmpty() && aDesc.text.toString().isNotEmpty() && aName.text.toString().isNotEmpty()){
                val imgId = UUID.randomUUID()
                val imgRef = storageRef.child("image/${imgId}.png")
                imgRef.putFile(galleryUri)
                    .addOnSuccessListener { taskSnapshot ->
                        // Get download URL
                        imgRef.downloadUrl.addOnSuccessListener { uri ->
                            val rands = (0..1000).random()
                            var pId = rands
                            val product = Product(pId.toString(), aName.text.toString(), true, aDesc.text.toString(), aPrice.text.toString().toInt() ,uri.toString())
                            //user side
                            dbRef.child(id).child("Product").child(pId.toString()).setValue(product)
                            //product side
                            pdbRef.child(pId.toString()).setValue(product)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "add successful", Toast.LENGTH_LONG).show()
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
                                    Toast.makeText(requireContext(), "Fail to add product", Toast.LENGTH_LONG).show()
                                }

                        }
                    }

            }







        }

        return view
    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        galleryUri = it!!
        try{
            aImg.setImageURI(galleryUri)
        }catch(e:Exception){
            e.printStackTrace()
        }

    }


}