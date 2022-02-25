package com.example.neostoremvvm.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.neostoremvvm.R
import com.example.neostoremvvm.activities.ProductDetailActivity
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.databinding.FragmentAddToCartBinding
import com.example.neostoremvvm.factoryclass.AddToCartFactory
import com.example.neostoremvvm.model.AddToCartViewModel
import com.example.neostoremvvm.repository.MainRepository
import com.example.neostoremvvm.storage.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_add_to_cart.*
import kotlinx.android.synthetic.main.fragment_add_to_cart.view.*


class AddToCardtFragment : DialogFragment() {

    private var _binding: FragmentAddToCartBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: AddToCartViewModel
    lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddToCartBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this, AddToCartFactory(MainRepository(RetrofitService.retrofitService))).get(
            AddToCartViewModel::class.java)

        val imageUrl = arguments?.getString("ImageURl")
        val productName = arguments?.getString("productName")
        val productId =  arguments?.getString("productId")?.toInt()
        val activity = activity as ProductDetailActivity
        accessToken = SharedPreferenceManager.getInstance(activity).data.access_token

        Glide.with(this).load(imageUrl).into(view.imgQtyItemImage)
        view.txtQtyItemName.text = productName

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Setup the button in our fragment to call getUpdatedText method in viewModel
    private fun setupClickListeners() {

        binding.btnQtySubmitButton.setOnClickListener(){
            if(!validateEnterQuantity())
                return@setOnClickListener
            else
                addToCartObserve()
        }
        binding.txtQtyEnterQuantity.setOnClickListener(){
            txtQtyEnterQuantity.setBackgroundResource(R.drawable.quantity_border_green)
        }
    }
    private fun addToCartObserve() {
        viewModel.addToCartResponse.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                val totalCarts = it.total_carts
                val sharedPreferences = activity?.getSharedPreferences("my_private_sharedpref",Context.MODE_PRIVATE)
                val editor = sharedPreferences?.edit()
                editor?.putInt("total_carts",totalCarts)
                editor?.apply()
                Toast.makeText(activity,"${it.user_msg}",Toast.LENGTH_LONG).show()
            }else
                Toast.makeText(activity,"${it}",Toast.LENGTH_LONG).show()
        })

        arguments?.getString("productId")?.toInt()?.let {
            viewModel.getAddToCart(
                accessToken,
                it,
                txtQtyEnterQuantity.text.toString().toInt()
            )
        }
    }

    private fun validateEnterQuantity(): Boolean {
        val enterQuantity = txtQtyEnterQuantity.text.toString()
        if(enterQuantity.isEmpty()){
            txtQtyEnterQuantity.setBackgroundResource(R.drawable.image_border_red)
            return false
        }
        else{
            txtQtyEnterQuantity.setBackgroundResource(R.drawable.quantity_border_green)
            return true
        }
    }
}