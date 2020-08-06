package com.surfboardapp.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.Models.Progression_Enrollment
import com.surfboardapp.R

class ProgressionModelAdapter(
    val activity: MainActivity,
    private var progressionEnrollmentList: ArrayList<Progression_Enrollment>
) : RecyclerView.Adapter<ProgressionModelAdapter.MyViewHolder>() {
    lateinit var context: Context
    private lateinit var progressionType: String

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvType: TextView = itemView.findViewById(R.id.tv_type)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_status)
//        var linearRecyclerProgression: LinearLayout =
//            itemView.findViewById(R.id.linear_recycler_progression)
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.design_recycler_progression, null)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return progressionEnrollmentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val progressionEnrollment: Progression_Enrollment = progressionEnrollmentList[position]
        when (progressionEnrollment.ProgressionType) {
            "discovery_enrollment" -> {
                progressionType = context.resources.getString(R.string.discoveryEnrollment)
            }
//            "progression_enrollment" -> {
//                progressionType = context.resources.getString(R.string.discoveryEnrollment)
//            }
            "cruiser" -> {
                progressionType = context.resources.getString(R.string.progressionlevel1)
            }
            "explorer" -> {
                progressionType = context.resources.getString(R.string.progressionlevel2)
            }
            "sport" -> {
                progressionType = context.resources.getString(R.string.progressionlevel3)
            }
            "pro" -> {
                progressionType = context.resources.getString(R.string.progressionlevel4)
            }
            else -> {
                progressionType = ""
            }
        }
        holder.tvType.text = progressionType
        if (progressionEnrollment.status == "1") {

            holder.tvStatus.setText(R.string.completed)
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_green))

        } else {
            holder.tvStatus.setText(R.string.pending)
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_red))
        }
//        holder.linear_recycler_progression.setOnClickListener {
//            val builder = Dialog(context)
//            val layoutInflater = LayoutInflater.from(context)
//            val dialog: View =
//                layoutInflater.inflate(R.layout.progression_status_popup, null)
//            val txt_progression_enrollment_date =
//                dialog.findViewById<TextView>(R.id.txt_progression_enrollment_date)
//            val txt_progression_enrollment_comment =
//                dialog.findViewById<TextView>(R.id.txt_progression_enrollment_comment)
//            val txt_progression_enrollment_instructor_name =
//                dialog.findViewById<TextView>(R.id.txt_progression_enrollment_instructor_name)
//            val txt_progression_enrollment_location_name =
//                dialog.findViewById<TextView>(R.id.txt_progression_enrollment_location_name)
//
//            txt_progression_enrollment_date.text = progressionEnrollment.date
//            txt_progression_enrollment_comment.text = progressionEnrollment.comment
//
//            builder.setContentView(dialog)
//            builder.show()
//            builder.setCancelable(false)
//
//            val img_close = dialog.findViewById<ImageView>(R.id.img_close)
//
//            img_close.setOnClickListener {
//                builder.dismiss()
//            }
//        }

    }
}