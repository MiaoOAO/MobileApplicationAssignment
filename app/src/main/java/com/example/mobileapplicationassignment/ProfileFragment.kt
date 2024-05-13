package com.example.mobileapplicationassignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

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
        var delPBtn: TextView = view.findViewById(R.id.deleteProduct)
        var viewPBtn: TextView = view.findViewById(R.id.viewProduct)
        var exBtnH: ImageView = view.findViewById(R.id.expandBtnH)
        var viewPurchase:TextView = view.findViewById(R.id.purchaseHistory)
        var viewSell:TextView = view.findViewById(R.id.sellHistory)
        exBtn.setOnClickListener {
            if (addPBtn.visibility == View.VISIBLE) {
                addPBtn.visibility = View.GONE
                delPBtn.visibility = View.GONE
                viewPBtn.visibility = View.GONE
                exBtn.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            } else {
                addPBtn.visibility = View.VISIBLE
                delPBtn.visibility = View.VISIBLE
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







            return view
    }

}