package com.alqorut.mystory.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.alqorut.mystory.R
import com.alqorut.mystory.api.response.ListStoryItem
import com.alqorut.mystory.data.dbStory
import com.alqorut.mystory.helpers.toas
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


internal class StackRemoteViewsFactory(
    private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {
    private var mWidgetItems = listOf<ListStoryItem>()
    private var data : dbStory? = null
    private val disposables = CompositeDisposable()

    override fun onCreate() {
        data = dbStory.getIntance(mContext)
        loadData()
    }

    private fun loadData(){
        val disposable = data?.story()?.getStory()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                mWidgetItems = it
            },{
                toas(mContext, it.message!!)
            })
        disposables.add(disposable!!)
    }

    override fun onDataSetChanged() {
        loadData()
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_widget)
        try {
            val bitmap: Bitmap = Glide.with(mContext)
                .asBitmap()
                .load(mWidgetItems[position].photoUrl)
                .submit()
                .get()
            rv.setImageViewBitmap(R.id.imageGambar, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val extras = bundleOf(
                StoryWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageGambar, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}