package com.example.neostoremvvm.activities

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neostoremvvm.R
import com.example.neostoremvvm.adapter.CartListAdapter
import com.example.neostoremvvm.adapter.OrderDetailAdapter
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.dataclass.OrderDetail
import com.example.neostoremvvm.databinding.ActivityOrderDetailBinding
import com.example.neostoremvvm.factoryclass.OrderDetailFactory
import com.example.neostoremvvm.model.OrderDetailViewModel
import com.example.neostoremvvm.repository.MainRepository
import com.example.neostoremvvm.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_my_order.myOrdersTollbarBack
import kotlinx.android.synthetic.main.activity_my_order.myOrdersToolbarTitle
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlin.properties.Delegates

class OrderDetailActivity : AppCompatActivity() {

    var  orderId by Delegates.notNull<Int>()
    val accessToken = SharedPreferenceManager.getInstance(this).data.access_token
    lateinit var viewModel: OrderDetailViewModel
    private lateinit var binding: ActivityOrderDetailBinding
    val orderAdapter = OrderDetailAdapter(this)

    private val retrofitService = RetrofitService.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderId = intent.getStringExtra("ORDER_ID")?.toInt()!!
        viewModel = ViewModelProvider(
            this,
            OrderDetailFactory(MainRepository(retrofitService))
        ).get(OrderDetailViewModel::class.java)

        myOrdersTollbarBack.setOnClickListener() {
            onBackPressed()
        }

        binding.OrdersDetailRecyclerView.adapter = orderAdapter
        binding.OrdersDetailRecyclerView.layoutManager = LinearLayoutManager(
            this@OrderDetailActivity,
            LinearLayoutManager.VERTICAL, false
        )

        viewModel.errorMessage.observe(this, Observer {

        })

        viewModel.detailList.observe(this) {
            val orderId = intent.getStringExtra("ORDER_ID")?.toInt()
            Log.d(TAG, "getOrderDetail: $orderId")

            myOrdersToolbarTitle.text = "Order ID: $orderId"
            if (it.status == 200) {
                val cost = RupeeConvertorHelperClass().convertorfunction(it.data.cost)
                Orderprice.text = cost
                val orderDetailList = mutableListOf<OrderDetail>()
                for (item in it.data.order_details) {
                    orderDetailList.add(item)
                }
                OrdersDetailRecyclerView.adapter = orderAdapter
                OrdersDetailRecyclerView.layoutManager = LinearLayoutManager(
                    this@OrderDetailActivity,
                    LinearLayoutManager.VERTICAL, false
                )

            }
            orderAdapter.setProductListDetail(it.data.order_details)
        }
        viewModel.getDetailData(accessToken, orderId)

    }
}