package com.example.neostoremvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neostoremvvm.activities.RupeeConvertorHelperClass
import com.example.neostoremvvm.dataclass.OrderDetail
import com.example.neostoremvvm.databinding.OrderDetailListBinding
import com.example.neostoremvvm.dataclass.OrderdetailData
import kotlinx.android.synthetic.main.order_detail_list.view.*

class OrderDetailAdapter(mcontext: Context): RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    var context: Context = mcontext
    var orderDetail = mutableListOf<OrderDetail>()

    fun setProductListDetail(productDetail: List<OrderDetail>) {
        this.orderDetail = productDetail.toMutableList()
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: OrderDetailListBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = OrderDetailListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItemPos = orderDetail[position]
        holder.binding.textOrderName.text= currentItemPos.prod_name
        holder.binding.textOrderCategories.text = "("+ currentItemPos.prod_cat_name+")"
        holder.binding.textOrderQty.text = "QTY: "+currentItemPos.quantity.toString()
        val cost = RupeeConvertorHelperClass().convertorfunction(currentItemPos.total)
        holder.binding.orderPrice.text = cost
        val url = currentItemPos.prod_image
        Glide.with(context).load(url).into(holder.binding.orderItemImage)
    }

    override fun getItemCount(): Int {
        return orderDetail.size
    }
}