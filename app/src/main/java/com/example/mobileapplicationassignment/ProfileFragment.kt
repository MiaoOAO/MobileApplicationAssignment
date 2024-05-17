package com.example.mobileapplicationassignment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mobileapplicationassignment.data.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbRef : DatabaseReference
    private lateinit var pdbRef : DatabaseReference
    private lateinit var cdbRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragmentContainerView, fragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        var exBtn: ImageButton = view.findViewById(R.id.expandBtnP)
        var addPBtn: TextView = view.findViewById(R.id.addProduct)
        var viewPBtn: TextView = view.findViewById(R.id.viewProduct)
        var exBtnH: ImageButton = view.findViewById(R.id.expandBtnH)
        var viewPurchase:TextView = view.findViewById(R.id.myPurchList)
        var viewSell:TextView = view.findViewById(R.id.sellHistory)
        var logOutBtn:Button = view.findViewById(R.id.logOutBtn)
        var proImg:ImageView = view.findViewById(R.id.profileImg)
        var proId:TextView = view.findViewById(R.id.profileId)
        var proName:TextView = view.findViewById(R.id.profileName)
        var edit:ImageView = view.findViewById(R.id.editBtn)

        //var myViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        val id = arguments?.getString("id").toString()
        dbRef = FirebaseDatabase.getInstance().getReference("User")
        pdbRef = FirebaseDatabase.getInstance().getReference("Product")
        cdbRef = FirebaseDatabase.getInstance().getReference("Cart")

//        var product = Product("1","Gaming Chair",true,"condition 80% new, bought 2 months ago, TTracing brand",120,"gs://campus-marketplace-8cc1c.appspot.com/Product1.png")
//        dbRef.child(id).child("Product").child(product.id).setValue(product)
//        //product side
//        pdbRef.child(product.id).setValue(product)
        exBtn.setOnClickListener {
            if (addPBtn.visibility == View.VISIBLE) {
                addPBtn.visibility = View.GONE
                viewPBtn.visibility = View.GONE
                exBtn.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            } else {
                addPBtn.visibility = View.VISIBLE
                viewPBtn.visibility = View.VISIBLE
                exBtn.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            }
        }
        exBtnH.setOnClickListener {
            if (viewPurchase.visibility == View.VISIBLE) {
                viewPurchase.visibility = View.GONE
                viewSell.visibility = View.GONE
                exBtnH.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            } else {
                viewPurchase.visibility = View.VISIBLE
                viewSell.visibility = View.VISIBLE
                exBtnH.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            }
        }

        addPBtn.setOnClickListener{
            val fragment = AddProductFragment()
            val bundle = Bundle()
            bundle.putString("id",id)
            fragment.arguments = bundle

            replaceFragment(fragment)
        }

        viewPBtn.setOnClickListener{
            val fragment = ViewProductFragment()
            val bundle = Bundle()
            bundle.putString("id",id)
            fragment.arguments = bundle

            replaceFragment(fragment)
        }
        viewPurchase.setOnClickListener{
            val fragment = ViewPurchaseHistory()
            val bundle = Bundle()
            bundle.putString("id",id)
            fragment.arguments = bundle

            replaceFragment(fragment)
        }
        viewSell.setOnClickListener{
            val fragment = ViewSellingHistory()
            val bundle = Bundle()
            bundle.putString("id",id)
            fragment.arguments = bundle

            replaceFragment(fragment)
        }
        edit.setOnClickListener{
//            val bundle = Bundle()
//            bundle.putString("id", id) // Replace "key" with your key and "value" with the actual value
//            findNavController().navigate(R.id.action_profileFragment_to_editFragment, bundle)
            //findNavController().navigate(R.id.action_profileFragment_to_editFragment)
            val fragment = EditFragment()
            val bundle = Bundle()
            bundle.putString("id",id)
            fragment.arguments = bundle

            replaceFragment(fragment)

        }

        logOutBtn.setOnClickListener{
            //myViewModel.clearId()
            cdbRef.removeValue()
            var go = Intent(requireContext(),MainActivity::class.java)
            startActivity(go)


        }
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    var imgP = snapshot.child(id).child("profileImage").getValue()
                    var img = imgP.toString()
                    var stuId = snapshot.child(id).child("name").getValue()
                    var imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(img)
                    val ONE_MEGABYTE: Long = 5120 * 5120
                    imgRef.getBytes(ONE_MEGABYTE)
                        .addOnSuccessListener { bytes ->
                            // Convert the bytes to a Bitmap
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                            // Display the Bitmap in an ImageView
                            //imgPhoto.setImageBitmap(bitmap)
                            proImg.setImageBitmap(bitmap)
                        }

                    proId.text = stuId.toString()
                    proName.text = id
                }


            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
            }
        })




        return view
    }

}