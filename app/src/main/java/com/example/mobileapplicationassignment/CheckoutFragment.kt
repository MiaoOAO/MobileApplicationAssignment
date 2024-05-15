package com.example.mobileapplicationassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplicationassignment.data.Product
import com.example.mobileapplicationassignment.dataAdapter.CartAdapter
import com.example.mobileapplicationassignment.dataAdapter.CheckoutAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var productList: ArrayList<Product>
    private lateinit var dbRef : DatabaseReference
    private var totalPrice = 0.0

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
        var view = inflater.inflate(R.layout.fragment_checkout, container, false)
        var recyclerView: RecyclerView = view.findViewById(R.id.CheckoutRecyclerView)
        var totalAmount: TextView = view.findViewById(R.id.checkoutAmt)
        val id = arguments?.getString("id").toString()

        dbRef = FirebaseDatabase.getInstance().getReference("User").child(id)

        fetchData(recyclerView, totalAmount)

        return view
    }

    private fun fetchData(recyclerView: RecyclerView, totalAmountText: TextView){

        productList = arrayListOf()
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()

                if(snapshot.exists()) {
                    for (personSnap in snapshot.child("Product").children) {
                        val product = personSnap.getValue(Product::class.java)
                        productList.add(product!!)
                    }
                    for (product in productList) {
                        totalPrice += product.price.toDouble()  // Convert price to Double for sum
                    }
                    val totalAmount = "Total Amount: $${String.format("%.2f", totalPrice)}"
                    totalAmountText.text = totalAmount

                }


                recyclerView.adapter = CheckoutAdapter(productList)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.setHasFixedSize(true)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }


}