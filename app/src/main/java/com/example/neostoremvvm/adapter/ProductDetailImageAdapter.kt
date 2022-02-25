package com.example.neostoremvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.neostoremvvm.R
import com.example.neostoremvvm.databinding.ProductDetailImagesItemListBinding
import com.example.neostoremvvm.dataclass.ProductImage
import kotlinx.android.synthetic.main.product_detail_images_item_list.view.*

class ProductDetailImageAdapter(mcontext: Context) : RecyclerView.Adapter<ProductDetailImageAdapter.ViewHolder>() {
    var context:Context=mcontext
    var productDetail = mutableListOf<ProductImage>()
    var row_index = -1
    lateinit var mlisterner: onImageClickListernet

    fun setProductListDetail(productDetail: List<ProductImage>) {
        this.productDetail = productDetail.toMutableList()
        notifyDataSetChanged()
    }

    interface onImageClickListernet{
        fun onClickListerner(position: String)
    }
    fun setOnClickListerner(listerner:onImageClickListernet) {
        mlisterner = listerner
    }
    inner class ViewHolder(val binding: ProductDetailImagesItemListBinding, listerner: onImageClickListernet):
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listerner.onClickListerner(productDetail[adapterPosition].image)
                row_index = adapterPosition
                notifyDataSetChanged()


            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductDetailImageAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ProductDetailImagesItemListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding,mlisterner)

    }
    override fun onBindViewHolder(holder: ProductDetailImageAdapter.ViewHolder, position: Int) {
        val currentImage = productDetail[position]
        val url = currentImage.image

        Glide.with(context).load(url).into(holder.binding.PrdouctItemImageList)

        if(row_index == position){
            holder.binding.PrdouctItemImageList.setBackgroundResource(R.drawable.image_border_red)
        }else{
            holder.binding.PrdouctItemImageList.setBackgroundResource(R.drawable.image_border_grey)
        }




    }

    override fun getItemCount(): Int {

        return productDetail.size
    }
    }
