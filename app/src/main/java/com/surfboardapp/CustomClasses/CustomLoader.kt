package com.surfboardapp.CustomClasses

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import com.surfboardapp.R

class CustomLoader {
    @SuppressLint("InflateParams")
    fun loader(context: Context?): Dialog {
        val dialog = Dialog(context!!)
        val v = LayoutInflater.from(context).inflate(R.layout.custom_loader, null)
        dialog.setContentView(v)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        val aniRotate =
            AnimationUtils.loadAnimation(context, R.anim.rotate)
        v.startAnimation(aniRotate)
        return dialog
    }

    fun stopLoader(d: Dialog) {
        if (d.isShowing) {
            d.dismiss()
        }

    }
}