package com.udea.petcare.Fragments

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udea.petcare.Entities.Place
import com.udea.petcare.R
import kotlinx.android.synthetic.main.item.view.*

class VeterinaryAdapter (val veterinaryList:List<Place>, val clickListener: onClickListener):  RecyclerView.Adapter <VeterinaryAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun initialize(item: Place, action: onClickListener) {
            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return veterinaryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = veterinaryList[position]

        holder.itemView.placeName.text=currentItem.namePlace
        holder.itemView.placeLoctaion.text = currentItem.locationPlace
        holder.initialize(veterinaryList.get(position), clickListener,)

    }

    /*


    /* fun setData(places: List<POI>) {
         this.placeList = places
         notifyDataSetChanged()
     }*/*/

    interface onClickListener {
        fun onItemClick(item: Place, index: Int)
    }
}