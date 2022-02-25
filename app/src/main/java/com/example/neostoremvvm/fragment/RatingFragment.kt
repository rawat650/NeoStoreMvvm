package com.example.neostoremvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.neostoremvvm.api.RetrofitService
import com.example.neostoremvvm.databinding.FragmentRatingBinding
import com.example.neostoremvvm.factoryclass.RatingFactoryClass
import com.example.neostoremvvm.model.RatingViewModel
import com.example.neostoremvvm.repository.MainRepository
import kotlinx.android.synthetic.main.fragment_rating.view.*
import kotlin.properties.Delegates

class RatingFragment : DialogFragment() {

    private var _binding: FragmentRatingBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: RatingViewModel
    lateinit var productId: String
  //  var rating by Delegates.notNull<Int>()
    private var rating: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatingBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this, RatingFactoryClass(MainRepository(RetrofitService.retrofitService))).get(
            RatingViewModel::class.java)

        productId = arguments?.getString("productId").toString()
        rating = view.ratingbarsubmision.rating.toInt()
        val imageUrl = arguments?.getString("ImageURl")
        val productName = arguments?.getString("productName")

        Glide.with(this@RatingFragment).load(imageUrl).into(view.itemimage)
        view.txtItemName.text = productName


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        ratingObserve()
    }
    private fun setupClickListeners(){
        binding.btnRateNow.setOnClickListener(){
            updateRating(productId,rating)
        }
    }
    private fun ratingObserve() {
        viewModel.ratingViewModel.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it != null){
                val responseMsg = it.user_msg
                Toast.makeText(context,"$responseMsg",Toast.LENGTH_LONG).show()
                dismiss()

            }else
                Toast.makeText(context,"${it}",Toast.LENGTH_LONG).show()
        })
    }
    private fun updateRating(productId: String, rating: Int?) {
        viewModel.getRate(productId,rating)
    }
}