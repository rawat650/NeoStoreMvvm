package com.example.neostoremvvm.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neostoremvvm.R
import com.example.neostoremvvm.activities.RupeeConvertorHelperClass
import com.example.neostoremvvm.dataclass.CartData
import com.example.neostoremvvm.dataclass.Product
import com.example.neostoremvvm.databinding.ActivityMyCartBinding
import com.example.neostoremvvm.databinding.AddToCartItemListBinding
import com.example.neostoremvvm.databinding.ProductListItemBinding
import kotlinx.android.synthetic.main.add_to_cart_item_list.view.*

class CartListAdapter(mcontext: Context) : RecyclerView.Adapter<CartListAdapter.ViewHolder>() {

    var context: Context = mcontext
    lateinit var mlistner: onItemClickListerner
    var cartList = mutableListOf<CartData>()

    fun setCartData(cartList: List<CartData>) {
        this.cartList.addAll(cartList.toMutableList())
        notifyDataSetChanged()
    }
    /*fun setCart(cartData: List<Product>){
        this.cartData.addAll(cartData.toMutableList())
        notifyDataSetChanged()
    }*/

    interface onItemClickListerner{
        fun onClick(position: Int, adapterPosition: Int)
    }
    fun setOnItemClickListerner(listerner: onItemClickListerner){
        mlistner = listerner
    }
    inner class ViewHolder(val binding: AddToCartItemListBinding, listerner: onItemClickListerner):RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listerner.onClick(cartList[adapterPosition].id,adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val inflater = LayoutInflater.from(parent.context)

       val binding = AddToCartItemListBinding.inflate(inflater, parent, false)
       return ViewHolder(binding,mlistner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPos = cartList[position]
        holder.binding.textAddToCartName.text = itemPos.product.name
        holder.binding.textAddToCartCategory.text = itemPos.product.product_category


        val url = itemPos.product.product_images
        Glide.with(context).load(url).into(holder.binding.cartItemImageView)

        val listOfNumber = context.resources.getStringArray(R.array.quantity)
        val arrayAdapter = ArrayAdapter(context,R.layout.drop_down_item,listOfNumber)

        holder.binding.dropdown.setOnItemClickListener (object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                //this is your selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemClick(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val pos = p2+1
                val adapterPosition = holder.adapterPosition
                Log.d(TAG, "onItemClick: $pos and $adapterPosition")
                mlistner.onClick(pos,adapterPosition)
            }
        })
        holder.binding.dropdown.setAdapter(arrayAdapter)
        holder.binding.dropdown.setText("${itemPos.quantity}",false)
        val cost = RupeeConvertorHelperClass().convertorfunction(itemPos.product.cost)
        holder.binding.txtPrice.text = cost
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    fun deletItem(i:Int){
        cartList.removeAt(i)
        notifyDataSetChanged()
    }


}