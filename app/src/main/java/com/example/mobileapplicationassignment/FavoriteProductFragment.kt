package com.example.mobileapplicationassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplicationassignment.data.Product
import com.example.mobileapplicationassignment.dataAdapter.FavoriteAdapter
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
 * Use the [FavoriteProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteProductFragment : Fragment(),FavoriteAdapter.ButtonClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var productList: ArrayList<Product>
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
        val view = inflater.inflate(R.layout.fragment_favorite_product, container, false)

        // Inflate the layout for this fragment
        var recyclerView: RecyclerView = view.findViewById(R.id.FavRecycleView)
//        var checkout: Button = view.findViewById(R.id.FavDelete)
        // Create a storage reference from our app
        val id = arguments?.getString("id").toString()
        dbRef = FirebaseDatabase.getInstance().getReference("User").child(id)
        //val person = Person("P002", "Yashiro", "gs://fir-622cc.appspot.com/myImg/Yashiro.png")

        fetchData(recyclerView)

//        checkout.setOnClickListener{
//            val fragment = CheckoutFragment()
//            val bundle = Bundle()
//            bundle.putString("id",id)
//            fragment.arguments = bundle
//            val transaction = activity?.supportFragmentManager?.beginTransaction()
//            transaction?.replace(R.id.fragmentContainerView, fragment)
//            transaction?.addToBackStack(null)
//            transaction?.commit()
//        }
        return view
    }

    private fun fetchData(recyclerView: RecyclerView){
        productList = arrayListOf()
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                if(snapshot.exists()) {
                    for (personSnap in snapshot.child("Favourite").children) {
                        val product = personSnap.getValue(Product::class.java)
                        productList.add(product!!)
                    }

                }
                recyclerView.adapter = FavoriteAdapter(productList,this@FavoriteProductFragment)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.setHasFixedSize(true)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onButtonClick(position: Int) {
        val aProduct = productList[position]
        dbRef.child("Favourite").child(aProduct.id).removeValue()
    }

}