package com.surfboardapp.Adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.surfboardapp.Activity.MainActivity
import com.surfboardapp.CustomClasses.CustomMethods
import com.surfboardapp.Models.ProgressionLevel2
import com.surfboardapp.R

class ProgressionLevel2Adapter(
    val activity: MainActivity,
    private var progressionLevel2List: ArrayList<ProgressionLevel2>,
    private var statusType2: String,
    private var status: String
) : RecyclerView.Adapter<ProgressionLevel2Adapter.MyViewHolder>() {
    lateinit var context: Context
    private lateinit var progressionType: String

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvType: TextView = itemView.findViewById(R.id.tv_type)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        var linearRecyclerProgression: LinearLayout =
            itemView.findViewById(R.id.linear_recycler_progression)
        var btn_book_now: Button = itemView.findViewById(R.id.btn_book_now)
        var lin_book_now: LinearLayout = itemView.findViewById(R.id.lin_book_now)
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.design_recycler_progression, null)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return progressionLevel2List.size
    }

    @SuppressLint("InflateParams")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val progressionLevel2List: ProgressionLevel2 = progressionLevel2List[position]

        if (statusType2.equals("true") || (!status.equals("1"))) {
            holder.lin_book_now.visibility = View.INVISIBLE
        } else {
            holder.lin_book_now.visibility = View.VISIBLE
        }

        when (progressionLevel2List.ProgressionType) {
            "discovery_enrollment" -> {
                progressionType = context.resources.getString(R.string.progressionEnrollment)
            }
            "cruiser" -> {
                progressionType = context.resources.getString(R.string.cruiser)
            }
            "explorer" -> {
                progressionType = context.resources.getString(R.string.explorer)
            }
            "sport" -> {
                progressionType = context.resources.getString(R.string.sport)
            }
            "pro" -> {
                progressionType = context.resources.getString(R.string.pro)
            }
            else -> {
                progressionType = ""
            }
        }
        holder.tvType.text = progressionType
        if (progressionLevel2List.status == "1") {
            holder.tvStatus.setText(R.string.completed)
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_green))

        } else if ((progressionLevel2List.status == "0") && (!progressionLevel2List.ProgressionId.equals(
                ""
            ))
        ) {

            holder.tvStatus.setText(R.string.pending)
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_red))

        } else if (progressionLevel2List.status == "2") {

            holder.tvStatus.text = "Fail"
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_red))
        } else if (progressionLevel2List.status == "3") {
            holder.tvStatus.text = "Canceled"
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_red))
        } else {
            holder.tvStatus.text = ""
        }
        holder.btn_book_now.setOnClickListener {

            CustomMethods().openPage(activity, MainActivity().locationFinder)
            activity.clearSelection()

        }
        holder.linearRecyclerProgression.setOnClickListener {
            val builder = Dialog(context)
            val layoutInflater = LayoutInflater.from(context)
            val dialog: View =
                layoutInflater.inflate(R.layout.progression_status_popup, null)
            val txtProgressionCommentedby =
                dialog.findViewById<TextView>(R.id.txt_progression_commentedBy)
            val txtProgressionEnrollmentComment =
                dialog.findViewById<TextView>(R.id.txt_progression_enrollment_comment)


            val txtDeatils = dialog.findViewById<TextView>(R.id.txt_deatils)
            txtDeatils.text = context.resources.getString(R.string.explorer)
            txtProgressionCommentedby.text = progressionLevel2List.instructorName
            txtProgressionEnrollmentComment.text = progressionLevel2List.comment

            builder.setContentView(dialog)
            builder.show()
            builder.setCancelable(false)

            val imgClose = dialog.findViewById<ImageView>(R.id.img_close)

            imgClose.setOnClickListener {
                builder.dismiss()
            }
        }


    }
}