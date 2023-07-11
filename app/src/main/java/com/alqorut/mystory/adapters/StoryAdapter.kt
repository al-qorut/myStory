package com.alqorut.mystory.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alqorut.mystory.R
import com.alqorut.mystory.activities.DetailActivity
import com.alqorut.mystory.api.response.ListStoryItem
import com.alqorut.mystory.helpers.msg
import com.alqorut.mystory.models.Story
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class StoryAdapter(private val context: Context, private var list:List<ListStoryItem>):
    RecyclerView.Adapter<StoryAdapter.Holder>() {

    class Holder(view: View) :RecyclerView.ViewHolder(view){
        val textNama = view.findViewById<TextView>(R.id.item_name)
        val textDetail = view.findViewById<TextView>(R.id.item_details)
        val textDate = view.findViewById<TextView>(R.id.item_date)
        val img = view.findViewById<ImageView>(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.item_story_list,parent,false))
    }

    override fun getItemCount() : Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list[position]
        holder.textNama.text = item.name
        holder.textDetail.text = item.description
        holder.textDate.text = item.createdAt
        var options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .transform(CenterCrop(), RoundedCorners(10))

        Glide.with(context)
            .load(item.photoUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(options)
            .into(holder.img)
        holder.itemView.setOnClickListener {
            val detail = Intent(context, DetailActivity::class.java)
            detail.putExtra("detail",item)
            context.startActivity(detail)
        }
    }
    fun setItems(data: List<ListStoryItem>){
        list = data
        notifyDataSetChanged()
    }
}