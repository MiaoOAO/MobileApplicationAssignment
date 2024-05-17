package com.example.mobileapplicationassignment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplicationassignment.data.Product
import com.example.mobileapplicationassignment.data.User
import com.example.mobileapplicationassignment.dataAdapter.ListAdapter
import com.example.mobileapplicationassignment.dataAdapter.VPAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var pdbRef : DatabaseReference
    private lateinit var dbRef : DatabaseReference
    private lateinit var cdbRef : DatabaseReference
    private var productList: ArrayList<Product> = arrayListOf()
    private lateinit var product: Product

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
        var view = inflater.inflate(R.layout.fragment_detail, container, false)
        var imageProduct: ImageView = view.findViewById(R.id.imageProduct)
        var name: TextView = view.findViewById(R.id.name)
        var rm: TextView = view.findViewById(R.id.rm)
        var price: TextView = view.findViewById(R.id.price)
        var description: TextView = view.findViewById(R.id.description)
        var detailAddToCart: ImageButton = view.findViewById(R.id.detailAddToCart)
        var detailFavourite: ImageButton = view.findViewById(R.id.detailFavourite)
        var backButton = view.findViewById<FloatingActionButton>(R.id.detailBackButton)
        var productStatus: TextView = view.findViewById(R.id.productStatus)
        var productId = arguments?.getString("productId").toString()
        var userId = arguments?.getString("id").toString()
        // Inflate the layout for this fragment
        cdbRef = FirebaseDatabase.getInstance().getReference("Cart")
        pdbRef = FirebaseDatabase.getInstance().getReference("Product")
        dbRef = FirebaseDatabase.getInstance().getReference("User").child(userId)
        pdbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                if(snapshot.exists()) {
                    product = snapshot.child(productId).getValue(Product::class.java)!!
                    var imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(product.image)
                    val ONE_MEGABYTE: Long = 5120 * 5120
                    imgRef.getBytes(ONE_MEGABYTE)
                        .addOnSuccessListener { bytes ->
                            // Convert the bytes to a Bitmap
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                           // Display the Bitmap in an ImageView
                            //imgPhoto.setImageBitmap(bitmap)
                            imageProduct.setImageBitmap(bitmap)
                        }
                    name.text = product.name
                    price.text = product.price.toString()
                    description.text = product.description
                    productStatus.text = "Unsold"
                }


            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
            }
        })

        detailAddToCart.setOnClickListener{


            pdbRef.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    product = snapshot.child(productId).getValue(Product::class.java)!!
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
                }
            })
            cdbRef.child(productId).setValue(product)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Product is added to cart successful", Toast.LENGTH_LONG).show()
                    val fragment = ShoppingCartFragment()
                    val bundle = Bundle()
                    bundle.putString("id", userId)
                    fragment.arguments = bundle
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragmentContainerView, fragment)
                    transaction?.addToBackStack(null)
                    transaction?.commit()
                }
                .addOnFailureListener{
                    Toast.makeText(requireContext(), "Product is failed to add to cart", Toast.LENGTH_LONG).show()
                }
        }

        detailFavourite.setOnClickListener{

            pdbRef.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    product = snapshot.child(productId).getValue(Product::class.java)!!

                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
                }
            })
            var start:Boolean = false
            dbRef.child("Favourite").child(productId).setValue(product)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Seller is added to favourite successful", Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener{
                    Toast.makeText(requireContext(), "Seller is failed to add to favourite", Toast.LENGTH_LONG).show()
                }


        }

        backButton.setOnClickListener{
            val fragment = HomepageFragment()
            val bundle = Bundle()
            bundle.putString("id", userId.toString())
            fragment.arguments = bundle
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentContainerView, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()

        }
        return view
    }


}