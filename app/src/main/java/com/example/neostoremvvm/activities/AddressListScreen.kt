package com.example.neostoremvvm.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neostoremvvm.R
import com.example.neostoremvvm.adapter.AddressAdapter
import com.example.neostoremvvm.dataclass.AddressInfo
import com.example.neostoremvvm.model.AddressViewModel
import com.example.neostoremvvm.storage.AddressDb
import com.example.neostoremvvm.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_address_list_screen.*
import kotlinx.android.synthetic.main.activity_address_list_screen.btnAddToCartTollbarBack
import kotlinx.android.synthetic.main.activity_address_list_screen.imgAddAddress


class AddressListScreen : AppCompatActivity(), AddressAdapter.cartInterface {
    var list = ArrayList<AddressInfo>()
    lateinit var UserName: TextView
    lateinit var viewModel: AddressViewModel
    lateinit var fullAddress: String
    lateinit var recycler_view: RecyclerView
    lateinit var database: AddressDb
    lateinit var adapter: AddressAdapter
    lateinit var addressString: String
    lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list_screen)

        initAdapter()

        imgAddAddress.setOnClickListener() {
            val intent = Intent(this, AddAdress::class.java)
            startActivityForResult(intent, 101)
        }
        btnPlaceOrder.setOnClickListener(){
            placeOrder()
            startActivity(Intent(this,MyOrderActivity::class.java))
        }

        btnAddToCartTollbarBack.setOnClickListener() {
            onBackPressed()
        }
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(AddressViewModel::class.java)

        val intent = Intent()
        accessToken = SharedPreferenceManager.getInstance(this).data.access_token

//        intent.getStringExtra("finalAddress")

        viewModel.allAddress.observe(this,Observer{
        adapter.addItems(it as MutableList<AddressInfo>)
        })
        viewModel.getData()
        viewModel.delete.observe(this, Observer {
            viewModel.getData()
        })

    }

    private fun placeOrder(){
        viewModel.placeOrderLiveData.observe(this, Observer {
            if(it.status == 200){
                Toast.makeText(this@AddressListScreen,"${it.user_msg}", Toast.LENGTH_LONG).show()
            }else
                Toast.makeText(this@AddressListScreen,"${it}", Toast.LENGTH_LONG).show()

        })
        viewModel.getOrderPlace(accessToken,addressString)
    }

    private fun initAdapter() {
        recycler_view = findViewById<RecyclerView>(R.id.addressListRecyclerView)
        adapter = AddressAdapter( this)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter =adapter
    }

    override fun onClick(position: Int, item: AddressInfo) {
        addressString = item.toString()
    }

    override fun onClose(data: AddressInfo, position: Int) {

            viewModel.deleteAdress(data)

    }
}





















