package dev.ogabek.kotlin

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val context: Context, private val photos: ArrayList<Uri>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItem: ImageView = view.findViewById(R.id.iv_item)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = photos[position]

        if (holder is PhotoViewHolder) {
            holder.apply {
                ivItem.setImageURI(photo)
            }
        }
    }

    override fun getItemCount() = photos.size


}