package com.example.mobileapplicationassignment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
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
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
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
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        var exBtn: ImageView = view.findViewById(R.id.expandBtnP)
        var addPBtn: TextView = view.findViewById(R.id.addProduct)
        var viewPBtn: TextView = view.findViewById(R.id.viewProduct)
        var exBtnH: ImageView = view.findViewById(R.id.expandBtnH)
        var viewPurchase:TextView = view.findViewById(R.id.purchaseHistory)
        var viewSell:TextView = view.findViewById(R.id.sellHistory)
        var logOutBtn:Button = view.findViewById(R.id.logOutBtn)
        var proImg:ImageView = view.findViewById(R.id.profileImg)
        var proId:TextView = view.findViewById(R.id.profileId)
        var proName:TextView = view.findViewById(R.id.profileName)
        var edit:ImageView = view.findViewById(R.id.editBtn)

        //var myViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        val name = arguments?.getString("name").toString()

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
            findNavController().navigate(R.id.action_profileFragment_to_addProductFragment)
        }

        viewPBtn.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_viewProductFragment)
        }
        viewPurchase.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_viewPurchaseHistory)
        }
        viewSell.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_viewSellingHistory)
        }
        edit.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_editFragment)
        }

        logOutBtn.setOnClickListener{
            //myViewModel.clearId()
            var go = Intent(requireContext(),MainActivity::class.java)
            startActivity(go)


        }
        dbRef = FirebaseDatabase.getInstance().getReference("User")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    var imgP = snapshot.child(name).child("ProfileImage").getValue()
                    var img = imgP.toString()
                    var stuId = snapshot.child(name).child("Id").getValue()
                    var imgRef = FirebaseStorage.getInstance().getReferenceFromUrl(img)
                    val ONE_MEGABYTE: Long = 1024 * 1024
                    imgRef.getBytes(ONE_MEGABYTE)
                        .addOnSuccessListener { bytes ->
                            // Convert the bytes to a Bitmap
                            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                            // Display the Bitmap in an ImageView
                            //imgPhoto.setImageBitmap(bitmap)
                            proImg.setImageBitmap(bitmap)
                        }

                    proId.text = stuId.toString()
                    proName.text = name
                }


            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
            }
        })




        return view
    }

}