package com.surfboardapp.Fragments


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Preference.Preference
import com.surfboardapp.R
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*


class GetCertifiedFragment : Fragment() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tvScreenTitle: TextView
    private lateinit var btnScheduleNow: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_get_certified, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setDrawerSelection()
        onClick()
    }

    @SuppressLint("InflateParams")
    private fun onClick() {
        activity!!.back.setOnClickListener {
            CustomMethods().openPage(activity!!, MainActivity().dashboard)
            activity!!.back.visibility = View.GONE
            activity!!.menu.visibility = View.VISIBLE
            activity!!.bottom_navigation.visibility = View.VISIBLE
            (activity as MainActivity).clearSelection()
        }
        btnScheduleNow.setOnClickListener {

            if (Preference().getUserData(context!!)!!.certificate_status == "1") {
                CustomMethods().openPage(activity!!, MainActivity().locationFinder)
                (activity as MainActivity).clearSelection()

            } else {
                val builderCertified = Dialog(context!!)
                val layoutInflater = LayoutInflater.from(context)
                val dialogCertified: View =
                    layoutInflater.inflate(
                        R.layout.certified_popup,
                        null
                    )

                val txtSelect = dialogCertified.findViewById<TextView>(R.id.txt_select)
                val tvCertified =
                    dialogCertified.findViewById<TextView>(R.id.tv_certified)
                val btnOk =
                    dialogCertified.findViewById<TextView>(R.id.btn_ok)
                txtSelect.text = getString(R.string.linkAffiliate)
                tvCertified.text = getString(R.string.cannotBookSession)
                builderCertified.setContentView(dialogCertified)
                builderCertified.show()
                builderCertified.setCancelable(false)

                btnOk.setOnClickListener { builderCertified.dismiss() }
            }
        }
    }

    private fun init() {
        activity!!.back.visibility = View.VISIBLE
        tvScreenTitle = activity!!.findViewById(R.id.tv_screenTitle)
        tvScreenTitle.text = context!!.resources.getString(R.string.getCertified)
        btnScheduleNow = view!!.findViewById(R.id.btn_scheduleNow)
    }

    private fun setDrawerSelection() {
        drawerLayout = activity!!.findViewById(R.id.drawer_layout)
        val home = drawerLayout.findViewById<LinearLayout>(R.id.nav_certified_ll)
        val homeImg = drawerLayout.findViewById<ImageView>(R.id.nav_certified_img)
        val hometxt = drawerLayout.findViewById<TextView>(R.id.nav_certified_txt)

        home?.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        homeImg?.setColorFilter(ContextCompat.getColor(context!!, R.color.white))
        hometxt?.setTextColor(ContextCompat.getColor(context!!, R.color.white))
    }


}
