package com.example.mobileapplicationassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplicationassignment.data.Product
import com.example.mobileapplicationassignment.dataAdapter.ListAdapter
import com.example.mobileapplicationassignment.dataAdapter.VPAdapter
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
 * Use the [HomepageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomepageFragment : Fragment(), ListAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var pdbRef : DatabaseReference
    private lateinit var productList: ArrayList<Product>
    private var result: String = ""
    private lateinit var id:String

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
        var view = inflater.inflate(R.layout.fragment_homepage, container, false)
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var search: TextView = view.findViewById(R.id.search)
        var btnSearch: ImageButton = view.findViewById(R.id.btnSearch)
        var products: RecyclerView = view.findViewById(R.id.products)
        id = arguments?.getString("id").toString()

        btnSearch.setOnClickListener{
            result = search.text.toString()
            //tvTitle.text = search.text
            fetchData(products)

        }

        pdbRef = FirebaseDatabase.getInstance().getReference("Product")
        fetchData(products)
        return view
    }


    private fun fetchData(recyclerView: RecyclerView){
        productList = arrayListOf()

        pdbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                if(snapshot.exists()) {
                    for (personSnap in snapshot.children) {
                        val product = personSnap.getValue(Product::class.java)!!
                        if (product.name.contains(result)) {
                            productList.add(product)
                        }
                    }

                }

                recyclerView.adapter  = ListAdapter(productList, this@HomepageFragment)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.setHasFixedSize(true)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun itemClick(position: Int) {
        val aProduct = productList[position]

        val fragment = DetailFragment()
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("productId", aProduct.id)
        fragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

}