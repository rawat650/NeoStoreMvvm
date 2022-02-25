package com.example.neostoremvvm.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neostoremvvm.R
import com.example.neostoremvvm.adapter.CartListAdapter
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.databinding.ActivityMyCartBinding
import com.example.neostoremvvm.factoryclass.MyCartFactory
import com.example.neostoremvvm.model.MyCartViewModel
import com.example.neostoremvvm.repository.MainRepository
import com.example.neostoremvvm.storage.SharedPreferenceManager
import com.example.neostoremvvm.swipe.SwipeGesture
import kotlinx.android.synthetic.main.activity_my_cart.*

class MyCartActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMyCartBinding
    lateinit var viewModel: MyCartViewModel
    private val retrofitService = RetrofitService.getInstance()
    lateinit var accessToken: String
    val cartAdapter = CartListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, MyCartFactory(MainRepository(retrofitService))).
        get(MyCartViewModel::class.java)
        accessToken = SharedPreferenceManager.getInstance(this).data.access_token

        updateCartData()
        setupClickListeners()
        dataObserve()
    }
    private fun setupClickListeners() {
        btnOrderNow.setOnClickListener(){
            val intent = Intent(this,AddressListScreen::class.java)
            startActivity(intent)
        }

        btnAddToCartTollbarBack.setOnClickListener(){
            onBackPressed()
        }
    }
    private fun dataObserve(){

        viewModel.myCartLiveData.observe(this, Observer {

            if(it !=null){
                val cost = RupeeConvertorHelperClass().convertorfunction(it.total)
                price.text = cost

                myCartRecyclerview.adapter = cartAdapter
                myCartRecyclerview.layoutManager = LinearLayoutManager(this@MyCartActivity,
                    LinearLayoutManager.VERTICAL,false)
                val swipeGesture = object : SwipeGesture(this@MyCartActivity){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        when(direction){
                            ItemTouchHelper.LEFT->{
                                val productItemId = it.data[viewHolder.adapterPosition].product_id
                                viewModel.getCartDelete(accessToken,productItemId)
                                cartAdapter.deletItem(viewHolder.adapterPosition)
                            }
                        }
                    }
                }
                val touchHelper = ItemTouchHelper(swipeGesture)
                touchHelper.attachToRecyclerView(myCartRecyclerview)

                cartAdapter.setOnItemClickListerner(object : CartListAdapter.onItemClickListerner {
                    override fun onClick(position: Int, adapterPosition: Int) {
                        val productId = it.data[adapterPosition].product_id
                        val qty = position
                        viewModel.getCartEdit(accessToken,productId,qty)
                    }
                })
                cartAdapter.setCartData(it.data)
            }else{
                txtTotal.visibility = View.GONE
                btnOrderNow.visibility = View.GONE
                price.visibility = View.GONE
                Toast.makeText(this@MyCartActivity,"Cart empty",Toast.LENGTH_LONG).show()
            }

        })

        viewModel.errorMessage.observe(this, Observer {

        })

        viewModel.cartEditLiveData.observe(this, Observer {
            if(it !=null){
                Toast.makeText(this@MyCartActivity,"${it.user_msg}",Toast.LENGTH_LONG).show()
                updateCartData()
            }else
                Toast.makeText(this@MyCartActivity,"$it",Toast.LENGTH_SHORT).show()
        })

        viewModel.cartDeleteLiveData.observe(this, Observer {
            if(it.status == 200){
                Toast.makeText(this@MyCartActivity,"${it.user_msg}",Toast.LENGTH_LONG).show()
                val sharedPreferences = this@MyCartActivity.getSharedPreferences("my_private_sharedpref",
                    Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val totalCartsVal = it.total_carts
                editor.putInt("total_carts",totalCartsVal)
                editor.apply()
                updateCartData()
            }else
                Log.d(ContentValues.TAG, "onResponse: ${it}")
        })

       /* viewModel.myCartLiveData.observe(this, Observer {
            Log.d("Test", "onCreate: $it")
            cartAdapter.setCartData(it)
        })*/
    }
    private fun updateCartData(){
        viewModel.getCartList(accessToken)
    }
}