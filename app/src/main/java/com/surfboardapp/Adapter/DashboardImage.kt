package com.surfboardapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import com.surfboardapp.R

class DashboardImage(var context: Context, private var mResources: IntArray) :
    PagerAdapter() {
    private var layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return mResources.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View =
            layoutInflater.inflate(R.layout.design_dashboard_viewpager, container, false)
        val imageView =
            itemView.findViewById<ImageView>(R.id.viewPagerItem_image1)
        Picasso.get().load(mResources[position]).fit().into(imageView)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as LinearLayout)
    }

}