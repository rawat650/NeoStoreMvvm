package com.example.neostoremvvm.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neostoremvvm.R
import com.example.neostoremvvm.adapter.ProductDetailImageAdapter
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.databinding.ActivityProductDetailBinding
import com.example.neostoremvvm.dataclass.ProductImage
import com.example.neostoremvvm.factoryclass.ProductDetailViewmodelFactory
import com.example.neostoremvvm.fragment.AddToCardtFragment
import com.example.neostoremvvm.fragment.RatingFragment
import com.example.neostoremvvm.model.ProductDetailViewModel
import com.example.neostoremvvm.repository.MainRepository
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_product_detail.*
import java.text.DecimalFormat
import java.text.NumberFormat

class ProductDetailActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    lateinit var viewModel: ProductDetailViewModel
    private lateinit var binding: ActivityProductDetailBinding
    private val retrofitService = RetrofitService.getInstance()
    var getId:String = ""
    val adapter = ProductDetailImageAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this,ProductDetailViewmodelFactory(MainRepository(retrofitService))).get(ProductDetailViewModel::class.java)
        setSupportActionBar(toolBar)
        getId = intent.getStringExtra("PRODUCT_ID").toString()

        setupClickListeners()
        dataObserve()
    }
    private fun setupClickListeners(){
        productDetailSharing.setOnClickListener() {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            //http://staging.php-dev.in:8844/trainingapp/api/products/getDetail?product_id=$getId
            shareIntent.putExtra(Intent.EXTRA_TEXT, "www.neostore.com/product_id?id=$getId")
            startActivity(Intent.createChooser(shareIntent, "Share link using"))
        }
    }
    private fun dataObserve() {
        viewModel.errorMessage.observe(this, Observer {

        })
        viewModel.productImageList.observe(this) {
            Log.d(TAG, "onCreate: $it")
            adapter.setProductListDetail(it)
        }
        viewModel.productDetailList.observe(this) {

            if (it != null) {
                val productResponse = it
                productDetailtoolbarTitle.setText(productResponse.name)
                val productName = productResponse.name
                ProductDetailName.text = productName
                val categoryId = productResponse.product_category_id
                if (categoryId == 1)
                    productDetailCategory.text = "Category - Tables"
                else if (categoryId == 2)
                    productDetailCategory.text = "Category - Chairs"
                else if (categoryId == 3)
                    productDetailCategory.text = "Category - Sofas"
                else if (categoryId == 4)
                    productDetailCategory.text = "Category - Cupboards"
                else
                    productDetailCategory.text = ""


                productDetailProducer.text = productResponse.producer
                productDetailRatingBar.rating = productResponse.rating!!.toFloat()

                val nf = NumberFormat.getCurrencyInstance()
                val pattern = (nf as DecimalFormat).toPattern()
                val newPattern = pattern.replace("\u00A4", "").trim { it <= ' ' }
                val newFormat: NumberFormat = DecimalFormat(newPattern)
                val answer = newFormat.format(productResponse?.cost).trim().dropLast(3)
                productDetailPrice.text = "Rs. " + answer

                productDetailDescription.text = productResponse.description

                val imageUrl = productResponse.product_images[0].image
                Glide.with(this@ProductDetailActivity).load(imageUrl).into(productDetailImage)

                val imageList = mutableListOf<ProductImage>()
                for (image in productResponse.product_images) {
                    imageList.add(image)
                }
                Log.d(ContentValues.TAG, "onResponse: ${productResponse.product_images[0].image}")

                btnProductDetailBuyNow.setOnClickListener() {
                    val buyNowPopUp: DialogFragment = AddToCardtFragment()
                    val bundle = Bundle()
                    bundle.putString("productId", getId)
                    bundle.putString("productName", productName)
                    bundle.putString("ImageURl", imageUrl)
                    buyNowPopUp.arguments = bundle
                    buyNowPopUp.show(supportFragmentManager, "AddToCartScreen")

                    binding.productDetailRecyclerview.layoutManager = LinearLayoutManager(this)
                    binding.productDetailRecyclerview.adapter = adapter

                    adapter.setOnClickListerner(object : ProductDetailImageAdapter.onImageClickListernet {
                        override fun onClickListerner(position: String) {
                            Glide.with(this@ProductDetailActivity).load(position).into(productDetailImage)
                        }
                    })
                }
                ProductDetailRatingPopUp.setOnClickListener(){

                    val fragmentPopUp: DialogFragment = RatingFragment()
                    val bundle = Bundle()
                    bundle.putString("productId",getId)
                    bundle.putString("productName" , productName)
                    bundle.putString("ImageURl",imageUrl)
                    fragmentPopUp.arguments = bundle
                    fragmentPopUp.show(supportFragmentManager,"RatingPOPUpScreen")
                }

            } else {
                Toast.makeText(this@ProductDetailActivity, "${it}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getProductDetailList(getId)
    }
}