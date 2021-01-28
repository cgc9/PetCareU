package com.udea.petcare.Fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udea.petcare.Entities.Publication
import com.udea.petcare.R
import kotlinx.android.synthetic.main.card_view.view.*

class PetAdapter (val clickListener: onClickListener,var currentUserId:Int):  RecyclerView.Adapter <PetAdapter.MyViewHolder>() {

    private var publicationList= emptyList<Publication>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun initialize(item: Publication, action: onClickListener) {

            itemView.delete.setOnClickListener {
                action.onItemDelete(item, adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return publicationList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = publicationList[position]

        var imgResource: Int

        if(currentItem.image=="no"){
            imgResource = R.drawable.dogo
            holder.itemView.image.setImageResource(imgResource)
        }else {

            Picasso.get()
                .load(currentItem.image)
                .into(holder.itemView.image)
        }


        holder.itemView.title.text = currentItem.title
        holder.itemView.publicationDescription.text = currentItem.description
        holder.itemView.publicationCity.text = currentItem.city

        if(currentUserId==currentItem.user) {
            Log.d("Holder",currentUserId.toString()+currentItem.user)
            holder.itemView.delete.visibility = VISIBLE
        }

        holder.initialize(publicationList.get(position), clickListener)

    }



    fun setData(publications: List<Publication>) {
        this.publicationList = publications
        notifyDataSetChanged()
    }

    interface onClickListener {
        fun onItemDelete(item: Publication, index: Int)
    }
}