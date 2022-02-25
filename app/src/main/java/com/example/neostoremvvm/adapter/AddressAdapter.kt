package com.example.neostoremvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.neostoremvvm.R
import com.example.neostoremvvm.dataclass.AddressInfo
import kotlinx.android.synthetic.main.address_list_item.view.*


class AddressAdapter(var click : cartInterface): RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    var  allNotes  = mutableListOf<AddressInfo>()
    private var lastChecked: RadioButton? = null
    private var lastCheckedPos = 0
    lateinit var context: Context
     var mlisterner = click
    fun addItems(list:MutableList<AddressInfo>){
        allNotes.clear()
        allNotes.addAll(list)
        notifyDataSetChanged()

    }

    fun click(listerner:cartInterface){
        mlisterner = listerner}
        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val radioButton = itemView.addressRadioButton
//            var username= itemView.findViewById<TextView>(R.id.UserName)
            var fulladdress= itemView.findViewById<TextView>(R.id.fullAddress)
            var imageview= itemView.findViewById<ImageView>(R.id.close)
            /*val close = itemView.close*/
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.ViewHolder {

            var itemView= LayoutInflater.from(parent.context).
            inflate(R.layout.address_list_item,parent,false)
            context = parent.context
            return  ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val item = allNotes[position]
            holder.fulladdress.setText(item.address)
            holder.imageview.setOnClickListener() {
                mlisterner.onClick(position, item = item)
                mlisterner.onClose(item, position)

            }

            if (position == 0) {
                holder.radioButton.isChecked = true

                lastChecked = holder.radioButton
                lastCheckedPos = 0

                val item = allNotes[lastCheckedPos]
                mlisterner.onClick(lastCheckedPos, item)
            }
            holder.radioButton.setOnClickListener() {
                val pos = holder.adapterPosition
                if (holder.radioButton.isChecked) {
                    if (lastChecked != null) {
                        lastChecked!!.isChecked = false
                    }
                    lastChecked = holder.radioButton
                    lastCheckedPos = holder.adapterPosition
                    val item = allNotes[lastCheckedPos]
                    mlisterner.onClick(lastCheckedPos, item)

                    Toast.makeText(context, "$lastCheckedPos and $item", Toast.LENGTH_LONG).show()
                } else {
                    lastChecked = null
                }
            }
        }

    override fun getItemCount(): Int {
            return allNotes.size
        }
        interface cartInterface {
            fun onClick(position: Int, item: AddressInfo)
            fun onClose(data: AddressInfo, position: Int)
        }
    }





