package com.example.aleftestproject

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class Adapter(val context: Context, val messages: ArrayList<String>, val dialog: Dialog) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val img: ImageView = itemView.findViewById(R.id.img) as ImageView
        private val imgDialog: ImageView = dialog.findViewById<ImageView>(R.id.imgDialog)

        init {
            img.setOnClickListener(this)
            imgDialog.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            when (p0?.id) {
                R.id.img -> {
                    val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT
                    dialog.show()
                    dialog.window?.attributes = lp
                    dialog.window?.setBackgroundDrawableResource(android.R.color.black)
                    dialog.setDismissMessage(null)
                    Picasso.get().load(messages[position]).into(imgDialog)

                }
                R.id.imgDialog -> dialog.hide()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(messages[position]).into(holder.img)
    }
}