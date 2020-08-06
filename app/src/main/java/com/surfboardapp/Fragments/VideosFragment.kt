package com.surfboardapp.Fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Activity.PlayVideoActivity
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.R
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*


class VideosFragment : Fragment() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tvScreenTitle: TextView
    private lateinit var relativeVideo1: RelativeLayout
    private lateinit var relativeVideo2: RelativeLayout
    private lateinit var relativeVideo3: RelativeLayout
    private lateinit var relativeVideo4: RelativeLayout
    private lateinit var relativeVideo5: RelativeLayout
    private lateinit var relativeVideo6: RelativeLayout
    private lateinit var relativeVideo7: RelativeLayout
    private lateinit var relativeVideo8: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_videos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setDrawerSelection()
        onClick()
    }

    private fun init() {
        activity!!.back.visibility = View.VISIBLE
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.videoFragment)

        relativeVideo1 = view!!.findViewById(R.id.relative_video1)
        relativeVideo2 = view!!.findViewById(R.id.relative_video2)
        relativeVideo3 = view!!.findViewById(R.id.relative_video3)
        relativeVideo4 = view!!.findViewById(R.id.relative_video4)
        relativeVideo5 = view!!.findViewById(R.id.relative_video5)
        relativeVideo6 = view!!.findViewById(R.id.relative_video6)
        relativeVideo7 = view!!.findViewById(R.id.relative_video7)
        relativeVideo8 = view!!.findViewById(R.id.relative_video8)

    }

    private fun onClick() {
        activity!!.back.setOnClickListener {
            CustomMethods().openPage(activity!!, MainActivity().dashboard)
            activity!!.back.visibility = View.GONE
            activity!!.menu.visibility = View.VISIBLE
            activity!!.bottom_navigation.visibility = View.VISIBLE
            (activity as MainActivity).clearSelection()
        }

        relativeVideo1.setOnClickListener {
            val i = Intent(activity, PlayVideoActivity::class.java)
            i.putExtra("key", "1yEmtz4ZVMM")
            startActivity(i)
        }

        relativeVideo2.setOnClickListener {
            val i = Intent(activity, PlayVideoActivity::class.java)
            i.putExtra("key", "FenSw41WbvQ")
            startActivity(i)
        }

        relativeVideo3.setOnClickListener {
            val i = Intent(activity, PlayVideoActivity::class.java)
            i.putExtra("key", "PlxX_3gsQQ0")
            startActivity(i)
        }

        relativeVideo4.setOnClickListener {
            val i = Intent(activity, PlayVideoActivity::class.java)
            i.putExtra("key", "WaLrpuUawfA")
            startActivity(i)
        }

        relativeVideo5.setOnClickListener {
            val i = Intent(activity, PlayVideoActivity::class.java)
            i.putExtra("key", "znDhzzGPzWQ")
            startActivity(i)
        }

        relativeVideo6.setOnClickListener {
            val i = Intent(activity, PlayVideoActivity::class.java)
            i.putExtra("key", "UEmvO1Am69s")
            startActivity(i)
        }

        relativeVideo7.setOnClickListener {
            val i = Intent(activity, PlayVideoActivity::class.java)
            i.putExtra("key", "gHJ-VLhW8Hk")
            startActivity(i)
        }

        relativeVideo8.setOnClickListener {
            val i = Intent(activity, PlayVideoActivity::class.java)
            i.putExtra("key", "Ff-osqqyY7A")
            startActivity(i)
        }

    }

    private fun setDrawerSelection() {
        drawerLayout = activity!!.findViewById(R.id.drawer_layout)
        val home = drawerLayout.findViewById<LinearLayout>(R.id.nav_videos_ll)
        val homeImg = drawerLayout.findViewById<ImageView>(R.id.nav_videos_img)
        val hometxt = drawerLayout.findViewById<TextView>(R.id.nav_videos_txt)

        home?.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        homeImg?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))
        hometxt?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
    }


}
