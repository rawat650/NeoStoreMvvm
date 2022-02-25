package com.example.neostoremvvm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neostoremvvm.R
import com.example.neostoremvvm.adapter.ProductListAdapter
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.databinding.ActivityProductListingBinding
import com.example.neostoremvvm.factoryclass.ProductListFactory
import com.example.neostoremvvm.model.ProductListViewModel
import com.example.neostoremvvm.repository.MainRepository
import kotlinx.android.synthetic.main.activity_product_listing.*


class ProductListingActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityProductListingBinding
    lateinit var viewModel: ProductListViewModel
    private val retrofitService = RetrofitService.getInstance()
    val adapter = ProductListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_listing)
        binding = ActivityProductListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val getIntent = intent.getStringExtra("CATEGORY_VALUE")

        viewModel = ViewModelProvider(this, ProductListFactory(MainRepository(retrofitService))).get(ProductListViewModel::class.java)
        setupClickListeners()
        dataObserve()
    }
    private fun setupClickListeners() {
        val getIntent = intent.getStringExtra("CATEGORY_VALUE")
        binding.productRecyclerView.layoutManager= LinearLayoutManager(this@ProductListingActivity,
            LinearLayoutManager.VERTICAL, false)
        binding.productRecyclerView?.adapter = adapter
        adapter.setOnItemClickListerner(object :
            ProductListAdapter.onItemClickListerner {
            override fun onItemClick(position: Int) {
                val id = position.toString()
                val prodcutItemIntent =
                    Intent(this@ProductListingActivity, ProductDetailActivity::class.java)
                prodcutItemIntent.putExtra("PRODUCT_ID", id)
                startActivity(prodcutItemIntent)
            }
        })
        when (getIntent) {
            "1" -> {
                productToolbarTitle.text = "Tables"
                viewModel.getProductList("1")
            }
            "2" -> {
                productToolbarTitle.text = "Chairs"
                viewModel.getProductList("2")
            }
            "3" -> {
                productToolbarTitle.text = "Sofas"
                viewModel.getProductList("3")
            }
            "4" -> {
                productToolbarTitle.text = "Cupboards"
                viewModel.getProductList("4")
            }
        }
    }
    private fun dataObserve(){

        viewModel.productList.observe(this, Observer {
            Log.d(TAG, "onCreate: $it")
            adapter.setProductList(it)
        })

        viewModel.errorMessage.observe(this, Observer {

        })
    }
}