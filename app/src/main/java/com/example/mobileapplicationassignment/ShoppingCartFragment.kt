package com.example.mobileapplicationassignment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplicationassignment.data.Product
import com.example.mobileapplicationassignment.dataAdapter.CartAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ShoppingCartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShoppingCartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbRef : DatabaseReference

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
        var view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        val productList : List<Product> = listOf(
            Product("John", "john@gmail.com", true, "good", 10,),
            Product("John", "john@gmail.com", true, "good", 10,),
            Product("John", "john@gmail.com", true, "good", 10,),
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.CartRecycleView)
        recyclerView.adapter = CartAdapter(productList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
//        var productImage: ImageView = view.findViewById(R.id.CartProductImg)
//
//
//        val database = FirebaseDatabase.getInstance()
//        val reference = database.getReference("Product") // Replace "Product" with your root node name
//
//        reference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val products = mutableListOf<Product>()  // Create a list to store products
//
//                for (childSnapshot in dataSnapshot.children) {
//                    val productMap = childSnapshot.value as HashMap<*, *>  // Cast to HashMap
//
//                    // Handle potential null values in the map
//                    val id = productMap["id"] as String?
//                    val name = productMap["name"] as String?
//                    val status = productMap["status"] as Boolean?  // Assuming boolean values are stored correctly
//                    val description = productMap["description"] as String?
//                    val price = productMap["price"] as Int?  // Assuming integer values are stored correctly
//                    val image = productMap["image"] as String?
//
//                    // Create Product object only if all required fields have values (optional)
//                    if (id != null && name != null && description != null && price != null && image != null) {
//                        val product = Product(id, name, status ?: true, description, price, image)  // Use default value if status is null
//                        products.add(product)
//                    } else {
//                        // Handle cases where some data is missing (optional)
//                        Log.w("Firebase", "Product data incomplete for child: ${childSnapshot.key}")
//                    }
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.w("Firebase", "Error retrieving data", databaseError.toException())
//            }
//        })

//        val Pid = arguments?.getString("ProductId").toString()
//
//        dbRef = FirebaseDatabase.getInstance().getReference("Product")
//        dbRef.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()) {
//                    var imgP = snapshot.child(Pid).child("ProductImage").getValue()
//                    var img = imgP.toString()
////                    var stuId = snapshot.child(name).child("ProductId").getValue()
//                    var imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(img)
//                    val ONE_MEGABYTE: Long = 1024 * 1024
//                    imgRef.getBytes(ONE_MEGABYTE)
//                        .addOnSuccessListener { bytes ->
//                            // Convert the bytes to a Bitmap
//                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
////                            // Display the Bitmap in an ImageView
//                            //imgPhoto.setImageBitmap(bitmap)
//                            productImage.setImageBitmap(bitmap)
//                        }
//
//
//                }
//
//
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
//            }
//        })

                return view
            }

}