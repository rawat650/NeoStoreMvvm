package com.example.neostoremvvm.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neostoremvvm.databinding.ProductListItemBinding
import com.example.neostoremvvm.dataclass.ProductData
import kotlinx.android.synthetic.main.product_list_item.view.*
import java.text.DecimalFormat
import java.text.NumberFormat

class ProductListAdapter(mcontext: Context): RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
     var context:Context=mcontext
    lateinit var mlistner : onItemClickListerner
    var product = mutableListOf<ProductData>()

    fun setProductList(product: List<ProductData>) {
        this.product.addAll(product.toMutableList())
        notifyDataSetChanged()
    }

    interface onItemClickListerner {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListerner(listerner:onItemClickListerner){
        mlistner = listerner
    }
     inner class ViewHolder(val binding: ProductListItemBinding, listerner:onItemClickListerner):RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listerner.onItemClick(product[adapterPosition].id)
            }
        }
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = product[position]
        holder.binding.txtItemName.text = currentItem.name
        holder.binding.txtItemSellerName.text = currentItem.producer


        val nf = NumberFormat.getCurrencyInstance()
        val pattern = (nf as DecimalFormat).toPattern()
        val newPattern = pattern.replace("\u00A4", "").trim { it <= ' ' }
        val newFormat: NumberFormat = DecimalFormat(newPattern)
        val answer = newFormat.format(currentItem.cost).trim().dropLast(3)
        //val answer = format.format(currentItem.cost).trim()
        holder.binding.txtItemPrice.text = "Rs. " + answer
        holder.binding.ratingBar.rating = currentItem.rating.toFloat()
        val url = currentItem.product_images
        Glide.with(context).load(url).into(holder.binding.imgRecyclerItem)
        //holder.productPrice.text = position.toString() + "/" + dataList.size

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ProductListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding,mlistner)
    }

    override fun getItemCount(): Int {
        return product.size
    }
}
