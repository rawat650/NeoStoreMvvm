package com.example.neostoremvvm.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neostoremvvm.R
import com.example.neostoremvvm.adapter.OrderListAdapter
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.dataclass.OrderListData
import com.example.neostoremvvm.databinding.ActivityMyOrderBinding
import com.example.neostoremvvm.factoryclass.MyOrderFactory
import com.example.neostoremvvm.model.MyOrderViewModel
import com.example.neostoremvvm.repository.MainRepository
import com.example.neostoremvvm.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_my_order.*
import java.util.*

class MyOrderActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    lateinit var orderList : MutableList<OrderListData>
    lateinit var temporaryList : MutableList<OrderListData>
    private lateinit var binding: ActivityMyOrderBinding
    lateinit var viewModel: MyOrderViewModel
    private val retrofitService = RetrofitService.getInstance()
    val accessToken = SharedPreferenceManager.getInstance(this).data.access_token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)

        binding = ActivityMyOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(myOrdersToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        orderList = mutableListOf()
        temporaryList = mutableListOf()


        val orderAdapter = OrderListAdapter(this)
        viewModel = ViewModelProvider(this,
            MyOrderFactory(MainRepository(retrofitService))
        ).get(MyOrderViewModel::class.java)

        binding.OrdersRecyclerView.layoutManager= LinearLayoutManager(this@MyOrderActivity,
            LinearLayoutManager.VERTICAL, false)
        binding.OrdersRecyclerView?.adapter= orderAdapter

        orderAdapter.onSetClickListerner(object : OrderListAdapter.OnItemClickListerner {

            override fun onClickListerner(position: Number) {
                val intent = Intent(this@MyOrderActivity,OrderDetailActivity::class.java)
                intent.putExtra("ORDER_ID",position.toString())
                startActivity(intent)
            }
        })
        viewModel.orderList.observe(this) {
            Log.d(TAG, "onCreate: $it")
            orderAdapter.setProductList(it)

        }
        viewModel.errorMessage.observe(this) {

        }
        viewModel.getOrderlist(accessToken)


    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu , menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchitem = menu?.findItem(R.id.action_search)
        val searchView = searchitem?.actionView as androidx.appcompat.widget.SearchView
        searchView?.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.queryHint = Html.fromHtml("<font color = #ffffff>" + "Search ..")


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MyOrderActivity, "No Data Found", Toast.LENGTH_LONG).show()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                temporaryList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    orderList.forEach {
                        if(it.id.toString().toLowerCase().contains(searchText)){
                            temporaryList.add(it)
                        }
                    }
                    OrdersRecyclerView.adapter!!.notifyDataSetChanged()
                }else{
                    temporaryList.clear()
                    temporaryList.addAll(orderList)
                    OrdersRecyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}


