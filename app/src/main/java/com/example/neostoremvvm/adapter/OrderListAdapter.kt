package com.example.neostoremvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neostoremvvm.activities.RupeeConvertorHelperClass
import com.example.neostoremvvm.dataclass.OrderListData
import com.example.neostoremvvm.databinding.MyOrdersListItemBinding
import kotlinx.android.synthetic.main.my_orders_list_item.view.*


class OrderListAdapter(mcontext: Context): RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {
    var context:Context=mcontext
    lateinit var mlistner : OnItemClickListerner
    var order = mutableListOf<OrderListData>()

    fun setProductList(order: List<OrderListData>) {
        this.order.addAll(order.toMutableList())
        notifyDataSetChanged()
    }

    interface OnItemClickListerner{
        fun onClickListerner(position: Number)
    }

    fun onSetClickListerner(listerner:OnItemClickListerner){
        mlistner = listerner
    }

    inner class ViewHolder(val binding: MyOrdersListItemBinding, listerner: OrderListAdapter.OnItemClickListerner):RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener(){
                listerner.onClickListerner(order[adapterPosition].id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = MyOrdersListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding,mlistner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = order[position]
        holder.binding.txtOrderId.text = "Order Id: " + currentItem.id.toString()
        holder.binding.txtPrice.text= "Order Id: " + currentItem.id.toString()
        holder.binding.txtCreated.text = "Ordered date:"+ currentItem.created.toString()
        val cost = RupeeConvertorHelperClass().convertorfunction(currentItem.cost)
        holder.binding.txtPrice.text = cost
    }

    override fun getItemCount(): Int {
        return order.size
    }
}