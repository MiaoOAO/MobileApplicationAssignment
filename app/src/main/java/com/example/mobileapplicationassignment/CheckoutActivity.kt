package com.example.mobileapplicationassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapplicationassignment.data.Product
import com.example.mobileapplicationassignment.dataAdapter.CheckoutAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultWithDataListener, ExternalWalletListener {
    private lateinit var productList: ArrayList<Product>
    private var cartList:ArrayList<Product> =arrayListOf()
    private lateinit var dbRef : DatabaseReference
    private lateinit var cdbRef : DatabaseReference
    private lateinit var pdbRef:  DatabaseReference
    private lateinit var newpdbRef : DatabaseReference
    private var totalPrice = 0.0
    private lateinit var id:String
    lateinit var paymentBtn: Button
    lateinit var successBtn: Button
    lateinit var recyclerView: RecyclerView
    lateinit var cancelBtn : Button
    lateinit var orderPayId : TextView
    lateinit var titleName : TextView
    lateinit var successMsg : TextView
    lateinit var email : TextView
    lateinit var phoneNo : TextView
    lateinit var emailMsg : TextView
    lateinit var phoneNoMsg : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        recyclerView = findViewById(R.id.ACheckoutRecyclerView)
        var totalAmount: TextView = findViewById(R.id.checkoutAmount)
        cancelBtn = findViewById(R.id.aCancelBtn)
        paymentBtn = findViewById(R.id.activityPayBtn)
        successBtn = findViewById(R.id.successBtn)
        orderPayId = findViewById(R.id.pOrderId)
        successMsg = findViewById(R.id.successMsg)
        titleName = findViewById(R.id.checkOutTitle)
        email = findViewById(R.id.EmailInput)
        phoneNo = findViewById(R.id.phoneNoInput)
        emailMsg = findViewById(R.id.EmailMsg)
        phoneNoMsg = findViewById(R.id.phoneNoMsg)

        orderPayId.visibility = View.GONE
        successMsg.visibility = View.GONE
        successBtn.visibility = View.GONE

        id = intent.getStringExtra("id").toString()
        cartList.clear()

//        id = arguments?.getString("id").toString()
        Checkout.preload(this)
        val co = Checkout()

        cancelBtn.setOnClickListener{
            var intent = Intent(this,MainMenu::class.java)
            intent.putExtra("Fragment", "ShoppingCart") // or any identifier for the fragment
            intent.putExtra("Id", id)
            startActivity(intent)
        }

        dbRef = FirebaseDatabase.getInstance().getReference("User").child(id)
        cdbRef = FirebaseDatabase.getInstance().getReference("User")

        newpdbRef = FirebaseDatabase.getInstance().getReference("Cart")
        fetchData(recyclerView, totalAmount)

        paymentBtn.setOnClickListener{
            makePayment()
        }

    }

    private fun fetchData(recyclerView: RecyclerView, totalAmountText: TextView){
        productList = arrayListOf()
        dbRef = FirebaseDatabase.getInstance().getReference("Cart")
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()

                if(snapshot.exists()) {
                    for (personSnap in snapshot.children) {
                        val product = personSnap.getValue(Product::class.java)
                        productList.add(product!!)
                        cartList.add(product!!)

                    }
                    for (product in productList) {
                        totalPrice += product.price.toDouble()  // Convert price to Double for sum
                    }
                    val totalAmount = "Total Amount: $${String.format("%.2f", totalPrice)}"
                    totalAmountText.text = totalAmount
                }


                recyclerView.adapter = CheckoutAdapter(productList)
                recyclerView.layoutManager = LinearLayoutManager(this@CheckoutActivity)
                recyclerView.setHasFixedSize(true)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CheckoutActivity, "Error: $error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun makePayment(){

//        Checkout.preload(requireContext())
        val co = Checkout()
        var amount = totalPrice


        try {
            val options = JSONObject()
            options.put("name","GIVEMEYOURMONEY Corp")
            options.put("description","USE FOR FUN")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTL2TXx6URQL59L3PE-LgjB2wfACAyOMmnUwKBfE2zgPQ&s")
            options.put("theme.color", "#ff9900");
            options.put("currency","MYR");
            options.put("amount",amount*100)//pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email",email.text)
            prefill.put("contact",phoneNo.text)

            options.put("prefill",prefill)
            co.open(this,options)

        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Payment successful $p0", Toast.LENGTH_LONG).show()
        cdbRef = FirebaseDatabase.getInstance().getReference("User")

        paymentBtn.visibility = View.GONE
        recyclerView.visibility = View.GONE
        cancelBtn.visibility = View.GONE
        titleName.visibility = View.GONE
        email.visibility = View.GONE
        phoneNo.visibility = View.GONE
        emailMsg.visibility = View.GONE
        phoneNoMsg.visibility = View.GONE

        successBtn.visibility = View.VISIBLE
        orderPayId.visibility = View.VISIBLE
        successMsg.visibility = View.VISIBLE

        orderPayId.text = "ORDER ID : $p0"
        pdbRef = FirebaseDatabase.getInstance().getReference("Product")
        cdbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (personSnap in snapshot.children) {
                        val personId = personSnap.child("id").getValue(String::class.java)
                        if (personId != null) {
                            // Update Favourite items
                            for (favouriteSnap in personSnap.child("Favourite").children) {
                                val product = favouriteSnap.getValue(Product::class.java)
                                if (product != null) {
                                    for (cart in cartList) {
                                        if (cart.id == product.id) {
                                            cdbRef.child(personId).child("Favourite").child(cart.id).child("status").setValue(false)
                                        }
                                    }
                                }
                            }

                            // Update Selling and Product status
                            for (cart in cartList) {
                                if (cart.owner == personId) {
                                    cdbRef.child(cart.owner).child("Selling").child(cart.id).setValue(cart)
                                    cdbRef.child(cart.owner).child("Product").child(cart.id).child("status").setValue(false)

                                }
                                pdbRef.child(cart.id).removeValue()
                                cdbRef.child(id).child("Purchase").child(cart.id).setValue(cart)
                            }
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CheckoutActivity, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })

        successBtn.setOnClickListener{
            newpdbRef.removeValue()
            var intent = Intent(this,MainMenu::class.java)
            intent.putExtra("Fragment", "ShoppingCart") // or any identifier for the fragment
            intent.putExtra("Id", id)
            startActivity(intent)
        }


    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment Failed $p0", Toast.LENGTH_LONG).show()

        paymentBtn.visibility = View.GONE
        recyclerView.visibility = View.GONE
        cancelBtn.visibility = View.GONE
        titleName.visibility = View.GONE
        email.visibility = View.GONE
        phoneNo.visibility = View.GONE
        emailMsg.visibility = View.GONE
        phoneNoMsg.visibility = View.GONE

        successBtn.visibility = View.VISIBLE
        orderPayId.visibility = View.VISIBLE

        orderPayId.text = "PAYMENT UNSUCCESS, PLEASE TRY AGAIN!"

        successBtn.setOnClickListener{
            var intent = Intent(this,MainMenu::class.java)
            intent.putExtra("Fragment", "ShoppingCart") // or any identifier for the fragment
            intent.putExtra("Id", id)
            startActivity(intent)
        }
    }

    override fun onExternalWalletSelected(p0: String?, p1: PaymentData?) {
        TODO("Not yet implemented")
    }
}