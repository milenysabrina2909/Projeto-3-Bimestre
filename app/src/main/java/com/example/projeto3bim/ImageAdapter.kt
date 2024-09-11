package com.example.projeto3bim

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.lang.Exception

class ImageAdapter (private var items:List<Item>, private val context: Context):
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Picasso.get().load(item.imageUrl).into(holder.imageView)

        holder.imageView.setOnClickListener {
            setupDialog(item)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    private fun setupDialog(item: Item) {
        val dialog = Dialog(context, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog)
        val dialogImageView = dialog.findViewById<ImageView>(R.id.dialogImageView)
        val dialogProgressBar = dialog.findViewById<ProgressBar>(R.id.dialogProgressBar)

        dialogProgressBar.visibility = View.VISIBLE

        Picasso.get().load(item.imageUrl).into(dialogImageView, object : com.squareup.picasso.Callback {
            override fun onSuccess() {
                dialogProgressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                Log.d("errorLoad", e.toString())
            }

        })

        dialog.show()

    }

}